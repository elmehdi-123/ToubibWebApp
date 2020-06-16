package com.zennaki.toubibrdv.web.rest;

import com.zennaki.toubibrdv.ToubibRdvWebApp;
import com.zennaki.toubibrdv.domain.Person;
import com.zennaki.toubibrdv.domain.Address;
import com.zennaki.toubibrdv.domain.User;
import com.zennaki.toubibrdv.domain.Specialty;
import com.zennaki.toubibrdv.domain.Appointment;
import com.zennaki.toubibrdv.repository.PersonRepository;
import com.zennaki.toubibrdv.service.PersonService;
import com.zennaki.toubibrdv.service.dto.PersonDTO;
import com.zennaki.toubibrdv.service.mapper.PersonMapper;
import com.zennaki.toubibrdv.service.dto.PersonCriteria;
import com.zennaki.toubibrdv.service.PersonQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zennaki.toubibrdv.domain.enumeration.TypeCivilite;
import com.zennaki.toubibrdv.domain.enumeration.DocteurOrPatientEnum;
/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@SpringBootTest(classes = ToubibRdvWebApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PersonResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TELE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TELE = "BBBBBBBBBB";

    private static final String DEFAULT_E_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_E_MAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final TypeCivilite DEFAULT_CIVILITE = TypeCivilite.MADAME;
    private static final TypeCivilite UPDATED_CIVILITE = TypeCivilite.MONSIEUR;

    private static final DocteurOrPatientEnum DEFAULT_DOCTEUR_OR_PATIENT = DocteurOrPatientEnum.DOCTEUR;
    private static final DocteurOrPatientEnum UPDATED_DOCTEUR_OR_PATIENT = DocteurOrPatientEnum.PATIENT;

    @Autowired
    private PersonRepository personRepository;

    @Mock
    private PersonRepository personRepositoryMock;

    @Autowired
    private PersonMapper personMapper;

    @Mock
    private PersonService personServiceMock;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonQueryService personQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .numTele(DEFAULT_NUM_TELE)
            .eMail(DEFAULT_E_MAIL)
            .dateDeNaissance(DEFAULT_DATE_DE_NAISSANCE)
            .civilite(DEFAULT_CIVILITE)
            .docteurOrPatient(DEFAULT_DOCTEUR_OR_PATIENT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        person.setUser(user);
        return person;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        Person person = new Person()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numTele(UPDATED_NUM_TELE)
            .eMail(UPDATED_E_MAIL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .civilite(UPDATED_CIVILITE)
            .docteurOrPatient(UPDATED_DOCTEUR_OR_PATIENT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        person.setUser(user);
        return person;
    }

    @BeforeEach
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);
        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPerson.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPerson.getNumTele()).isEqualTo(DEFAULT_NUM_TELE);
        assertThat(testPerson.geteMail()).isEqualTo(DEFAULT_E_MAIL);
        assertThat(testPerson.getDateDeNaissance()).isEqualTo(DEFAULT_DATE_DE_NAISSANCE);
        assertThat(testPerson.getCivilite()).isEqualTo(DEFAULT_CIVILITE);
        assertThat(testPerson.getDocteurOrPatient()).isEqualTo(DEFAULT_DOCTEUR_OR_PATIENT);

        // Validate the id for MapsId, the ids must be same
        assertThat(testPerson.getId()).isEqualTo(testPerson.getUser().getId());
    }

    @Test
    @Transactional
    public void createPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // Create the Person with an existing ID
        person.setId(1L);
        PersonDTO personDTO = personMapper.toDto(person);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc.perform(post("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updatePersonMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        int databaseSizeBeforeCreate = personRepository.findAll().size();


        // Load the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);

        // Update the User with new association value
        //updatedPerson.setUser();
        PersonDTO updatedPersonDTO = personMapper.toDto(updatedPerson);

        // Update the entity
        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonDTO)))
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
        Person testPerson = personList.get(personList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testPerson.getId()).isEqualTo(testPerson.getUser().getId());
    }

    @Test
    @Transactional
    public void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc.perform(get("/api/people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].numTele").value(hasItem(DEFAULT_NUM_TELE)))
            .andExpect(jsonPath("$.[*].eMail").value(hasItem(DEFAULT_E_MAIL)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].docteurOrPatient").value(hasItem(DEFAULT_DOCTEUR_OR_PATIENT.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPeopleWithEagerRelationshipsIsEnabled() throws Exception {
        when(personServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonMockMvc.perform(get("/api/people?eagerload=true"))
            .andExpect(status().isOk());

        verify(personServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPeopleWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonMockMvc.perform(get("/api/people?eagerload=true"))
            .andExpect(status().isOk());

        verify(personServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.numTele").value(DEFAULT_NUM_TELE))
            .andExpect(jsonPath("$.eMail").value(DEFAULT_E_MAIL))
            .andExpect(jsonPath("$.dateDeNaissance").value(DEFAULT_DATE_DE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.civilite").value(DEFAULT_CIVILITE.toString()))
            .andExpect(jsonPath("$.docteurOrPatient").value(DEFAULT_DOCTEUR_OR_PATIENT.toString()));
    }


    @Test
    @Transactional
    public void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPeopleByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nom equals to DEFAULT_NOM
        defaultPersonShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the personList where nom equals to UPDATED_NOM
        defaultPersonShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nom not equals to DEFAULT_NOM
        defaultPersonShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the personList where nom not equals to UPDATED_NOM
        defaultPersonShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByNomIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultPersonShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the personList where nom equals to UPDATED_NOM
        defaultPersonShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nom is not null
        defaultPersonShouldBeFound("nom.specified=true");

        // Get all the personList where nom is null
        defaultPersonShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByNomContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nom contains DEFAULT_NOM
        defaultPersonShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the personList where nom contains UPDATED_NOM
        defaultPersonShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByNomNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nom does not contain DEFAULT_NOM
        defaultPersonShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the personList where nom does not contain UPDATED_NOM
        defaultPersonShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllPeopleByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prenom equals to DEFAULT_PRENOM
        defaultPersonShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the personList where prenom equals to UPDATED_PRENOM
        defaultPersonShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prenom not equals to DEFAULT_PRENOM
        defaultPersonShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the personList where prenom not equals to UPDATED_PRENOM
        defaultPersonShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultPersonShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the personList where prenom equals to UPDATED_PRENOM
        defaultPersonShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prenom is not null
        defaultPersonShouldBeFound("prenom.specified=true");

        // Get all the personList where prenom is null
        defaultPersonShouldNotBeFound("prenom.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByPrenomContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prenom contains DEFAULT_PRENOM
        defaultPersonShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the personList where prenom contains UPDATED_PRENOM
        defaultPersonShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllPeopleByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prenom does not contain DEFAULT_PRENOM
        defaultPersonShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the personList where prenom does not contain UPDATED_PRENOM
        defaultPersonShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }


    @Test
    @Transactional
    public void getAllPeopleByNumTeleIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where numTele equals to DEFAULT_NUM_TELE
        defaultPersonShouldBeFound("numTele.equals=" + DEFAULT_NUM_TELE);

        // Get all the personList where numTele equals to UPDATED_NUM_TELE
        defaultPersonShouldNotBeFound("numTele.equals=" + UPDATED_NUM_TELE);
    }

    @Test
    @Transactional
    public void getAllPeopleByNumTeleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where numTele not equals to DEFAULT_NUM_TELE
        defaultPersonShouldNotBeFound("numTele.notEquals=" + DEFAULT_NUM_TELE);

        // Get all the personList where numTele not equals to UPDATED_NUM_TELE
        defaultPersonShouldBeFound("numTele.notEquals=" + UPDATED_NUM_TELE);
    }

    @Test
    @Transactional
    public void getAllPeopleByNumTeleIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where numTele in DEFAULT_NUM_TELE or UPDATED_NUM_TELE
        defaultPersonShouldBeFound("numTele.in=" + DEFAULT_NUM_TELE + "," + UPDATED_NUM_TELE);

        // Get all the personList where numTele equals to UPDATED_NUM_TELE
        defaultPersonShouldNotBeFound("numTele.in=" + UPDATED_NUM_TELE);
    }

    @Test
    @Transactional
    public void getAllPeopleByNumTeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where numTele is not null
        defaultPersonShouldBeFound("numTele.specified=true");

        // Get all the personList where numTele is null
        defaultPersonShouldNotBeFound("numTele.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByNumTeleContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where numTele contains DEFAULT_NUM_TELE
        defaultPersonShouldBeFound("numTele.contains=" + DEFAULT_NUM_TELE);

        // Get all the personList where numTele contains UPDATED_NUM_TELE
        defaultPersonShouldNotBeFound("numTele.contains=" + UPDATED_NUM_TELE);
    }

    @Test
    @Transactional
    public void getAllPeopleByNumTeleNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where numTele does not contain DEFAULT_NUM_TELE
        defaultPersonShouldNotBeFound("numTele.doesNotContain=" + DEFAULT_NUM_TELE);

        // Get all the personList where numTele does not contain UPDATED_NUM_TELE
        defaultPersonShouldBeFound("numTele.doesNotContain=" + UPDATED_NUM_TELE);
    }


    @Test
    @Transactional
    public void getAllPeopleByeMailIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where eMail equals to DEFAULT_E_MAIL
        defaultPersonShouldBeFound("eMail.equals=" + DEFAULT_E_MAIL);

        // Get all the personList where eMail equals to UPDATED_E_MAIL
        defaultPersonShouldNotBeFound("eMail.equals=" + UPDATED_E_MAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByeMailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where eMail not equals to DEFAULT_E_MAIL
        defaultPersonShouldNotBeFound("eMail.notEquals=" + DEFAULT_E_MAIL);

        // Get all the personList where eMail not equals to UPDATED_E_MAIL
        defaultPersonShouldBeFound("eMail.notEquals=" + UPDATED_E_MAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByeMailIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where eMail in DEFAULT_E_MAIL or UPDATED_E_MAIL
        defaultPersonShouldBeFound("eMail.in=" + DEFAULT_E_MAIL + "," + UPDATED_E_MAIL);

        // Get all the personList where eMail equals to UPDATED_E_MAIL
        defaultPersonShouldNotBeFound("eMail.in=" + UPDATED_E_MAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByeMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where eMail is not null
        defaultPersonShouldBeFound("eMail.specified=true");

        // Get all the personList where eMail is null
        defaultPersonShouldNotBeFound("eMail.specified=false");
    }
                @Test
    @Transactional
    public void getAllPeopleByeMailContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where eMail contains DEFAULT_E_MAIL
        defaultPersonShouldBeFound("eMail.contains=" + DEFAULT_E_MAIL);

        // Get all the personList where eMail contains UPDATED_E_MAIL
        defaultPersonShouldNotBeFound("eMail.contains=" + UPDATED_E_MAIL);
    }

    @Test
    @Transactional
    public void getAllPeopleByeMailNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where eMail does not contain DEFAULT_E_MAIL
        defaultPersonShouldNotBeFound("eMail.doesNotContain=" + DEFAULT_E_MAIL);

        // Get all the personList where eMail does not contain UPDATED_E_MAIL
        defaultPersonShouldBeFound("eMail.doesNotContain=" + UPDATED_E_MAIL);
    }


    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance equals to DEFAULT_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.equals=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance equals to UPDATED_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.equals=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance not equals to DEFAULT_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.notEquals=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance not equals to UPDATED_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.notEquals=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance in DEFAULT_DATE_DE_NAISSANCE or UPDATED_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.in=" + DEFAULT_DATE_DE_NAISSANCE + "," + UPDATED_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance equals to UPDATED_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.in=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance is not null
        defaultPersonShouldBeFound("dateDeNaissance.specified=true");

        // Get all the personList where dateDeNaissance is null
        defaultPersonShouldNotBeFound("dateDeNaissance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance is greater than or equal to DEFAULT_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.greaterThanOrEqual=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance is greater than or equal to UPDATED_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.greaterThanOrEqual=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance is less than or equal to DEFAULT_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.lessThanOrEqual=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance is less than or equal to SMALLER_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.lessThanOrEqual=" + SMALLER_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance is less than DEFAULT_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.lessThan=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance is less than UPDATED_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.lessThan=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllPeopleByDateDeNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where dateDeNaissance is greater than DEFAULT_DATE_DE_NAISSANCE
        defaultPersonShouldNotBeFound("dateDeNaissance.greaterThan=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the personList where dateDeNaissance is greater than SMALLER_DATE_DE_NAISSANCE
        defaultPersonShouldBeFound("dateDeNaissance.greaterThan=" + SMALLER_DATE_DE_NAISSANCE);
    }


    @Test
    @Transactional
    public void getAllPeopleByCiviliteIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where civilite equals to DEFAULT_CIVILITE
        defaultPersonShouldBeFound("civilite.equals=" + DEFAULT_CIVILITE);

        // Get all the personList where civilite equals to UPDATED_CIVILITE
        defaultPersonShouldNotBeFound("civilite.equals=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    public void getAllPeopleByCiviliteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where civilite not equals to DEFAULT_CIVILITE
        defaultPersonShouldNotBeFound("civilite.notEquals=" + DEFAULT_CIVILITE);

        // Get all the personList where civilite not equals to UPDATED_CIVILITE
        defaultPersonShouldBeFound("civilite.notEquals=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    public void getAllPeopleByCiviliteIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where civilite in DEFAULT_CIVILITE or UPDATED_CIVILITE
        defaultPersonShouldBeFound("civilite.in=" + DEFAULT_CIVILITE + "," + UPDATED_CIVILITE);

        // Get all the personList where civilite equals to UPDATED_CIVILITE
        defaultPersonShouldNotBeFound("civilite.in=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    public void getAllPeopleByCiviliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where civilite is not null
        defaultPersonShouldBeFound("civilite.specified=true");

        // Get all the personList where civilite is null
        defaultPersonShouldNotBeFound("civilite.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByDocteurOrPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where docteurOrPatient equals to DEFAULT_DOCTEUR_OR_PATIENT
        defaultPersonShouldBeFound("docteurOrPatient.equals=" + DEFAULT_DOCTEUR_OR_PATIENT);

        // Get all the personList where docteurOrPatient equals to UPDATED_DOCTEUR_OR_PATIENT
        defaultPersonShouldNotBeFound("docteurOrPatient.equals=" + UPDATED_DOCTEUR_OR_PATIENT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDocteurOrPatientIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where docteurOrPatient not equals to DEFAULT_DOCTEUR_OR_PATIENT
        defaultPersonShouldNotBeFound("docteurOrPatient.notEquals=" + DEFAULT_DOCTEUR_OR_PATIENT);

        // Get all the personList where docteurOrPatient not equals to UPDATED_DOCTEUR_OR_PATIENT
        defaultPersonShouldBeFound("docteurOrPatient.notEquals=" + UPDATED_DOCTEUR_OR_PATIENT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDocteurOrPatientIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where docteurOrPatient in DEFAULT_DOCTEUR_OR_PATIENT or UPDATED_DOCTEUR_OR_PATIENT
        defaultPersonShouldBeFound("docteurOrPatient.in=" + DEFAULT_DOCTEUR_OR_PATIENT + "," + UPDATED_DOCTEUR_OR_PATIENT);

        // Get all the personList where docteurOrPatient equals to UPDATED_DOCTEUR_OR_PATIENT
        defaultPersonShouldNotBeFound("docteurOrPatient.in=" + UPDATED_DOCTEUR_OR_PATIENT);
    }

    @Test
    @Transactional
    public void getAllPeopleByDocteurOrPatientIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where docteurOrPatient is not null
        defaultPersonShouldBeFound("docteurOrPatient.specified=true");

        // Get all the personList where docteurOrPatient is null
        defaultPersonShouldNotBeFound("docteurOrPatient.specified=false");
    }

    @Test
    @Transactional
    public void getAllPeopleByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Address address = AddressResourceIT.createEntity(em);
        em.persist(address);
        em.flush();
        person.addAddress(address);
        personRepository.saveAndFlush(person);
        Long addressId = address.getId();

        // Get all the personList where address equals to addressId
        defaultPersonShouldBeFound("addressId.equals=" + addressId);

        // Get all the personList where address equals to addressId + 1
        defaultPersonShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }


    @Test
    @Transactional
    public void getAllPeopleByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = person.getUser();
        personRepository.saveAndFlush(person);
        Long userId = user.getId();

        // Get all the personList where user equals to userId
        defaultPersonShouldBeFound("userId.equals=" + userId);

        // Get all the personList where user equals to userId + 1
        defaultPersonShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllPeopleBySpecialtyIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Specialty specialty = SpecialtyResourceIT.createEntity(em);
        em.persist(specialty);
        em.flush();
        person.addSpecialty(specialty);
        personRepository.saveAndFlush(person);
        Long specialtyId = specialty.getId();

        // Get all the personList where specialty equals to specialtyId
        defaultPersonShouldBeFound("specialtyId.equals=" + specialtyId);

        // Get all the personList where specialty equals to specialtyId + 1
        defaultPersonShouldNotBeFound("specialtyId.equals=" + (specialtyId + 1));
    }


    @Test
    @Transactional
    public void getAllPeopleByAppointmentIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Appointment appointment = AppointmentResourceIT.createEntity(em);
        em.persist(appointment);
        em.flush();
        person.addAppointment(appointment);
        personRepository.saveAndFlush(person);
        Long appointmentId = appointment.getId();

        // Get all the personList where appointment equals to appointmentId
        defaultPersonShouldBeFound("appointmentId.equals=" + appointmentId);

        // Get all the personList where appointment equals to appointmentId + 1
        defaultPersonShouldNotBeFound("appointmentId.equals=" + (appointmentId + 1));
    }


    @Test
    @Transactional
    public void getAllPeopleByDisponibiltiesIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Appointment disponibilties = AppointmentResourceIT.createEntity(em);
        em.persist(disponibilties);
        em.flush();
        person.addDisponibilties(disponibilties);
        personRepository.saveAndFlush(person);
        Long disponibiltiesId = disponibilties.getId();

        // Get all the personList where disponibilties equals to disponibiltiesId
        defaultPersonShouldBeFound("disponibiltiesId.equals=" + disponibiltiesId);

        // Get all the personList where disponibilties equals to disponibiltiesId + 1
        defaultPersonShouldNotBeFound("disponibiltiesId.equals=" + (disponibiltiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].numTele").value(hasItem(DEFAULT_NUM_TELE)))
            .andExpect(jsonPath("$.[*].eMail").value(hasItem(DEFAULT_E_MAIL)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].docteurOrPatient").value(hasItem(DEFAULT_DOCTEUR_OR_PATIENT.toString())));

        // Check, that the count call also returns 1
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc.perform(get("/api/people?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc.perform(get("/api/people/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get("/api/people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numTele(UPDATED_NUM_TELE)
            .eMail(UPDATED_E_MAIL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .civilite(UPDATED_CIVILITE)
            .docteurOrPatient(UPDATED_DOCTEUR_OR_PATIENT);
        PersonDTO personDTO = personMapper.toDto(updatedPerson);

        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPerson.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPerson.getNumTele()).isEqualTo(UPDATED_NUM_TELE);
        assertThat(testPerson.geteMail()).isEqualTo(UPDATED_E_MAIL);
        assertThat(testPerson.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testPerson.getCivilite()).isEqualTo(UPDATED_CIVILITE);
        assertThat(testPerson.getDocteurOrPatient()).isEqualTo(UPDATED_DOCTEUR_OR_PATIENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc.perform(put("/api/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc.perform(delete("/api/people/{id}", person.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
