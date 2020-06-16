package com.zennaki.toubibrdv.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.zennaki.toubibrdv.domain.Appointment} entity.
 */
public class AppointmentDTO implements Serializable {
    
    private Long id;

    private String motif;

    private LocalDate dateRdv;


    private Long personId;

    private Long docteurId;
    
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

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(LocalDate dateRdv) {
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
