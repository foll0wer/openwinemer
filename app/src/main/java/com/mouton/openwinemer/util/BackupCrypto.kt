// BackupCrypto.kt
package com.mouton.openwinemer.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object BackupCrypto {

    // Génère une clé à partir d’un mot de passe (simplifié pour l’exemple).
    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    fun encrypt(plainText: String, password: String): String {
        val salt = ByteArray(16).apply { java.security.SecureRandom().nextBytes(this) }
        val key = deriveKey(password, salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16).apply { java.security.SecureRandom().nextBytes(this) }
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // On concatène salt + iv + données chiffrées, puis on encode en Base64.
        val all = salt + iv + encrypted
        return Base64.encodeToString(all, Base64.DEFAULT)
    }

    fun decrypt(cipherTextBase64: String, password: String): String {
        val all = Base64.decode(cipherTextBase64, Base64.DEFAULT)
        val salt = all.copyOfRange(0, 16)
        val iv = all.copyOfRange(16, 32)
        val data = all.copyOfRange(32, all.size)
        val key = deriveKey(password, salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decrypted = cipher.doFinal(data)
        return String(decrypted, Charsets.UTF_8)
    }
}
