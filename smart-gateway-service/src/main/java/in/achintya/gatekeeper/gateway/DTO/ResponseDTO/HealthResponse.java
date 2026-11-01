package in.achintya.gatekeeper.gateway.DTO.ResponseDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthResponse {

    private String serviceName;
    private String serviceDescription;
    private String appStatus;

}
