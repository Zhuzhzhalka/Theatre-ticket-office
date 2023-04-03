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
	
	@Column(name = "seats_ground_floor")
	private int seatsGroundFloor;
	
	@Column(name = "seats_balcony")
	private int seatsBalcony;
	
	@Column(name = "seats_mezzanine")
	private int seatsMezzanine;

	@Column(name ="building")
	private int building;

	@Column(name ="street")
	private String street;

	@Column(name ="town")
	private String town;

	@Column(name ="country")
	private String country;
	
	public Theatre() {}
	
	public Theatre(String name, int seatsGroundFloor, int seatsBalcony, int seatsMezzanine,
				   int building, String street, String town, String country) {
		this.name = name;
		this.seatsGroundFloor = seatsGroundFloor;
		this.seatsBalcony = seatsBalcony;
		this.seatsMezzanine = seatsMezzanine;
		this.building = building;
		this.street = street;
		this.town = town;
		this.country = country;
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

	public int getSeatsGroundFloor() {
		return seatsGroundFloor;
	}

	public void setSeatsGroundFloor(int seatsGroundFloor) {
		this.seatsGroundFloor = seatsGroundFloor;
	}

	public int getSeatsBalcony() {
		return seatsBalcony;
	}

	public void setSeatsBalcony(int seatsBalcony) {
		this.seatsBalcony = seatsBalcony;
	}

	public int getSeatsMezzanine() {
		return seatsMezzanine;
	}

	public void setSeatsMezzanine(int seatsMezzanine) {
		this.seatsMezzanine = seatsMezzanine;
	}

	public int getBuilding() {
		return building;
	}

	public void setBuilding(int building) {
		this.building = building;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
