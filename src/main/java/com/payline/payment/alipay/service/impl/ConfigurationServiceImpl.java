package com.payline.payment.alipay.service.impl;

import com.payline.payment.alipay.bean.configuration.RequestConfiguration;
import com.payline.payment.alipay.exception.PluginException;
import com.payline.payment.alipay.utils.Constants;
import com.payline.payment.alipay.utils.http.AlipayHttpClient;
import com.payline.payment.alipay.utils.i18n.I18nService;
import com.payline.payment.alipay.utils.properties.ReleaseProperties;
import com.payline.pmapi.bean.configuration.ReleaseInformation;
import com.payline.pmapi.bean.configuration.parameter.AbstractParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.InputParameter;
import com.payline.pmapi.bean.configuration.parameter.impl.ListBoxParameter;
import com.payline.pmapi.bean.configuration.request.ContractParametersCheckRequest;
import com.payline.pmapi.logger.LogManager;
import com.payline.pmapi.service.ConfigurationService;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConfigurationServiceImpl implements ConfigurationService {

    // TODO: remove if not used !
    private static final String I18N_CONTRACT_PREFIX = "contract.";
    private I18nService i18n = I18nService.getInstance();
    private ReleaseProperties releaseProperties = ReleaseProperties.getInstance();
    private AlipayHttpClient alipayHttpClient = AlipayHttpClient.getInstance();
    /**------------------------------------------------------------------------------------------------------------------*/
    @Override
    public List<AbstractParameter> getParameters(Locale locale) {
        List<AbstractParameter> parameters = new ArrayList<>();

        // INPUT_CHARSET
        InputParameter inputCharset = new InputParameter();
        inputCharset.setKey( Constants.ContractConfigurationKeys.INPUT_CHARSET );
        inputCharset.setLabel( i18n.getMessage("contract.INPUT_CHARSET.label", locale) );
        inputCharset.setDescription( i18n.getMessage("contract.INPUT_CHARSET.description", locale) );
        inputCharset.setRequired( true );
        parameters.add( inputCharset );

        // TRANSACTION ID
        InputParameter transactionId = new InputParameter();
        transactionId.setKey( Constants.ContractConfigurationKeys.TRANSACTION_ID );
        transactionId.setLabel( i18n.getMessage("contract.TRANSACTION_ID.label", locale) );
        transactionId.setDescription( i18n.getMessage("contract.TRANSACTION_ID.description", locale) );
        transactionId.setRequired( true );
        parameters.add( transactionId );

        // PARTNER ID
        InputParameter partnerId = new InputParameter();
        partnerId.setKey( Constants.ContractConfigurationKeys.PARTNER_ID );
        partnerId.setLabel( i18n.getMessage("contract.PARTNER_ID.label", locale) );
        partnerId.setDescription( i18n.getMessage("contract.PARTNER_ID.description", locale) );
        partnerId.setRequired( true );
        parameters.add( partnerId );

        // SERVICE
        InputParameter service = new InputParameter();
        service.setKey( Constants.ContractConfigurationKeys.SERVICE );
        service.setLabel( i18n.getMessage("contract.SERVICE.label", locale) );
        service.setDescription( i18n.getMessage("contract.SERVICE.description", locale) );
        service.setRequired( true );
        parameters.add( service );

        // SIGN_TYPE
        InputParameter signType = new InputParameter();
        signType.setKey( Constants.ContractConfigurationKeys.SIGN_TYPE );
        signType.setLabel( i18n.getMessage("contract.SIGN_TYPE.label", locale) );
        signType.setDescription( i18n.getMessage("contract.SIGN_TYPE.description", locale) );
        signType.setRequired( true );
        parameters.add( signType );

        return parameters;
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    @Override
    public Map<String, String> check(ContractParametersCheckRequest contractParametersCheckRequest) {
        final Map<String, String> errors = new HashMap<>();

        Map<String, String> accountInfo = contractParametersCheckRequest.getAccountInfo();
        Locale locale = contractParametersCheckRequest.getLocale();

        // check required fields
        for( AbstractParameter param : this.getParameters( locale ) ){
            if( param.isRequired() && accountInfo.get( param.getKey() ) == null ){
                String message = i18n.getMessage(I18N_CONTRACT_PREFIX + param.getKey() + ".requiredError", locale);
                errors.put( param.getKey(), message );
            }
        }

        // Check the connection to the API by executing the verifyConnection function
        RequestConfiguration requestConfiguration = RequestConfiguration.build( contractParametersCheckRequest );


        // If type of the signature is missing, no need to go further, as it is required
        String signType = Constants.ContractConfigurationKeys.SIGN_TYPE;

        if( errors.containsKey(signType)){
            return errors;
        }

        try {
            // Try to retrieve an access token
            alipayHttpClient.verifyConnection(requestConfiguration);
        }
        catch( PluginException e ){
            // If an exception is thrown, it means that the client private key is wrong
            errors.put( signType, e.getErrorCode() );
        }
        return errors;
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    @Override
    public ReleaseInformation getReleaseInformation() {
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate(LocalDate.parse(releaseProperties.get("release.date"), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .withVersion(releaseProperties.get("release.version"))
                .build();
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getName(Locale locale) {
        return i18n.getMessage("paymentMethod.name", locale);
    }
    /**------------------------------------------------------------------------------------------------------------------*/

}


