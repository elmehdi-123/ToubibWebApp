package com.zennaki.toubibrdv.web.rest;

import com.zennaki.toubibrdv.service.SpecialtyService;
import com.zennaki.toubibrdv.web.rest.errors.BadRequestAlertException;
import com.zennaki.toubibrdv.service.dto.SpecialtyDTO;
import com.zennaki.toubibrdv.service.dto.SpecialtyCriteria;
import com.zennaki.toubibrdv.service.SpecialtyQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.zennaki.toubibrdv.domain.Specialty}.
 */
@RestController
@RequestMapping("/api")
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private static final String ENTITY_NAME = "specialty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialtyService specialtyService;

    private final SpecialtyQueryService specialtyQueryService;

    public SpecialtyResource(SpecialtyService specialtyService, SpecialtyQueryService specialtyQueryService) {
        this.specialtyService = specialtyService;
        this.specialtyQueryService = specialtyQueryService;
    }

    /**
     * {@code POST  /specialties} : Create a new specialty.
     *
     * @param specialtyDTO the specialtyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialtyDTO, or with status {@code 400 (Bad Request)} if the specialty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialties")
    public ResponseEntity<SpecialtyDTO> createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) throws URISyntaxException {
        log.debug("REST request to save Specialty : {}", specialtyDTO);
        if (specialtyDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialtyDTO result = specialtyService.save(specialtyDTO);
        return ResponseEntity.created(new URI("/api/specialties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialties} : Updates an existing specialty.
     *
     * @param specialtyDTO the specialtyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialtyDTO,
     * or with status {@code 400 (Bad Request)} if the specialtyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialtyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialties")
    public ResponseEntity<SpecialtyDTO> updateSpecialty(@RequestBody SpecialtyDTO specialtyDTO) throws URISyntaxException {
        log.debug("REST request to update Specialty : {}", specialtyDTO);
        if (specialtyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpecialtyDTO result = specialtyService.save(specialtyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialtyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties(SpecialtyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Specialties by criteria: {}", criteria);
        Page<SpecialtyDTO> page = specialtyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specialties/count} : count all the specialties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/specialties/count")
    public ResponseEntity<Long> countSpecialties(SpecialtyCriteria criteria) {
        log.debug("REST request to count Specialties by criteria: {}", criteria);
        return ResponseEntity.ok().body(specialtyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialty.
     *
     * @param id the id of the specialtyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialtyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialties/{id}")
    public ResponseEntity<SpecialtyDTO> getSpecialty(@PathVariable Long id) {
        log.debug("REST request to get Specialty : {}", id);
        Optional<SpecialtyDTO> specialtyDTO = specialtyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialtyDTO);
    }

    /**
     * {@code DELETE  /specialties/:id} : delete the "id" specialty.
     *
     * @param id the id of the specialtyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        log.debug("REST request to delete Specialty : {}", id);
        specialtyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
