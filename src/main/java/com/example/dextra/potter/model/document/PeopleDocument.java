package com.example.dextra.potter.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "People")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDocument {

    @Id
    private String id;
    private String name;
    private String role;
    private String school;
    private String house;
    private String patronus;
}
