package testapp;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RequestContextService implements GatewayRequestContext{
    private final HttpServletRequest request;

    public RequestContextService(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Boolean trialTransportCertificatePresent()
    {
        return request.getHeader(AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID) != null;
    }


    @Override
    public Boolean trustedTransportCertificatePresent()
    {
        return request.getHeader(AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID) != null;
    }

    @Override
    public UUID getTrialTransportCertificateId() {
        assert trialTransportCertificatePresent();
        return UUID.fromString(request.getHeader(AGHUB_GATEWAYSVC_TRIAL_TRANSPORT_CERT_ID));

    }

    @Override
    public UUID getTrustedTransportCerfificateId() {
        assert trustedTransportCertificatePresent();
        return UUID.fromString(request.getHeader(AGHUB_GATEWAYSVC_TRUSTED_TRANSPORT_CERT_ID));
    }

    @Override
    public String targetServer() {
        return request.getHeader(AGHUB_GATEWAYSVC_TARGET_SERVER);
    }

    @Override
    public HttpMethod requestMethod()
    {
        return HttpMethod.valueOf(request.getMethod());
    }

    @Override
    public String proxy() {
        return request.getHeader(AGHUB_GATEWAYSVC_PROXY);
    }

    @Override
    public HttpServletRequest request()
    {
        return request;
    }

    @Override
    public char[] getPathPhrase() {
        String header = request.getHeader(AGHUB_GATEWAYSVC_PASSPHRASE);
        return header == null ? null : header.toCharArray();
    }

    @Override
    public List<String> getOriginalHeaderNames() {
        String[] split = request.getHeader(AGHUB_GATEWAYSVC_ORIGINAL_HEADERS).split(", ");
        return Stream.of(split).map(String::toLowerCase).collect(Collectors.toList());
    }
}
