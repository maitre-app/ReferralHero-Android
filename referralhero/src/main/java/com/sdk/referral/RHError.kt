package com.sdk.referral

/**
 *
 *
 * Returns a general error if the Referral Hero server back-end is down.
 * @author jayden (Jaspalsinh Gohil)
 *
 */
class RHError {
    /**
     * Returns the message explaining the error.
     *
     * @return A [String] value that can be used in error logging or for dialog display to the user.
     */
    val message: String
        get() = "Trouble communicating with server. Please try again"

    /**
     * Overridden toString method for this object; returns the error message rather than the object's address.
     *
     * @return A [String] value representing the object's current state.
     */
    override fun toString(): String {
        return message
    }
}