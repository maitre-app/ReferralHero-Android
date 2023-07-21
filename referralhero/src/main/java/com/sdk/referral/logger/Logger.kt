package com.sdk.referral.logger

import android.util.Log
import java.util.Locale

class Logger : ILogger {
    var LOGTAG = "ReferralHero SDK:"
    private var logLevel: LogLevel? = null
    private var logLevelLocked = false
    private var isProductionEnvironment = false

    init {
        setLogLevel(LogLevel.INFO, isProductionEnvironment)
    }

    override fun setLogLevel(logLevel: LogLevel?, isProductionEnvironment: Boolean) {
        if (logLevelLocked) {
            return
        }
        this.logLevel = logLevel
        this.isProductionEnvironment = isProductionEnvironment
    }

    override fun setLogLevelString(logLevelString: String?, isProductionEnvironment: Boolean) {
        if (null != logLevelString) {
            try {
                setLogLevel(LogLevel.valueOf(logLevelString.uppercase()), isProductionEnvironment)
            } catch (iae: IllegalArgumentException) {
                error("Malformed logLevel '%s', falling back to 'info'", logLevelString)
            }
        }
    }

    override fun verbose(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if ((logLevel?.androidLogLevel ?: Log.VERBOSE) <= Log.VERBOSE) {
            try {
                formatString(message, parameters)?.let { Log.v(LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun debug(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if ((logLevel?.androidLogLevel ?: Log.DEBUG) <= Log.DEBUG) {
            try {
                formatString(message, parameters)?.let { Log.d(LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun info(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if ((logLevel?.androidLogLevel ?: Log.INFO) <= Log.INFO) {
            try {
                formatString(message, parameters)?.let { Log.i(LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun warn(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if ((logLevel?.androidLogLevel ?: Log.WARN) <= Log.WARN) {
            try {
                formatString(message, parameters)?.let { Log.w(LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun warnInProduction(message: String?, vararg parameters: Any?) {
        if ((logLevel?.androidLogLevel ?: Log.WARN) <= Log.WARN) {
            try {
                formatString(message, parameters)?.let { Log.w(LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun error(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if ((logLevel?.androidLogLevel ?: Log.ERROR) <= Log.ERROR) {
            try {
                formatString(message, parameters)?.let { Log.e(LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun Assert(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if ((logLevel?.androidLogLevel ?: Log.ASSERT) <= Log.ASSERT) {
            try {
                formatString(message, parameters)?.let { Log.println(Log.ASSERT, LOGTAG, it) }
            } catch (e: Exception) {
                formatString(formatErrorMessage, message, parameters.contentToString())?.let {
                    Log.e(
                        LOGTAG,
                        it
                    )
                }
            }
        }
    }

    override fun lockLogLevel() {
        logLevelLocked = true
    }

    companion object {
        private const val formatErrorMessage = "Error formating log message: %s, with params: %s"
    }

    fun formatString(format: String?, vararg args: Any?): String? {
        if (format.isNullOrBlank()) {
            return "null"
        }

        return try {
            java.lang.String.format(Locale.US, format, args)
        } catch (e: Exception) {
            // Handle any formatting exceptions here (e.g., MissingFormatArgumentException)
            Log.e("formatString", "Formatting error: ${e.message}")
            null
        }
    }
}