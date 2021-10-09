package com.znerd.utilclassverifier.internal;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isInterface;
import static java.lang.reflect.Modifier.isPrivate;
import static java.util.Objects.requireNonNull;

import com.znerd.utilclassverifier.Checker;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SingleClassChecker<T> implements Checker<T> {

  @NotNull private final Class<T> c;

  @Contract(pure = true)
  public SingleClassChecker(@NotNull final Class<T> clazz) {
    this.c = requireNonNull(clazz, "clazz");
  }

  @Override
  public void verify() {
    new Worker().work();
  }

  private class Worker {

    @NotNull private final String className;
    @NotNull private final Constructor<?>[] constructors;
    @NotNull private final List<String> issues;

    private Worker() {
      this.className = c.getName();
      this.constructors = c.getDeclaredConstructors();
      this.issues = new ArrayList<>();
    }

    private void work() {
      if (checkIsFinalClass()) {
        checkAllConstructorsPrivate();
        checkOnlyNonArgConstructor();
        checkDefaultConstructorThrowsUnsupportedOperationException();
      }

      final String message = constructMessage();
      if (message != null) {
        throw new AssertionError(message);
      }
    }

    /** @return true if it is a concrete class */
    private boolean checkIsFinalClass() {
      if (isInterface(c.getModifiers())) {
        addIssue("Expected [%s] to be a class, but it is an interface.", className);
        return false;
      } else if (c.isEnum()) {
        addIssue("Expected [%s] to be a class, but it is an enum.", className);
        return false;
      } else if (isAbstract(c.getModifiers())) {
        addIssue("Expected class [%s] to be concrete, but it is abstract.", className);
        return false;
      } else if (!isFinal(c.getModifiers())) {
        addIssue("Expected class [%s] to be final.", className);
      }
      return true;
    }

    private void checkAllConstructorsPrivate() {
      for (final Constructor<?> constructor : this.constructors) {
        checkPrivateConstructor(constructor);
      }
    }

    private void addIssue(@NotNull final String message, @NotNull final Object... args) {
      issues.add(String.format(message, args));
    }

    private void checkPrivateConstructor(@NotNull final Constructor<?> constructor) {
      if (!isPrivate(constructor.getModifiers())) {
        addIssue("Constructor [%s] is not private.", constructor);
      }
    }

    private void checkDefaultConstructorThrowsUnsupportedOperationException() {
      final Constructor<?> constructor;
      try {
        constructor = c.getDeclaredConstructor();
      } catch (final NoSuchMethodException e) {
        addIssue("Class [%s] does not have a default constructor.", className);
        return;
      }

      constructor.setAccessible(true);
      try {
        constructor.newInstance();
        addIssue(
            "Expected UnsupportedOperationException when invoking class [%s] default constructor.",
            className);
      } catch (final InvocationTargetException e) {
        final Throwable cause = e.getCause();
        if (!(cause instanceof UnsupportedOperationException)) {
          addIssue(
              "Expected class [%s] default constructor to throw UnsupportedOperationException when invoked, but instead it threw [%s] with message [%s].",
              className, cause.getClass().getName(), cause.getMessage());
        }
      } catch (final IllegalAccessException | InstantiationException e) {
        addIssue( // FIXME
            "Caught unexpected [%s] while invoking default constructor on class [%s].",
            className, e.getClass().getName());
      }
    }

    private void checkOnlyNonArgConstructor() {
      for (final Constructor<?> constructor : constructors) {
        if (constructor.getParameterCount() > 0) {
          addIssue(
              "Expected class [%s] to have only 1 non-argument constructor, but found [%s].",
              className, constructor);
        }
      }
    }

    @Nullable
    private String constructMessage() {
      if (issues.isEmpty()) {
        return null;
      }

      final StringBuilder buffer = new StringBuilder();
      buffer.append("Class [").append(className).append("] does not validate as a utility class:");
      for (final String issue : issues) {
        buffer.append("\n- ").append(issue);
      }
      return buffer.toString();
    }
  }
}
