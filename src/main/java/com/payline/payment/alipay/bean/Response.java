package com.payline.payment.alipay.bean;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.payline.payment.alipay.exception.InvalidDataException;

import java.io.IOException;

public class Response {
    private static XmlMapper xmlMapper = new XmlMapper();

    public static XmlMapper getXmlMapper() {
        return xmlMapper;
    }

    public static void setXmlMapper(XmlMapper xmlMapper) {
        Response.xmlMapper = xmlMapper;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    private Trade trade;

    public static Response fromXml(String xml) {
        try {
            return xmlMapper.readValue(xml, Response.class);
        } catch (IOException e) {
            throw new InvalidDataException("Unable to parse XML CheckStatusResponse", e);
        }
    }
}
