package com.payline.payment.alipay;


import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Manual {

    private static final String MD5_SIGNATURE_KEY = "uosfexjuija0155j7quwwigb7hudp1ym";
    private static final String MERCHANT_PID = "2088621926786355";

    private static final SimpleDateFormat timestamp = new SimpleDateFormat("YYYYMMddHHmmss");

    public static void main( String[] args ) throws NoSuchAlgorithmException {
        create_forex_trade();
        //create_forex_trade_wap();
        //single_trade_query("PAYLINE20200116105304");
    }

    private static void create_forex_trade(){
        System.out.println("--- create_forex_trade ---\n");

        String tradeNo = "PAYLINE" + timestamp.format(new Date());

        Map<String, String> params = new HashMap<>();
        params.put("_input_charset", "utf-8");
        params.put("currency", "EUR");
        params.put("out_trade_no", tradeNo);
        params.put("partner", MERCHANT_PID);
        params.put("product_code", "NEW_OVERSEAS_SELLER");
        params.put("refer_url", "https://www.google.fr");
        params.put("service", "create_forex_trade");
        params.put("subject", "Test");
        params.put("total_fee", "10.00");

        System.out.println("out_trade_no: " + tradeNo);
        printSignatures( params );
    }

    private static void create_forex_trade_wap(){
        System.out.println("--- create_forex_trade_wap ---\n");

        String tradeNo = "PAYLINE" + timestamp.format(new Date());

        Map<String, String> params = new HashMap<>();
        params.put("_input_charset", "utf-8");
        params.put("currency", "EUR");
        params.put("out_trade_no", tradeNo);
        params.put("partner", MERCHANT_PID);
        params.put("product_code", "NEW_WAP_OVERSEAS_SELLER");
        //params.put("refer_url", "https://www.google.fr");
        params.put("service", "create_forex_trade_wap");
        params.put("subject", "Test");
        params.put("total_fee", "10.00");

        System.out.println("out_trade_no: " + tradeNo);
        printSignatures( params );
    }

    private static void single_trade_query( String outTradeNo ){
        System.out.println("--- single_trade_query ---\n");

        Map<String, String> params = new HashMap<>();
        params.put("_input_charset", "utf-8");
        params.put("out_trade_no", outTradeNo);
        params.put("partner", MERCHANT_PID);
        params.put("service", "single_trade_query");

        printSignatures( params );
    }

    private static String preSigningString(Map<String, String> params ){
        String preSign = params.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey()+"="+e.getValue())
                .collect(joining("&"));
        return preSign;
    }

    private static void printSignatures( Map<String, String> params ){
        // pre-signing string
        String preSigning = preSigningString( params );
        System.out.println("pre-signing string: " + preSigning);
        // MD5
        System.out.println("sign MD5: " + signMd5( preSigning ));
        // RSA2
        String sha256withRsa = signSHA256withRSA( preSigning );
        System.out.println("sign RSA2: " + sha256withRsa);
        try {
            System.out.println("sign RSA2 (URL encoded): " + URLEncoder.encode(sha256withRsa, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String signMd5( String preSigningString ){
        String signingString = preSigningString + MD5_SIGNATURE_KEY;
        byte[] digest = new byte[0];
        try {
            digest = MessageDigest.getInstance("MD5").digest( signingString.getBytes(StandardCharsets.UTF_8) );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary( digest ).toLowerCase();
    }

    private static String signSHA256withRSA( String preSigningString ){
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign( getPk() );
            signature.update( preSigningString.getBytes( StandardCharsets.UTF_8 ) );

            return Base64.getEncoder().encodeToString( signature.sign() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PrivateKey getPk(){
        String keyStoreType = "pkcs12";
        String keyStorePath = "/home/dev/Documents/AliPay/AlipaySandbox20200115.p12";
        char[] passwd = "AlipayCert2020".toCharArray();
        String alias = "selfsigned";

        // Load the keystore and recover the private key
        // @see https://www.baeldung.com/java-keystore
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(keyStoreType);
            ks.load(new FileInputStream(keyStorePath), passwd);
            Certificate cert = ks.getCertificate(alias);
            return (PrivateKey) ks.getKey(alias, passwd);
        }
        catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
