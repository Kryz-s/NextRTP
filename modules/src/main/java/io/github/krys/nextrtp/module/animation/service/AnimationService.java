package io.github.krys.nextrtp.module.animation.service;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import io.github.krys.nextrtp.module.animation.config.AnimationUIConfiguration;
import io.github.krys.nextrtp.module.animation.model.Animation;
import io.github.krys.nextrtp.module.animation.task.AnimationUIController;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class AnimationService {

  public static final Set<Animation> ANIMATIONS = ConcurrentHashMap.newKeySet();
  public static final Set<UUID> PLAYERS_IN_ANIMATION = ConcurrentHashMap.newKeySet();

  private final AnimationUIConfiguration animationUIConfiguration;
  private final AbstractConfiguration configuration;
  private final Runnable init;

  public AnimationService(AnimationUIConfiguration animations,
                          AbstractConfiguration configuration,
                          Runnable controller) {
    this.animationUIConfiguration = animations;
    this.configuration = configuration;
    this.init = controller;
    this.init.run();
  }

  public void startAnimation(Player player, String id) {
    PLAYERS_IN_ANIMATION.add(player.getUniqueId());

    final Section section = configuration.getSection(id);

    final var title = animationUIConfiguration.getUI(section.getString("title"));
    final var subtitle = animationUIConfiguration.getUI(section.getString("subtitle"));
    final var actionBar = animationUIConfiguration.getUI(section.getString("action-bar"));

    final Animation activeAnimation = new Animation(player, title, subtitle, actionBar);
    ANIMATIONS.add(activeAnimation);
    
    if (!AnimationUIController.running) {
      this.init.run();
      AnimationUIController.running = true;
    }
  }

  public void stopAnimation(Player player) {
    UUID uuid = player.getUniqueId();
    for (Animation animation : ANIMATIONS) {
      if (animation.player().getUniqueId().equals(uuid)) {
        animation.markFinalized();
      }
    }
    PLAYERS_IN_ANIMATION.remove(uuid);
  }
}
