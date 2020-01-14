package com.payline.payment.alipay;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Manual {

    private static final String MD5_SIGNATURE_KEY = "uosfexjuija0155j7quwwigb7hudp1ym";
    private static final String MERCHANT_PID = "2088621926786355";

    private static final SimpleDateFormat timestamp = new SimpleDateFormat("YYYYMMddHHmmss");

    public static void main( String[] args ) throws NoSuchAlgorithmException {
        //create_forex_trade();
        //create_forex_trade_wap();
        single_trade_query("PAYLINE20200114175915");
    }

    private static void create_forex_trade(){
        System.out.println("create_forex_trade");

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
        System.out.println("sign_type: MD5");
        System.out.println("sign: " + signMd5( params ) );
    }

    private static void create_forex_trade_wap(){
        System.out.println("create_forex_trade_wap");

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
        System.out.println("sign_type: MD5");
        System.out.println( signMd5( params ) );
    }

    private static void single_trade_query( String outTradeNo ){
        System.out.println("single_trade_query");
        Map<String, String> params = new HashMap<>();
        params.put("_input_charset", "utf-8");
        params.put("out_trade_no", outTradeNo);
        params.put("partner", MERCHANT_PID);
        params.put("service", "single_trade_query");

        System.out.println("sign_type: MD5");
        System.out.println("sign: " + signMd5( params ) );
    }

    private static String signMd5( Map<String, String> params ){
        String signingString = signingString( params );
        byte[] digest = new byte[0];
        try {
            digest = MessageDigest.getInstance("MD5").digest( signingString.getBytes(StandardCharsets.UTF_8) );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return DatatypeConverter.printHexBinary( digest ).toLowerCase();
    }

    private static String signingString( Map<String, String> params ){
        String preSign = params.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey()+"="+e.getValue())
                .collect(joining("&"));
        return preSign + MD5_SIGNATURE_KEY;
    }

}
