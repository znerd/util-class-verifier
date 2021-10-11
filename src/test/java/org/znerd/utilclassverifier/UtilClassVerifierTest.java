package org.znerd.utilclassverifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.ElementType;
import org.junit.jupiter.api.Test;

class UtilClassVerifierTest {

  @Test
  void testSelf() {
    UtilClassVerifier.forClass(UtilClassVerifier.class).verify();
  }

  @Test
  void testAbstractClass__java_lang_Number() {
    final String expectedMessage =
        "Class [java.lang.Number] does not validate as a utility class:\n"
            + "- Expected class [java.lang.Number] to be concrete, but it is abstract.";
    try {
      UtilClassVerifier.forClass(Number.class).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testAnnotation__java_lang_Deprecated() {
    final String expectedMessage =
        "Class [java.lang.Deprecated] does not validate as a utility class:\n"
            + "- Expected [java.lang.Deprecated] to be a class, but it is an interface.";
    try {
      UtilClassVerifier.forClass(Deprecated.class).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testClassWithoutDefaultConstructor__java_lang_Boolean() {
    final String expectedMessage =
        "Class [java.lang.Boolean] does not validate as a utility class:\n"
            + "- Constructor [public java.lang.Boolean(boolean)] is not private.\n"
            + "- Constructor [public java.lang.Boolean(java.lang.String)] is not private.\n"
            + "- Expected class [java.lang.Boolean] to have only 1 non-argument constructor, but found [public java.lang.Boolean(boolean)].\n"
            + "- Expected class [java.lang.Boolean] to have only 1 non-argument constructor, but found [public java.lang.Boolean(java.lang.String)].\n"
            + "- Class [java.lang.Boolean] does not have a default constructor.";
    try {
      UtilClassVerifier.forClass(Boolean.class).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testEnum__java_lang_annotation_ElementType() {
    final String expectedMessage =
        "Class [java.lang.annotation.ElementType] does not validate as a utility class:\n"
            + "- Expected [java.lang.annotation.ElementType] to be a class, but it is an enum.";
    try {
      UtilClassVerifier.forClass(ElementType.class).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testExceptionClass__java_lang_ArithmeticException() {
    final String expectedMessage =
        "Class [java.lang.ArithmeticException] does not validate as a utility class:\n"
            + "- Expected class [java.lang.ArithmeticException] to be final.\n"
            + "- Constructor [public java.lang.ArithmeticException()] is not private.\n"
            + "- Constructor [public java.lang.ArithmeticException(java.lang.String)] is not private.\n"
            + "- Expected class [java.lang.ArithmeticException] to have only 1 non-argument constructor, but found [public java.lang.ArithmeticException(java.lang.String)].\n"
            + "- Expected UnsupportedOperationException when invoking class [java.lang.ArithmeticException] default constructor.";
    try {
      UtilClassVerifier.forClass(ArithmeticException.class).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testInterface__java_lang_Appendable() {
    final String expectedMessage =
        "Class [java.lang.Appendable] does not validate as a utility class:\n"
            + "- Expected [java.lang.Appendable] to be a class, but it is an interface.";
    try {
      UtilClassVerifier.forClass(Appendable.class).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testConstructorThatThrowsUnexpectedExceptionWithMessage() {
    final Class<?> clazz = ThrowsUnexpectedExceptionWithMessage.class;
    final String className = clazz.getName();
    final String expectedMessage =
        "Class ["
            + className
            + "] does not validate as a utility class:\n"
            + "- Expected class ["
            + className
            + "] default constructor to throw UnsupportedOperationException when invoked, but instead it threw [java.lang.NumberFormatException] with message [Test 1-2-3.].";
    try {
      UtilClassVerifier.forClass(clazz).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  @Test
  void testConstructorThatThrowsUnexpectedExceptionWithoutMessage() {
    final Class<?> clazz = ThrowsUnexpectedExceptionWithoutMessage.class;
    final String className = clazz.getName();
    final String expectedMessage =
        "Class ["
            + className
            + "] does not validate as a utility class:\n"
            + "- Expected class ["
            + className
            + "] default constructor to throw UnsupportedOperationException when invoked, but instead it threw [java.lang.NumberFormatException] with message [null].";
    try {
      UtilClassVerifier.forClass(clazz).verify();
    } catch (final AssertionError e) {
      assertEquals(expectedMessage, e.getMessage());
      return;
    }

    fail("Expected AssertionError.");
  }

  private static final class ThrowsUnexpectedExceptionWithMessage {

    private ThrowsUnexpectedExceptionWithMessage() {
      throw new NumberFormatException("Test 1-2-3.");
    }
  }

  private static final class ThrowsUnexpectedExceptionWithoutMessage {

    private ThrowsUnexpectedExceptionWithoutMessage() {
      throw new NumberFormatException();
    }
  }
}
