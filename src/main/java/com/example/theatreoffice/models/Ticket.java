package com.example.theatreoffice.models;

import jakarta.persistence.*;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, name = "id")
	private long id;
	
	@Column(nullable = false, name = "person_firstname")
	private String personFirstName;
	
	@Column(nullable = false, name = "person_lastname")
	private String personLastName;
	
	@Column(nullable = false, name = "seat")
	private int seat;
	
	@Column(nullable = false, name = "price")
	private double price;
	
	@JoinColumn(name = "schedule")
	@ManyToOne(targetEntity = Schedule.class, fetch = FetchType.EAGER)
	private Schedule schedule;
	
	public Ticket() {}

	public Ticket(String personFirstName, String personLastName, int seat, double price, Schedule schedule) {
		this.personFirstName = personFirstName;
		this.personLastName = personLastName;
		this.seat = seat;
		this.price = price;
		this.schedule = schedule;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
}
