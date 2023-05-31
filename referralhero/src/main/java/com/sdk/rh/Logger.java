package com.sdk.rh;

import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

public class Logger implements ILogger {

    private static final String formatErrorMessage = "Error formating log message: %s, with params: %s";
    String LOGTAG = "ReferralHero";
    private LogLevel logLevel;
    private boolean logLevelLocked;
    private boolean isProductionEnvironment;

    public Logger() {
        isProductionEnvironment = false;
        logLevelLocked = false;
        setLogLevel(LogLevel.INFO, isProductionEnvironment);
    }

    @Override
    public void setLogLevel(LogLevel logLevel, boolean isProductionEnvironment) {
        if (logLevelLocked) {
            return;
        }
        this.logLevel = logLevel;
        this.isProductionEnvironment = isProductionEnvironment;
    }

    @Override
    public void setLogLevelString(String logLevelString, boolean isProductionEnvironment) {
        if (null != logLevelString) {
            try {
                setLogLevel(LogLevel.valueOf(logLevelString.toUpperCase(Locale.US)), isProductionEnvironment);
            } catch (IllegalArgumentException iae) {
                error("Malformed logLevel '%s', falling back to 'info'", logLevelString);
            }
        }
    }

    @Override
    public void verbose(String message, Object... parameters) {
        if (isProductionEnvironment) {
            return;
        }
        if (logLevel.androidLogLevel <= Log.VERBOSE) {
            try {
                Log.v(LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }

    @Override
    public void debug(String message, Object... parameters) {
        if (isProductionEnvironment) {
            return;
        }
        if (logLevel.androidLogLevel <= Log.DEBUG) {
            try {
                Log.d(LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }

    @Override
    public void info(String message, Object... parameters) {
        if (isProductionEnvironment) {
            return;
        }
        if (logLevel.androidLogLevel <= Log.INFO) {
            try {
                Log.i(LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }

    @Override
    public void warn(String message, Object... parameters) {
        if (isProductionEnvironment) {
            return;
        }
        if (logLevel.androidLogLevel <= Log.WARN) {
            try {
                Log.w(LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }

    @Override
    public void warnInProduction(String message, Object... parameters) {
        if (logLevel.androidLogLevel <= Log.WARN) {
            try {
                Log.w(LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }


    @Override
    public void error(String message, Object... parameters) {
        if (isProductionEnvironment) {
            return;
        }
        if (logLevel.androidLogLevel <= Log.ERROR) {
            try {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }

    @Override
    public void Assert(String message, Object... parameters) {
        if (isProductionEnvironment) {
            return;
        }
        if (logLevel.androidLogLevel <= Log.ASSERT) {
            try {
                Log.println(Log.ASSERT, LOGTAG, RHUtil.INSTANCE.formatString(message, parameters));
            } catch (Exception e) {
                Log.e(LOGTAG, RHUtil.INSTANCE.formatString(formatErrorMessage, message, Arrays.toString(parameters)));
            }
        }
    }

    @Override
    public void lockLogLevel() {
        logLevelLocked = true;
    }
}