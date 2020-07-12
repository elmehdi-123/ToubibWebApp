package com.zennaki.toubibrdv.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.zennaki.toubibrdv.domain.enumeration.TypeCivilite;

import com.zennaki.toubibrdv.domain.enumeration.DocteurOrPatientEnum;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "num_tele")
    private String numTele;

    @Column(name = "e_mail")
    private String eMail;

    @Column(name = "date_de_naissance")
    private LocalDate dateDeNaissance;

    @Enumerated(EnumType.STRING)
    @Column(name = "civilite")
    private TypeCivilite civilite;

    @Enumerated(EnumType.STRING)
    @Column(name = "docteur_or_patient")
    private DocteurOrPatientEnum docteurOrPatient;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> addresses = new HashSet<>();

    

    @ManyToMany
    @JoinTable(name = "person_specialty",
               joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "specialty_id", referencedColumnName = "id"))
    private Set<Specialty> specialties = new HashSet<>();

    @OneToMany(mappedBy = "person")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "docteur")
    private Set<Appointment> disponibilties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Person nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Person prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumTele() {
        return numTele;
    }

    public Person numTele(String numTele) {
        this.numTele = numTele;
        return this;
    }

    public void setNumTele(String numTele) {
        this.numTele = numTele;
    }

    public String geteMail() {
        return eMail;
    }

    public Person eMail(String eMail) {
        this.eMail = eMail;
        return this;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public Person dateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
        return this;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public TypeCivilite getCivilite() {
        return civilite;
    }

    public Person civilite(TypeCivilite civilite) {
        this.civilite = civilite;
        return this;
    }

    public void setCivilite(TypeCivilite civilite) {
        this.civilite = civilite;
    }

    public DocteurOrPatientEnum getDocteurOrPatient() {
        return docteurOrPatient;
    }

    public Person docteurOrPatient(DocteurOrPatientEnum docteurOrPatient) {
        this.docteurOrPatient = docteurOrPatient;
        return this;
    }

    public void setDocteurOrPatient(DocteurOrPatientEnum docteurOrPatient) {
        this.docteurOrPatient = docteurOrPatient;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Person addresses(Set<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Person addAddress(Address address) {
        this.addresses.add(address);
        address.setPerson(this);
        return this;
    }

    public Person removeAddress(Address address) {
        this.addresses.remove(address);
        address.setPerson(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public Person specialties(Set<Specialty> specialties) {
        this.specialties = specialties;
        return this;
    }

    public Person addSpecialty(Specialty specialty) {
        this.specialties.add(specialty);
        specialty.getPeople().add(this);
        return this;
    }

    public Person removeSpecialty(Specialty specialty) {
        this.specialties.remove(specialty);
        specialty.getPeople().remove(this);
        return this;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        this.specialties = specialties;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public Person appointments(Set<Appointment> appointments) {
        this.appointments = appointments;
        return this;
    }

    public Person addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.setPerson(this);
        return this;
    }

    public Person removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.setPerson(null);
        return this;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<Appointment> getDisponibilties() {
        return disponibilties;
    }

    public Person disponibilties(Set<Appointment> appointments) {
        this.disponibilties = appointments;
        return this;
    }

    public Person addDisponibilties(Appointment appointment) {
        this.disponibilties.add(appointment);
        appointment.setDocteur(this);
        return this;
    }

    public Person removeDisponibilties(Appointment appointment) {
        this.disponibilties.remove(appointment);
        appointment.setDocteur(null);
        return this;
    }

    public void setDisponibilties(Set<Appointment> appointments) {
        this.disponibilties = appointments;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", numTele='" + getNumTele() + "'" +
            ", eMail='" + geteMail() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", civilite='" + getCivilite() + "'" +
            ", docteurOrPatient='" + getDocteurOrPatient() + "'" +
            "}";
    }
}
