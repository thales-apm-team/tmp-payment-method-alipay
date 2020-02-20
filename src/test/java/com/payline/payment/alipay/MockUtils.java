package com.payline.payment.alipay;

import com.payline.payment.alipay.utils.Constants;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.payment.ContractConfiguration;
import com.payline.pmapi.bean.payment.ContractProperty;
import com.payline.pmapi.bean.payment.Environment;
import com.payline.pmapi.bean.payment.Order;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.math.BigInteger;
import java.util.*;

/**
 * Utility class that generates mocks of frequently used objects.
 */
public class MockUtils {
    /**
     * Generate a valid {@link Environment}.
     */
    public static Environment anEnvironment() {
        return new Environment("http://notificationURL.com",
                "http://redirectionURL.com",
                "http://redirectionCancelURL.com",
                true);
    }

    /**
     * Generate a valid {@link PartnerConfiguration}.
     */
    public static PartnerConfiguration aPartnerConfiguration() {
        Map<String, String> partnerConfigurationMap = new HashMap<>();

        partnerConfigurationMap.put(Constants.PartnerConfigurationKeys.ALIPAY_URL, "https://mapi.alipaydev.com/gateway.do");

        Map<String, String> sensitiveConfigurationMap = new HashMap<>();

        return new PartnerConfiguration(partnerConfigurationMap, sensitiveConfigurationMap);
    }
    /**
     * Generate a valid {@link ContractConfiguration} to verify the connection to the API.
     */
    public static ContractConfiguration aContractConfigurationToVerifyConnection() {

        Map<String, ContractProperty> contractProperties = new HashMap<>();
        contractProperties.put(Constants.ContractConfigurationKeys.INPUT_CHARSET, new ContractProperty("utf-8"));
        contractProperties.put(Constants.ContractConfigurationKeys.PARTNER_ID, new ContractProperty("2088621926786355"));
        contractProperties.put(Constants.ContractConfigurationKeys.SERVICE, new ContractProperty("single_trade_query"));
        contractProperties.put(Constants.ContractConfigurationKeys.SIGN_TYPE, new ContractProperty("RSA2"));
        contractProperties.put(Constants.ContractConfigurationKeys.TRANSACTION_ID, new ContractProperty("0"));
        contractProperties.put(Constants.ContractConfigurationKeys.SIGN, new ContractProperty("TKV11jLM8tBAoFFiNT0LzX%2BX%2FYFZnMh4ZjQm4L0aQkxjyMPXFOmCD8hHtLxYN4reQXLC1Pe7vYz2GKFLv1oSVpxWrQ1Ww9so%2F5v7JUsAUcIQY7PqdQ5hHouOSdEw8lGYMjTcI0RZPUlmmWqPNEH2Fw80XR943r7IrQgDQ5kIOXm%2BFQJhvjwNr4o89KldjzRRya3wE3fozbTkBlgRMVqmlGlHFiQzCngkQudF5zijeF8zvSlHrfElYKEQLrv2CSpwFB1AlFA8FMkMHOIe2kJSl35LdqxnajRSRuccuUztioUHPtx5mfAKzWEvLit4tJKDHY54Cvt%2BzkDzh79U7Jnjyw%3D%3D"));

        return new ContractConfiguration("Alipay", contractProperties);
    }    /**
     * Generate a valid {@link ContractConfiguration} to verify the connection to the API.
     */
    public static ContractConfiguration aContractConfiguration() {

        Map<String, ContractProperty> contractProperties = new HashMap<>();
        contractProperties.put(Constants.ContractConfigurationKeys.INPUT_CHARSET, new ContractProperty("utf-8"));
        contractProperties.put(Constants.ContractConfigurationKeys.PARTNER_ID, new ContractProperty("2088621926786355"));
        contractProperties.put(Constants.ContractConfigurationKeys.SERVICE, new ContractProperty("single_trade_query"));
        contractProperties.put(Constants.ContractConfigurationKeys.SIGN_TYPE, new ContractProperty("RSA2"));
        contractProperties.put(Constants.ContractConfigurationKeys.TRANSACTION_ID, new ContractProperty("PAYLINE20200116105303"));
        contractProperties.put(Constants.ContractConfigurationKeys.SIGN, new ContractProperty("TKV11jLM8tBAoFFiNT0LzX%2BX%2FYFZnMh4ZjQm4L0aQkxjyMPXFOmCD8hHtLxYN4reQXLC1Pe7vYz2GKFLv1oSVpxWrQ1Ww9so%2F5v7JUsAUcIQY7PqdQ5hHouOSdEw8lGYMjTcI0RZPUlmmWqPNEH2Fw80XR943r7IrQgDQ5kIOXm%2BFQJhvjwNr4o89KldjzRRya3wE3fozbTkBlgRMVqmlGlHFiQzCngkQudF5zijeF8zvSlHrfElYKEQLrv2CSpwFB1AlFA8FMkMHOIe2kJSl35LdqxnajRSRuccuUztioUHPtx5mfAKzWEvLit4tJKDHY54Cvt%2BzkDzh79U7Jnjyw%3D%3D"));

        return new ContractConfiguration("Alipay", contractProperties);
    }
    /**
     * Generate a valid parameters to verify the connection to the API.
     */
    public static List aValidParametersList() {
        // Create parameters
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("_input_charset", "utf-8"));
        params.add(new BasicNameValuePair("out_trade_no", "0"));
        params.add(new BasicNameValuePair("partner", "2088621926786355"));
        params.add(new BasicNameValuePair("service", "single_trade_query"));
        params.add(new BasicNameValuePair("sign_type", "RSA2"));

        return params;
    }

    /**
     * Generate a valid {@link PaymentFormLogoRequest}.
     */
    public static PaymentFormLogoRequest aPaymentFormLogoRequest() {
        return PaymentFormLogoRequest.PaymentFormLogoRequestBuilder.aPaymentFormLogoRequest()
                .withContractConfiguration(aContractConfiguration())
                .withEnvironment(anEnvironment())
                .withPartnerConfiguration(aPartnerConfiguration())
                .withLocale(Locale.getDefault())
                .build();
    }
    /**
     * Generate a valid {@link ContractParametersCheckRequest}.
     */
    public static ContractParametersCheckRequest aContractParametersCheckRequest() {
        return aContractParametersCheckRequestBuilder().build();
    }
    /**
     * Generate a builder for a valid {@link ContractParametersCheckRequest}.
     * This way, some attributes may be overridden to match specific test needs.
     */
    public static ContractParametersCheckRequest.CheckRequestBuilder aContractParametersCheckRequestBuilder() {
        return ContractParametersCheckRequest.CheckRequestBuilder.aCheckRequest()
                .withAccountInfo(anAccountInfo())
                .withContractConfiguration(aContractConfiguration())
                .withEnvironment(anEnvironment())
                .withLocale(Locale.getDefault())
                .withPartnerConfiguration(aPartnerConfiguration());
    }
    /**
     * Generate a valid accountInfo, an attribute of a {@link ContractParametersCheckRequest} instance.
     */
    public static Map<String, String> anAccountInfo() {
        return anAccountInfo(aContractConfiguration());
    }
    /**------------------------------------------------------------------------------------------------------------------*/

    /**
     * Generate a valid accountInfo, an attribute of a {@link ContractParametersCheckRequest} instance,
     * from the given {@link ContractConfiguration}.
     *
     * @param contractConfiguration The model object from which the properties will be copied
     */
    public static Map<String, String> anAccountInfo(ContractConfiguration contractConfiguration) {
        Map<String, String> accountInfo = new HashMap<>();
        for (Map.Entry<String, ContractProperty> entry : contractConfiguration.getContractProperties().entrySet()) {
            accountInfo.put(entry.getKey(), entry.getValue().getValue());
        }
        return accountInfo;
    }

    /**
     * Generate a valid {@link PaymentFormConfigurationRequest}.
     */
    public static PaymentFormConfigurationRequest aPaymentFormConfigurationRequest() {
        return aPaymentFormConfigurationRequestBuilder().build();
    }
    /**
     * Generate a builder for a valid {@link PaymentFormConfigurationRequest}.
     * This way, some attributes may be overridden to match specific test needs.
     */
    public static PaymentFormConfigurationRequest.PaymentFormConfigurationRequestBuilder aPaymentFormConfigurationRequestBuilder() {
        return PaymentFormConfigurationRequest.PaymentFormConfigurationRequestBuilder.aPaymentFormConfigurationRequest()
                .withAmount(aPaylineAmount())
                .withBuyer(aBuyer())
                .withContractConfiguration(aContractConfiguration())
                .withEnvironment(anEnvironment())
                .withLocale(Locale.FRANCE)
                .withOrder(aPaylineOrder())
                .withPartnerConfiguration(aPartnerConfiguration());
    }
    /**
     * Generate a valid Payline Amount.
     */
    public static com.payline.pmapi.bean.common.Amount aPaylineAmount() {
        return new com.payline.pmapi.bean.common.Amount(BigInteger.valueOf(1000), Currency.getInstance("EUR"));
    }
    /**
     * Generate a valid {@link Buyer}.
     */
    public static Buyer aBuyer() {
        return Buyer.BuyerBuilder.aBuyer()
                .withFullName(new Buyer.FullName("Marie", "Durand", "1"))
                .withEmail("foo@bar.baz")
                .build();
    }
    /**
     * Generate a valid, but not complete, {@link com.payline.pmapi.bean.payment.Order}
     */
    public static com.payline.pmapi.bean.payment.Order aPaylineOrder() {
        List<Order.OrderItem> items = new ArrayList<>();

        items.add(com.payline.pmapi.bean.payment.Order.OrderItem.OrderItemBuilder
                .anOrderItem()
                .withReference("foo")
                .withAmount(aPaylineAmount())
                .withQuantity((long) 1)
                .build());

        return com.payline.pmapi.bean.payment.Order.OrderBuilder.anOrder()
                .withDate(new Date())
                .withAmount(aPaylineAmount())
                .withItems(items)
                .withReference("ORDER-REF-123456")
                .build();
    }

}
