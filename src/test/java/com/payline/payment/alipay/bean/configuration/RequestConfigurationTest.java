package com.payline.payment.alipay.bean.configuration;

import com.payline.payment.alipay.MockUtils;
import com.payline.payment.alipay.exception.PluginException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestConfigurationTest {

    @Test
    void constructor_nominal(){
        // given: the constructor is passed valid arguments, when: calling the constructor
        RequestConfiguration requestConfiguration = new RequestConfiguration( MockUtils.aContractConfiguration(),
                MockUtils.anEnvironment(), MockUtils.aPartnerConfiguration() );

        // then: the instance is not null, no exception is thrown
        assertNotNull( requestConfiguration );
    }

    @Test
    void constructor_nullContractConfiguration(){
        // given: the constructor is a null ContractConfiguration, when: calling the constructor, then: an exception is thrown
        assertThrows(PluginException.class, () -> new RequestConfiguration( null,
                MockUtils.anEnvironment(), MockUtils.aPartnerConfiguration() ) );
    }

    @Test
    void constructor_nullEnvironment(){
        // given: the constructor is a null ContractConfiguration, when: calling the constructor, then: an exception is thrown
        assertThrows(PluginException.class, () -> new RequestConfiguration( MockUtils.aContractConfiguration(),
                null, MockUtils.aPartnerConfiguration() ) );
    }

    @Test
    void constructor_nullPartnerConfiguration(){
        // given: the constructor is a null ContractConfiguration, when: calling the constructor, then: an exception is thrown
        assertThrows(PluginException.class, () -> new RequestConfiguration( MockUtils.aContractConfiguration(),
                MockUtils.anEnvironment(), null ) );
    }

}
