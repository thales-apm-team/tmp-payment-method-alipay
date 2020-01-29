import com.payline.payment.alipay.MockUtils;
import com.payline.payment.alipay.bean.configuration.RequestConfiguration;
import com.payline.payment.alipay.utils.http.AlipayHttpClient;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


class MainTester {
    private static final Logger LOGGER = LogManager.getLogger(MainTester.class);
    private static final AlipayHttpClient alipayHttpClient = AlipayHttpClient.getInstance();
    /**------------------------------------------------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException {
        Boolean connectionStatus;

        try {

            RequestConfiguration requestConfiguration = new RequestConfiguration(MockUtils.aContractConfiguration(), MockUtils.anEnvironment(), MockUtils.aPartnerConfiguration());

            // Test : VerifyPrivateKey
            connectionStatus = alipayHttpClient.verifyConnection(requestConfiguration);
            LOGGER.info("Private Key Status : " + connectionStatus);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**------------------------------------------------------------------------------------------------------------------*/
}
