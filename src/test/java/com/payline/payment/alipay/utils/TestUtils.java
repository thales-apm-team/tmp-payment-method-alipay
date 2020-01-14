package com.payline.payment.alipay.utils;

import com.payline.pmapi.bean.payment.response.impl.PaymentResponseFailure;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseOnHold;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseRedirect;
import com.payline.pmapi.bean.payment.response.impl.PaymentResponseSuccess;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {

    public static Date addTime(Date to, int field, int days ){
        Calendar cal = Calendar.getInstance();
        cal.setTime( to );
        cal.add(field, days);
        return cal.getTime();
    }

    /**
     * Check the validity of a <code>PaymentResponseFailure</code>,
     * based on <code>PaymentResponseFailure#verifyIntegrity()</code> content
     * and the best practices.
     */
    public static void checkPaymentResponse( PaymentResponseFailure response ){
        assertNotNull( response.getErrorCode() );
        assertTrue( response.getErrorCode().length() <= 50 );
        assertNotNull( response.getFailureCause() );
    }

    /**
     * Check the validity of a <code>PaymentResponseOnHold</code>,
     * based on <code>PaymentResponseOnHold#verifyIntegrity()</code> content.
     */
    public static void checkPaymentResponse( PaymentResponseOnHold response ){
        assertNotNull( response.getPartnerTransactionId() );
        assertNotNull( response.getOnHoldCause() );
    }

    /**
     * Check the validity of a <code>PaymentResponseSuccess</code>,
     * based on <code>PaymentResponseSuccess#verifyIntegrity()</code> content.
     */
    public static void checkPaymentResponse( PaymentResponseRedirect response ){
        assertNotNull( response.getPartnerTransactionId() );
        assertNotNull( response.getRedirectionRequest() );
    }

    /**
     * Check the validity of a <code>PaymentResponseSuccess</code>,
     * based on <code>PaymentResponseSuccess#verifyIntegrity()</code> content.
     */
    public static void checkPaymentResponse( PaymentResponseSuccess response ){
        assertNotNull( response.getPartnerTransactionId() );
        assertNotNull( response.getTransactionDetails() );
    }

}
