package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Theatre;

import java.util.List;
import java.util.Optional;

public interface TheatreDAO {
    List<Theatre> getTheatresByName(String name);
    List<Theatre> getTheatresByLocation(String town, String country);
    Optional<Theatre> getTheatre(String name, String town, String country);
    List<Theatre> getAllTheatres();
    Optional<Theatre> getTheatreById(long id);
    Theatre save(Theatre theatre);
    void delete(Theatre theatre);
}
