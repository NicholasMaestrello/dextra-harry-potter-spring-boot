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
public class HouseIntegrationDTO implements Serializable {

    private String name;
    private List<String> colors;
    private String headOfHouse;
    private String id;
    private String school;
    private String founder;
    private List<String> values;
    private String houseGhost;
    private String lion;
}
