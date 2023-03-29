package com.example.theatreoffice.daos.impl;

import com.example.theatreoffice.daos.ParticipantDAO;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.repos.ParticipantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParticipantImplDAO implements ParticipantDAO {
    @Autowired
    private ParticipantRepo participantRepo;

    @Override
    public Optional<Participant> getParticipantByFirstNameAndLastName(String firstName, String lastName) {
        return participantRepo.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Optional<Participant> getParticipantById(long id) {
        return participantRepo.findById(id);
    }
}
