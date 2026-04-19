package com.radkovich.serializationlibrary;

import com.radkovich.serializationlibrary.fortest.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializerTest {
    private JsonSerializer jsonSerializer;

    @BeforeEach
    void setUp() {
        jsonSerializer = new JsonSerializer();
    }

    @Test
    void testWithSimpleObject() {
        class User {
            String name = "John";
            int age = 30;
        }

        String json = jsonSerializer.serialize(new User());

        assertTrue(json.contains("\"name\": \"John\""));
        assertTrue(json.contains("\"age\": 30"));
    }

    @Test
    void testWithAllTypesObject() {
        class User {
            String name = "Nick";
            int age = 44;
            HomeTest home = new HomeTest("Nesterova");
            JobTest job = new JobTest(new String[]{"work1", "work2", "work3"});
            SkillsTest skills = new SkillsTest(List.of("skill1", "skill2", "skill3", "skill4"));
        }
        String json = jsonSerializer.serialize(new User());

        assertTrue(json.contains("\"name\": \"Nick\""));
        assertTrue(json.contains("\"age\": 44"));
        assertTrue(json.contains("\"home\": {\"address\": \"Nesterova\"}"));
        assertTrue(json.contains("\"job\": {\"todos\": [\"work1\",\"work2\",\"work3\"]}"));
        assertTrue(json.contains("\"skills\": [\"skill1\",\"skill2\",\"skill3\",\"skill4\"]}"));
    }

    @Test
    void testForCyclicalDependencyObject() {
        class Person {
            String name;
            Person friend;
        }

        Person p1 = new Person();
        p1.name = "Liza";

        Person p2 = new Person();
        p2.name = "Dima";

        p1.friend = p2;
        p2.friend = p1;

        assertThrows(SerializationException.class, () -> {
            jsonSerializer.serialize(p1);
        });
    }

    @Test
    void testAnnotations() {
        class User {
            @JsonName("user_id")
            String id = "123456789";

            @Exclude
            String password = "1111";
        }

        String json = jsonSerializer.serialize(new User());

        assertTrue(json.contains("\"user_id\": \"123456789\""));
        assertFalse(json.contains("22"));
        assertFalse(json.contains("password"));
    }

    @Test
    void testIgnoreTransientAndStatic() {
        class User {
            static String name = "Dan";
            transient int age = 22;
        }

        String json = jsonSerializer.serialize(new User());

        assertFalse(json.contains("\"name\": \"Dan\""));
        assertFalse(json.contains("22"));
    }

    @Test
    void testPrettyPrint() {
        jsonSerializer.setPrettyPrint(true);

        class User {
            String name = "Dirk";
        }

        String json = jsonSerializer.serialize(new User());

        assertTrue(json.contains("\n"));
    }

    @Test
    void testNullFields() {
        jsonSerializer.setPrettyPrint(true);

        class User {
            String name = null;
        }

        String json = jsonSerializer.serialize(new User());

        assertTrue(json.contains("\"name\": null"));
    }

    @Test
    void testMap() {
        jsonSerializer.setPrettyPrint(true);

        class Digits {
            Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
        }

        String json = jsonSerializer.serialize(new Digits());

        assertTrue(json.contains("\"a\": 1"));
        assertTrue(json.contains("\"b\": 2"));
        assertTrue(json.contains("\"c\": 3"));
    }
}
