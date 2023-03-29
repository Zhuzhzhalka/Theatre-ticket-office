package com.example.theatreoffice.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Theatre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRepo extends CrudRepository<Theatre, Long> {
    List<Theatre> findByName(String name);
    List<Theatre> findByTown(String town);
    List<Theatre> findByCountry(String country);
    List<Theatre> findByTownAndCountry(String town, String country);
    Optional<Theatre> findByNameAndTownAndCountry(String name, String town, String country);
}
