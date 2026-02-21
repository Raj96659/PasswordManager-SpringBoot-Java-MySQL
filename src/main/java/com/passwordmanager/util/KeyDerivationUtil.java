package com.passwordmanager.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class KeyDerivationUtil {

    public static SecretKeySpec deriveKey(
            String password,
            String salt) {

        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    Base64.getDecoder().decode(salt),
                    65536,
                    256
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] keyBytes =
                    factory.generateSecret(spec).getEncoded();

            return new SecretKeySpec(keyBytes, "AES");

        } catch (Exception e) {
            throw new RuntimeException("Key derivation failed");
        }
    }
}
