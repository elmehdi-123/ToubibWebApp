package com.zennaki.toubibrdv.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.zennaki.toubibrdv.domain.Person;
import com.zennaki.toubibrdv.domain.*; // for static metamodels
import com.zennaki.toubibrdv.repository.PersonRepository;
import com.zennaki.toubibrdv.service.dto.PersonCriteria;
import com.zennaki.toubibrdv.service.dto.PersonDTO;
import com.zennaki.toubibrdv.service.mapper.PersonMapper;

/**
 * Service for executing complex queries for {@link Person} entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonDTO} or a {@link Page} of {@link PersonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private final Logger log = LoggerFactory.getLogger(PersonQueryService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonQueryService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    /**
     * Return a {@link List} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonDTO> findByCriteria(PersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personMapper.toDto(personRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> findByCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification, page)
            .map(personMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Person> createSpecification(PersonCriteria criteria) {
        Specification<Person> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Person_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Person_.nom));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Person_.prenom));
            }
            if (criteria.getNumTele() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumTele(), Person_.numTele));
            }
            if (criteria.geteMail() != null) {
                specification = specification.and(buildStringSpecification(criteria.geteMail(), Person_.eMail));
            }
            if (criteria.getDateDeNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDeNaissance(), Person_.dateDeNaissance));
            }
            if (criteria.getCivilite() != null) {
                specification = specification.and(buildSpecification(criteria.getCivilite(), Person_.civilite));
            }
            if (criteria.getDocteurOrPatient() != null) {
                specification = specification.and(buildSpecification(criteria.getDocteurOrPatient(), Person_.docteurOrPatient));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildSpecification(criteria.getAddressId(),
                    root -> root.join(Person_.addresses, JoinType.LEFT).get(Address_.id)));
            }
            if (criteria.getSpecialtyId() != null) {
                specification = specification.and(buildSpecification(criteria.getSpecialtyId(),
                    root -> root.join(Person_.specialties, JoinType.LEFT).get(Specialty_.id)));
            }
            if (criteria.getAppointmentId() != null) {
                specification = specification.and(buildSpecification(criteria.getAppointmentId(),
                    root -> root.join(Person_.appointments, JoinType.LEFT).get(Appointment_.id)));
            }
            if (criteria.getDisponibiltiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getDisponibiltiesId(),
                    root -> root.join(Person_.disponibilties, JoinType.LEFT).get(Appointment_.id)));
            }
        }
        return specification;
    }
}
