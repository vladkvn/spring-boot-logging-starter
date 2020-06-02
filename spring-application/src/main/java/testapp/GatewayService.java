package testapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
public class GatewayService {

    @Autowired
    private Function<UUID, RestTemplate> trialCertificateRestTemplateFactory;

    @Autowired
    private RequestContextService requestContextService;


    public ResponseEntity doRequest(String requestBody) {
        RestTemplate restTemplateForCall = getRestTemplateForCall();
        try {
            ResponseEntity<String> exchange = restTemplateForCall.exchange(
                    URI.create(requestContextService.targetServer()),
                    requestContextService.requestMethod(),
                    new HttpEntity<>(requestBody, headersFromOriginalRequest()),
                    String.class);
            return ResponseEntity
                    .status(exchange.getStatusCode())
                    .headers(exchange.getHeaders())
                    .body(exchange.getBody());
        }
        catch (HttpStatusCodeException statusCodeException)
        {
            return ResponseEntity
                    .status(statusCodeException.getStatusCode())
                    .headers(statusCodeException.getResponseHeaders())
                    .body(statusCodeException.getResponseBodyAsString());
        }
    }

    private HttpHeaders headersFromOriginalRequest() {
        final HttpServletRequest request = requestContextService.request();
        List<String> originalHeaderNames = requestContextService.getOriginalHeaderNames();
        return new HttpHeaders() {{
            Collections.list(request.getHeaderNames())
                    .stream()
                    .map(String::toLowerCase)
                    .filter(originalHeaderNames::contains)
                    .forEach(headerName -> set(headerName, request.getHeader(headerName)));
        }};
    }

    private RestTemplate getRestTemplateForCall() {
        if (requestContextService.trialTransportCertificatePresent()) {
            return trialCertificateRestTemplateFactory.apply(requestContextService.getTrialTransportCertificateId());
        } else {
            return new RestTemplate();
        }
    }
}
