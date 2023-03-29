package com.example.theatreoffice.daos.impl;

import com.example.theatreoffice.daos.ParticipanceDAO;
import com.example.theatreoffice.models.Participance;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.repos.ParticipanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanceImplDAO implements ParticipanceDAO {
    @Autowired
    private ParticipanceRepo participanceRepo;

    @Override
    public List<Participance> getParticipancesByPerformance(Performance performance) {
        return participanceRepo.findByPerformance(performance);
    }

    @Override
    public List<Participance> getParticipancesByParticipant(Participant participant) {
        return participanceRepo.findByParticipant(participant);
    }

    @Override
    public Optional<String> participantRoleInPerformance(Participant participant, Performance performance) {
        Optional<Participance> participance = participanceRepo.findByPerformanceAndParticipant(performance, participant);
        return participance.map(Participance::getRole);
    }
}
