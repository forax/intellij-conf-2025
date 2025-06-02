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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

// Benchmark                                      Mode  Cnt  Score   Error  Units
// NoIntegrityArrayBench.complexArrayLoop         avgt    5  1.723 ± 0.058  ms/op
// NoIntegrityArrayBench.complexNotNullArrayLoop  avgt    5  0.865 ± 0.004  ms/op
// NoIntegrityArrayBench.doubleArrayLoop          avgt    5  0.864 ± 0.002  ms/op

// export JAVA_HOME=/Users/forax/valhalla-live2/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java -jar target/benchmarks.jar
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = { "--enable-preview", "--add-exports=java.base/jdk.internal.value=ALL-UNNAMED" })
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class NoIntegrityArrayBench {

  @jdk.internal.vm.annotation.LooselyConsistentValue
  value record NoIntegrityComplex(double re, double im) {
    NoIntegrityComplex add(NoIntegrityComplex other) {
      return new NoIntegrityComplex(re + other.re, im + other.im);
    }
  }

  NoIntegrityComplex[] complexArray = new NoIntegrityComplex[1_000_000];
  {
    for(var i = 0; i < complexArray.length; i++) {
      complexArray[i] = new NoIntegrityComplex(i, i);
    }
    Collections.shuffle(Arrays.asList(complexArray));
  }

  NoIntegrityComplex[] complexNotNullArray = (NoIntegrityComplex[])
      jdk.internal.value.ValueClass.newNullRestrictedNonAtomicArray(NoIntegrityComplex.class, 1_000_000, new NoIntegrityComplex(0, 0));
  {
    for(var i = 0; i < complexNotNullArray.length; i++) {
      complexNotNullArray[i] = new NoIntegrityComplex(i, i);
    }
    Collections.shuffle(Arrays.asList(complexNotNullArray));
  }

  double[] doubleArray = new double[1_000_000 * 2];
  {
    for (int i = 0; i < doubleArray.length; i += 2) {
      doubleArray[i] = i;
      doubleArray[i + 1] = i;
    }
  }

  //@Benchmark
  public double complexArrayLoop() {
    var sum = new NoIntegrityComplex(0, 0);
    for(var complex : complexArray) {
      sum = sum.add(complex);
    }
    return sum.re + sum.im;
  }

  //@Benchmark
  public double complexNotNullArrayLoop() {
    var sum = new NoIntegrityComplex(0, 0);
    for(var complex : complexNotNullArray) {
      sum = sum.add(complex);
    }
    return sum.re + sum.im;
  }

  //@Benchmark
  public double doubleArrayLoop() {
    var sumRe = 0.0;
    var sumIm = 0.0;
    for (int i = 0; i < doubleArray.length; i += 2) {
      var complexRe = doubleArray[i];
      var complexIm = doubleArray[i + 1];
      sumRe += complexRe;
      sumIm += complexIm;
    }
    return sumRe + sumIm;
  }
}
