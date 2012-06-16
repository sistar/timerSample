package ui;

import org.jboss.solder.messages.Message;

import org.jboss.solder.logging.Log;

import org.jboss.solder.logging.MessageLogger;


@MessageLogger
public interface ProcessLog {
    @Log @Message("Spotted %s processes")
    void processesQueried(String processes);
}