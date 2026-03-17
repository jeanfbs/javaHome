package br.com.javahome.logging;

public interface Logger {

    void debug(String msg, Throwable thrown);
    void info(String msg, Throwable thrown);
    void warn(String msg, Throwable thrown);
    void error(String msg, Throwable thrown);

    void debug(String msg);
    void info(String msg);
    void warn(String msg);
    void error(String msg);

}
