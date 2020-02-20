package com.payline.payment.alipay.utils.http;

import com.payline.payment.alipay.bean.AlipayAPIResponse;
import com.payline.payment.alipay.bean.configuration.RequestConfiguration;
import com.payline.payment.alipay.exception.InvalidDataException;
import com.payline.payment.alipay.exception.PluginException;
import com.payline.payment.alipay.utils.Constants;
import com.payline.payment.alipay.utils.PluginUtils;
import com.payline.payment.alipay.utils.properties.ConfigProperties;
import com.payline.pmapi.bean.common.FailureCause;
import com.payline.pmapi.logger.LogManager;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class AlipayHttpClient {

    private static final Logger LOGGER = LogManager.getLogger(AlipayHttpClient.class);

    private static  final String KEY_STORE_PATH = "/home/dev/Documents/AliPay/AlipaySandbox20200115.p12";

    // Exceptions messages
    private static final String SERVICE_URL_ERROR = "Service URL is invalid";
    private static final String SYNTAX_ENCODING = "Syntax Exception";
    /**
     * The number of time the client must retry to send the request if it doesn't obtain a response.
     */
    private int retries;

    private HttpClient client;

    // --- Singleton Holder pattern + initialization BEGIN


    // TODO : Faire une méthode init() comme sur Equens. Normalement on aura pas besoin de la partie ssl.Pas besoin de certificat.
    // TODO : Il faut juste récupérer la clé privée dans les partners configuration pour générer la signature.
    /**------------------------------------------------------------------------------------------------------------------*/
    AlipayHttpClient() {
        int connectionRequestTimeout;
        int connectTimeout;
        int socketTimeout;
        try {
            // request config timeouts (in seconds)
            ConfigProperties config = ConfigProperties.getInstance();
            connectionRequestTimeout = Integer.parseInt(config.get("http.connectionRequestTimeout"));
            connectTimeout = Integer.parseInt(config.get("http.connectTimeout"));
            socketTimeout = Integer.parseInt(config.get("http.socketTimeout"));

            // retries
            this.retries = Integer.parseInt(config.get("http.retries"));
        } catch (NumberFormatException e) {
            throw new PluginException("plugin error: http.* properties must be integers", e);
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout * 1000)
                .setConnectTimeout(connectTimeout * 1000)
                .setSocketTimeout(socketTimeout * 1000)
                .build();

        // instantiate Apache HTTP client
        this.client = HttpClientBuilder.create()
                .useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(new SSLConnectionSocketFactory(HttpsURLConnection.getDefaultSSLSocketFactory(), SSLConnectionSocketFactory.getDefaultHostnameVerifier()))
                .build();

    }
    /**------------------------------------------------------------------------------------------------------------------*/
    private static class Holder {
        private static final AlipayHttpClient instance = new AlipayHttpClient();
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    public static AlipayHttpClient getInstance() {
        return Holder.instance;
    }
    // --- Singleton Holder pattern + initialization END
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Send the request, with a retry system in case the client does not obtain a proper response from the server.
     *
     * @param httpRequest The request to send.
     * @return The response converted as a {@link StringResponse}.
     * @throws PluginException If an error repeatedly occurs and no proper response is obtained.
     */
    StringResponse execute(HttpRequestBase httpRequest) {
        StringResponse strResponse = null;
        int attempts = 1;

        while (strResponse == null && attempts <= this.retries) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Start call to partner API (attempt {}) :" + System.lineSeparator() + PluginUtils.requestToString(httpRequest), attempts);
            } else {
                LOGGER.info("Start call to partner API [{} {}] (attempt {})", httpRequest.getMethod(), httpRequest.getURI(), attempts);
            }
            try (CloseableHttpResponse httpResponse = (CloseableHttpResponse) this.client.execute(httpRequest)) {
                strResponse = StringResponse.fromHttpResponse(httpResponse);
            } catch (IOException e) {
                LOGGER.error("An error occurred during the HTTP call :", e);
                strResponse = null;
            } finally {
                attempts++;
            }
        }

        if (strResponse == null) {
            throw new PluginException("Failed to contact the partner API", FailureCause.COMMUNICATION_ERROR);
        }
        LOGGER.info("Response obtained from partner API [{} {}]", strResponse.getStatusCode(), strResponse.getStatusMessage());
        return strResponse;
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Verify if API url are present
     *
     * @param requestConfiguration
     */
    private void verifyPartnerConfigurationURL(RequestConfiguration requestConfiguration) {
        if (requestConfiguration.getPartnerConfiguration().getProperty(Constants.PartnerConfigurationKeys.ALIPAY_URL) == null) {
            throw new InvalidDataException("Missing API url from partner configuration (sensitive properties)");
        }

    }
    /**------------------------------------------------------------------------------------------------------------------*/
    public AlipayAPIResponse single_trade_query(RequestConfiguration requestConfiguration){
        // Create parameters
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("_input_charset", requestConfiguration.getContractConfiguration().getProperty(Constants.ContractConfigurationKeys.INPUT_CHARSET).getValue()));
        //TODO : Récupérer le numéro de transaction via request.getTransaction
        params.add(new BasicNameValuePair("out_trade_no", requestConfiguration.getContractConfiguration().getProperty(Constants.ContractConfigurationKeys.TRANSACTION_ID).getValue()));
        params.add(new BasicNameValuePair("partner", requestConfiguration.getContractConfiguration().getProperty(Constants.ContractConfigurationKeys.PARTNER_ID).getValue()));
        params.add(new BasicNameValuePair("service", requestConfiguration.getContractConfiguration().getProperty(Constants.ContractConfigurationKeys.SERVICE).getValue()));
        params.add(new BasicNameValuePair("sign_type", requestConfiguration.getContractConfiguration().getProperty(Constants.ContractConfigurationKeys.SIGN_TYPE).getValue()));

        // Get the result of the request
        StringResponse response = get(requestConfiguration, params);

        AlipayAPIResponse alipayAPIResponse = AlipayAPIResponse.fromXml(response.getContent());

        return alipayAPIResponse;
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Verify API connection
     *
     * @param requestConfiguration
     * @return
     */
    public Boolean verifyConnection(RequestConfiguration requestConfiguration) {

        // Create parameters
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("_input_charset", "utf-8"));
        params.add(new BasicNameValuePair("out_trade_no", "0"));
        params.add(new BasicNameValuePair("partner", requestConfiguration.getContractConfiguration().getProperty(Constants.ContractConfigurationKeys.PARTNER_ID).getValue()));
        params.add(new BasicNameValuePair("service", "single_trade_query"));
        params.add(new BasicNameValuePair("sign_type", "RSA2"));

        // Get the result of the request
        StringResponse response = get(requestConfiguration, params);

        // Check the response content
        if (response.getContent() == null) {
            LOGGER.error("No response body");
            return false;
        }

        return response.getContent().contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<alipay><is_success>F</is_success><error>TRADE_NOT_EXIST</error></alipay>");
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Manage Post API call
     *
     * @param requestConfiguration
     * @return
     */
    public StringResponse get(RequestConfiguration requestConfiguration, List<NameValuePair> params) {
        URI uri;
        URI baseUrl;

        // Check if API url is present
        verifyPartnerConfigurationURL(requestConfiguration);

        // Get the API URL
        try {
            baseUrl = new URI(requestConfiguration.getPartnerConfiguration().getProperty(Constants.PartnerConfigurationKeys.ALIPAY_URL));
        } catch (URISyntaxException e) {
            throw new InvalidDataException(SERVICE_URL_ERROR, e);
        }

        // Create the pre-signing string
        String preSigning = preSigningString(params);

        // Create the RSA2 signature
        String sha256withRsa = signSHA256withRSA(preSigning);

        // Add the signature to the parameters
        params.add(new BasicNameValuePair("sign", sha256withRsa));

        // Create the HttpGet url with parameters
        try {
            uri = new URIBuilder(baseUrl).setParameters(params).build();
        } catch (URISyntaxException e) {
            throw new InvalidDataException(SYNTAX_ENCODING, e);
        }

        // Create the HttpGet request
        HttpGet httpGet = new HttpGet(uri);

        // Execute request
        return this.execute(httpGet);
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Format parameters to generate a signature
     *
     * @param params
     * @return
     */
    private static String preSigningString(List<NameValuePair> params) {
        StringBuilder preSign = new StringBuilder();
        boolean first = true;

        // Build a string from parameters
        for (NameValuePair nameValuePair : params) {

            // The "sign_type" parameter is not used to generate the request signature
            if (!nameValuePair.getName().equals("sign_type")) {
                // Add the separator
                if (!first) {
                    preSign.append("&");
                }
                // Add name and value
                preSign.append(nameValuePair.getName() + "=" + nameValuePair.getValue());
            }
            first = false;
        }

        return preSign.toString();
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Generate a RSA signature based on the query parameters
     *
     * @param preSigningString
     * @return
     */
    private static String signSHA256withRSA(String preSigningString) {  ///NoSuchAlgorithmException, InvalidKeyException, SignatureException
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(getPk());
            signature.update(preSigningString.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            LOGGER.error("signSHA256withRSA",e);

        }
        return null;
    }
    /**------------------------------------------------------------------------------------------------------------------*/
    /**
     * Return the private key in the keyStore
     *
     * @return
     */
    private static PrivateKey getPk() {

        // TODO: récupérer la clé privée dans les partners configuration

        String keyStoreType = "pkcs12";
        char[] passwd = "AlipayCert2020".toCharArray();
        String alias = "selfsigned";

        // Load the keystore and recover the private key
        // @see https://www.baeldung.com/java-keystore
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(keyStoreType);
            ks.load(new FileInputStream(KEY_STORE_PATH), passwd);
            return (PrivateKey) ks.getKey(alias, passwd);
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("getPk",e);
        }
        return null;
    }
    /**------------------------------------------------------------------------------------------------------------------*/

}
