package com.example.theatreoffice.models;

import java.util.Date;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, name = "id")
	private long id;
	
	@JoinColumn(name = "performance_id")
	@ManyToOne(targetEntity = Performance.class, fetch = FetchType.EAGER)
	private Performance performance;
	
	@JoinColumn(name = "theatre_id")
	@ManyToOne(targetEntity = Theatre.class, fetch = FetchType.EAGER)
	private Theatre theatre;
	
	@Column(nullable = false, name = "date_time")
	@DateTimeFormat(pattern = "yyyy-mm-dd hh-mm")
	private Date dateTime;
	
	@Column(name = "free_seats_ground_floor")
	private int freeSeatsGroundFloor;
	
	@Column(name = "free_seats_balcony")
	private int freeSeatsBalcony;
	
	@Column(name = "free_seats_mezzanine")
	private int freeSeatsMezzanine;
	
	@Column(name = "price_ground_floor")
	private long priceGroundFloor;
	
	@Column(name = "price_balcony")
	private long priceBalcony;
	
	@Column(name = "price_mezzanine")
	private long priceMezzanine;
	
	public Schedule() {}
	
	public Schedule(Performance performance, Theatre theatre, Date dateTime, int freeSeatsGroundFloor,
					int freeSeatsBalcony, int freeSeatsMezzanine, long priceGroundFloor, long priceBalcony,
					long priceMezzanine) {
		this.performance = performance;
		this.theatre = theatre;
		this.dateTime = dateTime;
		this.freeSeatsGroundFloor = freeSeatsGroundFloor;
		this.freeSeatsBalcony = freeSeatsBalcony;
		this.freeSeatsMezzanine = freeSeatsMezzanine;
		this.priceGroundFloor = priceGroundFloor;
		this.priceBalcony = priceBalcony;
		this.priceMezzanine = priceMezzanine;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public Theatre getTheatre() {
		return theatre;
	}

	public void setTheatre(Theatre theatre) {
		this.theatre = theatre;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public int getFreeSeatsGroundFloor() {
		return freeSeatsGroundFloor;
	}

	public void setFreeSeatsGroundFloor(int freeSeatsGroundFloor) {
		this.freeSeatsGroundFloor = freeSeatsGroundFloor;
	}

	public int getFreeSeatsBalcony() {
		return freeSeatsBalcony;
	}

	public void setFreeSeatsBalcony(int freeSeatsBalcony) {
		this.freeSeatsBalcony = freeSeatsBalcony;
	}

	public int getFreeSeatsMezzanine() {
		return freeSeatsMezzanine;
	}

	public void setFreeSeatsMezzanine(int freeSeatsMezzanine) {
		this.freeSeatsMezzanine = freeSeatsMezzanine;
	}

	public long getPriceGroundFloor() {
		return priceGroundFloor;
	}

	public void setPriceGroundFloor(long priceGroundFloor) {
		this.priceGroundFloor = priceGroundFloor;
	}

	public long getPriceBalcony() {
		return priceBalcony;
	}

	public void setPriceBalcony(long priceBalcony) {
		this.priceBalcony = priceBalcony;
	}

	public long getPriceMezzanine() {
		return priceMezzanine;
	}

	public void setPriceMezzanine(long priceMezzanine) {
		this.priceMezzanine = priceMezzanine;
	}
	
}
