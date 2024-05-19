
package com.example.ck_4_navigation_drawer

import java.security.MessageDigest
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

data class Dish(
    val idDish: Int,
    val nameDish: String,
    val rating: Float,
    val recipe: String,
    val ingredients: String,
    val photo: String,
    val tags: String
)

object URL{
    //val URL = "http://192.168.43.233:8080"
    val URL = "http://192.168.1.12:8080"


}

object HashPass{
    private const val ALGORITHM = "PBKDF2WithHmacSHA512"
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH = 256
    private const val SECRET = "SomeRandomSecret"

    fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    fun generateHash(password: String, salt: String): String {
        val combinedSalt = "$salt$SECRET".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()
    }
    fun hashWithSHA512(input: String): String?
    {
        val md: MessageDigest = MessageDigest.getInstance("SHA-512")
        val hashBytes: ByteArray = md.digest(input.toByteArray())

        // Convert byte array to hexadecimal string
        val hexString = StringBuilder()
        for (b in hashBytes) {
            val hex = Integer.toHexString(0xff and b.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

}

object account {
    var UserInfo: () -> Unit = {
        //  функция по умолчанию
    }
}




