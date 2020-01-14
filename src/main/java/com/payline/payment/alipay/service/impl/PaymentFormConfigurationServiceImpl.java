package com.payline.payment.alipay.service.impl;

import com.payline.payment.alipay.exception.PluginException;
import com.payline.payment.alipay.service.LogoPaymentFormConfigurationService;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.paymentform.request.PaymentFormConfigurationRequest;
import com.payline.pmapi.bean.paymentform.response.configuration.PaymentFormConfigurationResponse;
import com.payline.pmapi.bean.paymentform.response.configuration.impl.PaymentFormConfigurationResponseFailure;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

public class PaymentFormConfigurationServiceImpl extends LogoPaymentFormConfigurationService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentFormConfigurationServiceImpl.class);

    @Override
    public PaymentFormConfigurationResponse getPaymentFormConfiguration(PaymentFormConfigurationRequest paymentFormConfigurationRequest) {
        PaymentFormConfigurationResponse pfcResponse;
        try {
            pfcResponse = null; // TODO: replace by the real code
        }
        catch( PluginException e ){
            pfcResponse = e.toPaymentFormConfigurationResponseFailureBuilder().build();
        }
        catch( RuntimeException e ){
            LOGGER.error("Unexpected plugin error", e);
            pfcResponse = PaymentFormConfigurationResponseFailure.PaymentFormConfigurationResponseFailureBuilder
                    .aPaymentFormConfigurationResponseFailure()
                    .withErrorCode( PluginException.runtimeErrorCode( e ) )
                    .withFailureCause( FailureCause.INTERNAL_ERROR )
                    .build();
        }

        return pfcResponse;
    }
}
