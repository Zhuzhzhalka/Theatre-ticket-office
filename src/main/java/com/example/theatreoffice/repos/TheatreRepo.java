package com.example.theatreoffice.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Theatre;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepo extends CrudRepository<Theatre, Long> {

}
