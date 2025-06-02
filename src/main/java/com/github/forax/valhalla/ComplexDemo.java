package com.github.forax.valhalla;

import java.io.IO;

public class ComplexDemo {
  public static void main(String[] args) {
    var c1 = new Complex(1.0, 2.0);
    var c2 = new Complex(1.0, 2.0);
    IO.println(c1 + " " + c2 + " " + (c1 == c2));

    //c1 = new Complex(1.0, Double.NaN);
    //c2 = new Complex(1.0, Double.longBitsToDouble(Double.doubleToRawLongBits(Double.NaN) | 0b1));
    //IO.println(c1 + " " + c2 + " " + (c1 == c2));

    IO.println(System.identityHashCode(c1) + " " + System.identityHashCode(c2));

    Object o = c1;
    synchronized (o) {}
  }
}
