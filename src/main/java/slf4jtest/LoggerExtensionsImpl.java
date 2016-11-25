package slf4jtest;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

class LoggerExtensionsImpl implements LoggerExtensions {
    private final Settings settings;
    private final long startTime;
    private final Queue<LogMessage> rows = new ConcurrentLinkedQueue<>();

    public LoggerExtensionsImpl(Settings settings, long startTime) {
        this.settings = settings;
        this.startTime = startTime;
    }

    public void record(LogMessage message) {
        doLogging(message);
        doConsole(message);
    }

    private void doLogging(LogMessage message) {
        if (settings.isEnabled(message.level))
            rows.add(message);
    }

    private void doConsole(LogMessage message) {
        if (settings.isEnabled(message.level) && settings.printingEnabled && !isPrintSuppressed(message.text)) {
            PrintStream out = settings.printStreams.get(message.level);
            out.println(layout(message));
            out.flush();
        }
    }

    public String layout(LogMessage message) {
        long delta = message.timeStamp - startTime;
        return delta +
                " " + message.level +
                " [" + message.threadName + "] " +
                message.logName +
                " - " + message.text;
    }

    private boolean isPrintSuppressed(String msg) {
        for (String regex : settings.printSuppressions) {
            if (msg.matches(regex)) return true;
        }
        return false;
    }

    public Collection<LogMessage> lines() {
        return Collections.unmodifiableCollection(rows);
    }

    public boolean contains(String regex) {
        for (LogMessage row : rows) {
            if (row.text.matches(regex)) return true;
        }
        return false;
    }

    public boolean contains(LogLevel level, String regex) {
        for (LogMessage row : rows) {
            if (row.level == level && row.text.matches(regex)) return true;
        }
        return false;
    }

    public void clear() {
        rows.clear();
    }
}
