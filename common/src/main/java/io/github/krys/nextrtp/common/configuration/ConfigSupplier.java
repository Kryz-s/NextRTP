package io.github.krys.nextrtp.common.configuration;

import java.io.IOException;

public interface ConfigSupplier<T> {

  T get() throws IOException;
}
