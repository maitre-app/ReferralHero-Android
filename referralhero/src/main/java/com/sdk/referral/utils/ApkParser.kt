package com.sdk.referral.utils

class ApkParser {
    /**
     * Returns the result of decompression of XML as a [String].
     * @param xml A [Byte[]] containing the XML to be decompressed.
     * @return A [String] containing the result of the decompression action.
     */
    fun decompressXML(xml: ByteArray): String? {
        // Compressed XML file/bytes starts with 24x bytes of data,
        // 9 32 bit words in little endian order (LSB first):
        //   0th word is 03 00 08 00
        //   3rd word SEEMS TO BE:  Offset at then of StringTable
        //   4th word is: Number of strings in string table
        // WARNING: Sometime I indiscriminently display or refer to word in
        //   little endian storage format, or in integer format (ie MSB first).
        val numbStrings = LEW(xml, 4 * 4)

        // StringIndexTable starts at offset 24x, an array of 32 bit LE offsets
        // of the length/string data in the StringTable.
        val sitOff = 0x24 // Offset of start of StringIndexTable

        // StringTable, each string is represented with a 16 bit little endian
        // character count, followed by that number of 16 bit (LE) (Unicode) chars.
        val stOff = sitOff + numbStrings * 4 // StringTable follows StrIndexTable

        // XMLTags, The XML tag tree starts after some unknown content after the
        // StringTable.  There is some unknown data after the StringTable, scan
        // forward from this point to the flag for the start of an XML start tag.
        var xmlTagOff = LEW(xml, 3 * 4) // Start from the offset in the 3rd word.
        // Scan forward until we find the bytes: 0x02011000(x00100102 in normal int)
        var ii = xmlTagOff
        while (ii < xml.size - 4) {
            if (LEW(xml, ii) == startTag) {
                xmlTagOff = ii
                break
            }
            ii += 4
        }

        // XML tags and attributes:
        // Every XML start and end tag consists of 6 32 bit words:
        //   0th word: 02011000 for startTag and 03011000 for endTag
        //   1st word: a flag?, like 38000000
        //   2nd word: Line of where this tag appeared in the original source file
        //   3rd word: FFFFFFFF ??
        //   4th word: StringIndex of NameSpace name, or FFFFFFFF for default NS
        //   5th word: StringIndex of Element Name
        //   (Note: 01011000 in 0th word means end of XML document, endDocTag)

        // Start tags (not end tags) contain 3 more words:
        //   6th word: 14001400 meaning??
        //   7th word: Number of Attributes that follow this tag(follow word 8th)
        //   8th word: 00000000 meaning??

        // Attributes consist of 5 words:
        //   0th word: StringIndex of Attribute Name's Namespace, or FFFFFFFF
        //   1st word: StringIndex of Attribute Name
        //   2nd word: StringIndex of Attribute Value, or FFFFFFF if ResourceId used
        //   3rd word: Flags?
        //   4th word: str ind of attr value again, or ResourceId of value

        // Step through the XML tree element tags and attributes
        var attrValue: String?
        var attrName: String?
        var off = xmlTagOff
        while (off < xml.size) {
            val tag0 = LEW(xml, off)
            if (tag0 == startTag) { // XML START TAG
                val numbAttrs = LEW(xml, off + 7 * 4) // Number of Attributes to follow
                off += 9 * 4 // Skip over 6+3 words of startTag data
                // Look for the Attributes
                for (ii in 0 until numbAttrs) {
                    val attrNameSi = LEW(xml, off + 1 * 4) // AttrName String Index
                    val attrValueSi = LEW(xml, off + 2 * 4) // AttrValue Str Ind, or FFFFFFFF
                    val attrResId =
                        LEW(xml, off + 4 * 4) // AttrValue ResourceId or dup AttrValue StrInd
                    off += 5 * 4 // Skip over the 5 words of an attribute
                    attrName = compXmlString(xml, sitOff, stOff, attrNameSi)
                    if (attrName == "scheme") {
                        attrValue = if (attrValueSi != -1) compXmlString(
                            xml,
                            sitOff,
                            stOff,
                            attrValueSi
                        ) else "resourceID 0x" + Integer.toHexString(attrResId)
                        if (validURI(attrValue)) return attrValue
                    }
                }
            } else if (tag0 == endTag) { // XML END TAG
                off += 6 * 4 // Skip over 6 words of endTag data
            } else if (tag0 == endDocTag) {  // END OF XML DOC TAG
                break
            } else {
                break
            }
        } // end of while loop scanning tags and attributes of XML tree
        return ""
    } // end of decompressXML

    /**
     * <P>Checks whether the supplied [String] is of a valid/known URI protocol type.</P>
     *
     * <P>
     * Valid protocol types:
    </P> *
     *  * http
     *  * https
     *  * geo
     *  * *
     *  * package
     *  * sms
     *  * smsto
     *  * mms
     *  * mmsto
     *  * tel
     *  * voicemail
     *  * file
     *  * content
     *  * mailto
     *
     *
     *
     *
     * @param value The [String] value to be assessed.
     * @return A [Boolean] value; if valid returns true, else false.
     */
    private fun validURI(value: String?): Boolean {
        if (value != null) {
            if (value != "http"
                && value != "https"
                && value != "geo"
                && value != "*"
                && value != "package"
                && value != "sms"
                && value != "smsto"
                && value != "mms"
                && value != "mmsto"
                && value != "tel"
                && value != "voicemail"
                && value != "file"
                && value != "content"
                && value != "mailto"
            ) {
                return true
            }
        }
        return false
    }

    /**
     *
     *
     * @param xml
     * @param sitOff
     * @param stOff
     * @param strInd
     * @return
     */
    fun compXmlString(xml: ByteArray, sitOff: Int, stOff: Int, strInd: Int): String? {
        if (strInd < 0) return null
        val strOff = stOff + LEW(xml, sitOff + strInd * 4)
        return compXmlStringAt(xml, strOff)
    }

    // compXmlStringAt -- Return the string stored in StringTable format at
    // offset strOff.  This offset points to the 16 bit string length, which
    // is followed by that number of 16 bit (Unicode) chars.
    fun compXmlStringAt(arr: ByteArray, strOff: Int): String {
        val strLen = arr[strOff + 1].toInt() shl 8 and 0xff00 or (arr[strOff].toInt() and 0xff)
        val chars = ByteArray(strLen)
        for (ii in 0 until strLen) {
            chars[ii] = arr[strOff + 2 + ii * 2]
        }
        return String(chars) // Hack, just use 8 byte chars
    } // end of compXmlStringAt

    /**
     * LEW (Little-Endian Word)
     *
     * @param arr The [Byte[]] to process.
     * @param off An [int] value indicating the offset from which the return value should be
     * taken.
     * @return The [int] Little Endian 32 bit word taken from the input [Byte[]] at the
     * [int] offset provided.
     */
    fun LEW(arr: ByteArray, off: Int): Int {
        return arr[off + 3].toInt() shl 24 and -0x1000000 or (arr[off + 2].toInt() shl 16 and 0xff0000) or (arr[off + 1].toInt() shl 8 and 0xff00) or (arr[off].toInt() and 0xFF)
    } // end of LEW

    companion object {
        // decompressXML -- Parse the 'compressed' binary form of Android XML docs
        // such as for AndroidManifest.xml in .apk files
        var endDocTag = 0x00100101
        var startTag = 0x00100102
        var endTag = 0x00100103
    }
}