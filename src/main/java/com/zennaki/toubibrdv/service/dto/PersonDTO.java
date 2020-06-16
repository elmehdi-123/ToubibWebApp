package com.zennaki.toubibrdv.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.zennaki.toubibrdv.domain.enumeration.TypeCivilite;
import com.zennaki.toubibrdv.domain.enumeration.DocteurOrPatientEnum;

/**
 * A DTO for the {@link com.zennaki.toubibrdv.domain.Person} entity.
 */
public class PersonDTO implements Serializable {
    
    private Long id;

    private String nom;

    private String prenom;

    private String numTele;

    private String eMail;

    private LocalDate dateDeNaissance;

    private TypeCivilite civilite;

    private DocteurOrPatientEnum docteurOrPatient;


    private Long userId;

    private String userLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumTele() {
        return numTele;
    }

    public void setNumTele(String numTele) {
        this.numTele = numTele;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public TypeCivilite getCivilite() {
        return civilite;
    }

    public void setCivilite(TypeCivilite civilite) {
        this.civilite = civilite;
    }

    public DocteurOrPatientEnum getDocteurOrPatient() {
        return docteurOrPatient;
    }

    public void setDocteurOrPatient(DocteurOrPatientEnum docteurOrPatient) {
        this.docteurOrPatient = docteurOrPatient;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if (personDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), personDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", numTele='" + getNumTele() + "'" +
            ", eMail='" + geteMail() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", civilite='" + getCivilite() + "'" +
            ", docteurOrPatient='" + getDocteurOrPatient() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
