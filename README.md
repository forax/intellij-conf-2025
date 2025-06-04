# intellij-conf-2025
The repo of the presentation of Valhalla at IntelliJ Conf 2025

## Build the valhalla prototype JDK

Clone the OpenJDK valhalla repository
```bash
git clone https://github.com/openjdk/valhalla
```

Your default JDK should be Java 24 or 25
```bash
cd valhalla
./configure
make images
```

## Setup IntelliJ

Download latest IntelliJ Idea early access build
  https://www.jetbrains.com/idea/nextversion/

Run it and clone the current Github repository in IntelliJ (`File > New > Project from Version Control`)
  https://github.com/forax/intellij-conf-2025

In `File > Project Structure > SDKs` add the JDK image you have just built

  `your_path/valhalla/build/macosx-aarch64-server-release/images/jdk`

And set it as your JDK for the current project (in `File > Project Structure > Project > SDK`),
the language level should be `SDK default`.

You can build the repo using Maven, `mvn clean package`

At that point, you should be able to run the examples in IntelliJ.


## Benchmarks

Optionally, you can run the perf benchmarks by uncommenting the @Benchmark methods,
re-compiling with Maven `mvn clean package` and running

```
your_path/valhalla/build/macosx-aarch64-server-release/images/jdk/bin/java target/benchmarks.jar
```
