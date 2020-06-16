package com.zennaki.toubibrdv.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.zennaki.toubibrdv.domain.Address} entity.
 */
public class AddressDTO implements Serializable {
    
    private Long id;

    private String nomRue;

    private String ville;

    private String commun;

    private String codePostal;

    private String willaya;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomRue() {
        return nomRue;
    }

    public void setNomRue(String nomRue) {
        this.nomRue = nomRue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCommun() {
        return commun;
    }

    public void setCommun(String commun) {
        this.commun = commun;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getWillaya() {
        return willaya;
    }

    public void setWillaya(String willaya) {
        this.willaya = willaya;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (addressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), addressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + getId() +
            ", nomRue='" + getNomRue() + "'" +
            ", ville='" + getVille() + "'" +
            ", commun='" + getCommun() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", willaya='" + getWillaya() + "'" +
            "}";
    }
}
