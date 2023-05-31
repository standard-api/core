package ai.stapi.utils;

import java.util.List;
import java.util.function.Supplier;

public interface Retryable<T> {

  static <T> T retry(int maxRetries, long retryInterval, Supplier<T> supplier,
                     Integer expectedInteger) {
    int retries = maxRetries;
    T result = null;
    while (retries != 0) {
      result = supplier.get();
      if (
          result != null
              && (
              result instanceof List && ((List<?>) result).size() >= expectedInteger
                  || result instanceof Number number && number.intValue() >= expectedInteger
          )
      ) {
        break;
      }
      try {
        Thread.sleep(retryInterval);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      retries--;
    }
    return result;
  }

  T run();
}
