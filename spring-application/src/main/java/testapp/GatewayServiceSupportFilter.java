package testapp;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.net.URI;
import java.util.UUID;
import java.util.stream.Collectors;

@Priority(0)
public class GatewayServiceSupportFilter implements ClientRequestFilter {

    private final static URI PROXY_SERVICE_URI = URI.create("http://localhost:8098/gateway");
    private final static String AGHUB_GATEWAYSVC_TARGET_SERVER = "AGHUB_GATEWAYSVC_TARGET_SERVER";
    private final static String AGHUB_GATEWAYSVC_PROXY = "AGHUB_GATEWAYSVC_PROXY";
    private final static String AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID = "AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID";
    private final static String AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID = "AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID";
    private final static String AGHUB_GATEWAYSVC_TRUSTED_SIGNING_CERT_ID = "AGHUB_GATEWAYSVC_TRUSTED_SIGNING_CERT_ID";
    private final static String AGHUB_GATEWAYSVC_TRIAL_SIGNING_CERT_ID = "AGHUB_GATEWAYSVC_TRIAL_SIGNING_CERT_ID";
    private final static String AGHUB_GATEWAYSVC_PASSPHRASE = "AGHUB_GATEWAYSVC_PASSPHRASE";
    private final static String AGHUB_GATEWAYSVC_ORIGINAL_HEADERS = "AGHUB_GATEWAYSVC_ORIGINAL_HEADERS";


    private final BankConfigBase bankConfigBase;

    public GatewayServiceSupportFilter(BankConfigBase bankConfigBase) {
        this.bankConfigBase = bankConfigBase;
    }

    @Override
    public void filter(ClientRequestContext request) {
        String originalHeaders = String.join(", ", request.getHeaders().keySet());
        request.getHeaders().add(AGHUB_GATEWAYSVC_ORIGINAL_HEADERS, originalHeaders);
        request.getHeaders().add(AGHUB_GATEWAYSVC_TARGET_SERVER, request.getUri().toString());
        request.setUri(PROXY_SERVICE_URI);
        request.getHeaders().add(AGHUB_GATEWAYSVC_PROXY, "cache.konts.lv:8080");
        request.getHeaders().add(AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID, UUID.randomUUID().toString());
        request.getHeaders().add(AGHUB_GATEWAYSVC_TRIAL_SIGNING_CERT_ID, bankConfigBase.getTrialSigningCertifivateId());
        request.getHeaders().add(AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID, bankConfigBase.getTrustedTransportCertificateId());
        request.getHeaders().add(AGHUB_GATEWAYSVC_TRUSTED_SIGNING_CERT_ID, bankConfigBase.getTrustedSigningCertificateId());
        String passPhrase = bankConfigBase.getPassPhrase();
        if (passPhrase!=null) {
            request.getHeaders().add(AGHUB_GATEWAYSVC_PASSPHRASE, passPhrase);
        }

    }
}
