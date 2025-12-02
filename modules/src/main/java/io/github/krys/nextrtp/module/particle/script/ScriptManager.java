package io.github.krys.nextrtp.module.particle.script;

import io.github.krys.nextrtp.common.reload.Loader;
import org.bukkit.plugin.Plugin;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public final class ScriptManager implements Loader {

  private final Plugin plugin;
  private final ScriptEngine engine;
  private final Invocable invocableEngine;
  private final Map<String, AnimationScript<?>> animationCache = new HashMap<>();

  public ScriptManager(Plugin plugin) {
    this.plugin = plugin;

    this.engine = JSEngine.getEngine();

    if (!(engine instanceof Invocable)) {
      throw new IllegalStateException("JavaScript engine is not Invocable.");
    }

    this.invocableEngine = (Invocable) engine;
  }

  public void load() {
    animationCache.clear();
    File animationsDir = new File(plugin.getDataFolder(), "particles");

    if (!animationsDir.exists()) {
      animationsDir.mkdirs();
    }

    File scripts = new File(animationsDir, "scripts");

    if (!scripts.exists()) {
      scripts.mkdirs();
      final String[] files = {"dna", "vortex", "double-spiral", "halo", "spiral", "wave-spiral", "orbital"};

      for (String fileName : files) {
        plugin.saveResource("particles/scripts/" + fileName + ".js", false);
      }
    }

    File[] scriptFiles = scripts.listFiles((dir, name) -> name.endsWith(".js"));
    if (scriptFiles == null) {
      return;
    }

    for (File file : scriptFiles) {
      String name = file.getName().substring(0, file.getName().length() - 3);
      try {
        String scriptContent = new String(Files.readAllBytes(file.toPath()));

        Object jsObject = engine.eval(scriptContent);

        if (jsObject == null) {
          continue;
        }

        AnimationScript animation = invocableEngine.getInterface(jsObject, AnimationScript.class);

        if (animation != null) {
          animationCache.put(name, animation);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public <T> AnimationScript<T> getAnimation(String name) {
    return (AnimationScript<T>) animationCache.get(name);
  }
}