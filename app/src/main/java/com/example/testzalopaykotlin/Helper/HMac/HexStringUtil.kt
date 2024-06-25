package com.example.testzalopaykotlin.Helper.HMac
import java.util.*

object HexStringUtil {
    private val HEX_CHAR_TABLE = charArrayOf(
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f'
    )

    /**
     * Convert a byte array to a hexadecimal string
     *
     * @param raw A raw byte array
     * @return Hexadecimal string
     */
    fun byteArrayToHexString(raw: ByteArray): String {
        val hex = CharArray(2 * raw.size)
        var index = 0

        for (b in raw) {
            val v = b.toInt() and 0xFF
            hex[index++] = HEX_CHAR_TABLE[v ushr 4]
            hex[index++] = HEX_CHAR_TABLE[v and 0xF]
        }
        return String(hex)
    }

    /**
     * Convert a hexadecimal string to a byte array
     *
     * @param hex A hexadecimal string
     * @return The byte array
     */
    fun hexStringToByteArray(hex: String): ByteArray {
        val hexStandard = hex.lowercase(Locale.ENGLISH)
        val sz = hexStandard.length / 2
        val bytesResult = ByteArray(sz)

        var idx = 0
        var i = 0
        while (i < sz) {
            bytesResult[i] = (hexStandard[idx]).code.toByte()
            idx++
            var tmp = (hexStandard[idx]).code.toByte()
            idx++

            if (bytesResult[i] > HEX_CHAR_TABLE[9].code.toByte()) {
                bytesResult[i] = (bytesResult[i] - ('a'.code.toByte() - 10)).toByte()
            } else {
                bytesResult[i] = (bytesResult[i] - '0'.code.toByte()).toByte()
            }
            if (tmp > HEX_CHAR_TABLE[9].code.toByte()) {
                tmp = (tmp - ('a'.code.toByte() - 10)).toByte()
            } else {
                tmp = (tmp - '0'.code.toByte()).toByte()
            }

            bytesResult[i] = (bytesResult[i] * 16 + tmp).toByte()
            i++
        }
        return bytesResult
    }
}
