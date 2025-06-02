import com.github.forax.valhalla.Complex;

static /*value*/ class JavaBox {
  private final Complex/*!*/ complex;

  public JavaBox(Complex complex) {
    println("this: " + this);
    this.complex = complex;
    //super();
  }

  @Override
  public String toString() {
    return "JavaBox(" + complex + ")";
  }
}

// export JAVA_HOME=/Users/forax/valhalla-live/valhalla/build/macosx-aarch64-server-release/images/jdk/
// $JAVA_HOME/bin/java --enable-preview src/main/java/JavaBoxDemo.java
void main() {
  var complex = new Complex(3.0, 4.0);
  var box = new JavaBox(complex);
  //println("box state :" + box);
}

