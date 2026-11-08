package in.achintya.gatekeeper.gateway.Controller;

import in.achintya.gatekeeper.gateway.Service.GatewayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class GatewayController {
    private final GatewayService gatewayService;
    public GatewayController(GatewayService gatewayService){
        this.gatewayService = gatewayService;
    }
    @RequestMapping("/**")
    public ResponseEntity<byte[]> proxy(HttpServletRequest request){
        return gatewayService.handle(request);
    }
}
