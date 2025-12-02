package io.github.krys.nextrtp.core.rtp.identity;

import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import org.jetbrains.annotations.NotNull;

public enum TeleportIdentityTypes implements TeleportIdentity {
  CORE("core"),
  SIGN("sign"),
  LOCATION("location");

  private final @NotNull String id;

  TeleportIdentityTypes(@NotNull String id) {
    this.id = id;
  }

  @NotNull
  @Override
  public String id() {
    return id;
  }
}
