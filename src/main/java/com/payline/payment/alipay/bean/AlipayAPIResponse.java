package com.payline.payment.alipay.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.payline.payment.alipay.exception.InvalidDataException;

import java.io.IOException;


@JsonIgnoreProperties({ "request" })
@JacksonXmlRootElement(namespace = "alipay", localName = "AlipayAPIResponse")
public class AlipayAPIResponse {
    private static XmlMapper xmlMapper = new XmlMapper();

    public static XmlMapper getXmlMapper() {
        return xmlMapper;
    }

    public static void setXmlMapper(XmlMapper xmlMapper) {
        AlipayAPIResponse.xmlMapper = xmlMapper;
    }

    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    private String is_success;
    private Response response;
    private String sign;
    private String sign_type;

    public static AlipayAPIResponse fromXml(String xml) {
        try {
            return xmlMapper.readValue(xml, AlipayAPIResponse.class);
        } catch (IOException e) {
            throw new InvalidDataException("Unable to parse XML CheckStatusResponse", e);
        }
    }
}
