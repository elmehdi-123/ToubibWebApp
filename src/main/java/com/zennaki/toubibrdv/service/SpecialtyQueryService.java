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

import com.zennaki.toubibrdv.domain.Specialty;
import com.zennaki.toubibrdv.domain.*; // for static metamodels
import com.zennaki.toubibrdv.repository.SpecialtyRepository;
import com.zennaki.toubibrdv.service.dto.SpecialtyCriteria;
import com.zennaki.toubibrdv.service.dto.SpecialtyDTO;
import com.zennaki.toubibrdv.service.mapper.SpecialtyMapper;

/**
 * Service for executing complex queries for {@link Specialty} entities in the database.
 * The main input is a {@link SpecialtyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpecialtyDTO} or a {@link Page} of {@link SpecialtyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecialtyQueryService extends QueryService<Specialty> {

    private final Logger log = LoggerFactory.getLogger(SpecialtyQueryService.class);

    private final SpecialtyRepository specialtyRepository;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyQueryService(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    /**
     * Return a {@link List} of {@link SpecialtyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpecialtyDTO> findByCriteria(SpecialtyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyMapper.toDto(specialtyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SpecialtyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecialtyDTO> findByCriteria(SpecialtyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.findAll(specification, page)
            .map(specialtyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecialtyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecialtyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Specialty> createSpecification(SpecialtyCriteria criteria) {
        Specification<Specialty> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Specialty_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Specialty_.libelle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Specialty_.description));
            }
            if (criteria.getPersonId() != null) {
                specification = specification.and(buildSpecification(criteria.getPersonId(),
                    root -> root.join(Specialty_.people, JoinType.LEFT).get(Person_.id)));
            }
        }
        return specification;
    }
}
