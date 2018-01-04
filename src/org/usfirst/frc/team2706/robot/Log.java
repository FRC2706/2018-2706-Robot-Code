package org.usfirst.frc.team2706.robot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;

/**
 * Logs to DriverStation at levels debug, info, warning, error
 */
public class Log {

    public static final String LOGGER_TABLE = "logging-level";
    
    public static final String ROOT_LOGGER_NAME = "";

    private static final Logger logger = Logger.getLogger(ROOT_LOGGER_NAME);
    
    private static TableEntryListener updateListener;
    
    private static ByteArrayOutputStream out;
    
    private static final Formatter formatter = new Formatter() {
        @Override
        public String format(LogRecord record) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date dt = new Date(record.getMillis());
            String S = sdf.format(dt);

            return record.getLevel() + " " + S + " " + record.getSourceClassName() + "."
                            + record.getSourceMethodName() + "() " + record.getLoggerName()
                            + " " + record.getMessage() + "\n";
        }
    };

    public static String getCallerClassName() throws ClassNotFoundException {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        return Class.forName(stElements[4].getClassName()).getSimpleName() + "."
                        + stElements[4].getMethodName();
    }

    /**
     * Configures the logger
     */
    public static void setUpLogging() {
        ConsoleHandler ch = new ConsoleHandler();
        out = new ByteArrayOutputStream();
        StreamHandler tableOut = new EStreamHandler(out, formatter);
        
        try {
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);

            for (Handler h : logger.getHandlers()) {
                logger.removeHandler(h);
            }

            ch.setFormatter(formatter);
            

            logger.addHandler(ch);
            logger.addHandler(tableOut);

            ch.setLevel(Level.ALL);
            tableOut.setLevel(Level.ALL);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    ch.flush();
                    ch.close();
                    
                    tableOut.flush();
                    tableOut.close();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        
        updateListener = new TableEntryListener() {

            @Override
            public void valueChanged(NetworkTable source, String key,
                            NetworkTableEntry entry, NetworkTableValue value, int flags) {
                Level level = Level.parse(((int)entry.getNumber(Level.ALL.intValue()))+"");
                ch.setLevel(level);
                tableOut.setLevel(level);
                logger.setLevel(level);
                
            }
        };
        
        NetworkTableInstance.getDefault().getTable(LOGGER_TABLE).addEntryListener("level", updateListener, EntryListenerFlags.kUpdate);
    }

    public static void updateTableLog() {
        byte[] a = NetworkTableInstance.getDefault().getTable(LOGGER_TABLE).getEntry("Value").getRaw(new byte[0]);
        byte[] b = out.toByteArray();
        
        byte[] results = new byte[0];
        
        if(a == new byte[0]) {
            results = b;
        }
        else if(b.length == 0) {
            return;
        }
        else {
            results = new byte[a.length + b.length]; 
            System.arraycopy(a, 0, results, 0, a.length); 
            System.arraycopy(b, 0, results, a.length, b.length); 
        }
        
        out.reset();
        
        NetworkTableInstance.getDefault().getTable(LOGGER_TABLE).getEntry("Value").setRaw(results);
    }
    
    /**
     * Debug log
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     */
    public static void d(Object name, Object message) {
        LogLevels.DEBUG.log(name, message);
    }

    /**
     * Debug log with exception
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     * @param t The Throwable to log
     */
    public static void d(Object name, Object message, Throwable t) {
        LogLevels.DEBUG.log(name, message, t);
    }

    /**
     * Info log
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     */
    public static void i(Object name, Object message) {
        LogLevels.INFO.log(name, message);
    }

    /**
     * Info log with exception
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     * @param t The Throwable to log
     */
    public static void i(Object name, Object message, Throwable t) {
        LogLevels.INFO.log(name, message, t);
    }

    /**
     * Warning log
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     */
    public static void w(Object name, Object message) {
        LogLevels.WARNING.log(name, message);
    }

    /**
     * Warning log with exception
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     * @param t The Throwable to log
     */
    public static void w(Object name, Object message, Throwable t) {
        LogLevels.WARNING.log(name, message, t);
    }

    /**
     * Error log
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     */
    public static void e(Object name, Object message) {
        LogLevels.ERROR.log(name, message);
    }

    /**
     * Error log with exception
     * 
     * @param name The object (or String) name to log
     * @param message The object (or String) message to log
     * @param t The Throwable to log
     */
    public static void e(Object name, Object message, Throwable t) {
        LogLevels.ERROR.log(name, message, t);
    }

    private static enum LogLevels {
        DEBUG(Level.CONFIG), INFO(Level.INFO), WARNING(Level.WARNING), ERROR(Level.SEVERE);

        private final Level level;

        private LogLevels(Level level) {
            this.level = level;
        }

        public void log(Object name, Object message) {
            String[] cm = new String[] {"Unknown", "Unknown"};

            try {
                cm = getCallerClassName().split("\\.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Logger.getLogger(name.toString()).logp(level, cm[0], cm[1], message.toString());
        }

        public void log(Object name, Object message, Throwable t) {
            String[] cm = new String[] {"Unknown", "Unknown"};

            try {
                cm = getCallerClassName().split("\\.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Logger.getLogger(name.toString()).logp(level, cm[0], cm[1], message.toString());
        }
    }
    
    private static class EStreamHandler extends StreamHandler {
        public EStreamHandler(OutputStream out, Formatter formatter) {
            super(out, formatter);
        }
        
        @Override
        public synchronized void publish(LogRecord record) {       
            if (!isLoggable(record)) {
                return;
            }
            String msg;
            try {
                msg = getFormatter().format(record);
            } catch (Exception ex) {
                // We don't want to throw an exception here, but we
                // report the exception to any registered ErrorManager.
                reportError(null, ex, ErrorManager.FORMAT_FAILURE);
                return;
            }
            
            try {
                out.write(msg.getBytes());
            } catch (Exception ex) {
                // We don't want to throw an exception here, but we
                // report the exception to any registered ErrorManager.
                reportError(null, ex, ErrorManager.WRITE_FAILURE);
            }
        }
    }
}
