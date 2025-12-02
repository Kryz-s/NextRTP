package io.github.krys.nextrtp.core;

import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.condition.ConditionProcessor;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.core.module.AbstractModule;
import io.github.krys.nextrtp.common.module.Module;
import io.github.krys.nextrtp.common.module.ModuleManager;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.core.rtp.TeleportPendingRepository;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class NextAPIImpl implements NextAPI {

  private final ModuleManager<PluginBridge> moduleManager;
  private final TeleportPendingRepository repository;

  public NextAPIImpl(ModuleManager<PluginBridge> moduleManager, TeleportPendingRepository repository) {
    this.moduleManager = moduleManager;
    this.repository = repository;
  }

  @Override
  public void registerModule(Module<PluginBridge> module) {
    this.moduleManager.register(module);
  }

  @Override
  public @Nullable AbstractModule getModule(String id) {
    return (AbstractModule) this.moduleManager.getModule(id);
  }

  @Override
  public <T> void registerConditionProcessor(String id, ConditionProcessor<T> conditionProcessor) {
    Registries.CONDITION_PROCESSOR.addProcessor(id, conditionProcessor);
  }

  @Override
  public void startTeleport(Player player, @Nullable BaseTeleportInfo info, @Nullable CheckStatementValidator validator, TeleportIdentity identity) {
    repository.start(player, info, validator, identity);
  }
}
