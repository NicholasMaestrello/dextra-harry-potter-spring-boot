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
public class PeopleSearchDTO implements Serializable {

    private String name;
    private String role;
    private String school;
    private String house;
    private String patronus;
}
