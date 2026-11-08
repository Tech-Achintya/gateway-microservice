package in.achintya.gatekeeper.gateway.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitResult {

    private boolean allowed;
    private Long limit;
    private Long remaining;
    private Long retryAfterSeconds;

    public static RateLimitResult skipped(){
        return new RateLimitResult(true,0L,0L,0L);
    }
}
