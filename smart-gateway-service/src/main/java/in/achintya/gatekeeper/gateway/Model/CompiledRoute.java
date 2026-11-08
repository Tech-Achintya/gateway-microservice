package in.achintya.gatekeeper.gateway.Model;

import in.achintya.gatekeeper.gateway.DTO.GatewayRoute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.util.pattern.PathPattern;

@Getter
@AllArgsConstructor
public class CompiledRoute {
    private GatewayRoute gatewayRoute;
    private PathPattern pathPattern;
    private String sourcePathPrefix;

}
