package com.payline.payment.alipay;

import com.payline.payment.alipay.utils.TestUtils;
import com.payline.pmapi.bean.common.Buyer;
import com.payline.pmapi.bean.configuration.PartnerConfiguration;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.bean.configuration.request.RetrievePluginConfigurationRequest;
import com.payline.pmapi.bean.payment.*;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.request.RedirectionPaymentRequest;
import com.payline.pmapi.bean.payment.request.TransactionStatusRequest;
import com.payline.pmapi.bean.paymentform.bean.form.BankTransferForm;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.request.PaymentFormLogoRequest;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utility class that generates mocks of frequently used objects.
 */
public class MockUtils {

    /**
     * Generate a valid accountInfo, an attribute of a {@link ContractParametersCheckRequest} instance.
     */
    public static Map<String, String> anAccountInfo(){
        return anAccountInfo( aContractConfiguration() );
    }

    /**
     * Generate a valid accountInfo, an attribute of a {@link ContractParametersCheckRequest} instance,
     * from the given {@link ContractConfiguration}.
     *
     * @param contractConfiguration The model object from which the properties will be copied
     */
    public static Map<String, String> anAccountInfo( ContractConfiguration contractConfiguration ){
        Map<String, String> accountInfo = new HashMap<>();
        for( Map.Entry<String, ContractProperty> entry : contractConfiguration.getContractProperties().entrySet() ){
            accountInfo.put(entry.getKey(), entry.getValue().getValue());
        }
        return accountInfo;
    }

    /**
     * Generate a valid {@link Browser}.
     */
    public static Browser aBrowser(){
        return Browser.BrowserBuilder.aBrowser()
                .withLocale( Locale.getDefault() )
                .withIp( "192.168.0.1" )
                .withUserAgent( aUserAgent() )
                .build();
    }

    /**
     * Generate a valid {@link Buyer}.
     */
    public static Buyer aBuyer(){
        return Buyer.BuyerBuilder.aBuyer()
                .withFullName( new Buyer.FullName( "Marie", "Durand", "1" ) )
                .build();
    }

    /**
     * Generate a valid {@link ContractConfiguration}.
     */
    public static ContractConfiguration aContractConfiguration(){
        Map<String, ContractProperty> contractProperties = new HashMap<>();
        // TODO: Complete

        // TODO
        return new ContractConfiguration("Change me", contractProperties);
    }

    /**
     * Generate a valid {@link ContractParametersCheckRequest}.
     */
    public static ContractParametersCheckRequest aContractParametersCheckRequest(){
        return aContractParametersCheckRequestBuilder().build();
    }

    /**
     * Generate a builder for a valid {@link ContractParametersCheckRequest}.
     * This way, some attributes may be overridden to match specific test needs.
     */
    public static ContractParametersCheckRequest.CheckRequestBuilder aContractParametersCheckRequestBuilder(){
        return ContractParametersCheckRequest.CheckRequestBuilder.aCheckRequest()
                .withAccountInfo( anAccountInfo() )
                .withContractConfiguration( aContractConfiguration() )
                .withEnvironment( anEnvironment() )
                .withLocale( Locale.getDefault() )
                .withPartnerConfiguration( aPartnerConfiguration() );
    }

    /**
     * Generate a valid {@link Environment}.
     */
    public static Environment anEnvironment(){
        return new Environment("http://notificationURL.com",
                "http://redirectionURL.com",
                "http://redirectionCancelURL.com",
                true);
    }

    /**
     * Generate a valid, but not complete, {@link Order}
     */
    public static Order anOrder(){
        return Order.OrderBuilder.anOrder()
                .withDate( new Date() )
                .withAmount( aPaylineAmount() )
                .withReference( "ORDER-REF-123456" )
                .build();
    }

    /**
     * Generate a valid {@link PartnerConfiguration}.
     */
    public static PartnerConfiguration aPartnerConfiguration(){
        Map<String, String> partnerConfigurationMap = new HashMap<>();
        // TODO: complete

        Map<String, String> sensitiveConfigurationMap = new HashMap<>();
        // TODO: complete

        return new PartnerConfiguration( partnerConfigurationMap, sensitiveConfigurationMap );
    }

    /**
     * Generate a valid Payline <code>Address</code>.
     */
    public static Buyer.Address aPaylineAddress(){
        return Buyer.Address.AddressBuilder.anAddress()
                .withCity("Aix-en-Provence")
                .withCountry("FR")
                .withEmail("john.doe@mythalesgroup.io")
                .withFullName( new Buyer.FullName("Thales", "Services", "M.") )
                .withStreet1("150 rue dont le nom est le plus long que j'ai jamais vu. Y'a pas idée d'habiter un endroit pareil !")
                .withStreet2("Le grand bâtiment orange, avec les fenêtres un peu hautes mais un peu larges aussi, et un toit bleu")
                .withZipCode("13100")
                .build();
    }

    /**
     * Generate a valid Payline Amount.
     */
    public static com.payline.pmapi.bean.common.Amount aPaylineAmount(){
        return new com.payline.pmapi.bean.common.Amount(BigInteger.valueOf(1000), Currency.getInstance("EUR"));
    }

    /**
     * Generate a valid {@link PaymentRequest}.
     */
    public static PaymentRequest aPaylinePaymentRequest(){
        return aPaylinePaymentRequestBuilder().build();
    }

    /**
     * Generate a builder for a valid {@link PaymentRequest}.
     * This way, some attributes may be overridden to match specific test needs.
     */
    public static PaymentRequest.Builder aPaylinePaymentRequestBuilder(){
        return PaymentRequest.builder()
                .withAmount( aPaylineAmount() )
                .withBrowser( aBrowser() )
                .withBuyer( aBuyer() )
                .withCaptureNow( true )
                .withContractConfiguration( aContractConfiguration() )
                .withDifferedActionDate( TestUtils.addTime( new Date(), Calendar.DATE, 5) )
                .withEnvironment( anEnvironment() )
                .withLocale( Locale.getDefault() )
                .withOrder( anOrder() )
                .withPartnerConfiguration( aPartnerConfiguration() )
                .withPaymentFormContext( aPaymentFormContext() )
                .withPluginConfiguration( aPluginConfiguration() )
                .withSoftDescriptor( "softDescriptor" )
                .withTransactionId( "123456789012345678901" );
    }

    /**
     * Generate a valid {@link PaymentFormContext}.
     */
    public static PaymentFormContext aPaymentFormContext(){
        Map<String, String> paymentFormParameter = new HashMap<>();
        paymentFormParameter.put( BankTransferForm.BANK_KEY, "CMBRFR2BARK" );

        return PaymentFormContext.PaymentFormContextBuilder.aPaymentFormContext()
                .withPaymentFormParameter( paymentFormParameter )
                .withSensitivePaymentFormParameter( new HashMap<>() )
                .build();
    }

    /**
     * Generate a valid {@link PaymentFormConfigurationRequest}.
     */
    public static PaymentFormConfigurationRequest aPaymentFormConfigurationRequest(){
        return aPaymentFormConfigurationRequestBuilder().build();
    }

    /**
     * Generate a builder for a valid {@link PaymentFormConfigurationRequest}.
     * This way, some attributes may be overridden to match specific test needs.
     */
    public static PaymentFormConfigurationRequest.PaymentFormConfigurationRequestBuilder aPaymentFormConfigurationRequestBuilder(){
        return PaymentFormConfigurationRequest.PaymentFormConfigurationRequestBuilder.aPaymentFormConfigurationRequest()
                .withAmount( aPaylineAmount() )
                .withBuyer( aBuyer() )
                .withContractConfiguration( aContractConfiguration() )
                .withEnvironment( anEnvironment() )
                .withLocale( Locale.getDefault() )
                .withOrder( anOrder() )
                .withPartnerConfiguration( aPartnerConfiguration() )
                .withPluginConfiguration( aPluginConfiguration() );
    }

    /**
     * Generate a valid {@link PaymentFormLogoRequest}.
     */
    public static PaymentFormLogoRequest aPaymentFormLogoRequest(){
        return PaymentFormLogoRequest.PaymentFormLogoRequestBuilder.aPaymentFormLogoRequest()
                .withContractConfiguration( aContractConfiguration() )
                .withEnvironment( anEnvironment() )
                .withPartnerConfiguration( aPartnerConfiguration() )
                .withLocale( Locale.getDefault() )
                .build();
    }

    /**
     * Generate a valid plugin configuration, as a <code>String</code>.
     */
    public static String aPluginConfiguration(){
        // TODO: complete if necessary (and remove this comment)
        return null;
    }

    /**
     * Generate a valid {@link RedirectionPaymentRequest}.
     */
    public static RedirectionPaymentRequest aRedirectionPaymentRequest(){
        return RedirectionPaymentRequest.builder()
                .withAmount( aPaylineAmount() )
                .withBrowser( aBrowser() )
                .withBuyer( aBuyer() )
                .withContractConfiguration( aContractConfiguration() )
                .withEnvironment( anEnvironment() )
                .withOrder( anOrder() )
                .withPartnerConfiguration( aPartnerConfiguration() )
                .withTransactionId( aTransactionId() )
                .build();
    }

    /**
     * Generate a valid {@link RetrievePluginConfigurationRequest}.
     */
    public static RetrievePluginConfigurationRequest aRetrievePluginConfigurationRequest(){
        return aRetrievePluginConfigurationRequestBuilder().build();
    }

    /**
     * Generate a builder for a valid {@link RetrievePluginConfigurationRequest}.
     * This way, some attributes may be overridden to match specific test needs.
     */
    public static RetrievePluginConfigurationRequest.RetrieveConfigurationRequestBuilder aRetrievePluginConfigurationRequestBuilder(){
        return RetrievePluginConfigurationRequest.RetrieveConfigurationRequestBuilder.aRetrieveConfigurationRequest()
                .withContractConfiguration( aContractConfiguration() )
                .withEnvironment( anEnvironment() )
                .withPartnerConfiguration( aPartnerConfiguration() )
                .withPluginConfiguration( aPluginConfiguration() );
    }

    /**
     * @return a valid transaction ID.
     */
    public static String aTransactionId(){
        return "123456789012345678901";
    }

    /**
     * Generate a valid {@link TransactionStatusRequest}.
     */
    public static TransactionStatusRequest aTransactionStatusRequest(){
        return TransactionStatusRequest.TransactionStatusRequestBuilder.aNotificationRequest()
                .withAmount( aPaylineAmount() )
                .withBuyer( aBuyer() )
                .withContractConfiguration( aContractConfiguration() )
                .withEnvironment( anEnvironment() )
                .withOrder( anOrder() )
                .withPartnerConfiguration( aPartnerConfiguration() )
                .withTransactionId( aTransactionId() )
                .build();
    }

    /**
     * Generate a unique identifier that matches the API expectations.
     */
    public static String aUniqueIdentifier(){
        return "MONEXT" +  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * @return a valid user agent.
     */
    public static String aUserAgent(){
        return "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0";
    }
}
