package com.example.theatreoffice.repos;

import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Participance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanceRepo extends CrudRepository<Participance, Long> {
    List<Participance> findByPerformance(Performance performance);
    List<Participance> findByParticipant(Participant participant);
    List<Participance> findByRole(String role);
    Optional<Participance> findByPerformanceAndParticipant(Performance performance, Participant participant);
}
