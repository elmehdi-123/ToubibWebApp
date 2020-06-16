package com.zennaki.toubibrdv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "motif")
    private String motif;

    @Column(name = "date_rdv")
    private LocalDate dateRdv;

    @ManyToOne
    @JsonIgnoreProperties("appointments")
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties("disponibilties")
    private Person docteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public Appointment motif(String motif) {
        this.motif = motif;
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public Appointment dateRdv(LocalDate dateRdv) {
        this.dateRdv = dateRdv;
        return this;
    }

    public void setDateRdv(LocalDate dateRdv) {
        this.dateRdv = dateRdv;
    }

    public Person getPerson() {
        return person;
    }

    public Appointment person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getDocteur() {
        return docteur;
    }

    public Appointment docteur(Person person) {
        this.docteur = person;
        return this;
    }

    public void setDocteur(Person person) {
        this.docteur = person;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment)) {
            return false;
        }
        return id != null && id.equals(((Appointment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", motif='" + getMotif() + "'" +
            ", dateRdv='" + getDateRdv() + "'" +
            "}";
    }
}
