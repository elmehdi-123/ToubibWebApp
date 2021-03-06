package com.zennaki.toubibrdv.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.zennaki.toubibrdv.domain.Person;

/**
 * A DTO for the {@link com.zennaki.toubibrdv.domain.Specialty} entity.
 */
public class SpecialtyDTO implements Serializable {
    
    private Long id;

    private String libelle;

    private String description;
    
    private Set<Person> people = new HashSet<>();

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Person> getPeople() {
		return people;
	}

	public void setPeople(Set<Person> people) {
		this.people = people;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpecialtyDTO specialtyDTO = (SpecialtyDTO) o;
        if (specialtyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), specialtyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpecialtyDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
