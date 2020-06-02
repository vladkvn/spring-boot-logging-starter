package testapp;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class GatewayServiceSupportFeature implements Feature {
    private final GatewayServiceSupportFilter filter;


    public GatewayServiceSupportFeature(BankConfigBase bankConfigBase) {
        this.filter = new GatewayServiceSupportFilter(bankConfigBase);
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(filter);
        return true;
    }
}
