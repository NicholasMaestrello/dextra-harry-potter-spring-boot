package com.example.dextra.potter.integration;

import com.example.dextra.potter.integration.dto.HouseIntegrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HarryPotterApiIntegration {

    @Autowired
    private HarryPotterApiClient harrayPotterApiClient;

    @Value("${hp.apikey:}")
    private String apikey;

    /**
     * Busca house com base na api do potterapi e a retorna caso existe ou retorna null.
     * @param houseId ID da house que se deseja buscar.
     * @return house que foi encontrada ou null.
     */
    public HouseIntegrationDTO findHouseByID(String houseId) {
        var houses = harrayPotterApiClient.getHouses(apikey);

        return houses.getHouses().stream()
                .filter(h -> h.getId().equals(houseId))
                .findFirst()
                .orElse(null);
    }
}
