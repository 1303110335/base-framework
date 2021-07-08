package com.xuleyan.frame.core.util;

import com.xuleyan.frame.core.constants.AcsCoreConstants;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class AESEncryptUtils {

    /**
     * 默认字符集编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 填充方式
     */
    private static final String PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密key长度
     */
    private static final int KEY_LENGTH = 16;


    private AESEncryptUtils() {
    }

    /**
     * 加密
     *
     * @param content    待加密内容
     * @param encryptKey 加密 KEY
     * @return
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        if (content == null || encryptKey == null) {
            throw new IllegalArgumentException("加密内容 content 和 encryptKey 不能为 null");
        }

        if (encryptKey.length() != KEY_LENGTH) {
            throw new IllegalArgumentException("加密的encryptKey必须为16位");
        }

        // 创建cipher实例 参数按"算法/模式/填充模式" "AES/ECB/PKCS5Padding"
        Cipher cipher = Cipher.getInstance(PADDING);
        // 初始化ciper,
        // (1)opmode ：Cipher.ENCRYPT_MODE(加密模式)和 Cipher.DECRYPT_MODE(解密模式)
        // (2)key ：密匙，使用传入的盐构造出一个密匙，可以使用SecretKeySpec、KeyGenerator和KeyPairGenerator创建密匙，其中
        // * SecretKeySpec和KeyGenerator支持AES，DES，DESede三种加密算法创建密匙
        // * KeyPairGenerator支持RSA加密算法创建密匙
        // (3)params ：使用CBC模式时必须传入该参数，该项目使用IvParameterSpec创建iv 对象
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM));
        /* org.apache.commons.codec.binary.Base64; Base64.encodeBase64String() 解决Base64加密换行问题  */
//        return new BASE64Encoder().encode(cipher.doFinal(content.getBytes(DEFAULT_ENCODING)));
        // 加密或解密,返回byte数组
        byte[] bytes = cipher.doFinal(content.getBytes(DEFAULT_ENCODING));
        return Base64.encodeBase64String(bytes);

    }

    /**
     * 加密
     *
     * @param content 待加密内容
     * @return
     */
    public static String encrypt(String content) throws Exception {
        return encrypt(content, AcsCoreConstants.DEFAULT_ENCRYPT_KEY);
    }

    public static void main(String[] args) throws Exception {
//        String encrypt = encrypt("leyan.xu");
//        System.out.println(encrypt);

        byte[] publicKey = getPublicKey("leyan.xu");
        System.out.println("publicKey = ");
        System.out.println(Base64.encodeBase64String(publicKey));

        byte[] privateKey = getPrivateKey("leyan.xu");
        System.out.println("privateKey = ");
        System.out.println(Base64.encodeBase64String(privateKey));
    }

    /**
     * 解密
     *
     * @param content    待解密内容
     * @param decryptKey 解密的 KEY
     * @return
     */
    public static String decrypt(String content, String decryptKey) throws Exception {
        if (content == null || decryptKey == null) {
            throw new IllegalArgumentException("解密内容或解密key不能为null");
        }

        if (decryptKey.length() != KEY_LENGTH) {
            throw new IllegalArgumentException("解密的decryptKey必须为16位");
        }

        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(DEFAULT_ENCODING), KEY_ALGORITHM));
        /*Base64.decodeBase64(content);*/
        byte[] bytes = Base64.decodeBase64(content);
        //byte[] bytes = new BASE64Decoder().decodeBuffer(content);
        bytes = cipher.doFinal(bytes);
        return new String(bytes, DEFAULT_ENCODING);

    }

    public static String decrypt(String content) throws Exception {
        return decrypt(content, AcsCoreConstants.DEFAULT_ENCRYPT_KEY);
    }

    public static String generateAesKey(String key) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(key.getBytes());
        kg.init(128, secureRandom);
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        String s = byteToHexString(b);
        if (s != null && s.length() > 0) {
            return s.substring(0, 16);
        } else {
            throw new IllegalArgumentException("秘钥生成失败");
        }
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     */
    private static byte[] getPublicKey(String slatKey) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(1024, random);//or 2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPublic().getEncoded();
    }

    /**
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     */
    private static byte[] getPrivateKey(String slatKey) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(1024, random);// or 2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPrivate().getEncoded();
    }

}
