package br.com.javahome.logging;

import br.com.javahome.ApplicationContext;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.System.Logger.Level.*;

public final class LoggerFactory implements Logger {

    private static final StackWalker STACK_WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private final System.Logger logger;
    private final Class<?> aClass;
    private final String logPattern;
    private final System.Logger.Level[] levels;

    private LoggerFactory(final Class<?> aClass) {
        this.aClass = aClass;
        logger = createLogger();
        logPattern = ApplicationContext.getProperty("app.logging-pattern");
        String levelName = ApplicationContext.getProperty("app.logging.level");
        var level = levelName.isBlank() ? DEBUG : valueOf(levelName);
        System.Logger.Level[] allLevels = {DEBUG, INFO, WARNING, ERROR};
        levels = Arrays.stream(allLevels)
                .filter(it -> it.getSeverity() >= level.getSeverity()).toArray(System.Logger.Level[]::new);

    }

    public static LoggerFactory getLogger(final Class<?> aClass) {
        return new LoggerFactory(aClass);
    }

    @Override
    public synchronized void debug(String msg, Throwable thrown) {
        if(isEnable(DEBUG)){
            logger.log(DEBUG, null, msg, thrown);
        }
    }

    private synchronized boolean isEnable(final System.Logger.Level level) {
        return Arrays.stream(levels).anyMatch(it -> it == level);
    }

    @Override
    public synchronized void info(String msg, Throwable thrown) {
        if(isEnable(INFO)){
            logger.log(INFO, null, msg, thrown);
        }
    }

    @Override
    public synchronized void warn(String msg, Throwable thrown) {
        if(isEnable(WARNING)){
            logger.log(WARNING, null, msg, thrown);
        }
    }

    @Override
    public synchronized void error(String msg, Throwable thrown) {
        if(isEnable(ERROR)){
            logger.log(ERROR, null, msg, thrown);
        }
    }

    @Override
    public synchronized void debug(String msg) {
        if(isEnable(DEBUG)){
            logger.log(DEBUG, msg);
        }
    }

    @Override
    public synchronized void info(String msg) {
        if(isEnable(INFO)){
            logger.log(INFO, msg);
        }
    }

    @Override
    public synchronized void warn(String msg) {
        if(isEnable(WARNING)){
            logger.log(WARNING, msg);
        }
    }

    @Override
    public synchronized void error(String msg) {
        if(isEnable(ERROR)){
            logger.log(ERROR, msg);
        }
    }


    private System.Logger createLogger() {
        return new System.Logger() {
            private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

            private static String getLevelText(Level level) {
                if (level.getName().equals("DEBUG") || level.getName().equals("ERROR")) {
                    return level.getName().substring(0, 5);
                }
                return level.getName().substring(0, 4);
            }

            @Override
            public String getName() {
                return aClass.getName();
            }

            @Override
            public boolean isLoggable(Level level) {
                return true;
            }

            @Override
            public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
                String levelText = getLevelText(level);
                StackWalker.StackFrame caller = getCaller();
                StringWriter out = new StringWriter();
                PrintWriter printWriter = new PrintWriter(out);
                thrown.printStackTrace(printWriter);
                var threadName = getThreadName();
                System.out.printf(logPattern + "%s\n", DATE_TIME_FORMATTER.format(LocalDateTime.now()), levelText.toUpperCase(),
                        threadName, aClass.getCanonicalName(), caller.getMethodName().replace("<", "").replace(">", ""),
                        caller.getLineNumber(), msg + " " + out);

            }

            @Override
            public void log(Level level, ResourceBundle bundle, String format, Object... params) {
                String levelText = getLevelText(level);
                StackWalker.StackFrame caller = getCaller();
                var threadName = getThreadName();
                System.out.printf(logPattern + format, DATE_TIME_FORMATTER.format(LocalDateTime.now()), levelText.toUpperCase(),
                        threadName, aClass.getCanonicalName(), caller.getMethodName().replace("<", "").replace(">", ""),
                        caller.getLineNumber(), params);
                System.out.println();
            }

            private static String getThreadName() {
                Thread thread = Thread.currentThread();
                int beginIndex = thread.getName().indexOf(".") + 1;
                return beginIndex > 0 ? thread.getName().substring(beginIndex) : thread.getName();
            }
        };
    }

    private StackWalker.StackFrame getCaller() {
        return STACK_WALKER.walk(stream -> Objects.requireNonNull(stream
                .filter(frame -> frame.getClassName().contains(aClass.getSimpleName()))
                .findFirst()
                .orElse(null))
        );
    }
}
