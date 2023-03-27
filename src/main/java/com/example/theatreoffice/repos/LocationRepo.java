package com.example.theatreoffice.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Location;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepo extends CrudRepository<Location, Long> {

}
