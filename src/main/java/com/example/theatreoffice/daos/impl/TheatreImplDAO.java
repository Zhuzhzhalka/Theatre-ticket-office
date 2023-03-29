package com.example.theatreoffice.daos.impl;

import com.example.theatreoffice.daos.TheatreDAO;
import com.example.theatreoffice.models.Theatre;
import com.example.theatreoffice.repos.TheatreRepo;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheatreImplDAO implements TheatreDAO {
    @Autowired
    private TheatreRepo theatreRepo;

    @Override
    public List<Theatre> getTheatresByName(String name) {
        return theatreRepo.findByName(name);
    }

    @Override
    public List<Theatre> getTheatresByLocation(String town, String country) {
        if (!(town.isEmpty()) && !(country.isEmpty())) {
            return theatreRepo.findByTownAndCountry(town, country);
        } else if (!(town.isEmpty())) {
            return theatreRepo.findByTown(town);
        } else if (!(country.isEmpty())) {
            return theatreRepo.findByCountry(country);
        }

        return IterableUtils.toList(theatreRepo.findAll());
    }

    @Override
    public List<Theatre> getAllTheatres() {
        return IterableUtils.toList(theatreRepo.findAll());
    }

    @Override
    public Optional<Theatre> getTheatreById(long id) {
        return theatreRepo.findById(id);
    }

    @Override
    public Optional<Theatre> getTheatre(String name, String town, String country) {
        return theatreRepo.findByNameAndTownAndCountry(name, town, country);
    }

    @Override
    public Theatre save(Theatre theatre) {
        return theatreRepo.save(theatre);
    }

    @Override
    public void delete(Theatre theatre) {
        theatreRepo.delete(theatre);
    }
}
