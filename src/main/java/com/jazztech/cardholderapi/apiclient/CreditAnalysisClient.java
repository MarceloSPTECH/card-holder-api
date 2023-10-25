package com.jazztech.cardholderapi.apiclient;

import com.jazztech.cardholderapi.apiclient.dtos.CreditAnalysisDTO;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "creditAnalysisClient", url = "localhost:9001/credit/analysis")
public interface CreditAnalysisClient {
    @GetMapping("/{creditAnalysisId}")
    CreditAnalysisDTO getCreditAnalysisById(@PathVariable(value = "creditAnalysisId") UUID creditAnalysisId);
}
