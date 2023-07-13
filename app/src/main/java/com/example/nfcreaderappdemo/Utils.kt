package com.example.nfcreaderappdemo

class Utils {

    companion object {
        private const val HEX_CHARS = "0123456789ABCDEF"

        fun hexStringToByteArray(data: String): ByteArray {
            val result = ByteArray(data.length / 2)

            for (i in data.indices step 2) {
                val firstIndex = HEX_CHARS.indexOf(data[i])
                val secondIndex = HEX_CHARS.indexOf(data[i + 1])

                val octet = firstIndex.shl(4).or(secondIndex)
                result[i / 2] = octet.toByte()
            }

            return result
        }

        fun toHex(byteArray: ByteArray): String {
            val charset = Charsets.UTF_8
            return byteArray.toString(charset)
        }
    }
}


