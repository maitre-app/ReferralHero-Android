package com.sdk.referral

import android.content.Context
import android.os.Build
import java.net.InetAddress
import java.net.NetworkInterface

/**
 *
 *
 * Class is  responsible for capturing device info and updating
 * device info to ReferralHero requests
 *
 */
class DeviceInfo(private val context: Context) {


    //getDeviceModel(): Returns the model of the device (e.g., "Pixel 5").

    fun getDeviceModel(): String {
        return Build.MODEL
    }

    //getDeviceManufacturer(): Returns the manufacturer of the device (e.g., "Google").
    fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER
    }

    // Returns the brand of the device (e.g., "Google").
    fun getDeviceBrand(): String {
        return Build.BRAND
    }

    //Returns the version of the Android OS running on the device (e.g., "10.0").
    fun getDeviceOsVersion(): String {
        return Build.VERSION.RELEASE
    }

    //Returns the SDK version of the Android OS (e.g., 29 for Android 10).
    fun getDeviceSdkVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    //Returns the language code of the device's primary language (e.g., "en" for English).
    fun getDeviceLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            ""
        }
    }

    //Returns the country code of the device's region (e.g., "US" for the United States).
    fun getDeviceCountry(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].country
        } else {
            ""
        }
    }

    /**
     * getIpAddress(): This method retrieves the IP address of the device.
     * It iterates through the available network interfaces and their associated addresses
     * to find a non-loopback IPv4 address. If found, it returns the IP address as a String.
     * If no suitable IP address is found, it returns null.
     * **/

    fun getIpAddress(): String? {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress && address is InetAddress && address.hostAddress?.contains(":")?.not() == true) {
                    return address.hostAddress
                }
            }
        }
        return null
    }

    //getOperatingSystem() will return the Android operating system name
    // instead of just the version number (e.g., "Android KitKat").

    fun getOperatingSystem(): String {
        val osName: String = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.BASE -> "Android Base"
            Build.VERSION_CODES.BASE_1_1 -> "Android Base 1.1"
            Build.VERSION_CODES.CUPCAKE -> "Android Cupcake"
            Build.VERSION_CODES.DONUT -> "Android Donut"
            Build.VERSION_CODES.ECLAIR -> "Android Eclair"
            Build.VERSION_CODES.ECLAIR_0_1 -> "Android Eclair 0.1"
            Build.VERSION_CODES.ECLAIR_MR1 -> "Android Eclair MR1"
            Build.VERSION_CODES.FROYO -> "Android Froyo"
            Build.VERSION_CODES.GINGERBREAD -> "Android Gingerbread"
            Build.VERSION_CODES.GINGERBREAD_MR1 -> "Android Gingerbread MR1"
            Build.VERSION_CODES.HONEYCOMB -> "Android Honeycomb"
            Build.VERSION_CODES.HONEYCOMB_MR1 -> "Android Honeycomb MR1"
            Build.VERSION_CODES.HONEYCOMB_MR2 -> "Android Honeycomb MR2"
            Build.VERSION_CODES.ICE_CREAM_SANDWICH -> "Android Ice Cream Sandwich"
            Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 -> "Android Ice Cream Sandwich MR1"
            Build.VERSION_CODES.JELLY_BEAN -> "Android Jelly Bean"
            Build.VERSION_CODES.JELLY_BEAN_MR1 -> "Android Jelly Bean MR1"
            Build.VERSION_CODES.JELLY_BEAN_MR2 -> "Android Jelly Bean MR2"
            Build.VERSION_CODES.KITKAT -> "Android KitKat"
            Build.VERSION_CODES.KITKAT_WATCH -> "Android KitKat Watch"
            Build.VERSION_CODES.LOLLIPOP -> "Android Lollipop"
            Build.VERSION_CODES.LOLLIPOP_MR1 -> "Android Lollipop MR1"
            Build.VERSION_CODES.M -> "Android Marshmallow"
            Build.VERSION_CODES.N -> "Android Nougat"
            Build.VERSION_CODES.N_MR1 -> "Android Nougat MR1"
            Build.VERSION_CODES.O -> "Android Oreo"
            Build.VERSION_CODES.O_MR1 -> "Android Oreo MR1"
            Build.VERSION_CODES.P -> "Android Pie"
            Build.VERSION_CODES.Q -> "Android 10"
            Build.VERSION_CODES.R -> "Android 11"
            Build.VERSION_CODES.S -> "Android 12"
            Build.VERSION_CODES.TIRAMISU -> "Android 13"
            else -> "Android"
        }
        return osName
    }
}