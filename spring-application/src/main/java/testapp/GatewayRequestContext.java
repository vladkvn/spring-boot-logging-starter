package testapp;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface GatewayRequestContext {
    String AGHUB_GATEWAYSVC_TARGET_SERVER = "AGHUB_GATEWAYSVC_TARGET_SERVER";
    String AGHUB_GATEWAYSVC_PROXY = "AGHUB_GATEWAYSVC_PROXY";
    String AGHUB_GATEWAYSVC_PASSPHRASE = "AGHUB_GATEWAYSVC_PASSPHRASE";

    String AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID = "AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID";
    String AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID = "AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID";
    String AGHUB_GATEWAYSVC_TRUSTED_SIGNING_CERT_ID = "AGHUB_GATEWAYSVC_TRUSTED_SIGNING_CERT_ID";
    String AGHUB_GATEWAYSVC_TRIAL_SIGNING_CERT_ID = "AGHUB_GATEWAYSVC_TRIAL_SIGNING_CERT_ID";

    String AGHUB_GATEWAYSVC_ORIGINAL_HEADERS = "AGHUB_GATEWAYSVC_ORIGINAL_HEADERS";


    Boolean trialTransportCertificatePresent();

    Boolean trustedTransportCertificatePresent();

    UUID getTrialTransportCertificateId();
    //UUID getTrialSigningCertificateId(); - future to do!

    UUID getTrustedTransportCerfificateId();
    //UUID getTrustedSigningCerfificateId(); - future to do!

    String targetServer();

    HttpMethod requestMethod();

    String proxy();

    HttpServletRequest request();

    char[] getPathPhrase();

    List<String> getOriginalHeaderNames();
}
