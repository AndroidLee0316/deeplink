package com.pasc.pascdeeplink;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;

import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test3Des() throws UnsupportedEncodingException, InvalidKeyException {

        byte[] key = "qwertyuiop[]~-!@#$%^&*()_+ASDFGHJKL:ZXCVBNM<>?1234567890QWERTYUIOP{}asdfghjkl;zxcvbnm,./".substring(0,24).getBytes("utf-8");
        //key = "qwertyui".getBytes(Charset.forName("utf-8"));
        byte[] iv = "00000000".getBytes("utf-8");

        String transformation = "DESede/CBC/PKCS5Padding";
        String des = EncryptUtils.encrypt3DES2HexString("com.pingan.smt/app/main/home?tabIndex=1&statsPageName=政务".getBytes("utf-8"), key, transformation, iv);
        System.out.println("des=" + des);


        assertEquals("CQP1oYKAz65ulZayVG+s4cq99+tg72/B6jujAiTFPpN44UXJmMTj0c5iRHiA4MjU1GzAoVsiAu4m4gTTWErq7OnAqMZ2yaxEkF95RQ9MI3k=",des);

        byte[] bytes = EncryptUtils.decryptHexStringDES(des, key, transformation, iv);
        String decrypt = new String(bytes);
        System.out.println("decrypt=" + decrypt);



    }
}