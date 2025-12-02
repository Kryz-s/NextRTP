package io.github.krys.nextrtp.module.sign.listener;

import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import io.github.krys.nextrtp.core.message.Messager;
import io.github.krys.nextrtp.core.rtp.TeleportPending;
import io.github.krys.nextrtp.core.rtp.identity.TeleportIdentityTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

@NullMarked
public final class SignListener implements Listener {

  private final AbstractConfiguration config;
  private final NextAPI api;
  private final NamespacedKey namespacedKey;
  private final PlainTextComponentSerializer plain = PlainTextComponentSerializer.plainText();

  private final Function<Location, TagResolver> locationArguments = location -> TagResolver.resolver(
    unparsed("x", String.valueOf(location.getBlockX())),
    unparsed("y", String.valueOf(location.getBlockY())),
    unparsed("z", String.valueOf(location.getBlockZ()))
  );

  public SignListener(AbstractConfiguration configuration, NextAPI api) {
    this.config = configuration;
    this.api = api;
    this.namespacedKey = new NamespacedKey("nextrtp", "rtp_sign");
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onSign(SignChangeEvent event) {
    var player = event.getPlayer();
    List<Component> lines = event.lines();

    if (!player.hasPermission("nextrtp.sign")) return;
    if (!isRtpSign(lines, config.getString("sign.line").orElse(null), config.getBoolean("sign.ignore-case", true)))
      return;

    var block = event.getBlock();
    var sign = (Sign) block.getState();
    var l = block.getLocation();

    PersistentDataContainer container = sign.getPersistentDataContainer();
    Boolean is = container.get(namespacedKey, PersistentDataType.BOOLEAN);

    if (container.has(namespacedKey)) return;
    if (is != null && !is) return;

    container.set(namespacedKey, PersistentDataType.BOOLEAN, true);
    if (!sign.update()) throw new UnsupportedOperationException("The update of the sign not success.");

    Messager.SIGN_SUCCESS.accept(player, locationArguments.apply(l));
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBlockInteract(PlayerInteractEvent event) {
    var block = event.getClickedBlock();
    if (block == null) return;

    var state = block.getState();
    if (!(state instanceof Sign sign)) return;

    PersistentDataContainer container = sign.getPersistentDataContainer();
    Boolean is = container.get(namespacedKey, PersistentDataType.BOOLEAN);
    if (is == null) return;
    if (!is) return;

    var player = event.getPlayer();

    final World world = player.getWorld();
    final WorldTeleportInfo teleportInfo = Registries.WORLD_TELEPORT_INFO.get(world.getName());

    api.startTeleport(player, teleportInfo, TeleportPending.DEFAULT, TeleportIdentityTypes.SIGN);
  }

  private boolean isRtpSign(List<Component> lines, @Nullable String expectedLine, boolean ignoreCase) {
    if (expectedLine == null) return false;
    if (expectedLine.isBlank()) return false;
    for (Component line : lines) {
      String text = plain.serialize(line);
      if (ignoreCase) {
        if (text.equalsIgnoreCase(expectedLine)) {
          return true;
        }
      } else {
        if (text.equals(expectedLine)) {
          return true;
        }
      }
    }
    return false;
  }
}

