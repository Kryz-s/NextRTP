package io.github.krys.nextrtp.core.command.excp;

public final class NoWorldPermissionException extends RuntimeException {
  public final String worldName;

  public NoWorldPermissionException(String msg) {
    this.worldName = msg;
  }
}
