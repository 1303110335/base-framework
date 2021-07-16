/**
 * xuleyan.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 * @author xuleyan
 * @version RSAUtil.java, v 0.1 2020-11-16 8:36 下午
 */
public class RSAUtil {

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    public RSAUtil(byte[] publicKey, byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        this.publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        this.privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 创建公钥和私钥
     * @throws NoSuchAlgorithmException
     */
    public RSAUtil() throws NoSuchAlgorithmException {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
        kpGen.initialize(1024);
        KeyPair keyPair = kpGen.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    /**
     * 公钥加密
     * @param message
     * @return
     * @throws GeneralSecurityException
     */
    public byte[] encrypt(byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
        return cipher.doFinal(message);
    }

    /**
     * 私钥解密
     * @param message
     * @return
     * @throws GeneralSecurityException
     */
    public byte[] decrypt(byte[] message) throws GeneralSecurityException  {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        return cipher.doFinal(message);
    }

    /**
     * 每次生成的公私钥都是不一样的
     * 每次加密后的字符串也是不一样的
     * 但是公钥和私钥都是一对，可以进行对应的加密和解密
     * @param args
     * @throws GeneralSecurityException
     */
    public static void main(String[] args) throws GeneralSecurityException {
        // 明文
        byte[] plain = "Hello, 使用RSA非对称加密算法对数据进行加密!".getBytes(StandardCharsets.UTF_8);
        RSAUtil rsa = new RSAUtil();
        // 加密
        byte[] encrypted = rsa.encrypt(plain);
        System.out.println("encrypted:" + Base64.getEncoder().encodeToString(encrypted));
        // 解密:
        byte[] decrypt = rsa.decrypt(encrypted);
        System.out.println("decrypt:" + new String(decrypt, StandardCharsets.UTF_8));

        String publicKeyStr = Base64.getEncoder().encodeToString(rsa.publicKey.getEncoded());
        System.out.println("公钥:" + publicKeyStr);
        String privateKeyStr = Base64.getEncoder().encodeToString(rsa.privateKey.getEncoded());
        System.out.println("私钥:" + privateKeyStr);

        byte[] publicKey = Base64.getDecoder().decode(publicKeyStr);
        byte[] privateKey = Base64.getDecoder().decode(privateKeyStr);
        RSAUtil rsa2 = new RSAUtil(publicKey, privateKey);

        // 加密
        byte[] encrypted2 = rsa2.encrypt(plain);
        System.out.println("encrypted2:" + Base64.getEncoder().encodeToString(encrypted2));
        // 解密:
        byte[] decrypt2 = rsa2.decrypt(encrypted2);
        System.out.println("decrypt2:" + new String(decrypt2, StandardCharsets.UTF_8));

        //公钥:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEhrbOqBdZsOlung1+o4C7nXr4cAqKcjux6mwS6WxVVC9fKx4CHAQCg1Sd6V0JbgM08ppR/9f3qoFVSrfPIqVFJU2pRdT+A6DvdhnV/php3o1ItCEIY6PZm12VjBoK1hpK5IAfI/kJ+XBZT7yUX6mbkdcz3K6268zUrpmDGsR0qwIDAQAB
        //私钥:MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAISGts6oF1mw6W6eDX6jgLudevhwCopyO7HqbBLpbFVUL18rHgIcBAKDVJ3pXQluAzTymlH/1/eqgVVKt88ipUUlTalF1P4DoO92GdX+mGnejUi0IQhjo9mbXZWMGgrWGkrkgB8j+Qn5cFlPvJRfqZuR1zPcrrbrzNSumYMaxHSrAgMBAAECgYBgiKh8nCB2KtG+y85UotDQ2QOMGED1XARzsqiKlhFdjnb7QbMYIkQheHyYkjY8+i/Hz7ftZwlv1HllIYoGjdwIFFiVAtX/vJXeS7M/vexHcCdPJC+t9CmQH+2NWBww5cu5u0Ikz/Gw/npWp9JMhfy74VHA1IbgGGZwUQZ8MjXUgQJBANegXJzCLF9Tj/mQKp7m0Mz7dXbfBn1z8oLlDDUJb4y6TubELIkitlZLUbjcSE5aYbN/6TYi9QONCjGZ5/CLUksCQQCdVxq6HEaFoVi+HTgXzXRoVSKODqIHAkPySnREUde2Z3Deg4WgIfzvDO9LwG5CDz17jZTxTV2WZvofLkaSTOshAkAGxWhjG83c6Hja82YDO2hPJ45Wv0Yq5ls1NRAfEj7Igok7welMHSTwrMjSL0it/TSYOGoQYySiielKCXU5HoblAkA6DG/k6Bewp5VpFriiN3/SGk7UYFuxAEn3MzbwGc3fEwzWW2nKaZ+6xmfm58kGAD9rvRGNcUCtA7MWG+mXgl9hAkBzo7Gx+RW0D5Zvg8kpJE62eWx1O9RvLNojvqA1ochywOCL/rduEdyyIBgJovgH8NTtFaliUonn+los3jlrnymj


        //公钥:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCokWSad8+h9YR2Zqbqw0SSno32GUrbOtMawzyZhGI+EuAoXXF490lkJWqhOp57ZX6ZtBU040W/v71BsTurtYBaMZgkoTyMqtZWFXcyZ0yt659HbOnS9fkygEVnUBiT3mThF1G2aRbzum12+7GQsBAZtn+gZ/cj/ICj45PVm3Tj+wIDAQAB
        //私钥:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKiRZJp3z6H1hHZmpurDRJKejfYZSts60xrDPJmEYj4S4ChdcXj3SWQlaqE6nntlfpm0FTTjRb+/vUGxO6u1gFoxmCShPIyq1lYVdzJnTK3rn0ds6dL1+TKARWdQGJPeZOEXUbZpFvO6bXb7sZCwEBm2f6Bn9yP8gKPjk9WbdOP7AgMBAAECgYBGukqF7YN3JzbfHw8mLaxWAKT7/3zDI5lz/zssBrnZ5b2b8iksYkWu+WwWHixlBh7qiv7i5AFeBSt5ps2srEkf3j+HUKtJfaAkl2xdRVRJdQfZrOtQZAmuqAQevENML0dUJpd+z/NFdNZLp1nQrTO6RCwT9PvojmJCh+mHjigLUQJBAPT+CjKwN5fvLnrjMrniCVWwjtYRJ703F2uXA7ylAGzDMxdSUyBBTpMLY/ekBHazgAuKLkBGdFpKfe6fwyD7DscCQQCwJE0v1Pv4ioU+2XFyFInyjXIufkcEdIyITSHTq4E/SfMik89IcVjjIE4168WLTXLr5ofMt2S0tjR1ub38Y10tAkB2mGd9ZBp1XR5l9IfAvfAA4Cg07jcLfJjEx2TStBlBjTi1SG/fBF4yBmiNH4PdpkUM0QZUrQZ0Eu1lT0dZNs9PAkAqWuBQoydf0XdO8JyN8RBmycpuEaFkGd59HAcJThFLNa4IX4CVi+Py4Z7M6n/bCmma/hkjienmZnXg5SQhH4eVAkEAq2QZxyBIWbSKAfS6sNXDLoV3KqAPZ5uqWWdvZR/dgXiuPcKVe0pTuKWzblPUbPFX1chDWhutj/cHLp0BZUL5Bw==
        //encrypted2:GuSriYKefh94Ahm0QzfGPs3CUt7R5SaGp0A4hCGX8XXrwVlvChrNOMDfgmHC/7lYkcGpZDMTsYkDycRpQGMqrNVmCmTN2SqqgZMoY7Pq6R8A1E1aLSUQmmOq5ur/4jBYlkD55NY7YpPftxvItSA0xn4KymjCxYhTuVZ0MNeQolQ=

    }
}