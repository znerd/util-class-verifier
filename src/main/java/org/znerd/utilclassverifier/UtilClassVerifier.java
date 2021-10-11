package org.znerd.utilclassverifier;

import org.znerd.utilclassverifier.internal.SingleClassChecker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class UtilClassVerifier {

  private UtilClassVerifier() {
    throw new UnsupportedOperationException();
  }

  @Contract("_ -> new")
  @NotNull
  public static <T> Checker<T> forClass(final Class<T> clazz) {
    return new SingleClassChecker<>(clazz);
  }
}
