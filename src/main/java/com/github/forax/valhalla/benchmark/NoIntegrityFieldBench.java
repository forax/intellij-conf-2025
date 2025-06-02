package com.github.forax.valhalla.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

// Benchmark                         Mode  Cnt  Score    Error  Units
// NoIntegrityFieldBench.complexSum  avgt    5  0.625 ±  0.001  ns/op
// NoIntegrityFieldBench.doubleSum   avgt    5  0.625 ±  0.001  ns/op

// export JAVA_HOME=/Users/forax/valhalla-live2/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java -jar target/benchmarks.jar
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = { "--enable-preview" })
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class NoIntegrityFieldBench {

  @jdk.internal.vm.annotation.LooselyConsistentValue
  value record NoIntegrityComplex(double re, double im) {
    NoIntegrityComplex add(NoIntegrityComplex other) {
      return new NoIntegrityComplex(re + other.re, im + other.im);
    }
  }

  static class DoubleComplexBox {
    double re;
    double im;
  }

  static class NoIntegrityComplexBox {
    @jdk.internal.vm.annotation.NullRestricted
    NoIntegrityComplex complex;
  }

  NoIntegrityComplexBox noIntegrityBox = new NoIntegrityComplexBox();
  {
    noIntegrityBox.complex = new NoIntegrityComplex(42, 42);
  }
  DoubleComplexBox doubleBox =new DoubleComplexBox();
  {
    doubleBox.re = 42;
    doubleBox.im = 42;
  }

  //@Benchmark
  public double complexSum() {
    var complex = noIntegrityBox.complex;
    return complex.re + complex.im;
  }

  //@Benchmark
  public double doubleSum() {
    return doubleBox.re + doubleBox.im;
  }
}
