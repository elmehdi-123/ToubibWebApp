package com.zennaki.toubibrdv.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.zennaki.toubibrdv.domain.enumeration.TypeCivilite;
import com.zennaki.toubibrdv.domain.enumeration.DocteurOrPatientEnum;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.zennaki.toubibrdv.domain.Person} entity. This class is used
 * in {@link com.zennaki.toubibrdv.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TypeCivilite
     */
    public static class TypeCiviliteFilter extends Filter<TypeCivilite> {

        public TypeCiviliteFilter() {
        }

        public TypeCiviliteFilter(TypeCiviliteFilter filter) {
            super(filter);
        }

        @Override
        public TypeCiviliteFilter copy() {
            return new TypeCiviliteFilter(this);
        }

    }
    /**
     * Class for filtering DocteurOrPatientEnum
     */
    public static class DocteurOrPatientEnumFilter extends Filter<DocteurOrPatientEnum> {

        public DocteurOrPatientEnumFilter() {
        }

        public DocteurOrPatientEnumFilter(DocteurOrPatientEnumFilter filter) {
            super(filter);
        }

        @Override
        public DocteurOrPatientEnumFilter copy() {
            return new DocteurOrPatientEnumFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter numTele;

    private StringFilter eMail;

    private LocalDateFilter dateDeNaissance;

    private TypeCiviliteFilter civilite;

    private DocteurOrPatientEnumFilter docteurOrPatient;

    private LongFilter addressId;

    private LongFilter userId;

    private LongFilter specialtyId;

    private LongFilter appointmentId;

    private LongFilter disponibiltiesId;

    public PersonCriteria() {
    }

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.numTele = other.numTele == null ? null : other.numTele.copy();
        this.eMail = other.eMail == null ? null : other.eMail.copy();
        this.dateDeNaissance = other.dateDeNaissance == null ? null : other.dateDeNaissance.copy();
        this.civilite = other.civilite == null ? null : other.civilite.copy();
        this.docteurOrPatient = other.docteurOrPatient == null ? null : other.docteurOrPatient.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.specialtyId = other.specialtyId == null ? null : other.specialtyId.copy();
        this.appointmentId = other.appointmentId == null ? null : other.appointmentId.copy();
        this.disponibiltiesId = other.disponibiltiesId == null ? null : other.disponibiltiesId.copy();
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public StringFilter getNumTele() {
        return numTele;
    }

    public void setNumTele(StringFilter numTele) {
        this.numTele = numTele;
    }

    public StringFilter geteMail() {
        return eMail;
    }

    public void seteMail(StringFilter eMail) {
        this.eMail = eMail;
    }

    public LocalDateFilter getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDateFilter dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public TypeCiviliteFilter getCivilite() {
        return civilite;
    }

    public void setCivilite(TypeCiviliteFilter civilite) {
        this.civilite = civilite;
    }

    public DocteurOrPatientEnumFilter getDocteurOrPatient() {
        return docteurOrPatient;
    }

    public void setDocteurOrPatient(DocteurOrPatientEnumFilter docteurOrPatient) {
        this.docteurOrPatient = docteurOrPatient;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(LongFilter specialtyId) {
        this.specialtyId = specialtyId;
    }

    public LongFilter getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(LongFilter appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LongFilter getDisponibiltiesId() {
        return disponibiltiesId;
    }

    public void setDisponibiltiesId(LongFilter disponibiltiesId) {
        this.disponibiltiesId = disponibiltiesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonCriteria that = (PersonCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(numTele, that.numTele) &&
            Objects.equals(eMail, that.eMail) &&
            Objects.equals(dateDeNaissance, that.dateDeNaissance) &&
            Objects.equals(civilite, that.civilite) &&
            Objects.equals(docteurOrPatient, that.docteurOrPatient) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(specialtyId, that.specialtyId) &&
            Objects.equals(appointmentId, that.appointmentId) &&
            Objects.equals(disponibiltiesId, that.disponibiltiesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        prenom,
        numTele,
        eMail,
        dateDeNaissance,
        civilite,
        docteurOrPatient,
        addressId,
        userId,
        specialtyId,
        appointmentId,
        disponibiltiesId
        );
    }

    @Override
    public String toString() {
        return "PersonCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (prenom != null ? "prenom=" + prenom + ", " : "") +
                (numTele != null ? "numTele=" + numTele + ", " : "") +
                (eMail != null ? "eMail=" + eMail + ", " : "") +
                (dateDeNaissance != null ? "dateDeNaissance=" + dateDeNaissance + ", " : "") +
                (civilite != null ? "civilite=" + civilite + ", " : "") +
                (docteurOrPatient != null ? "docteurOrPatient=" + docteurOrPatient + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (specialtyId != null ? "specialtyId=" + specialtyId + ", " : "") +
                (appointmentId != null ? "appointmentId=" + appointmentId + ", " : "") +
                (disponibiltiesId != null ? "disponibiltiesId=" + disponibiltiesId + ", " : "") +
            "}";
    }

}
