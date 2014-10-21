package io.packagecloud.client;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

/**
 * Static class for getting Loggers
 */
public class LoggerProvider {

    /**
     * Get the logger.
     *
     * @return the logger
     */
    public static Logger getLogger(){
        Formatter formatter = new LogFormatter();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        Logger logger = Logger.getAnonymousLogger();

        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        return logger;
    }

}
