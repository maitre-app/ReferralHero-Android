package com.sdk.referral.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import java.util.*

/**
 * we are using this class for to save data into SharedPreferences and  for display log in SDK classes
 */
class PrefHelper(context: Context) {
    /**
     * A single variable that holds a reference to the application's [SharedPreferences]
     * object for use whenever [SharedPreferences] values are read or written via this helper
     * class.
     */
    private val appSharedPrefs_: SharedPreferences

    /**
     * A single variable that holds a reference to an [SharedPreferences.Editor] object that is used by the
     * helper class whenever the preferences for the application are changed.
     */
    private var prefsEditor_: SharedPreferences.Editor?

    var mContext: Context?

    /**
     *
     * Constructor with context passed from calling [android.app.Activity].
     *
     * @param context A reference to the [Context] that the application is operating
     * within. This is normally the base context of the application.
     */
    init {
        appSharedPrefs_ = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        prefsEditor_ = appSharedPrefs_.edit()
        mContext = context
    }

    /**
     * Set the given Referral API Key  to preference. Clears the preference data if the key is a new key.
     *
     * @param key A [String] representing Referral API Key .
     *
     */
    fun setRHAccessTokenKey(key: String?) {
        setString(KEY_RH_ACCESS_TOKEN, key)
    }


    /**
     * Set the given Referral API Key  to preference. Clears the preference data if the key is a new key.
     *
     * @param key A [String] representing Referral API Key .
     */
    fun setRHCampaignID(key: String?) {
        setString(KEY_RH_CAMPAIGN_ID, key)
    }


    fun setRHPlaystoreLink(key: String?) {
        setString(KEY_RH_PLAYSTORE_LINK, key)
    }

    val rhCampaignID: String?
        get() = getString(KEY_RH_CAMPAIGN_ID)

    /**
     * Set the given Referral Link  to preference.
     *
     * @param rhReferralLink A [String] representing Referral Link.
     */
    var rHReferralLink: String?
        get() = getString(KEY_RH_LINK)
        set(rhReferralLink) {
            setString(KEY_RH_LINK, rhReferralLink)
        }

    var RHPlayStoreLink: String?
        get() = getString(KEY_RH_PLAYSTORE_LINK)
        set(RHPlayStoreLink) {
            setString(KEY_RH_PLAYSTORE_LINK, RHPlayStoreLink)
        }

    val rhAccessTokenKey: String?
        get() = getString(KEY_RH_ACCESS_TOKEN)


    /**
     * Set the given Subscriber ID  to preference.
     *
     * @param rhSubscriberID A [String] representing Subscriber ID.
     */
    var rHSubscriberID: String?
        get() = getString(KEY_RH_SUBSCRIBER_ID)
        set(rhSubscriberID) {
            setString(KEY_RH_SUBSCRIBER_ID, rhSubscriberID)
        }

    /**
     *
     * Clears all the RH referral shared preferences related .
     *
     */
    private fun clearAllPref() {
        prefsEditor_!!.clear()
        prefsEditor_!!.apply()
    }
    // ALL GENERIC CALLS
    /**
     * Gets the app store install referrer string
     *
     * @return [String]  App store install referrer string
     */
    /**
     * Sets the app store install referrer string
     *
     * @param referrer App store install referrer string
     */
    var appStoreReferrer: String?
        get() = getString(KEY_GOOGLE_PLAY_INSTALL_REFERRER_EXTRA)
        set(referrer) {
            setString(KEY_GOOGLE_PLAY_INSTALL_REFERRER_EXTRA, referrer)
        }

    /**
     * Gets the short link
     *
     * @return [String]  App store install referrer string
     */
    var shortLink: String?
        get() = getString(KEY_APP_SHORT_LINK)
        set(store) {
            if (!TextUtils.isEmpty(store)) {
                setString(KEY_APP_SHORT_LINK, store)
            }
        }
    var appStoreSource: String?
        get() = getString(KEY_APP_STORE_SOURCE)
        set(store) {
            if (!TextUtils.isEmpty(store)) {
                setString(KEY_APP_STORE_SOURCE, store)
            }
        }

    private fun serializeArrayList(strings: ArrayList<String>): String {
        var retString = ""
        for (value in strings) {
            retString = "$retString$value,"
        }
        retString = retString.substring(0, retString.length - 1)
        return retString
    }

    private fun deserializeString(list: String): ArrayList<String> {
        val strings = ArrayList<String>()
        val stringArr = list.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        Collections.addAll(strings, *stringArr)
        return strings
    }

    /**
     *
     * A basic method that returns a [Boolean] indicating whether some preference exists.
     *
     * @param key A [String] value containing the key to reference.
     * @return A [Boolean] indicating whether some preference exists.
     */
    fun hasPrefValue(key: String?): Boolean {
        return appSharedPrefs_.contains(key)
    }

    /**
     *
     * A basic method to remove some preference value.
     *
     * @param key A [String] value containing the key to the value that's to be deleted.
     */
    fun removePrefValue(key: String?) {
        prefsEditor_!!.remove(key).apply()
    }

    /**
     *
     * A basic method that returns an integer value from a specified preferences Key.
     *
     * @param key A [String] value containing the key to reference.
     * @return An [Integer] value of the specified key as stored in preferences.
     */
    fun getInteger(key: String?): Int {
        return getInteger(key, 0)
    }

    /**
     *
     * A basic method that returns an [Integer] value from a specified preferences Key, with a
     * default value supplied in case the value is null.
     *
     * @param key          A [String] value containing the key to reference.
     * @param defaultValue An [Integer] specifying the value to use if the preferences value
     * is null.
     * @return An [Integer] value containing the value of the specified key, or the supplied
     * default value if null.
     */
    fun getInteger(key: String?, defaultValue: Int): Int {
        return appSharedPrefs_.getInt(key, defaultValue)
    }

    /**
     *
     * A basic method that returns a [Long] value from a specified preferences Key.
     *
     * @param key A [String] value containing the key to reference.
     * @return A [Long] value of the specified key as stored in preferences.
     */
    fun getLong(key: String?): Long {
        return getLong(key, 0)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return appSharedPrefs_.getLong(key, defaultValue)
    }

    /**
     *
     * A basic method that returns a [Float] value from a specified preferences Key.
     *
     * @param key A [String] value containing the key to reference.
     * @return A [Float] value of the specified key as stored in preferences.
     */
    fun getFloat(key: String?): Float {
        return appSharedPrefs_.getFloat(key, 0f)
    }

    /**
     *
     * A basic method that returns a [String] value from a specified preferences Key.
     *
     * @param key A [String] value containing the key to reference.
     * @return A [String] value of the specified key as stored in preferences.
     */
    fun getString(key: String?): String? {
        return appSharedPrefs_.getString(key, NO_STRING_VALUE)
    }

    /**
     *
     * A basic method that returns a [Boolean] value from a specified preferences Key.
     *
     * @param key A [String] value containing the key to reference.
     * @return An [Boolean] value of the specified key as stored in preferences.
     */
    fun getBool(key: String?): Boolean {
        return appSharedPrefs_.getBoolean(key, false)
    }

    /**
     *
     * Sets the value of the [String] key value supplied in preferences.
     *
     * @param key   A [String] value containing the key to reference.
     * @param value An [Integer] value to set the preference record to.
     */
    fun setInteger(key: String?, value: Int) {
        prefsEditor_!!.putInt(key, value).apply()
    }

    /**
     *
     * Sets the value of the [String] key value supplied in preferences.
     *
     * @param key   A [String] value containing the key to reference.
     * @param value A [Long] value to set the preference record to.
     */
    fun setLong(key: String?, value: Long) {
        prefsEditor_!!.putLong(key, value).apply()
    }

    /**
     *
     * Sets the value of the [String] key value supplied in preferences.
     *
     * @param key   A [String] value containing the key to reference.
     * @param value A [Float] value to set the preference record to.
     */
    fun setFloat(key: String?, value: Float) {
        prefsEditor_!!.putFloat(key, value).apply()
    }

    /**
     *
     * Sets the value of the [String] key value supplied in preferences.
     *
     * @param key   A [String] value containing the key to reference.
     * @param value A [String] value to set the preference record to.
     */
    fun setString(key: String?, value: String?) {
        prefsEditor_!!.putString(key, value).apply()
    }

    /**
     *
     * Sets the value of the [String] key value supplied in preferences.
     *
     * @param key   A [String] value containing the key to reference.
     * @param value A [Boolean] value to set the preference record to.
     */
    fun setBool(key: String?, value: Boolean?) {
        prefsEditor_!!.putBoolean(key, value!!).apply()
    }

    /**
     *
     * Clears all the RH referral shared preferences related to the current key.
     * Should be called before setting a new Referral hero-AccessToken.
     */
    fun clearPrefOnBranchKeyChange() {
        prefsEditor_!!.clear()
        prefsEditor_!!.apply()
    }

    var appVersion: String?
        get() = getString(KEY_APP_VERSION)
        set(version) {
            setString(KEY_APP_VERSION, version)
        }
    /**
     *
     * Returns the currently set timeout value for calls to the Branch API. This will be the default
     * SDK setting unless it has been overridden manually between Branch object instantiation and
     * this call.
     *
     * @return An [Integer] value containing the currently set timeout value in
     * milliseconds.
     */
    /**
     *
     * Sets the duration in milliseconds to override the timeout value for calls to the Branch API.
     *
     * @param timeout The [Integer] value of the timeout setting in milliseconds.
     */
    var timeout: Int
        get() = getInteger(KEY_TIMEOUT, TIMEOUT)
        set(timeout) {
            setInteger(KEY_TIMEOUT, timeout)
        }

    companion object {
        const val NO_STRING_VALUE = "NO_STRING_VALUE"
        const val KEY_RH_ACCESS_TOKEN = "KEY_RH_ACCESS_TOKEN"
        const val KEY_RH_LINK = "KEY_RH_LINK"
        const val KEY_RH_PLAYSTORE_LINK = "KEY_RH_PLAYSTORE_LINK"
        const val KEY_RH_CAMPAIGN_ID = "KEY_RH_CAMPAIGN_ID"
        const val KEY_RH_SUBSCRIBER_ID = "KEY_RH_SUBSCRIBER_ID"

        /**
         * A [String] value used where no string value is available.
         */
        const val TIMEOUT = 5500 // Default timeout is 5.5 sec
        private const val TAG = "ReferralHeroSDK"
        private const val SHARED_PREF_FILE = "SHARED_PREF_FILE"
        private const val KEY_GOOGLE_PLAY_INSTALL_REFERRER_EXTRA =
            "rh_google_play_install_referrer_extras"
        private const val KEY_APP_STORE_SOURCE = "KEY_APP_STORE_SOURCE"
        private const val KEY_APP_SHORT_LINK = "KEY_APP_SHORT_LINK"
        private const val KEY_APP_VERSION = "KEY_APP_VERSION"
        private const val KEY_TIMEOUT = "KEY_TIMEOUT"

        /**
         * Internal static variable of own type [PrefHelper]. This variable holds the single
         * instance used when the class is instantiated via the Singleton pattern.
         */
        private var prefHelper_: PrefHelper? = null
        private var enableLogging_ = false

        /**
         *
         * Singleton method to return the pre-initialised, or newly initialise and return, a singleton
         * object of the type [PrefHelper].
         *
         * @param context The [Context] within which the object should be instantiated; this
         * parameter is passed to the private [.PrefHelper]
         * constructor method.
         * @return A [PrefHelper] object instance.
         */
        fun getInstance(context: Context): PrefHelper? {
            if (prefHelper_ == null) {
                prefHelper_ = PrefHelper(context)
            }
            return prefHelper_
        }

        // Package Private
        fun shutDown() {
            if (prefHelper_ != null) {
                prefHelper_!!.prefsEditor_ = null
            }
            prefHelper_ = null
        }

        /**
         *
         * Creates a **Debug** message in the debugger. If debugging is disabled, this will fail silently.
         *
         * @param message A [String] value containing the debug message to record.
         */
        fun Debug(message: String?) {
            if (!TextUtils.isEmpty(message)) {
                Log.d(TAG, message!!)
            }
        }

        /**
         *
         * Creates a **Info** message in the debugger. If debugging is disabled, this will fail silently.
         *
         * @param message A [String] value containing the INFO  message to record.
         */
        fun Info(message: String?) {
            if (!TextUtils.isEmpty(message)) {
                Log.i(TAG, message!!)
            }
        }

        fun LogException(message: String?, t: Exception?) {
            if (!TextUtils.isEmpty(message)) {
                Log.e(TAG, message, t)
            }
        }

        fun LogAlways(message: String?) {
            if (!TextUtils.isEmpty(message)) {
                Log.i(TAG, message!!)
            }
        }

        fun enableLogging(fEnable: Boolean) {
            enableLogging_ = fEnable
        }

        val aPIBaseUrl: String
            get() = "https://dev.referralhero.com/api/sdk/v1/lists/"

    }
}