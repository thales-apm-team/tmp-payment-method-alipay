package com.payline.payment.alipay.utils;

/**
 * Support for constants used everywhere in the plugin sources.
 */
public class Constants {

    /**
     * Keys for the entries in ContractConfiguration map.
     */
    public static class ContractConfigurationKeys {
        public static final String INPUT_CHARSET = "INPUT_CHARSET";
        public static final String TRANSACTION_ID = "TRANSACTION_ID";
        public static final String PARTNER_ID = "PARTNER_ID";
        public static final String SERVICE = "SERVICE";
        public static final String SIGN_TYPE = "SIGN_TYPE";
        public static final String SIGN = "SIGN";

        /* Static utility class : no need to instantiate it (Sonar bug fix) */
        private ContractConfigurationKeys(){}
    }

    /**
     * Keys for the entries in PartnerConfiguration maps.
     */
    public static class PartnerConfigurationKeys {


        public static final String ALIPAY_URL = "ALIPAY_URL";
        /* Static utility class : no need to instantiate it (Sonar bug fix) */
        private PartnerConfigurationKeys(){}
    }

    /**
     * Keys for the entries in RequestContext data.
     */
    public static class RequestContextKeys {

        public static final String PAYMENT_ID = "paymentId";

        /* Static utility class : no need to instantiate it (Sonar bug fix) */
        private RequestContextKeys(){}
    }

    /* Static utility class : no need to instantiate it (Sonar bug fix) */
    private Constants(){}

}
