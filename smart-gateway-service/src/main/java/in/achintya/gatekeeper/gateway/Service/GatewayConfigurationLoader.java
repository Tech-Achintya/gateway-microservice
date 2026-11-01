package in.achintya.gatekeeper.gateway.Service;

import in.achintya.gatekeeper.gateway.Client.ManagementApiClient;
import in.achintya.gatekeeper.gateway.DTO.GatewayRoute;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatewayConfigurationLoader implements ApplicationRunner {

    private final ManagementApiClient managementApiClient;

    public GatewayConfigurationLoader(ManagementApiClient managementApiClient) {
        this.managementApiClient = managementApiClient;
    }

    @Override
    public void run(ApplicationArguments arguments) {
        refresh();
    }

    public void refresh() {
        List<GatewayRoute> routes = managementApiClient.fetchActiveRoutes();

        //demo
        for(GatewayRoute route : routes) {
            System.out.println("Route : " + route.getTargetPathPrefix());
        }
    }
}