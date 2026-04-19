package com.radkovich.serializationlibrary.fortest;

import com.radkovich.serializationlibrary.JsonSerializer;

import java.util.List;
import java.util.Map;

public class Tester {

    public static void main(String[] args) {
        JsonSerializer serializer = new JsonSerializer();
        PersonTest tester1 = new PersonTest("Vasya", 22, new HomeTest("Pushkina"), new JobTest(new String[]{"1", "2"}), new SkillsTest(List.of("a", "b", "c")));

        class Digits {
            Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);
        }

        serializer.setPrettyPrint(false);

        String res1 = serializer.serialize(tester1);
        System.out.println(res1);

        String res2 = serializer.serialize(new Digits());
        System.out.println(res2);
    }
}
