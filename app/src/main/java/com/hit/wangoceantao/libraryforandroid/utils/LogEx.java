package com.hit.wangoceantao.libraryforandroid.utils;

import android.util.Log;

import com.hit.wangoceantao.libraryforandroid.BuildConfig;


/**
 * Custom Log class, add catch exception and crash support
 *
 * @author wangoceantao@gmail.com
 */

public class LogEx {
    public static final String TAG = "LEO_WIFI";

    /**
     * Default log level,the log above default will be logged.
     */
    public static int DEFAULT_LOG_LEVEL = android.util.Log.VERBOSE;

    public static void v(String aTag, String aMsg) {
        log(android.util.Log.VERBOSE, aTag, aMsg);
    }

    public static void v(String aTag, String aMsg, Throwable aThrowable) {
        log(android.util.Log.VERBOSE, aTag, aMsg, aThrowable);
    }

    public static void d(String aTag, String aMsg) {
        log(android.util.Log.DEBUG, aTag, aMsg);
    }

    public static void d(String aTag, String aMsg, Throwable aThrowable) {
        log(android.util.Log.DEBUG, aTag, aMsg, aThrowable);
    }

    public static void i(String aTag, String aMsg) {
        log(android.util.Log.INFO, aTag, aMsg);
    }

    public static void i(String aTag, String aMsg, Throwable aThrowable) {
        log(android.util.Log.INFO, aTag, aMsg, aThrowable);
    }

    public static void w(String aTag, String aMsg) {
        log(android.util.Log.WARN, aTag, aMsg);
    }

    public static void w(String aTag, String aMsg, Throwable aThrowable) {
        log(android.util.Log.WARN, aTag, aMsg, aThrowable);
    }

    public static void e(String aTag, String aMsg) {
        log(android.util.Log.ERROR, aTag, aMsg);
    }

    public static void e(String aTag, String aMsg, Throwable aThrowable) {
        log(android.util.Log.ERROR, aTag, aMsg, aThrowable);
    }

    public static void log(int aLogLevel, String aTag, String aMessage) {
        log(aLogLevel, aTag, aMessage, null);
    }

    /**
     * log Send a logLevel log message and log the exception, then collect the
     * log entry.
     * 
     * @param aLogLevel
     *            Used to identify log level/
     * @param aTag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param aMessage
     *            The message you would like logged.
     * @param aThrowable
     *            An exception to log
     */
    public static void log(int aLogLevel, String aTag, String aMessage, Throwable aThrowable) {
        if (isLoggable(aLogLevel)) {
            switch (aLogLevel) {
            case android.util.Log.VERBOSE:
                Log.v(TAG, aTag + ": " + aMessage, aThrowable);
                break;
            case android.util.Log.DEBUG:
                Log.d(TAG, aTag + ": " + aMessage, aThrowable);
                break;
            case android.util.Log.INFO:
                Log.i(TAG, aTag + ": " + aMessage, aThrowable);
                break;
            case android.util.Log.WARN:
                Log.w(TAG, aTag + ": " + aMessage, aThrowable);
                break;
            case android.util.Log.ERROR:
                Log.e(TAG, aTag + ": " + aMessage, aThrowable);
                break;
            default:
                Log.v(TAG, aTag + ": " + aMessage, aThrowable);
            }
        }
    }

    /**
     * call when enter the method body that you want to debug with only one line
     */
    public static void method() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        if (null == stack || 2 > stack.length) {
            return;
        }

        StackTraceElement s = stack[1];
        if (null != s) {
            String className = s.getClassName();
            String methodName = s.getMethodName();
            d(className, "+++++" + methodName);
        }
    }

    /**
     * call when enter the method body that you want to debug with only one line
     */
    public static void method(String className) {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        if (null == stack || 2 > stack.length) {
            return;
        }

        StackTraceElement s = stack[1];
        if (null != s) {
            String methodName = s.getMethodName();
            d(className, "+++++" + methodName);
        }
    }

    /**
     * call when enter the method body that you want to debug.
     */
    public static void enter() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        if (null == stack || 2 > stack.length) {
            return;
        }

        StackTraceElement s = stack[1];
        if (null != s) {
            String className = s.getClassName();
            String methodName = s.getMethodName();
            d(className, "====>" + methodName);
        }
    }

    /**
     * call when leave the method body that you want to debug.
     */
    public static void leave() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        if (null == stack || 2 > stack.length) {
            return;
        }

        StackTraceElement s = stack[1];
        if (null != s) {
            String className = s.getClassName();
            String methodName = s.getMethodName();
            d(className, "<====" + methodName);
        }
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at
     * the specified level.
     * 
     * @param aLevel
     *            The level to check.
     * @return Whether or not that this is allowed to be logged.
     */
    public static boolean isLoggable(int aLevel) {

        if (android.util.Log.ERROR == aLevel) {
            return true;
        }

        if (BuildConfig.debug) {
            if (aLevel >= DEFAULT_LOG_LEVEL) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
