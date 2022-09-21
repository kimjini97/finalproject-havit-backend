package com.havit.finalbe;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JasyptTest {

    public static void main(String[] args) {

        String password = "4oCYc3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LWhhbmdoYWUtYXNzaWdubWVudC1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3Qtc2VjcmV0LWtleeKAmQo=";

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("finalproject");
        jasypt.setAlgorithm("PBEWITHMD5ANDDES");

        String encryptedText = jasypt.encrypt(password);
        String decryptedText = jasypt.decrypt(encryptedText);

        System.out.println("encryptedText = " + encryptedText);
        System.out.println("decryptedText = " + decryptedText);
    }

}
