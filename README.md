# Utility Class Verifier #

Helper for verifying Java utility classes.


## Usage ##

To use this library with Maven, add this dependency:

    <dependency>
      <groupId>com.znerd</groupId>
      <artifactId>util-class-verifier</artifactId>
      <version>0.2.3</version>
    </dependency>

( Update the version accordingly. )

Then, use it from your test code e.g. as follows:

    @Test
    void utilClass() {
      UtilClassVerifier.forClass(XmlUtils.class).verify();
    }


## Development ##

This code uses:

* JetBrains' JSR 305 library for nullability annotations (`@NotNull` and `@Nullable`).

Code is under:

* `src/main/java/`

The automated tests are under:

* `src/test/java/`


## Compile, Build, Unit Test ##

This library is built using Apache Maven.

To perform a full build, and install the library in your local Maven repository, run:

    mvn clean install
