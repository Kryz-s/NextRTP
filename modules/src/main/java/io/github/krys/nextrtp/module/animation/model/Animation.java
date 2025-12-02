package io.github.krys.nextrtp.module.animation.model;

import org.bukkit.entity.Player;

public final class Animation {

  private final Player player;
  private final UIContainer title;
  private final UIContainer subtitle;
  private final UIContainer actionBar;

  private boolean timesSent;
  private boolean finalized;

  public Animation(Player player, UIContainer title, UIContainer subtitle, UIContainer actionBar) {
    this.player = player;
    this.title = title;
    this.subtitle = subtitle;
    this.actionBar = actionBar;
  }

  public Player player() {
    return player;
  }

  public UIContainer actionBar() {
    return actionBar;
  }

  public UIContainer title() {
    return title;
  }

  public UIContainer subtitle() {
    return subtitle;
  }

  public boolean hasSentTimes() {
    return timesSent;
  }

  public void markTimesSent() {
    this.timesSent = true;
  }

  public void markFinalized() {
    this.finalized = true;
  }

  public boolean isFinalized() {
    return this.finalized;
  }
}

