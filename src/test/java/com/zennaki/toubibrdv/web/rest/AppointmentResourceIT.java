package com.zennaki.toubibrdv.web.rest;

import com.zennaki.toubibrdv.ToubibRdvWebApp;
import com.zennaki.toubibrdv.domain.Appointment;
import com.zennaki.toubibrdv.domain.Person;
import com.zennaki.toubibrdv.repository.AppointmentRepository;
import com.zennaki.toubibrdv.service.AppointmentService;
import com.zennaki.toubibrdv.service.dto.AppointmentDTO;
import com.zennaki.toubibrdv.service.mapper.AppointmentMapper;
import com.zennaki.toubibrdv.service.dto.AppointmentCriteria;
import com.zennaki.toubibrdv.service.AppointmentQueryService;

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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AppointmentResource} REST controller.
 */
@SpringBootTest(classes = ToubibRdvWebApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AppointmentResourceIT {

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_RDV = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RDV = LocalDate.now();
    private static final LocalDate SMALLER_DATE_RDV = LocalDate.ofEpochDay(-1L);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentQueryService appointmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .motif(DEFAULT_MOTIF);
           // .dateRdv(DEFAULT_DATE_RDV);
        return appointment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createUpdatedEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .motif(UPDATED_MOTIF);
            //.dateRdv(UPDATED_DATE_RDV);
        return appointment;
    }

    @BeforeEach
    public void initTest() {
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testAppointment.getDateRdv()).isEqualTo(DEFAULT_DATE_RDV);
    }

    @Test
    @Transactional
    public void createAppointmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc.perform(post("/api/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].dateRdv").value(hasItem(DEFAULT_DATE_RDV.toString())));
    }
    
    @Test
    @Transactional
    public void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.dateRdv").value(DEFAULT_DATE_RDV.toString()));
    }


    @Test
    @Transactional
    public void getAppointmentsByIdFiltering() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        Long id = appointment.getId();

        defaultAppointmentShouldBeFound("id.equals=" + id);
        defaultAppointmentShouldNotBeFound("id.notEquals=" + id);

        defaultAppointmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAppointmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAppointmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAppointmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAppointmentsByMotifIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where motif equals to DEFAULT_MOTIF
        defaultAppointmentShouldBeFound("motif.equals=" + DEFAULT_MOTIF);

        // Get all the appointmentList where motif equals to UPDATED_MOTIF
        defaultAppointmentShouldNotBeFound("motif.equals=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByMotifIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where motif not equals to DEFAULT_MOTIF
        defaultAppointmentShouldNotBeFound("motif.notEquals=" + DEFAULT_MOTIF);

        // Get all the appointmentList where motif not equals to UPDATED_MOTIF
        defaultAppointmentShouldBeFound("motif.notEquals=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByMotifIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where motif in DEFAULT_MOTIF or UPDATED_MOTIF
        defaultAppointmentShouldBeFound("motif.in=" + DEFAULT_MOTIF + "," + UPDATED_MOTIF);

        // Get all the appointmentList where motif equals to UPDATED_MOTIF
        defaultAppointmentShouldNotBeFound("motif.in=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByMotifIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where motif is not null
        defaultAppointmentShouldBeFound("motif.specified=true");

        // Get all the appointmentList where motif is null
        defaultAppointmentShouldNotBeFound("motif.specified=false");
    }
                @Test
    @Transactional
    public void getAllAppointmentsByMotifContainsSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where motif contains DEFAULT_MOTIF
        defaultAppointmentShouldBeFound("motif.contains=" + DEFAULT_MOTIF);

        // Get all the appointmentList where motif contains UPDATED_MOTIF
        defaultAppointmentShouldNotBeFound("motif.contains=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByMotifNotContainsSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where motif does not contain DEFAULT_MOTIF
        defaultAppointmentShouldNotBeFound("motif.doesNotContain=" + DEFAULT_MOTIF);

        // Get all the appointmentList where motif does not contain UPDATED_MOTIF
        defaultAppointmentShouldBeFound("motif.doesNotContain=" + UPDATED_MOTIF);
    }


    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv equals to DEFAULT_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.equals=" + DEFAULT_DATE_RDV);

        // Get all the appointmentList where dateRdv equals to UPDATED_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.equals=" + UPDATED_DATE_RDV);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsNotEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv not equals to DEFAULT_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.notEquals=" + DEFAULT_DATE_RDV);

        // Get all the appointmentList where dateRdv not equals to UPDATED_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.notEquals=" + UPDATED_DATE_RDV);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsInShouldWork() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv in DEFAULT_DATE_RDV or UPDATED_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.in=" + DEFAULT_DATE_RDV + "," + UPDATED_DATE_RDV);

        // Get all the appointmentList where dateRdv equals to UPDATED_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.in=" + UPDATED_DATE_RDV);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsNullOrNotNull() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv is not null
        defaultAppointmentShouldBeFound("dateRdv.specified=true");

        // Get all the appointmentList where dateRdv is null
        defaultAppointmentShouldNotBeFound("dateRdv.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv is greater than or equal to DEFAULT_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.greaterThanOrEqual=" + DEFAULT_DATE_RDV);

        // Get all the appointmentList where dateRdv is greater than or equal to UPDATED_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.greaterThanOrEqual=" + UPDATED_DATE_RDV);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv is less than or equal to DEFAULT_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.lessThanOrEqual=" + DEFAULT_DATE_RDV);

        // Get all the appointmentList where dateRdv is less than or equal to SMALLER_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.lessThanOrEqual=" + SMALLER_DATE_RDV);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsLessThanSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv is less than DEFAULT_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.lessThan=" + DEFAULT_DATE_RDV);

        // Get all the appointmentList where dateRdv is less than UPDATED_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.lessThan=" + UPDATED_DATE_RDV);
    }

    @Test
    @Transactional
    public void getAllAppointmentsByDateRdvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList where dateRdv is greater than DEFAULT_DATE_RDV
        defaultAppointmentShouldNotBeFound("dateRdv.greaterThan=" + DEFAULT_DATE_RDV);

        // Get all the appointmentList where dateRdv is greater than SMALLER_DATE_RDV
        defaultAppointmentShouldBeFound("dateRdv.greaterThan=" + SMALLER_DATE_RDV);
    }


    @Test
    @Transactional
    public void getAllAppointmentsByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        appointment.setPerson(person);
        appointmentRepository.saveAndFlush(appointment);
        Long personId = person.getId();

        // Get all the appointmentList where person equals to personId
        defaultAppointmentShouldBeFound("personId.equals=" + personId);

        // Get all the appointmentList where person equals to personId + 1
        defaultAppointmentShouldNotBeFound("personId.equals=" + (personId + 1));
    }


    @Test
    @Transactional
    public void getAllAppointmentsByDocteurIsEqualToSomething() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        Person docteur = PersonResourceIT.createEntity(em);
        em.persist(docteur);
        em.flush();
        appointment.setDocteur(docteur);
        appointmentRepository.saveAndFlush(appointment);
        Long docteurId = docteur.getId();

        // Get all the appointmentList where docteur equals to docteurId
        defaultAppointmentShouldBeFound("docteurId.equals=" + docteurId);

        // Get all the appointmentList where docteur equals to docteurId + 1
        defaultAppointmentShouldNotBeFound("docteurId.equals=" + (docteurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppointmentShouldBeFound(String filter) throws Exception {
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].dateRdv").value(hasItem(DEFAULT_DATE_RDV.toString())));

        // Check, that the count call also returns 1
        restAppointmentMockMvc.perform(get("/api/appointments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppointmentShouldNotBeFound(String filter) throws Exception {
        restAppointmentMockMvc.perform(get("/api/appointments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppointmentMockMvc.perform(get("/api/appointments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get("/api/appointments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findById(appointment.getId()).get();
        // Disconnect from session so that the updates on updatedAppointment are not directly saved in db
        em.detach(updatedAppointment);
        updatedAppointment
            .motif(UPDATED_MOTIF);
            //.dateRdv(UPDATED_DATE_RDV);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testAppointment.getDateRdv()).isEqualTo(UPDATED_DATE_RDV);
    }

    @Test
    @Transactional
    public void updateNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc.perform(put("/api/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Delete the appointment
        restAppointmentMockMvc.perform(delete("/api/appointments/{id}", appointment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
