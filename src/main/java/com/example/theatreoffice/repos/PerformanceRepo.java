package com.example.theatreoffice.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Performance;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepo extends CrudRepository<Performance, Long> {

}
