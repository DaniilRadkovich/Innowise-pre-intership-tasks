package com.radkovich.minispring;

import com.radkovich.minispring.helpers.componentload.FirstComponent;
import com.radkovich.minispring.helpers.componentload.SecondComponent;
import com.radkovich.minispring.helpers.cyclicaltest.TestX;
import com.radkovich.minispring.helpers.prototypetest.PrototypeBean;
import com.radkovich.minispring.helpers.injection.UserController;
import com.radkovich.minispring.helpers.injection.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiniApplicationContextTest {

    @Test
    void shouldLoadAllComponentBeans() {
        MiniApplicationContext context = new MiniApplicationContext("com.radkovich.minispring.helpers.componentload");

        FirstComponent firstComponent = context.getBean(FirstComponent.class);
        SecondComponent secondComponent = context.getBean(SecondComponent.class);

        assertNotNull(firstComponent);
        assertNotNull(secondComponent);
    }

    @Test
    void shouldAutowiredInjectPrivateFields() {
        MiniApplicationContext context = new MiniApplicationContext("com.radkovich.minispring.helpers.injection");

        UserService userService = context.getBean(UserService.class);
        UserController userController = context.getBean(UserController.class);

        assertNotNull(userController.getUserService());
        assertSame(userService, userController.getUserService());
    }

    @Test
    void shouldCallLifecycleAfterDependencyInjection() {
        MiniApplicationContext context = new MiniApplicationContext("com.radkovich.minispring.helpers.injection");

        UserController userController = context.getBean(UserController.class);

        assertEquals(1, userController.getInitInvocationCount());
        assertTrue(userController.isDependencyInjectedDuringInitialization());
        assertEquals("Controller initialized with user: Test User", userController.getInitMessage());
    }

    @Test
    void shouldReuseSingletonBeans() {
        MiniApplicationContext context = new MiniApplicationContext("com.radkovich.minispring.helpers.injection");

        UserService firstUserService = context.getBean(UserService.class);
        UserService secondUserService = context.getBean(UserService.class);

        assertSame(firstUserService, secondUserService);
    }

    @Test
    void shouldPrototypeBeansReturnNewInstance() {
        MiniApplicationContext context = new MiniApplicationContext("com.radkovich.minispring.helpers.prototypetest");

        PrototypeBean firstPrototypeBean = context.getBean(PrototypeBean.class);
        PrototypeBean secondPrototypeBean = context.getBean(PrototypeBean.class);

        assertNotSame(firstPrototypeBean, secondPrototypeBean);
    }

    @Test
    void shouldCyclicalDependenciesFailsWithException() {
        MiniApplicationContext context = new MiniApplicationContext("com.radkovich.minispring.helpers.cyclicaltest");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> context.getBean(TestX.class));
        assertTrue(exception.getMessage().contains("already initialized and detected in circular dependency!"));
    }
}
