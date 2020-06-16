package com.zennaki.toubibrdv.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.zennaki.toubibrdv.domain.Appointment} entity. This class is used
 * in {@link com.zennaki.toubibrdv.web.rest.AppointmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appointments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppointmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter motif;

    private LocalDateFilter dateRdv;

    private LongFilter personId;

    private LongFilter docteurId;

    public AppointmentCriteria() {
    }

    public AppointmentCriteria(AppointmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.motif = other.motif == null ? null : other.motif.copy();
        this.dateRdv = other.dateRdv == null ? null : other.dateRdv.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.docteurId = other.docteurId == null ? null : other.docteurId.copy();
    }

    @Override
    public AppointmentCriteria copy() {
        return new AppointmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMotif() {
        return motif;
    }

    public void setMotif(StringFilter motif) {
        this.motif = motif;
    }

    public LocalDateFilter getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(LocalDateFilter dateRdv) {
        this.dateRdv = dateRdv;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter getDocteurId() {
        return docteurId;
    }

    public void setDocteurId(LongFilter docteurId) {
        this.docteurId = docteurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppointmentCriteria that = (AppointmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(motif, that.motif) &&
            Objects.equals(dateRdv, that.dateRdv) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(docteurId, that.docteurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        motif,
        dateRdv,
        personId,
        docteurId
        );
    }

    @Override
    public String toString() {
        return "AppointmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (motif != null ? "motif=" + motif + ", " : "") +
                (dateRdv != null ? "dateRdv=" + dateRdv + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
                (docteurId != null ? "docteurId=" + docteurId + ", " : "") +
            "}";
    }

}
