package org.example.volodyanoy.RestApp.controllers;


import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.example.volodyanoy.RestApp.dto.PersonDTO;
import org.example.volodyanoy.RestApp.models.Person;
import org.example.volodyanoy.RestApp.services.PeopleService;
import org.example.volodyanoy.RestApp.util.PersonErrorResponse;
import org.example.volodyanoy.RestApp.util.PersonNotCreatedException;
import org.example.volodyanoy.RestApp.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleRESTController {
    private final ModelMapper modelMapper;
    private final PeopleService peopleService;

    @Autowired
    public PeopleRESTController(ModelMapper modelMapper, PeopleService peopleService) {
        this.modelMapper = modelMapper;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<PersonDTO> getPeople(){
        return peopleService.findAll().stream().map(this::convertToPersonDTO)
                .toList(); // Jackson конвертирует в JSON
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id){
        return convertToPersonDTO(peopleService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
           StringBuilder errorMsg = new StringBuilder();
           List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors){
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }

        peopleService.save(convertToPerson(personDTO));

        //http ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id was not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // not_found = 404
    }

    @ExceptionHandler(PersonNotCreatedException.class)
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e){
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // not_found = 404
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }



}
