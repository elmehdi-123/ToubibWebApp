package com.zennaki.toubibrdv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_rue")
    private String nomRue;

    @Column(name = "ville")
    private String ville;

    @Column(name = "commun")
    private String commun;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "willaya")
    private String willaya;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("addresses")
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomRue() {
        return nomRue;
    }

    public Address nomRue(String nomRue) {
        this.nomRue = nomRue;
        return this;
    }

    public void setNomRue(String nomRue) {
        this.nomRue = nomRue;
    }

    public String getVille() {
        return ville;
    }

    public Address ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCommun() {
        return commun;
    }

    public Address commun(String commun) {
        this.commun = commun;
        return this;
    }

    public void setCommun(String commun) {
        this.commun = commun;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public Address codePostal(String codePostal) {
        this.codePostal = codePostal;
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getWillaya() {
        return willaya;
    }

    public Address willaya(String willaya) {
        this.willaya = willaya;
        return this;
    }

    public void setWillaya(String willaya) {
        this.willaya = willaya;
    }

    public Person getPerson() {
        return person;
    }

    public Address person(Person person) {
        this.person = person;
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", nomRue='" + getNomRue() + "'" +
            ", ville='" + getVille() + "'" +
            ", commun='" + getCommun() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", willaya='" + getWillaya() + "'" +
            "}";
    }
}
