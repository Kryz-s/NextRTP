package io.github.krys.nextrtp.common;

import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.condition.ConditionProcessor;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.common.module.Module;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface NextAPI {

  void registerModule(Module<PluginBridge> module);

  @Nullable
  Module<PluginBridge> getModule(String id);

  <T> void registerConditionProcessor(String id, ConditionProcessor<T> conditionProcessor);

  void startTeleport(Player player, @Nullable BaseTeleportInfo info, @Nullable CheckStatementValidator validator, TeleportIdentity identity);
}
