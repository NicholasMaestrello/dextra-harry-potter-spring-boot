package com.example.dextra.potter.controller;

import com.example.dextra.potter.model.dto.PeopleDTO;
import com.example.dextra.potter.model.dto.PeopleSearchDTO;
import com.example.dextra.potter.service.PeopleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/people")
@ApiOperation(value = "Api para cadastro de pessoas")
public class PeopleController {

    @Autowired
    private PeopleService peopleService;

    @GetMapping
    @ApiOperation(value = "Lista todas as pessoas com possibilidade de filtro")
    private List<PeopleDTO> listPeople(PeopleSearchDTO searchDTO) {
        return peopleService.listPeople(searchDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca uma unica pessoa por ID")
    private PeopleDTO findPeopleById(String id) {
        return peopleService.findPeopleById(id);
    }

    @PostMapping()
    @ApiOperation(value = "Salva uma nova pessoa")
    private PeopleDTO createPeople(@RequestBody PeopleDTO people) {
        return peopleService.createPeople(people);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Atualiza dados de uma pessoa")
    private PeopleDTO updatePeople(String id, @RequestBody PeopleDTO people) {
        return peopleService.updatePeople(id, people);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclui uma pessoa")
    private void createPlaneta(@PathVariable String id) {
        peopleService.deletePeople(id);
    }
}
