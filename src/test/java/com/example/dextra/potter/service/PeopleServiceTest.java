package com.example.dextra.potter.service;


import com.example.dextra.potter.exception.InvalidOperationException;
import com.example.dextra.potter.exception.NotFoundException;
import com.example.dextra.potter.integration.HarryPotterApiIntegration;
import com.example.dextra.potter.integration.dto.HouseIntegrationDTO;
import com.example.dextra.potter.model.document.PeopleDocument;
import com.example.dextra.potter.model.dto.PeopleDTO;
import com.example.dextra.potter.model.mapper.PeopleMapper;
import com.example.dextra.potter.repository.PeopleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class PeopleServiceTest {

    @InjectMocks
    private PeopleService peopleService;

    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private PeopleMapper peopleMapper;

    @Mock
    private HarryPotterApiIntegration harryPotterApiIntegration;

    @DisplayName("Lança Exception caso tente salvar uma pessoa sem fornecer seus dados")
    @Test
    public void test1() {
        Exception exception = Assertions.assertThrows(InvalidOperationException.class, () -> {
            peopleService.createPeople(new PeopleDTO());
        });

        var expectedMessage = "Campo 'name' não pode ser null;\nCampo 'role' não pode ser null;\nCampo 'school' não pode ser null;\nCampo 'house' não pode ser null;\nCampo 'patronus' não pode ser null;\n";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Lança Exception caso tente salvar uma pessoa com ID de house não existente")
    @Test
    public void test2() {
        var peopleDtoFake = getPeopleDtoFake();
        Mockito.when(harryPotterApiIntegration.findHouseByID(Mockito.any()))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            peopleService.createPeople(peopleDtoFake);
        });

        var expectedMessage = "Não foram encontrados Casas com o ID = 1";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Retorna corretamente após salvar uma pessoa")
    @Test
    public void test3() {
        var peopleDtoFake = getPeopleDtoFake();
        var peopleDocumentFake = getPeopleDocumentFake();
        var peopleDocumentMapperFake = getPeopleDocumentMapperFake();
        var peopleDtoMapperFake = getPeopleDtoMapperFake();

        Mockito.when(harryPotterApiIntegration.findHouseByID(Mockito.any()))
                .thenReturn(getHouseIntegrationDto());
        Mockito.when(peopleRepository.save(Mockito.any()))
                .thenReturn(peopleDocumentFake);
        Mockito.when(peopleMapper.peopleDtoToPeopleDocument(Mockito.any()))
                .thenReturn(peopleDocumentMapperFake);
        Mockito.when(peopleMapper.peopleDocumentToPeopleDto(Mockito.any()))
                .thenReturn(peopleDtoMapperFake);

        var peopleReturn = peopleService.createPeople(peopleDtoFake);

        Assertions.assertEquals(peopleReturn.getId(), "123");
        Assertions.assertEquals(peopleReturn.getName(), "nome pessoa");
        Assertions.assertEquals(peopleReturn.getRole(), "role pessoa");
        Assertions.assertEquals(peopleReturn.getSchool(), "school pessoa");
        Assertions.assertEquals(peopleReturn.getHouse(), "1");
        Assertions.assertEquals(peopleReturn.getHouseName(), "house da api");
        Assertions.assertEquals(peopleReturn.getPatronus(), "patronus pessoa");
    }

    @DisplayName("Lança Exception buscar por ID uma pessoa que não exite")
    @Test
    public void test4() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            peopleService.findPeopleById("1");
        });

        var expectedMessage = "Não foram encontrados Pessoas com o ID = 1";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Lança Exception buscar por ID uma pessoa com ID de house que não existe")
    @Test
    public void test10() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(getPeopleDocumentFake()));
        Mockito.when(harryPotterApiIntegration.findHouseByID(Mockito.any()))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            peopleService.findPeopleById("1");
        });

        var expectedMessage = "Não foram encontrados Casas com o ID = 1";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Retorna corretamente após encontrar a pessoa")
    @Test
    public void test11() {
        Mockito.when(harryPotterApiIntegration.findHouseByID(Mockito.any()))
                .thenReturn(getHouseIntegrationDto());
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(getPeopleDocumentFake()));
        Mockito.when(peopleMapper.peopleDocumentToPeopleDto(Mockito.any()))
                .thenReturn(getPeopleDtoMapperFake());

        var peopleReturn = peopleService.findPeopleById("1");

        Assertions.assertEquals(peopleReturn.getId(), "123");
        Assertions.assertEquals(peopleReturn.getName(), "nome pessoa");
        Assertions.assertEquals(peopleReturn.getRole(), "role pessoa");
        Assertions.assertEquals(peopleReturn.getSchool(), "school pessoa");
        Assertions.assertEquals(peopleReturn.getHouse(), "1");
        Assertions.assertEquals(peopleReturn.getHouseName(), "house da api");
        Assertions.assertEquals(peopleReturn.getPatronus(), "patronus pessoa");
    }

    @DisplayName("Lança Exception quando tentar atualizar uma pessoa que não exite")
    @Test
    public void test5() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            peopleService.updatePeople("1", new PeopleDTO());
        });

        var expectedMessage = "Não foram encontrados Pessoas com o ID = 1";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Lança Exception caso tente atualizar uma pessoa sem fornecer seus dados")
    @Test
    public void test6() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new PeopleDocument()));

        Exception exception = Assertions.assertThrows(InvalidOperationException.class, () -> {
            peopleService.updatePeople("1", new PeopleDTO());
        });

        var expectedMessage = "Campo 'name' não pode ser null;\nCampo 'role' não pode ser null;\nCampo 'school' não pode ser null;\nCampo 'house' não pode ser null;\nCampo 'patronus' não pode ser null;\n";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Lança Exception caso tente atualizar uma pessoa com ID de house não existente")
    @Test
    public void test7() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new PeopleDocument()));
        Mockito.when(harryPotterApiIntegration.findHouseByID(Mockito.any()))
                .thenReturn(null);

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            peopleService.updatePeople("1", getPeopleDtoFake());
        });

        var expectedMessage = "Não foram encontrados Casas com o ID = 1";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Retorna Corretamente após atualizar a pessoa")
    @Test
    public void test8() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(getPeopleDocumentFake()));
        Mockito.when(peopleRepository.save(Mockito.any()))
                .thenReturn(getPeopleDocumentFake());
        Mockito.when(harryPotterApiIntegration.findHouseByID(Mockito.any()))
                .thenReturn(getHouseIntegrationDto());
        Mockito.when(peopleMapper.peopleDtoToPeopleDocument(Mockito.any()))
                .thenReturn(getPeopleDocumentMapperFake());
        Mockito.when(peopleMapper.peopleDocumentToPeopleDto(Mockito.any()))
                .thenReturn(getPeopleDtoMapperFake());

        var peopleReturn = peopleService.updatePeople("1", getPeopleDtoFake());

        Assertions.assertEquals(peopleReturn.getId(), "123");
        Assertions.assertEquals(peopleReturn.getName(), "nome pessoa");
        Assertions.assertEquals(peopleReturn.getRole(), "role pessoa");
        Assertions.assertEquals(peopleReturn.getSchool(), "school pessoa");
        Assertions.assertEquals(peopleReturn.getHouse(), "1");
        Assertions.assertEquals(peopleReturn.getHouseName(), "house da api");
        Assertions.assertEquals(peopleReturn.getPatronus(), "patronus pessoa");
    }

    @DisplayName("Lança Exception quando tentar excluir uma pessoa que não exite")
    @Test
    public void test9() {
        Mockito.when(peopleRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> {
            peopleService.deletePeople("1");
        });

        var expectedMessage = "Não foram encontrados Pessoas com o ID = 1";
        var actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    private HouseIntegrationDTO getHouseIntegrationDto() {
        return HouseIntegrationDTO.builder().name("house da api").build();
    }

    private PeopleDTO getPeopleDtoFake() {
        return PeopleDTO.builder()
                .name("nome pessoa")
                .role("role pessoa")
                .school("school pessoa")
                .house("1")
                .patronus("patronus pessoa")
                .build();
    }

    private PeopleDTO getPeopleDtoMapperFake() {
        return PeopleDTO.builder()
                .id("123")
                .name("nome pessoa")
                .role("role pessoa")
                .school("school pessoa")
                .house("1")
                .patronus("patronus pessoa")
                .build();
    }

    private PeopleDocument getPeopleDocumentFake() {
        return PeopleDocument.builder()
                .name("123")
                .name("nome pessoa")
                .role("role pessoa")
                .school("school pessoa")
                .house("1")
                .patronus("patronus pessoa")
                .build();
    }

    private PeopleDocument getPeopleDocumentMapperFake() {
        return PeopleDocument.builder()
                .name("nome pessoa")
                .role("role pessoa")
                .school("school pessoa")
                .house("1")
                .patronus("patronus pessoa")
                .build();
    }
}
