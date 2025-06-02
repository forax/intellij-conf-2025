package com.github.forax.valhalla;

public value record Complex(double re, double im) {
  public Complex add(Complex other) {
    return new Complex(this.re + other.re, this.im + other.im);
  }

  public Complex multiply(Complex other) {
    return new Complex(this.re * other.re - this.im * other.im, this.re * other.im + this.im * other.re
    );
  }

  public double magnitudeSquared() {
    return re * re + im * im;
  }
}