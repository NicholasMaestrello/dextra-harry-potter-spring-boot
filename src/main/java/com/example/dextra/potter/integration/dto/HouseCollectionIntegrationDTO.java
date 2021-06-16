package com.example.dextra.potter.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseCollectionIntegrationDTO implements Serializable {

    private List<HouseIntegrationDTO> houses;
}
