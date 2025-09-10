package SistemaDeGestaoDePedidosERelatorios.VOs;

public class LogLevel {

    public enum Level {
        ERROR,
        WARN,
        INFO
    }

    private final Level levelError;

    public LogLevel(Level level) {
        if (level == null) {
            throw new IllegalArgumentException("LogLevel cannot be null.");
        }

        this.levelError = level;
    }

    public static LogLevel error() {
        return new LogLevel(Level.ERROR);
    }

    public static LogLevel warn() {
        return new LogLevel(Level.WARN);
    }

    public static LogLevel info() {
        return new LogLevel(Level.INFO);
    }

    public Level getLevel() {
        return levelError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogLevel)) return false;
        LogLevel that = (LogLevel) o;
        return levelError == that.levelError;
    }

    @Override
    public int hashCode() {
        return levelError.hashCode();
    }

    @Override
    public String toString() {
        return "LogLevel {" + levelError + "}";
    }
}