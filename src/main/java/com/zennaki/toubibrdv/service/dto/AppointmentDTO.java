package com.zennaki.toubibrdv.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.zennaki.toubibrdv.domain.Person;

/**
 * A DTO for the {@link com.zennaki.toubibrdv.domain.Appointment} entity.
 */
public class AppointmentDTO implements Serializable {
    
    private Long id;

    private String motif;

    private Date dateRdv;


    private Long personId;

    private Long docteurId;
    
    private Person docteur;
    
    private Person patient;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(Date dateRdv) {
        this.dateRdv = dateRdv;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getDocteurId() {
        return docteurId;
    }

    public void setDocteurId(Long personId) {
        this.docteurId = personId;
    }

    public Person getDocteur() {
		return docteur;
	}

	public void setDocteur(Person docteur) {
		this.docteur = docteur;
	}

	public Person getPatient() {
		return patient;
	}

	public void setPatient(Person patient) {
		this.patient = patient;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (appointmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", motif='" + getMotif() + "'" +
            ", dateRdv='" + getDateRdv() + "'" +
            ", personId=" + getPersonId() +
            ", docteurId=" + getDocteurId() +
            "}";
    }
}
