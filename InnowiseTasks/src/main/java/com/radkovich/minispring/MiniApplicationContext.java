package com.radkovich.minispring;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiniApplicationContext {
    private static final String SINGLETON = "singleton";
    private static final String PROTOTYPE = "prototype";

    private final Map<Class<?>, BeanDefinition> beanDefinitions = new LinkedHashMap<>();
    private final Map<Class<?>, Object> singletonBeans = new HashMap<>();
    private final Set<Class<?>> beansInCreation = new HashSet<>();

    public MiniApplicationContext(String basePackage) {
        Objects.requireNonNull(basePackage);
        registerBeanDefinitions(basePackage);
        initializeSingletons();
    }

    public <T> T getBean(Class<T> type) {
        Class<?> beanType = resolveBeanType(type);
        BeanDefinition definition = beanDefinitions.get(beanType);
        if (definition == null) {
            throw new IllegalArgumentException("Bean of type " + type.getName() + " not found!");
        }

        Object instance;
        if (definition.isPrototype()) {
            instance = createBean(definition.type());
        } else {
            instance = singletonBeans.get(beanType);
            if (instance == null) {
                instance = createBean(definition.type());
                singletonBeans.put(beanType, instance);
            }
        }
        return type.cast(instance);
    }

    private Class<?> resolveBeanType(Class<?> requestedType) {
        if (beanDefinitions.containsKey(requestedType)) {
            return requestedType;
        }

        List<Class<?>> matches = new ArrayList<>();
        for (Class<?> candidate : beanDefinitions.keySet()) {
            if (requestedType.isAssignableFrom(candidate)) {
                matches.add(candidate);
            }
        }
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("No bean of type " + requestedType.getName() + " found!");
        }
        if (matches.size() > 1) {
            throw new IllegalArgumentException("More than one bean of type " + requestedType.getName() + " found!");
        }
        return matches.get(0);
    }

    private void initializeSingletons() {
        for (BeanDefinition definition : beanDefinitions.values()) {
            if (!definition.isPrototype()) {
                continue;
            }
            Class<?> beanType = definition.type();
            if (!singletonBeans.containsKey(beanType)) {
                Object instance = createBean(beanType);
                singletonBeans.put(beanType, instance);
            }
        }

    }

    private Object createBean(Class<?> beanType) {
        if (!beansInCreation.add(beanType)) {
            throw new IllegalStateException("Bean of type " + beanType.getName() + " already initialized and detected in circular dependency! "
                    + buildCreationPath(beanType));
        }

        try {
            Object instance = instantiate(beanType);
            injectFields(instance);
            invokeLifecycle(instance);
            return instance;
        } finally {
            beansInCreation.remove(beanType);
        }
    }

    private String buildCreationPath(Class<?> beanType) {
        Deque<String> stack = new ArrayDeque<>();
        for (Class<?> current : new LinkedHashSet<>(beansInCreation)) {
            stack.addLast(current.getSimpleName());
        }
        stack.addLast(beanType.getSimpleName());
        return String.join(" -> ", stack);
    }

    private void invokeLifecycle(Object bean) {
        if (bean instanceof InitializingBean beanInitializer) {
            beanInitializer.afterPropertiesSet();
        }
    }

    private void injectFields(Object bean) {
        for (Field field : getAllFields(bean.getClass())) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            Object dependency = getBean(field.getType());

            try {
                field.setAccessible(true);
                field.set(bean, dependency);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not inject dependency " + field.getType().getName() + " into " + bean.getClass().getName(), e);
            }
        }
    }

    private List<Field> getAllFields(Class<?> type) {
        if (type == null || type == Object.class) return Collections.emptyList();

        List<Field> fields = new ArrayList<>(getAllFields(type.getSuperclass()));
        Collections.addAll(fields, type.getDeclaredFields());
        return fields;
    }

    private Object instantiate(Class<?> beanType) {
        try {
            Constructor<?> constructor = beanType.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not instantiate bean of type " + beanType.getName(), e);
        }
    }

    private void registerBeanDefinitions(String basePackage) {
        for (Class<?> candidateClass : scanPackage(basePackage)) {
            Component component = candidateClass.getAnnotation(Component.class);
            if (component == null) {
                continue;
            }
            BeanDefinition definition = new BeanDefinition(candidateClass, component.value(), resolveScope(candidateClass));
            beanDefinitions.put(candidateClass, definition);
        }
    }

    private String resolveScope(Class<?> type) {
        Scope scope = type.getAnnotation(Scope.class);
        String scopeValue = scope == null ? SINGLETON : scope.value().trim().toLowerCase();
        if (!SINGLETON.equals(scopeValue) && !PROTOTYPE.equals(scopeValue)) {
            throw new IllegalArgumentException("Unsupported scope " + scopeValue + " for type " + type.getName());
        }
        return scopeValue;
    }

    private List<Class<?>> scanPackage(String basePackage) {
        String packagePath = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(packagePath);

        if (resource == null) {
            throw new IllegalArgumentException("Package " + basePackage + " not found!");
        }
        if (!"file".equals(resource.getProtocol())) {
            throw new IllegalStateException("Unsupported package resource protocol " + resource.getProtocol());
        }

        try {
            Path basePath = Path.of(resource.toURI());
            try (Stream<Path> paths = Files.walk(basePath)) {
                return paths
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(path -> path.endsWith(".class"))
                        .filter(path -> !path.contains("$"))
                        .map(path -> toClassName(basePackage, basePath, Path.of(path)))
                        .map(this::loadClass)
                        .collect(Collectors.toList());
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Error while scanning package " + basePackage, e);
        }
    }

    private String toClassName(String basePackage, Path basePath, Path classFile) {
        Path relativePath = basePath.relativize(classFile);
        String normalized = relativePath.toString().replace(File.separatorChar, '.');
        String simpleName = normalized.substring(0, normalized.length() - ".class".length());
        return basePackage + "." + simpleName;
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class: " + className, e);
        }
    }

    private record BeanDefinition(Class<?> type, String name, String scope) {
        private boolean isPrototype() {
            return PROTOTYPE.equals(scope);
        }
    }
}
