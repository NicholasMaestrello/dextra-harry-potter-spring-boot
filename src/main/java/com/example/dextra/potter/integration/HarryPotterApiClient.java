package com.example.dextra.potter.integration;

import com.example.dextra.potter.integration.dto.HouseCollectionIntegrationDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Headers("Content-Type: application/json")
@FeignClient(name = "HarrayPotterApiClient", url = "${hp.baseurl:}")
public interface HarryPotterApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/houses")
    HouseCollectionIntegrationDTO getHouses(@RequestHeader("apikey") String apikey);

}
