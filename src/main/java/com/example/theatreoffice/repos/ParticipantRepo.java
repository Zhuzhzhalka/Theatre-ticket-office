package com.example.theatreoffice.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Participant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepo extends CrudRepository<Participant, Long> {
    Optional<Participant> findByFirstNameAndLastName(String firstName, String lastName);
}
