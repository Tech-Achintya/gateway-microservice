package in.achintya.gatekeeper.gateway.Service;

import in.achintya.gatekeeper.gateway.Model.CompiledRoute;
import in.achintya.gatekeeper.gateway.DTO.GatewayRoute;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class RouteRegistry {

    private final AtomicReference<List<CompiledRoute>> routes =
            new AtomicReference<>(Collections.emptyList());
    private PathPatternParser parser;

    public void replaceAll(List<GatewayRoute> gatewayRoutes){
        List<CompiledRoute> compiledRoutes = new ArrayList<>();
        if(gatewayRoutes!=null){
            for(GatewayRoute route : gatewayRoutes){
                PathPattern pattern = parser.parse(route.getPathPattern());
                compiledRoutes.add(new CompiledRoute(route,pattern,
                        sourcePrefix(route.getPathPattern())));
            }
        }

        Collections.sort(compiledRoutes, new Comparator<CompiledRoute>() {
            @Override
            public int compare(CompiledRoute o1, CompiledRoute o2) {
                return PathPattern.SPECIFICITY_COMPARATOR.compare
                        (o1.getPathPattern(), o2.getPathPattern());
            }
        });
        routes.set(Collections.unmodifiableList(compiledRoutes));
    }
    private String sourcePrefix(String pathPattern){
        if(pathPattern.endsWith("/**")){
            return pathPattern.substring(0,pathPattern.length()-3);
        }
        return pathPattern;
    }
    public Optional<CompiledRoute> find(String requestPath){
        PathContainer path = PathContainer.parsePath(requestPath);

        for(CompiledRoute route : routes.get()){
            if(route.getPathPattern().matches(path)){
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

}
