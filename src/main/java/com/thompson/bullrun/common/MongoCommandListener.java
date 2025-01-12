package com.thompson.bullrun.common;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoCommandListener implements CommandListener {
    private static final Logger logger = LoggerFactory.getLogger(MongoCommandListener.class);

    @Override
    public void commandStarted(CommandStartedEvent event) {
        logger.info("MongoDB command started: {}", event.getCommandName());
        logger.info("Command details: {}", event.getCommand().toJson());
    }

    @Override
    public void commandSucceeded(CommandSucceededEvent event) {
        logger.info("MongoDB command succeeded: {}", event.getCommandName());
    }

    @Override
    public void commandFailed(CommandFailedEvent event) {
        logger.error("MongoDB command failed: {}", event.getCommandName(), event.getThrowable());
    }
}