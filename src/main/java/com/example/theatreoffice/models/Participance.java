package com.example.theatreoffice.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Participance {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, name = "id")
	private long id;

	@ManyToOne(targetEntity = Performance.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "perf")
	private Performance performance;

	@ManyToOne(targetEntity = Participant.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "part")
	private Participant participant;
	
	@Column(name = "role")
	private String role;
	
	public Participance() {}

	public Participance(Performance performance, Participant participant, String role) {
		this.performance = performance;
		this.participant = participant;
		this.role = role;
	}

	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
