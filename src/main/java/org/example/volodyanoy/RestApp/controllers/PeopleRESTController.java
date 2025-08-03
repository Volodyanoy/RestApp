package org.example.volodyanoy.RestApp.controllers;

import org.apache.coyote.Response;
import org.example.volodyanoy.RestApp.models.Person;
import org.example.volodyanoy.RestApp.services.PeopleService;
import org.example.volodyanoy.RestApp.util.PersonErrorResponse;
import org.example.volodyanoy.RestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleRESTController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleRESTController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<Person> getPeople(){
        return peopleService.findAll(); // Jackson конвертирует в JSON
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id){
        return peopleService.findOne(id);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id was not found",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // not_found = 404
    }
}
