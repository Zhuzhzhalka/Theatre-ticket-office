package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Participance;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;

import java.util.List;
import java.util.Optional;

public interface ParticipanceDAO {
    List<Participance> getParticipancesByPerformance(Performance performance);
    List<Participance> getParticipancesByParticipant(Participant participant);
    Optional<String> participantRoleInPerformance(Participant participant, Performance performance);
    Participance save(Participance participance);
    void delete(Participance participance);
}
