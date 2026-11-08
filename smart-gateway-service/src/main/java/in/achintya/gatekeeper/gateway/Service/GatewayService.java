package in.achintya.gatekeeper.gateway.Service;

import in.achintya.gatekeeper.gateway.Model.CompiledRoute;
import in.achintya.gatekeeper.gateway.DTO.GatewayRoute;
import in.achintya.gatekeeper.gateway.DTO.ResponseDTO.GatewayErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class GatewayService {

    private final RouteRegistry routeRegistry;
    private final ObjectMapper objectMapper;
    public GatewayService(RouteRegistry routeRegistry, ObjectMapper objectMapper){
        this.routeRegistry = routeRegistry;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<byte[]> handle(HttpServletRequest request){
        long start = System.nanoTime();

        String coorelationId = getCorrelationId(request);

        Optional<CompiledRoute> matchesRoute = routeRegistry.find(request.getRequestURI());
        String outcome = "TARGET_ERROR";
        int statuscode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if(!matchesRoute.isPresent()){
            outcome = "ROUTE_NOT_FOUND";
            statuscode = HttpStatus.NOT_FOUND.value();
            return error(HttpStatus.NOT_FOUND,"No active route matches ",
                    coorelationId,null);
        }
        CompiledRoute compiledRoute = matchesRoute.get();

        GatewayRoute route = compiledRoute.getGatewayRoute();

        if(!route.getAllowedMethods().contains(request.getMethod())){
            outcome = "METHOD_NOT_ALLOWED";
            statuscode = HttpStatus.METHOD_NOT_ALLOWED.value();
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ALLOW,String.join(",",route.getAllowedMethods()));

            return error(HttpStatus.METHOD_NOT_ALLOWED,"This HTTP method is not allowed on route",
                    coorelationId,headers);
        }

        //checking rate limmiting


    }

    String getCorrelationId(HttpServletRequest request){
         String correlationId = request.getHeader("X-Correlation-ID");

         if(correlationId==null || correlationId.trim().isEmpty()){
             return UUID.randomUUID().toString();
         }
         return correlationId;
    }

    private ResponseEntity<byte[]> error(HttpStatus status,
                                         String message,
                                         String correlationId,
                                         HttpHeaders additionalHeaders){
        GatewayErrorResponse response = new GatewayErrorResponse(status.value(),message,correlationId, LocalDateTime.now());

        HttpHeaders headers = additionalHeaders == null ? new HttpHeaders() : additionalHeaders;

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Correlation-ID",correlationId);


        return ResponseEntity.status(status).headers(headers)
                .body(objectMapper.writeValueAsString(response).getBytes(StandardCharsets.UTF_8));

    }
}
