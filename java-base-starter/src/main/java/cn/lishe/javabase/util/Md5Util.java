package cn.lishe.javabase.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author YeJin
 * @date 2019/9/5 9:24
 */
public class Md5Util {
    private static String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String md5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("md5");
            byte[] digest = messageDigest.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                int h = 0x0f & (b >>> 4);
                int l = 0x0f & b;
                sb.append(chars[h]).append(chars[l]);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
