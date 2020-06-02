package testapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/proxy")
public class ProxyController {



    @Autowired
    private GatewayService gatewayService;

    @RequestMapping("/gateway")
    public ResponseEntity mirrorRest(@RequestBody(required = false) String body) {
        return gatewayService.doRequest(body);
    }

    @RequestMapping("/test")
    public ResponseEntity test()
    {
        String publicKey = "-----BEGIN CERTIFICATE-----\n" +
                "MIIDNzCCAh+gAwIBAgIGAXI6H3UKMA0GCSqGSIb3DQEBCwUAMBYxFDASBgNVBAMMC2RldnBvcnRhbENhMB4XDTIwMDUyMjAwMDAwMFoXDTIxMDUyMjAwMDAwMFowgaIxIzAhBgkqhkiG9w0BCQEWFHlhZmVrZXZvckAzZG1haWwudG9wMR0wGwYDVQQDDBR5YWZla2V2b3JAM2RtYWlsLnRvcDETMBEGA1UECxMKSW5ub3ZhdGlvbjESMBAGA1UEChMJU2VudGhhZGV2MRIwEAYDVQQHEwlUcm9uZGhlaW0xEjAQBgNVBAgTCVRyb25kaGVpbTELMAkGA1UEBhMCTk8wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCBsv//wAtaBf0RUSFeGVSMbz8NX9eN8dHjJ8OVYh1TO7mKVNYSL4dazXuMl7Erauy2sTqfKMaE200xwofd7MQmMmz8uVbxrtLLGj5EM3SKRd7EW4KbTTeQYZGvMVOm5aOT2557UNduDeNLnm+zUNLG4lDSTXnHJDaQ145XdZZcQSxIlVdYcIfcXsG8AKvGIMbK7ZaZoJ3arw7KbX1kKcA3UB45hXsLxBlKLqkAw3VhQetY69y0xXdVTh2Xkuf6hWS++9SC5f9ILTFqruvGm2Xc5Ilxs6j7PtbNeJKInuf0Ko+tY7ela4QHbgM2SOtFUC6matJbHzOZ0kNTub5DebOvAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAH5+E/FdoYcrsMoH9WYSZqlKxAGQd0tY1XdhJ0T0I/mRiTaRIxLaM/rdFK8DZdddJ9GGz78YkylL7NdABoColUj2OTVyjnRgW+Gz5XcoLW295ZiA+1ygGiyDfYJwRfkO4F+ZU4O5SzzC1HmrDWwtCvlm4nuExf2qIOV+hKA5FmpOa3h1kSc12m5iHZnhEENj3ov1oOxMQjEKQTZobqm4jiubzvk3GHtcmJaxB1GJANNANbBXrPujwnKjZwSQC8Q5GGf28IEGrfXH6+BoFnNaAQY0GeHIhwoxxISi/QWPIsihHosSSIKheDTBjvObKM1tA/X7yVfBdCypaYPm7Jn5fNs=\n" +
                "-----END CERTIFICATE-----";
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBsv//wAtaBf0RUSFeGVSMbz8NX9eN8dHjJ8OVYh1TO7mKVNYSL4dazXuMl7Erauy2sTqfKMaE200xwofd7MQmMmz8uVbxrtLLGj5EM3SKRd7EW4KbTTeQYZGvMVOm5aOT2557UNduDeNLnm+zUNLG4lDSTXnHJDaQ145XdZZcQSxIlVdYcIfcXsG8AKvGIMbK7ZaZoJ3arw7KbX1kKcA3UB45hXsLxBlKLqkAw3VhQetY69y0xXdVTh2Xkuf6hWS++9SC5f9ILTFqruvGm2Xc5Ilxs6j7PtbNeJKInuf0Ko+tY7ela4QHbgM2SOtFUC6matJbHzOZ0kNTub5DebOvAgMBAAECggEAFmaLDmnlzAOSxMXlZHIthx/DnRS7jvmADaKVhhxvAiwjjH/AhV2OHCG3P/4HDH+oKJmNjWWme9XAjGKTwsGc0xgMeM2t9YoDQC7lKDRW4XbV1cYUVaxNd3NSjRaQ4xdwXB4nThcVTh257xqLIkZIUS9PVDMrYDih7/w6ymI1uPMLBcgcRQNo4RNYmSEL0rfBxsAXP+kUYhxf47snts3gux/g3QzQERCQ3HvzC2ecgnyNwSaqLdZJvzF6gq59wwOA7LUGr2As4F6J062g/uqJ5V5cnztXFnmqTQJJKWj8OQIjq7hbp91Q7QIbrwKG8LQgC02JQqL2n2LNQwqYbSyMKQKBgQDz8/eCDuOXoJjj1qqVOpPrF9ecc/CT1wic1Q73opmmHU4n1hZLkusKTac/1g2DQZ3CXmqM2EDAfy1n9G+/e+nYgh6C01s7/kn2qGWapBnciFRAjqYH9JGNcFN8Q72Zfs4QRfcPb8w3E6kj7rLBidOO9hFCVE5QgRUWOgv/Nrs+PQKBgQCIGqWFj2YKNZIgeBUWnklV9GgCJ08BmLNb/sETnB5fVOkm7P9MG5WYHHfcb9Xnw0uWStpv/RN7KY5ioTzLr64eHJGclWZvO20TIRvGJFk3AdNMcmwXogmdE329uyPmFPXNRuaZO2muSarBt31r29mbePK2CqfgfOBxnDnv/3IkWwKBgQCAuh3CvUSPQCktIJtGv3BKP3yAjBVIjdF3oF2TgnN9Wj1he/Cr1Ey+VReJ8dOTjU5HwDaNN5eDfBhjoHJPIn3Ux2R+ODUORTKWO2nR3gHfgviiUCOAUydy8PC8w6oKkwT9RU4HEUE7Vt6LnWsqASZna6TT0VS52kNcRSkLdL/9KQKBgBs6+ZLXNdslDK9iVxXHZXTFCWok4zG2k2v8dDSwL4NzQZKAKDPZ8PwV9gUseud7s0vGfh2ZzKP/lHU3PSqjY3qyzI8/dNccVJ5XKW9jJlZTDytkWSwBR1sd4zJqlqwZ1XPUk55kvKgaJQfM5lMGEWuBr02spBnN4jnYv968hOGxAoGBAKnPxk8BCiryKSkG7VYdCGHRG7GEsSgJhfKoa8y7qLsgA876Ljj6qxP1CviFjbCIP0ztdpD/NNWyU8cLVyc+UImcaMJTCA2HcRSp72Jxm+ntxxLj9Rs5KGbazYNFnJQXY2Z1soILWEZAK7zZJUMN2BkKGtz/bEX/H6hiZxNO6RCi\n" +
                "-----END PRIVATE KEY-----";

        String host = "https://test.api.ob.baltics.sebgroup.com/v2/oauth/token";

        BankConfigBase bankConfigBase = new BankConfigBase();
        Client client = ProxyClientFactory.makeClient(bankConfigBase);
        Response response = client.target("https://test.api.ob.baltics.sebgroup.com/v2/oauth/token")
                .request((MediaType.APPLICATION_JSON))
                .header("Content-Type", "application/json")
                .header("x-request-id", UUID.randomUUID().toString())
                .post(Entity.json("{\"code\":\"fab954ec-8cba-450d-9b05-5670d6978fe7\",\"grant_type\":\"authorization_code\",\"redirect_uri\":\"https://dev.obdevportal.eu/callback-emulator\"}"));
        String responseBody = response.readEntity(String.class);
        return ResponseEntity.status(response.getStatus()).headers(new HttpHeaders(){{
            response.getHeaders().forEach((k,v)-> set(k, v.stream().map(Object::toString).collect(Collectors.joining(","))));
        }}).body(responseBody);
    }
}
