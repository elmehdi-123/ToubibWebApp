package com.zennaki.toubibrdv.web.rest;

import com.zennaki.toubibrdv.ToubibRdvWebApp;
import com.zennaki.toubibrdv.domain.Specialty;
import com.zennaki.toubibrdv.domain.Person;
import com.zennaki.toubibrdv.repository.SpecialtyRepository;
import com.zennaki.toubibrdv.service.SpecialtyService;
import com.zennaki.toubibrdv.service.dto.SpecialtyDTO;
import com.zennaki.toubibrdv.service.mapper.SpecialtyMapper;
import com.zennaki.toubibrdv.service.dto.SpecialtyCriteria;
import com.zennaki.toubibrdv.service.SpecialtyQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@SpringBootTest(classes = ToubibRdvWebApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SpecialtyResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private SpecialtyQueryService specialtyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        return specialty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        return specialty;
    }

    @BeforeEach
    public void initTest() {
        specialty = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecialty() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);
        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isCreated());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSpecialty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSpecialtyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty with an existing ID
        specialty.setId(1L);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSpecialties() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getSpecialtiesByIdFiltering() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        Long id = specialty.getId();

        defaultSpecialtyShouldBeFound("id.equals=" + id);
        defaultSpecialtyShouldNotBeFound("id.notEquals=" + id);

        defaultSpecialtyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSpecialtyShouldNotBeFound("id.greaterThan=" + id);

        defaultSpecialtyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSpecialtyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where libelle equals to DEFAULT_LIBELLE
        defaultSpecialtyShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the specialtyList where libelle equals to UPDATED_LIBELLE
        defaultSpecialtyShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where libelle not equals to DEFAULT_LIBELLE
        defaultSpecialtyShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the specialtyList where libelle not equals to UPDATED_LIBELLE
        defaultSpecialtyShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSpecialtyShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the specialtyList where libelle equals to UPDATED_LIBELLE
        defaultSpecialtyShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where libelle is not null
        defaultSpecialtyShouldBeFound("libelle.specified=true");

        // Get all the specialtyList where libelle is null
        defaultSpecialtyShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllSpecialtiesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where libelle contains DEFAULT_LIBELLE
        defaultSpecialtyShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the specialtyList where libelle contains UPDATED_LIBELLE
        defaultSpecialtyShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where libelle does not contain DEFAULT_LIBELLE
        defaultSpecialtyShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the specialtyList where libelle does not contain UPDATED_LIBELLE
        defaultSpecialtyShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description equals to DEFAULT_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description equals to UPDATED_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description not equals to DEFAULT_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description not equals to UPDATED_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the specialtyList where description equals to UPDATED_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description is not null
        defaultSpecialtyShouldBeFound("description.specified=true");

        // Get all the specialtyList where description is null
        defaultSpecialtyShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description contains DEFAULT_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description contains UPDATED_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSpecialtiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where description does not contain DEFAULT_DESCRIPTION
        defaultSpecialtyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the specialtyList where description does not contain UPDATED_DESCRIPTION
        defaultSpecialtyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllSpecialtiesByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        specialty.addPerson(person);
        specialtyRepository.saveAndFlush(specialty);
        Long personId = person.getId();

        // Get all the specialtyList where person equals to personId
        defaultSpecialtyShouldBeFound("personId.equals=" + personId);

        // Get all the specialtyList where person equals to personId + 1
        defaultSpecialtyShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpecialtyShouldBeFound(String filter) throws Exception {
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restSpecialtyMockMvc.perform(get("/api/specialties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpecialtyShouldNotBeFound(String filter) throws Exception {
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpecialtyMockMvc.perform(get("/api/specialties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialty are not directly saved in db
        em.detach(updatedSpecialty);
        updatedSpecialty
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(updatedSpecialty);

        restSpecialtyMockMvc.perform(put("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSpecialty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc.perform(put("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

        // Delete the specialty
        restSpecialtyMockMvc.perform(delete("/api/specialties/{id}", specialty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
