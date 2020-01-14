package com.payline.payment.alipay.service.impl;

import com.payline.payment.alipay.exception.PluginException;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.bean.payment.request.PaymentRequest;
import com.payline.pmapi.bean.payment.response.PaymentResponse;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.logger.LogManager;
import com.payline.pmapi.service.PaymentService;
import org.apache.logging.log4j.Logger;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);

    @Override
    public PaymentResponse paymentRequest(PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse;

        try {

            paymentResponse = null; // TODO: replace by the real code

        }
        catch( PluginException e ){
            paymentResponse = e.toPaymentResponseFailureBuilder().build();
        }
        catch( RuntimeException e ){
            LOGGER.error("Unexpected plugin error", e);
            paymentResponse = PaymentResponseFailure.PaymentResponseFailureBuilder
                    .aPaymentResponseFailure()
                    .withErrorCode( PluginException.runtimeErrorCode( e ) )
                    .withFailureCause( FailureCause.INTERNAL_ERROR )
                    .build();
        }

        return paymentResponse;
    }
}
