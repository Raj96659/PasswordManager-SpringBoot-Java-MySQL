package com.passwordmanager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    public static String encrypt(
            String data,
            SecretKeySpec key) {

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(data.getBytes()));

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed");
        }
    }

    public static String decrypt(
            String encrypted,
            SecretKeySpec key) {

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return new String(cipher.doFinal(
                    Base64.getDecoder().decode(encrypted)));

        } catch (Exception e) {
            throw new RuntimeException("Decryption failed");
        }
    }
}
