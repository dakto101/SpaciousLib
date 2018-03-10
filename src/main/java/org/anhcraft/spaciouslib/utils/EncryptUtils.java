package org.anhcraft.spaciouslib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
    /**
     * Hash a string with MD5 hashing algorithm
     *
     * @param hash a string
     *
     * @return MD5 hash
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] byteData = md.digest(hash.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Hash a string with SHA256 hashing algorithm
     *
     * @param hash a string
     *
     * @return MD5 hash
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sha256(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] md = digest.digest(hash.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < md.length; i++) {
            String hex = Integer.toHexString(0xff & md[i]);
            if(hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Hash a string with SHA512 hashing algorithm
     *
     * @param hash a string
     *
     * @return MD5 hash
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sha512(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] md = digest.digest(hash.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < md.length ; i++){
            sb.append(Integer.toString((md[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
