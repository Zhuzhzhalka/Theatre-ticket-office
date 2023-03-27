package com.example.theatreoffice.models;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "theatre")
public class Theatre {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, name = "id")
	private long id;
	
	@Column(nullable = false, name = "name")
	@NonNull
	private String name;
	
	@OneToOne(targetEntity = Location.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Location location;
	
	@Column(name = "seats_ground_floor")
	private int seatsGroundFloor;
	
	@Column(name = "seats_balcony")
	private int seatsBalcony;
	
	@Column(name = "seats_mezzanine")
	private int seatsMezzanine;
	
	public Theatre() {}
	
	public Theatre(String name, Location location, int seatsGroundFloor, int seatsBalcony, int seatsMezzanine) {
		this.name = name;
		this.location = location;
		this.seatsGroundFloor = seatsGroundFloor;
		this.seatsBalcony = seatsBalcony;
		this.seatsMezzanine = seatsMezzanine;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public long getSeatsGroundFloor() {
		return seatsGroundFloor;
	}

	public void setSeatsGroundFloor(int seatsGroundFloor) {
		this.seatsGroundFloor = seatsGroundFloor;
	}

	public long getSeatsBalcony() {
		return seatsBalcony;
	}

	public void setSeatsBalcony(int seatsBalcony) {
		this.seatsBalcony = seatsBalcony;
	}

	public long getSeatsMezzanine() {
		return seatsMezzanine;
	}

	public void setSeatsMezzanine(int seatsMezzanine) {
		this.seatsMezzanine = seatsMezzanine;
	}

}
