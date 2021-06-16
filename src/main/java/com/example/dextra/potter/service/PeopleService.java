package com.example.dextra.potter.service;

import com.example.dextra.potter.exception.InvalidOperationException;
import com.example.dextra.potter.exception.NotFoundException;
import com.example.dextra.potter.integration.HarryPotterApiIntegration;
import com.example.dextra.potter.integration.dto.HouseIntegrationDTO;
import com.example.dextra.potter.model.document.PeopleDocument;
import com.example.dextra.potter.model.dto.PeopleDTO;
import com.example.dextra.potter.model.dto.PeopleSearchDTO;
import com.example.dextra.potter.model.mapper.PeopleMapper;
import com.example.dextra.potter.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeopleService {

    @Autowired
    private HarryPotterApiIntegration harryPotterApiIntegration;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private PeopleMapper peopleMapper;

    public List<PeopleDTO> listPeople(PeopleSearchDTO searchDTO) {
        List<PeopleDocument> all = peopleRepository.findAll(createSearchExample(searchDTO));
        return all.stream()
                .map(p -> peopleMapper.peopleDocumentToPeopleDto(p))
                .collect(Collectors.toList());
    }

    public PeopleDTO findPeopleById(String id) {
        var people = peopleRepository.findById(id).orElseThrow(() -> getNotFoundExceptionById("Pessoas", id));
        return peopleMapper.peopleDocumentToPeopleDto(people);
    }

    public PeopleDTO createPeople(PeopleDTO peopleDTO) {
        validateDto(peopleDTO);
        var houseId = peopleDTO.getHouse();
        var houseByID = getHouseIntegrationDTO(houseId);
        var peopleDocument = peopleMapper.peopleDtoToPeopleDocument(peopleDTO);
        peopleDocument.setId(null);

        peopleDocument = peopleRepository.save(peopleDocument);
        peopleDTO = peopleMapper.peopleDocumentToPeopleDto(peopleDocument);
        peopleDTO.setHouseName(houseByID.getName());
        return peopleDTO;
    }

    public PeopleDTO updatePeople(String id, PeopleDTO peopleDTO) {
        var peopleDocument = peopleRepository.findById(id).orElseThrow(() -> getNotFoundExceptionById("Pessoas", id));
        validateDto(peopleDTO);
        var houseId = peopleDTO.getHouse();
        var houseByID = getHouseIntegrationDTO(houseId);

        peopleDocument = setUpdateFields(peopleDocument, peopleDTO);
        peopleDocument = peopleRepository.save(peopleDocument);
        peopleDTO = peopleMapper.peopleDocumentToPeopleDto(peopleDocument);
        peopleDTO.setHouseName(houseByID.getName());
        return peopleDTO;
    }

    public void deletePeople(String id) {
        var peopleDocument = peopleRepository.findById(id).orElseThrow(() -> getNotFoundExceptionById("Pessoas", id));
        peopleRepository.delete(peopleDocument);
    }

    private NotFoundException getNotFoundExceptionById(String entity, String id) {
        return new NotFoundException(String.format("Não foram encontrados %s com o ID = %s", entity, id));
    }

    private HouseIntegrationDTO getHouseIntegrationDTO(String houseId) {
        var houseByID = harryPotterApiIntegration.findHouseByID(houseId);
        if (houseByID == null) {
            throw getNotFoundExceptionById("Casas", houseId);
        }
        return houseByID;
    }

    private Example<PeopleDocument> createSearchExample(PeopleSearchDTO searchDTO) {
        var peopleDocument = new PeopleDocument();
        if (searchDTO.getName() != null) {
            peopleDocument.setName(searchDTO.getName());
        }
        if (searchDTO.getRole() != null) {
            peopleDocument.setRole(searchDTO.getRole());
        }
        if (searchDTO.getSchool() != null) {
            peopleDocument.setSchool(searchDTO.getSchool());
        }
        if (searchDTO.getHouse() != null) {
            peopleDocument.setHouse(searchDTO.getHouse());
        }
        if (searchDTO.getPatronus() != null) {
            peopleDocument.setPatronus(searchDTO.getPatronus());
        }
        return Example.of(peopleDocument);
    }

    private PeopleDocument setUpdateFields(PeopleDocument peopleDocument, PeopleDTO peopleDTO) {
        peopleDocument.setName(peopleDTO.getName());
        peopleDocument.setRole(peopleDTO.getRole());
        peopleDocument.setSchool(peopleDTO.getSchool());
        peopleDocument.setHouse(peopleDTO.getHouse());
        peopleDocument.setPatronus(peopleDTO.getPatronus());
        return peopleDocument;
    }

    private void validateDto(PeopleDTO peopleDTO) {
        var error = new StringBuilder();
        ;
        if (peopleDTO.getName() == null) {
            error.append("Campo 'name' não pode ser null;\n");
        }
        if (peopleDTO.getRole() == null) {
            error.append("Campo 'role' não pode ser null;\n");
        }
        if (peopleDTO.getSchool() == null) {
            error.append("Campo 'school' não pode ser null;\n");
        }
        if (peopleDTO.getHouse() == null) {
            error.append("Campo 'house' não pode ser null;\n");
        }
        if (peopleDTO.getPatronus() == null) {
            error.append("Campo 'patronus' não pode ser null;\n");
        }
        var errorMessage = error.toString();
        if (errorMessage.length() > 0) {
            throw new InvalidOperationException(errorMessage);
        }
    }
}
