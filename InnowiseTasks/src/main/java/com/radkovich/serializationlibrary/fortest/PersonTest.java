package com.radkovich.serializationlibrary.fortest;


public class PersonTest {
    String name;
    int age = 30;
    HomeTest home;
    JobTest job;
    SkillsTest skills;

    public PersonTest(String name, int age, HomeTest home, JobTest job, SkillsTest skills) {
        this.name = name;
        this.age = age;
        this.home = home;
        this.job = job;
        this.skills = skills;
    }

}
