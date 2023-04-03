package com.example.theatreoffice.models;

import java.math.BigDecimal;
import java.time.LocalTime;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

@Entity
public class Performance {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false, name = "id")
	@NonNull
	private long id;
	
	@Column(nullable = false, name = "title")
	private String title;
	
	@Column(name = "duration")
	@DateTimeFormat(pattern = "hh-mm")
	private LocalTime duration;
	
	@Column(name = "genre")
	private String genre;
	
	@Column(name = "rating")
	private BigDecimal rating;

	@OneToOne(targetEntity = Participant.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "director")
	private Participant director;
	
	public Performance() {}
	
	public Performance(String title, LocalTime duration, String genre, BigDecimal rating, Participant director) {
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.rating = rating;
		this.director = director;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalTime getDuration() {
		return duration;
	}

	public void setDuration(LocalTime duration) {
		this.duration = duration;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public Participant getDirector() {
		return director;
	}

	public void setDirector(Participant director) {
		this.director = director;
	}
}
