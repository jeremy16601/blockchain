package com.blockchain.robot.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SHA256 {

    //币安SHA256加密
    public static String signature(String secret, String message) {

        String hash = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            hash = byte2hex(sha256_HMAC.doFinal(message.getBytes()));
        } catch (Exception e) {
            System.out.println("Error");
        }

        return hash;
    }


    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}
