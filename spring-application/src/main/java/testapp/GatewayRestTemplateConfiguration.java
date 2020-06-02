package testapp;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class GatewayRestTemplateConfiguration {


    @Autowired
    RequestContextService requestContextService;

    @Bean
    Function<UUID, RestTemplate> trialCertificateRestTemplateFactory()
    {
        return this::trialCertificateRestTemplateFactoryMethod;
    }

    public RestTemplate trialCertificateRestTemplateFactoryMethod(UUID trialTransportCertificateId)
    {
        String publicKey = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDNzCCAh+gAwIBAgIGAXI6H3UKMA0GCSqGSIb3DQEBCwUAMBYxFDASBgNVBAMMC2RldnBvcnRhbENhMB4XDTIwMDUyMjAwMDAwMFoXDTIxMDUyMjAwMDAwMFowgaIxIzAhBgkqhkiG9w0BCQEWFHlhZmVrZXZvckAzZG1haWwudG9wMR0wGwYDVQQDDBR5YWZla2V2b3JAM2RtYWlsLnRvcDETMBEGA1UECxMKSW5ub3ZhdGlvbjESMBAGA1UEChMJU2VudGhhZGV2MRIwEAYDVQQHEwlUcm9uZGhlaW0xEjAQBgNVBAgTCVRyb25kaGVpbTELMAkGA1UEBhMCTk8wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCBsv//wAtaBf0RUSFeGVSMbz8NX9eN8dHjJ8OVYh1TO7mKVNYSL4dazXuMl7Erauy2sTqfKMaE200xwofd7MQmMmz8uVbxrtLLGj5EM3SKRd7EW4KbTTeQYZGvMVOm5aOT2557UNduDeNLnm+zUNLG4lDSTXnHJDaQ145XdZZcQSxIlVdYcIfcXsG8AKvGIMbK7ZaZoJ3arw7KbX1kKcA3UB45hXsLxBlKLqkAw3VhQetY69y0xXdVTh2Xkuf6hWS++9SC5f9ILTFqruvGm2Xc5Ilxs6j7PtbNeJKInuf0Ko+tY7ela4QHbgM2SOtFUC6matJbHzOZ0kNTub5DebOvAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAH5+E/FdoYcrsMoH9WYSZqlKxAGQd0tY1XdhJ0T0I/mRiTaRIxLaM/rdFK8DZdddJ9GGz78YkylL7NdABoColUj2OTVyjnRgW+Gz5XcoLW295ZiA+1ygGiyDfYJwRfkO4F+ZU4O5SzzC1HmrDWwtCvlm4nuExf2qIOV+hKA5FmpOa3h1kSc12m5iHZnhEENj3ov1oOxMQjEKQTZobqm4jiubzvk3GHtcmJaxB1GJANNANbBXrPujwnKjZwSQC8Q5GGf28IEGrfXH6+BoFnNaAQY0GeHIhwoxxISi/QWPIsihHosSSIKheDTBjvObKM1tA/X7yVfBdCypaYPm7Jn5fNs=\n" +
                "-----END CERTIFICATE-----";
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBsv//wAtaBf0RUSFeGVSMbz8NX9eN8dHjJ8OVYh1TO7mKVNYSL4dazXuMl7Erauy2sTqfKMaE200xwofd7MQmMmz8uVbxrtLLGj5EM3SKRd7EW4KbTTeQYZGvMVOm5aOT2557UNduDeNLnm+zUNLG4lDSTXnHJDaQ145XdZZcQSxIlVdYcIfcXsG8AKvGIMbK7ZaZoJ3arw7KbX1kKcA3UB45hXsLxBlKLqkAw3VhQetY69y0xXdVTh2Xkuf6hWS++9SC5f9ILTFqruvGm2Xc5Ilxs6j7PtbNeJKInuf0Ko+tY7ela4QHbgM2SOtFUC6matJbHzOZ0kNTub5DebOvAgMBAAECggEAFmaLDmnlzAOSxMXlZHIthx/DnRS7jvmADaKVhhxvAiwjjH/AhV2OHCG3P/4HDH+oKJmNjWWme9XAjGKTwsGc0xgMeM2t9YoDQC7lKDRW4XbV1cYUVaxNd3NSjRaQ4xdwXB4nThcVTh257xqLIkZIUS9PVDMrYDih7/w6ymI1uPMLBcgcRQNo4RNYmSEL0rfBxsAXP+kUYhxf47snts3gux/g3QzQERCQ3HvzC2ecgnyNwSaqLdZJvzF6gq59wwOA7LUGr2As4F6J062g/uqJ5V5cnztXFnmqTQJJKWj8OQIjq7hbp91Q7QIbrwKG8LQgC02JQqL2n2LNQwqYbSyMKQKBgQDz8/eCDuOXoJjj1qqVOpPrF9ecc/CT1wic1Q73opmmHU4n1hZLkusKTac/1g2DQZ3CXmqM2EDAfy1n9G+/e+nYgh6C01s7/kn2qGWapBnciFRAjqYH9JGNcFN8Q72Zfs4QRfcPb8w3E6kj7rLBidOO9hFCVE5QgRUWOgv/Nrs+PQKBgQCIGqWFj2YKNZIgeBUWnklV9GgCJ08BmLNb/sETnB5fVOkm7P9MG5WYHHfcb9Xnw0uWStpv/RN7KY5ioTzLr64eHJGclWZvO20TIRvGJFk3AdNMcmwXogmdE329uyPmFPXNRuaZO2muSarBt31r29mbePK2CqfgfOBxnDnv/3IkWwKBgQCAuh3CvUSPQCktIJtGv3BKP3yAjBVIjdF3oF2TgnN9Wj1he/Cr1Ey+VReJ8dOTjU5HwDaNN5eDfBhjoHJPIn3Ux2R+ODUORTKWO2nR3gHfgviiUCOAUydy8PC8w6oKkwT9RU4HEUE7Vt6LnWsqASZna6TT0VS52kNcRSkLdL/9KQKBgBs6+ZLXNdslDK9iVxXHZXTFCWok4zG2k2v8dDSwL4NzQZKAKDPZ8PwV9gUseud7s0vGfh2ZzKP/lHU3PSqjY3qyzI8/dNccVJ5XKW9jJlZTDytkWSwBR1sd4zJqlqwZ1XPUk55kvKgaJQfM5lMGEWuBr02spBnN4jnYv968hOGxAoGBAKnPxk8BCiryKSkG7VYdCGHRG7GEsSgJhfKoa8y7qLsgA876Ljj6qxP1CviFjbCIP0ztdpD/NNWyU8cLVyc+UImcaMJTCA2HcRSp72Jxm+ntxxLj9Rs5KGbazYNFnJQXY2Z1soILWEZAK7zZJUMN2BkKGtz/bEX/H6hiZxNO6RCi\n" +
                "-----END PRIVATE KEY-----";
        SSLContext sslContext = TrialCertificatesSslContextBuilder.createSslContext(requestContextService.targetServer(), publicKey, privateKey, requestContextService.getPathPhrase());

        CloseableHttpClient httpClient = HttpClients.custom()
                .setProxy(HttpHost.create(requestContextService.proxy()))
                .setSSLContext(sslContext)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
        RestTemplate restTemplate = new RestTemplate(bufferingClientHttpRequestFactory);
        return restTemplate;
    }


//    @Bean
    public String getString()
    {
        System.out.println("-----BEAN CREATED-----");
        String publicKey = "-----BEGIN CERTIFICATE-----\nMIID1zCCAr+gAwIBAgIEGeswejANBgkqhkiG9w0BAQsFADB6MQswCQYDVQQGEwJOTzEPMA0GA1UECAwGTm9yd2F5MQ0wCwYDVQQHDARPc2xvMRAwDgYDVQQKDAdETkIgQVNBMRkwFwYDVQQLDBBETkIgUFNEMiBTYW5kYm94MR4wHAYDVQQDDBVzYW5kYm94YXBpLnBzZC5kbmIubm8wHhcNMTkxMDAxMDc0MTA1WhcNMjAwOTMwMDAwMDAwWjB0MQ4wDAYDVQQKDAVUaWV0bzEOMAwGA1UEAwwFVGlldG8xJTAjBgoJkiaJk/IsZAEZFhVzYW5kYm94YXBpLnBzZC5kbmIubm8xCzAJBgNVBAYTAk5PMR4wHAYDVQRhDBVQU0ROTy1UU1ROQ0EtMjk3MjQzOTgwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCCPYkUQkhiXu9/KpfJiclmWdR0pAJ99waIpN+OByNVJ4jVQ4zpCq4OjXyxTWPnYuKBY1R7Fbwb+Q2DnwXxmD60lbkPEcnQdqiciBmIZciJwYAvkrhRafipD7hzIwA6vzQql7eDWBR12Llo7g5BppSluQV7G9OP4Bs8UKMVpJKnJPHqam2z8NoObE3vi3S2+M5WYcDaCySBjUflYQwbCpc7BrcaHRYy1W8zWbbI47VFz5wPToE+KPTDQVlZ3dFA+uYplOE1EPt187AIplAeuzWgzpm1JOADVLqltfDqpWqgY5PfF2Qe2nnXWkhiSa0tEbuW8gKj4CQrLw2Y5gYFWsBPAgMBAAGjazBpMGcGCCsGAQUFBwEDBFswWQYGBACBmCcCME8wOTARBgcEAIGYJwEDDAZQU1BfQUkwEQYHBACBmCcBAgwGUFNQX1BJMBEGBwQAgZgnAQQMBlBTUF9JQwwHRE5CIEFTQQwJTk8tVFNUTkNBMA0GCSqGSIb3DQEBCwUAA4IBAQCDtxuF3D1CkWQn4BxoXJ7o4J4uOUNrXlBIh7JJQciulMRRZNbLaNJYb1IBYdhJMIUpgNbjpOrZ1uURWvHDp8c5UQIS8V6qJbIb7X+d8VAWv7CLAVKwTmEGCr3v0b6k1XLFcNjTep1KtoxLvM9qecLCWsqPZ5vMWPeIBhpNxNtmVw15GZJdLLW0uRFi8zmIRS/5HQjH82jUX+Ap7CLZQW7Ub7HloScnBi+ZFkmgPYsPy/NRCRLT1jrU7eiqLz6HzKR6rR1ctogceBaQ9cAh3Yp/ILndhmHQNh8JNJBVdjpVdSspDXfDp3piPRYLF3XBB9CfzcDweEh8AA1ZSY1EV5sv\n-----END CERTIFICATE-----";
        String privateKey = "-----BEGIN RSA PRIVATE KEY-----\nMIIEowIBAAKCAQEAgj2JFEJIYl7vfyqXyYnJZlnUdKQCffcGiKTfjgcjVSeI1UOM6QquDo18sU1j52LigWNUexW8G/kNg58F8Zg+tJW5DxHJ0HaonIgZiGXIicGAL5K4UWn4qQ+4cyMAOr80Kpe3g1gUddi5aO4OQaaUpbkFexvTj+AbPFCjFaSSpyTx6mpts/DaDmxN74t0tvjOVmHA2gskgY1H5WEMGwqXOwa3Gh0WMtVvM1m2yOO1Rc+cD06BPij0w0FZWd3RQPrmKZThNRD7dfOwCKZQHrs1oM6ZtSTgA1S6pbXw6qVqoGOT3xdkHtp511pIYkmtLRG7lvICo+AkKy8NmOYGBVrATwIDAQABAoIBACKUpiX7524/GMYiEAVSEWicp12FAqhhg5madUUnvgjpI1rEwdBKpqZ0iMwm6UTRq6SBNDguMDEZMq5k9dYM/3FBY86NgZFZQDG3rVfa9qElAJ0hDtGHGqAA7Y+cln4CsJ4yhGZaZ6xWq/whYSWrTkKmWr8yRz0nWvPr0TF47ft03Z0tYtR46N+kMfL+lxm9+hNgmpgiujmRrMjzUvyLK3kK/o9XytdZJGg0KY4P3xJcBCoQDqTSYTJftqejPoUcSL8mW7mP1FxDHs8MDvwJFyuQsQTU6SfaXuR8OUXQrQdXg8AUGfv+MUB39JDrexCg8WlFPEnOoYjmX2zi3qMdy7kCgYEA9O99p2pAth8mNYM4rixUFcs1Lf62quHjK9fiALmZecMmeCqHbkiWiqD4m2Kvwv1WSPN/RCK11DAKvgpu2Hnlr9iy422daiXWF8aaTPtIyYmF1Ilxm0LU1+evYwEcwZvM8adaqFdev8QQ0L8wD2jAre5N5jIt1avyTWzkgKeVN8sCgYEAiB+syvfhK3G8rbNya1LTY65H+26a5SmeVb5BxU9urDa15Xuxz8RoPJKbOODdFgPA+mU0E+L0wKwmsCbYWZ7GgZM7P+jpoYYlHIeA1pM2HZRDVTxTXM8o0zVwhuiqpZM53BXQYzZ7MZHOLpSFHZiX0Qc+PQe2wl/BYzCEo7eJYQ0CgYAok5hEaNTJlwV0bgWwzQUtCxoHQaAvVPaWZ9A4AIdNZaf01k7TVVEjLCGjVEj7y45EzZnyxrFKsp7E9uOAF0mWxRZ1mM4wLI54J92eDS70vESgfhiE+SBbmDU0+JUz+cSM8AWTx82ZhTin7FRg6SCqb8UV8pAnSrH8Y6LDMYAF2wKBgHE9IGqssPWKJXK+wCWF/nKDDkCc72BKMfyUTHSOsruyu7jPIMl9U1VohJZZgbIJp2TmAd+1QvRxgbGybvKHAg3S5EwzPv/u7zp/gu9+AVuz9huXFqYzc3vYjea88RDSnCR5RSTzsqI28DWRufc39Luf8eelXiTusZsJRp5HVZKVAoGBANeXmOU0dJlxp5F9mmyr3kkAwv36xuw0wh9JUVyQZcjG72177l+gOmRxB+5Wkc78yVD7ZX8kHm5j3tmwsvgqWe6/hd98FsfqKWPUifKO2MvOg8h68JzcgXQZkaHhbJDKJ22EgW75zczG8pL95npfMNnDRhF7rg9h1BACPBZ3WkMt\n-----END RSA PRIVATE KEY-----";

        String host = "https://sandboxapi.psd.dnb.no/v1/consents";

        SSLContext sslContext = TrialCertificatesSslContextBuilder.createSslContext(host, publicKey, privateKey, null);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setProxy(HttpHost.create("cache.konts.lv:8080"))
                .setSSLContext(sslContext)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> exchange = restTemplate.exchange(URI.create(host), HttpMethod.POST, new HttpEntity<>("{\"access\":{\"accounts\":[],\"balances\":[],\"transactions\":[]},\"recurringIndicator\":true,\"validUntil\":\"2020-06-30T20:00:00.000\",\"frequencyPerDay\":4,\"combinedServiceIndicator\":false}", new HttpHeaders() {
            {
                set("Content-Type", MediaType.APPLICATION_JSON);
                set("Accept", MediaType.APPLICATION_JSON);
                set("accept-encoding", "gzip, deflate");
                set("X-Request-ID", UUID.randomUUID().toString());
                set("PSU-ID", "31125464346");                    // TODO: read more about this field format and value
                set("PSU-User-Agent", "Firefox");               // TODO: read more about this field format and value
                set("PSU-IP-Address", "1.1.1.1");                // TODO: read more about this field format and value
                set("TPP-Redirect-URI", "https://google.com");
                set("TPP-Nok-Redirect-URI", "https://google.com/error");
            }
        }), String.class);
        System.out.println(exchange.getBody());
        return exchange.getBody();
    }




}
