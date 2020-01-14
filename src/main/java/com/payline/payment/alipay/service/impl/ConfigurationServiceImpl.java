package com.payline.payment.alipay.service.impl;

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
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);
    private static final String I18N_CONTRACT_PREFIX = "contract.";

    private I18nService i18n = I18nService.getInstance();
    private ReleaseProperties releaseProperties = ReleaseProperties.getInstance();


    @Override
    public List<AbstractParameter> getParameters(Locale locale) {
        List<AbstractParameter> parameters = new ArrayList<>();

        // TODO

        return parameters;
    }

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

        // TODO: check connection

        return errors;
    }

    @Override
    public ReleaseInformation getReleaseInformation() {
        return ReleaseInformation.ReleaseBuilder.aRelease()
                .withDate(LocalDate.parse(releaseProperties.get("release.date"), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .withVersion(releaseProperties.get("release.version"))
                .build();
    }

    @Override
    public String getName(Locale locale) {
        return i18n.getMessage("paymentMethod.name", locale);
    }

    /**
     * Build and return a new <code>InputParameter</code> for the contract configuration.
     *
     * @param key The parameter key
     * @param required Is this parameter required ?
     * @param locale The current locale
     * @return The new input parameter
     */
    private InputParameter newInputParameter( String key, boolean required, Locale locale ){
        InputParameter inputParameter = new InputParameter();
        inputParameter.setKey( key );
        inputParameter.setLabel( i18n.getMessage(I18N_CONTRACT_PREFIX + key + ".label", locale) );
        inputParameter.setDescription( i18n.getMessage(I18N_CONTRACT_PREFIX + key + ".description", locale) );
        inputParameter.setRequired( required );
        return inputParameter;
    }

    /**
     * Build and return a new <code>ListBoxParameter</code> for the contract configuration.
     *
     * @param key The parameter key
     * @param values All the possible values for the list box
     * @param defaultValue The key of the default value (which will be selected by default)
     * @param required Is this parameter required ?
     * @param locale The current locale
     * @return The new list box parameter
     */
    private ListBoxParameter newListBoxParameter( String key, Map<String, String> values, String defaultValue, boolean required, Locale locale ){
        ListBoxParameter listBoxParameter = new ListBoxParameter();
        listBoxParameter.setKey( key );
        listBoxParameter.setLabel( i18n.getMessage(I18N_CONTRACT_PREFIX + key + ".label", locale) );
        listBoxParameter.setDescription( i18n.getMessage(I18N_CONTRACT_PREFIX + key + ".description", locale) );
        listBoxParameter.setList( values );
        listBoxParameter.setRequired( required );
        listBoxParameter.setValue( defaultValue );
        return listBoxParameter;
    }

}


