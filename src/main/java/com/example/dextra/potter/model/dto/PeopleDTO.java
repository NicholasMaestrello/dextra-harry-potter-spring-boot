package com.example.dextra.potter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDTO implements Serializable {

    private String id;
    private String name;
    private String role;
    private String school;
    private String house;
    private String houseName;
    private String patronus;
}
