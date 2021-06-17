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

    /**
     * Retorna todos as pessoas cadastradas na base de dados, com base no filtro informado.
     * @param searchDTO objeto de filtro que contem as propriedades que podem ser informadas para servirem de filtro.
     * @return lista de pessoas cadastradas.
     */
    public List<PeopleDTO> listPeople(PeopleSearchDTO searchDTO) {
        List<PeopleDocument> all = peopleRepository.findAll(createSearchExample(searchDTO));
        return all.stream()
                .map(p -> peopleMapper.peopleDocumentToPeopleDto(p))
                .collect(Collectors.toList());
    }

    /**
     * Faz a busca de uma unica pessoa com base em seu ID junto com o valor da casa a a qual ela pertence. Lança Exception caso não encontre nenhum registro para o ID.
     * @param id ID da pessoa que se deseja procurar.
     * @return Pessoa cadastrada na base de dados.
     */
    public PeopleDTO findPeopleById(String id) {
        var peopleDocument = peopleRepository.findById(id).orElseThrow(() -> getNotFoundExceptionById("Pessoas", id));

        var houseByID = getHouseIntegrationDTO(peopleDocument.getHouse());
        var peopleDTO = peopleMapper.peopleDocumentToPeopleDto(peopleDocument);
        peopleDTO.setHouseName(houseByID.getName());
        return peopleDTO;
    }

    /**
     * Cria uma pessoa na base de dados caso seus dados estejam corretos.
     * @param peopleDTO Objeto com dados da pessoa que se deseja cadastrar.
     * @return Pessoa cadastrada na base de dados com seu respectivo ID.
     */
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

    /**
     * Atualiza uma pessoa na base de dados caso ela exista e seus dados estejam corretos.
     * @param id ID da pessoa que se deseja alterar.
     * @param peopleDTO Objeto com dados da pessoa que se deseja alterar.
     * @return Pessoa cadastrada na base de dados com seu respectivo ID.
     */
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

    /**
     * Exclui pessoa da base de dados caso exista.
     * @param id ID da pessoa que se deseja alterar.
     * @return void.
     */
    public void deletePeople(String id) {
        var peopleDocument = peopleRepository.findById(id).orElseThrow(() -> getNotFoundExceptionById("Pessoas", id));
        peopleRepository.delete(peopleDocument);
    }

    /**
     * Cria uma nova NotFoundException formatada com campo e ID.
     * @param entity nome do campo ou propriedade que se deseja exibir.
     * @param id ID que se deseja exibir.
     * @return NotFoundException com mensagem formatada.
     */
    private NotFoundException getNotFoundExceptionById(String entity, String id) {
        return new NotFoundException(String.format("Não foram encontrados %s com o ID = %s", entity, id));
    }

    /**
     * Obtem house com base em ID e lança NotFoundException caso não encontre nenhuma.
     * @param houseId ID da house que se deseja buscar.
     * @return house que foi encontrada.
     */
    private HouseIntegrationDTO getHouseIntegrationDTO(String houseId) {
        var houseByID = harryPotterApiIntegration.findHouseByID(houseId);
        if (houseByID == null) {
            throw getNotFoundExceptionById("Casas", houseId);
        }
        return houseByID;
    }

    /**
     * Cria instancia de example faz o set de valor de forma dinamica com base no que foi passado no search.
     * @param searchDTO objeto com propriedade que se deseja fazer filtro.
     * @return example com ou sem as propriedades.
     */
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

    /**
     * Faz o set de propriedades que podem ser atualizadas no document.
     * @param peopleDocument base que vai servir para ser atualizado.
     * @param peopleDTO dto com as propriedades que vao sobreescrever o document.
     * @return document com propriedades atualizadas.
     */
    private PeopleDocument setUpdateFields(PeopleDocument peopleDocument, PeopleDTO peopleDTO) {
        peopleDocument.setName(peopleDTO.getName());
        peopleDocument.setRole(peopleDTO.getRole());
        peopleDocument.setSchool(peopleDTO.getSchool());
        peopleDocument.setHouse(peopleDTO.getHouse());
        peopleDocument.setPatronus(peopleDTO.getPatronus());
        return peopleDocument;
    }

    /**
     * Valida se os campos obrigatorios da pessoa foram informados. Caso algum não tenha sido informado lança InvalidOperationException.
     * @param peopleDTO dto com as propriedades que vão ser validadas.
     * @return void.
     */
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
