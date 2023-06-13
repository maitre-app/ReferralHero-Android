package com.sdk.referral.Logger

import android.util.Log
import com.sdk.referral.utils.RHUtil.formatString
import java.util.*

class Logger : ILogger {
    var LOGTAG = "ReferralHero"
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
        if (logLevel!!.androidLogLevel <= Log.VERBOSE) {
            try {
                Log.v(LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun debug(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if (logLevel!!.androidLogLevel <= Log.DEBUG) {
            try {
                Log.d(LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun info(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if (logLevel!!.androidLogLevel <= Log.INFO) {
            try {
                Log.i(LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun warn(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if (logLevel!!.androidLogLevel <= Log.WARN) {
            try {
                Log.w(LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun warnInProduction(message: String?, vararg parameters: Any?) {
        if (logLevel!!.androidLogLevel <= Log.WARN) {
            try {
                Log.w(LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun error(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if (logLevel!!.androidLogLevel <= Log.ERROR) {
            try {
                Log.e(LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun Assert(message: String?, vararg parameters: Any?) {
        if (isProductionEnvironment) {
            return
        }
        if (logLevel!!.androidLogLevel <= Log.ASSERT) {
            try {
                Log.println(Log.ASSERT, LOGTAG, formatString(message, *parameters)!!)
            } catch (e: Exception) {
                Log.e(
                    LOGTAG,
                    formatString(formatErrorMessage, message, Arrays.toString(parameters))!!
                )
            }
        }
    }

    override fun lockLogLevel() {
        logLevelLocked = true
    }

    companion object {
        private const val formatErrorMessage = "Error formating log message: %s, with params: %s"
    }
}