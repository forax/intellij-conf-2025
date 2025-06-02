package com.github.forax.valhalla.benchmark;

import jdk.internal.vm.annotation.NullRestricted;
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
import java.util.concurrent.TimeUnit;

import static jdk.internal.value.ValueClass.newNullRestrictedAtomicArray;

// Benchmark                                 Mode  Cnt    Score   Error  Units
// FlatteningBench.primitives                avgt    5  291,008 ± 0,830  us/op
// FlatteningBench.identities                avgt    5  332,557 ± 1,756  us/op
// FlatteningBench.nullRestrictedValues      avgt    5  291,261 ± 0,752  us/op
// FlatteningBench.nullRestrictedValueBoxes  avgt    5  291,209 ± 1,852  us/op

// export JAVA_HOME=/Users/forax/valhalla-live2/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java -jar target/benchmarks.jar
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = { "--enable-preview", "--add-exports=java.base/jdk.internal.value=ALL-UNNAMED" })
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class FlatteningBench {

  int[] primitives = new int[1_000_000];
  {
    Arrays.setAll(primitives, v -> v);
  }

  /*not-value*/ class IdentityInteger {
    final int value;

    public IdentityInteger(int value) {
      this.value = value;
    }
  }

  IdentityInteger[] identities;
  {
    identities = new IdentityInteger[1_000_000];
    Arrays.setAll(identities, IdentityInteger::new);
  }

  value class ValueInteger {
    final int value;

    public ValueInteger(int value) {
      this.value = value;
    }
  }

  ValueInteger[] nullRestrictedValues;
  {
    //values = new ValueInteger[1_000_000];
    nullRestrictedValues = (ValueInteger[]) newNullRestrictedAtomicArray(ValueInteger.class, 1_000_000, new ValueInteger(0));
    Arrays.setAll(nullRestrictedValues, ValueInteger::new);
  }

  value class ValueIntegerBox {
    @NullRestricted
    final ValueInteger value;

    public ValueIntegerBox(ValueInteger value) {
      this.value = value;
    }
  }

  ValueIntegerBox[] nullRestrictedValueBoxes;
  {
    //values = new ValueIntegerBox[1_000_000];
    nullRestrictedValueBoxes = (ValueIntegerBox[]) newNullRestrictedAtomicArray(ValueIntegerBox.class, 1_000_000, new ValueIntegerBox(new ValueInteger(0)));
    Arrays.setAll(nullRestrictedValueBoxes, i -> new ValueIntegerBox(new ValueInteger(i)));
  }

  @Benchmark
  public int primitives() {
    var sum = 0;
    for (int primitive : primitives) {
      sum += primitive;
    }
    return sum;
  }

  @Benchmark
  public int identities() {
    var sum = 0;
    for (IdentityInteger identity : identities) {
      sum += identity.value;
    }
    return sum;
  }

  @Benchmark
  public int nullRestrictedValues() {
    var sum = 0;
    for (ValueInteger value : nullRestrictedValues) {
      sum += value.value;
    }
    return sum;
  }

  @Benchmark
  public int nullRestrictedValueBoxes() {
    var sum = 0;
    for (ValueIntegerBox box : nullRestrictedValueBoxes) {
      sum += box.value.value;
    }
    return sum;
  }
}
