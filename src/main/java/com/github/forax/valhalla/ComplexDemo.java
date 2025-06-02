package com.github.forax.valhalla;

public class ComplexDemo {
  public static void main(String[] args) {
    var c1 = new Complex(1.0, 2.0);
    var c2 = new Complex(1.0, 2.0);
    System.out.println(c1 + " " + c2 + " " + (c1 == c2));

    //c1 = new Complex(1.0, Double.NaN);
    //c2 = new Complex(1.0, Double.longBitsToDouble(Double.doubleToRawLongBits(Double.NaN) | 0b1));
    //System.out.println(c1 + " " + c2 + " " + (c1 == c2));

    //System.out.println(System.identityHashCode(c1) + " " + System.identityHashCode(c2));

    //Object o = c1;
    //synchronized (o) {}
  }
}
