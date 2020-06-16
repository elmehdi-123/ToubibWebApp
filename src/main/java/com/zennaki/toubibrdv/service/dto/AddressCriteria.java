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

/**
 * Criteria class for the {@link com.zennaki.toubibrdv.domain.Address} entity. This class is used
 * in {@link com.zennaki.toubibrdv.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomRue;

    private StringFilter ville;

    private StringFilter commun;

    private StringFilter codePostal;

    private StringFilter willaya;

    private LongFilter personId;

    public AddressCriteria() {
    }

    public AddressCriteria(AddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nomRue = other.nomRue == null ? null : other.nomRue.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.commun = other.commun == null ? null : other.commun.copy();
        this.codePostal = other.codePostal == null ? null : other.codePostal.copy();
        this.willaya = other.willaya == null ? null : other.willaya.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNomRue() {
        return nomRue;
    }

    public void setNomRue(StringFilter nomRue) {
        this.nomRue = nomRue;
    }

    public StringFilter getVille() {
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getCommun() {
        return commun;
    }

    public void setCommun(StringFilter commun) {
        this.commun = commun;
    }

    public StringFilter getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(StringFilter codePostal) {
        this.codePostal = codePostal;
    }

    public StringFilter getWillaya() {
        return willaya;
    }

    public void setWillaya(StringFilter willaya) {
        this.willaya = willaya;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AddressCriteria that = (AddressCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nomRue, that.nomRue) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(commun, that.commun) &&
            Objects.equals(codePostal, that.codePostal) &&
            Objects.equals(willaya, that.willaya) &&
            Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nomRue,
        ville,
        commun,
        codePostal,
        willaya,
        personId
        );
    }

    @Override
    public String toString() {
        return "AddressCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nomRue != null ? "nomRue=" + nomRue + ", " : "") +
                (ville != null ? "ville=" + ville + ", " : "") +
                (commun != null ? "commun=" + commun + ", " : "") +
                (codePostal != null ? "codePostal=" + codePostal + ", " : "") +
                (willaya != null ? "willaya=" + willaya + ", " : "") +
                (personId != null ? "personId=" + personId + ", " : "") +
            "}";
    }

}
