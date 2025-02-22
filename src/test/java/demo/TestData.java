package demo;

import demo.model.Person;

public class TestData {

    public static Person person() {
        return new Person("Greg");
    }

    public static Person person1() {
        return new Person("Roy");
    }

    public static Person person2() {
        return new Person("Craig");
    }
}
