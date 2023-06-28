package tech.jazz.apicardholder.applicationservice.creditapi;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CreditApi", url = "${credit-api-host}")
public interface CreditApi {
    @GetMapping("analysis/{id}")
    CreditAnalysisResponse getAnalysis(@PathVariable UUID id);

}
