package testapp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ProxyClientFactory {

    public static Client makeClient(BankConfigBase bankConfigBase) {
        Client baseClient = ClientBuilder.newBuilder()
                .register(new GatewayServiceSupportFeature(bankConfigBase))
                .build();

        return baseClient;
    }
}
