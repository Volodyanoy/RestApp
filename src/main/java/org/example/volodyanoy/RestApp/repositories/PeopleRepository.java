package org.example.volodyanoy.RestApp.repositories;


import org.example.volodyanoy.RestApp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
