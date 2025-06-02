package com.github.forax.valhalla;

import jdk.internal.vm.annotation.Strict;

/* no-value */ class IdentityPerson {
  @Strict final String name;
  @Strict final int age;

  public IdentityPerson(String name, int age) {
    this.name = name;
    this.age = age;
    super();
  }

  public static void main(String[] args) {
    var person = new IdentityPerson("John", 25);
    System.out.println(person);
    System.out.println(person.getClass().isValue());
  }
}