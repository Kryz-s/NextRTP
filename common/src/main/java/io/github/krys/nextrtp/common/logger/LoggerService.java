package io.github.krys.nextrtp.common.logger;

import io.github.krys.nextrtp.common.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerService implements Service {

  private static final Logger logger = LoggerFactory.getLogger("NextRTP");

  public void info(String msg, Object... objects) {
    if(objects.length == 0) {
      logger.info(msg);
      return;
    }
    logger.info(msg, objects);
  }

  public void warn(String msg, Object... objects){
    if(objects.length == 0) {
      logger.warn(msg);
      return;
    }
    logger.warn(msg, objects);
  }

  public void err(String msg, Object... objects) {
    if(objects.length == 0) {
      logger.error(msg);
      return;
    }
    logger.error(msg, objects);
  }
}