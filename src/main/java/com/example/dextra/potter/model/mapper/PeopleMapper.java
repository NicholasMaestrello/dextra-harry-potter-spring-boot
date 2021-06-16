package com.example.dextra.potter.model.mapper;

import com.example.dextra.potter.model.document.PeopleDocument;
import com.example.dextra.potter.model.dto.PeopleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeopleMapper {

    PeopleDTO peopleDocumentToPeopleDto(PeopleDocument planeta);

    PeopleDocument peopleDtoToPeopleDocument(PeopleDTO planeta);
}
