package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Participant;

import java.util.Optional;

public interface ParticipantDAO {
    Optional<Participant> getParticipantByFirstNameAndLastName(String firstName, String lastName);
    Optional<Participant> getParticipantById(long id);
    Participant save(Participant participant);
    void delete(Participant participant);
}
