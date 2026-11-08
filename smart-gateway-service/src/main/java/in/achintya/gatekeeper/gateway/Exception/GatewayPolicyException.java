package in.achintya.gatekeeper.gateway.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GatewayPolicyException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String outcome;

    public GatewayPolicyException(HttpStatus httpStatus, String message, String outcome) {
        super(message);
        this.httpStatus = httpStatus;
        this.outcome = outcome;
    }
}
