package com.github.forax.valhalla;

public value class Person {
  final String name;
  final int age;

  public Person(String name, int age) {
    //super();
    this.name = name;
    this.age = age;
    super();
  }

  public static void main(String[] args) {
    var person = new Person("John", 25);
    System.out.println(person);
    System.out.println(person.getClass().isValue());
  }
}
