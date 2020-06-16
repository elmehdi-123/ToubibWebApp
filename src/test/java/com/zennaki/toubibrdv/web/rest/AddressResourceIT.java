package com.zennaki.toubibrdv.web.rest;

import com.zennaki.toubibrdv.ToubibRdvWebApp;
import com.zennaki.toubibrdv.domain.Address;
import com.zennaki.toubibrdv.repository.AddressRepository;
import com.zennaki.toubibrdv.service.AddressService;
import com.zennaki.toubibrdv.service.dto.AddressDTO;
import com.zennaki.toubibrdv.service.mapper.AddressMapper;
import com.zennaki.toubibrdv.service.dto.AddressCriteria;
import com.zennaki.toubibrdv.service.AddressQueryService;

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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@SpringBootTest(classes = ToubibRdvWebApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AddressResourceIT {

    private static final String DEFAULT_NOM_RUE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RUE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUN = "AAAAAAAAAA";
    private static final String UPDATED_COMMUN = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_WILLAYA = "AAAAAAAAAA";
    private static final String UPDATED_WILLAYA = "BBBBBBBBBB";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .nomRue(DEFAULT_NOM_RUE)
            .ville(DEFAULT_VILLE)
            .commun(DEFAULT_COMMUN)
            .codePostal(DEFAULT_CODE_POSTAL)
            .willaya(DEFAULT_WILLAYA);
        return address;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .nomRue(UPDATED_NOM_RUE)
            .ville(UPDATED_VILLE)
            .commun(UPDATED_COMMUN)
            .codePostal(UPDATED_CODE_POSTAL)
            .willaya(UPDATED_WILLAYA);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getNomRue()).isEqualTo(DEFAULT_NOM_RUE);
        assertThat(testAddress.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testAddress.getCommun()).isEqualTo(DEFAULT_COMMUN);
        assertThat(testAddress.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testAddress.getWillaya()).isEqualTo(DEFAULT_WILLAYA);
    }

    @Test
    @Transactional
    public void createAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address with an existing ID
        address.setId(1L);
        AddressDTO addressDTO = addressMapper.toDto(address);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomRue").value(hasItem(DEFAULT_NOM_RUE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].commun").value(hasItem(DEFAULT_COMMUN)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].willaya").value(hasItem(DEFAULT_WILLAYA)));
    }
    
    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.nomRue").value(DEFAULT_NOM_RUE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.commun").value(DEFAULT_COMMUN))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.willaya").value(DEFAULT_WILLAYA));
    }


    @Test
    @Transactional
    public void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        Long id = address.getId();

        defaultAddressShouldBeFound("id.equals=" + id);
        defaultAddressShouldNotBeFound("id.notEquals=" + id);

        defaultAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAddressesByNomRueIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where nomRue equals to DEFAULT_NOM_RUE
        defaultAddressShouldBeFound("nomRue.equals=" + DEFAULT_NOM_RUE);

        // Get all the addressList where nomRue equals to UPDATED_NOM_RUE
        defaultAddressShouldNotBeFound("nomRue.equals=" + UPDATED_NOM_RUE);
    }

    @Test
    @Transactional
    public void getAllAddressesByNomRueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where nomRue not equals to DEFAULT_NOM_RUE
        defaultAddressShouldNotBeFound("nomRue.notEquals=" + DEFAULT_NOM_RUE);

        // Get all the addressList where nomRue not equals to UPDATED_NOM_RUE
        defaultAddressShouldBeFound("nomRue.notEquals=" + UPDATED_NOM_RUE);
    }

    @Test
    @Transactional
    public void getAllAddressesByNomRueIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where nomRue in DEFAULT_NOM_RUE or UPDATED_NOM_RUE
        defaultAddressShouldBeFound("nomRue.in=" + DEFAULT_NOM_RUE + "," + UPDATED_NOM_RUE);

        // Get all the addressList where nomRue equals to UPDATED_NOM_RUE
        defaultAddressShouldNotBeFound("nomRue.in=" + UPDATED_NOM_RUE);
    }

    @Test
    @Transactional
    public void getAllAddressesByNomRueIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where nomRue is not null
        defaultAddressShouldBeFound("nomRue.specified=true");

        // Get all the addressList where nomRue is null
        defaultAddressShouldNotBeFound("nomRue.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByNomRueContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where nomRue contains DEFAULT_NOM_RUE
        defaultAddressShouldBeFound("nomRue.contains=" + DEFAULT_NOM_RUE);

        // Get all the addressList where nomRue contains UPDATED_NOM_RUE
        defaultAddressShouldNotBeFound("nomRue.contains=" + UPDATED_NOM_RUE);
    }

    @Test
    @Transactional
    public void getAllAddressesByNomRueNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where nomRue does not contain DEFAULT_NOM_RUE
        defaultAddressShouldNotBeFound("nomRue.doesNotContain=" + DEFAULT_NOM_RUE);

        // Get all the addressList where nomRue does not contain UPDATED_NOM_RUE
        defaultAddressShouldBeFound("nomRue.doesNotContain=" + UPDATED_NOM_RUE);
    }


    @Test
    @Transactional
    public void getAllAddressesByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where ville equals to DEFAULT_VILLE
        defaultAddressShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the addressList where ville equals to UPDATED_VILLE
        defaultAddressShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllAddressesByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where ville not equals to DEFAULT_VILLE
        defaultAddressShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the addressList where ville not equals to UPDATED_VILLE
        defaultAddressShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllAddressesByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultAddressShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the addressList where ville equals to UPDATED_VILLE
        defaultAddressShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllAddressesByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where ville is not null
        defaultAddressShouldBeFound("ville.specified=true");

        // Get all the addressList where ville is null
        defaultAddressShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByVilleContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where ville contains DEFAULT_VILLE
        defaultAddressShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the addressList where ville contains UPDATED_VILLE
        defaultAddressShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllAddressesByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where ville does not contain DEFAULT_VILLE
        defaultAddressShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the addressList where ville does not contain UPDATED_VILLE
        defaultAddressShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllAddressesByCommunIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where commun equals to DEFAULT_COMMUN
        defaultAddressShouldBeFound("commun.equals=" + DEFAULT_COMMUN);

        // Get all the addressList where commun equals to UPDATED_COMMUN
        defaultAddressShouldNotBeFound("commun.equals=" + UPDATED_COMMUN);
    }

    @Test
    @Transactional
    public void getAllAddressesByCommunIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where commun not equals to DEFAULT_COMMUN
        defaultAddressShouldNotBeFound("commun.notEquals=" + DEFAULT_COMMUN);

        // Get all the addressList where commun not equals to UPDATED_COMMUN
        defaultAddressShouldBeFound("commun.notEquals=" + UPDATED_COMMUN);
    }

    @Test
    @Transactional
    public void getAllAddressesByCommunIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where commun in DEFAULT_COMMUN or UPDATED_COMMUN
        defaultAddressShouldBeFound("commun.in=" + DEFAULT_COMMUN + "," + UPDATED_COMMUN);

        // Get all the addressList where commun equals to UPDATED_COMMUN
        defaultAddressShouldNotBeFound("commun.in=" + UPDATED_COMMUN);
    }

    @Test
    @Transactional
    public void getAllAddressesByCommunIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where commun is not null
        defaultAddressShouldBeFound("commun.specified=true");

        // Get all the addressList where commun is null
        defaultAddressShouldNotBeFound("commun.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByCommunContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where commun contains DEFAULT_COMMUN
        defaultAddressShouldBeFound("commun.contains=" + DEFAULT_COMMUN);

        // Get all the addressList where commun contains UPDATED_COMMUN
        defaultAddressShouldNotBeFound("commun.contains=" + UPDATED_COMMUN);
    }

    @Test
    @Transactional
    public void getAllAddressesByCommunNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where commun does not contain DEFAULT_COMMUN
        defaultAddressShouldNotBeFound("commun.doesNotContain=" + DEFAULT_COMMUN);

        // Get all the addressList where commun does not contain UPDATED_COMMUN
        defaultAddressShouldBeFound("commun.doesNotContain=" + UPDATED_COMMUN);
    }


    @Test
    @Transactional
    public void getAllAddressesByCodePostalIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where codePostal equals to DEFAULT_CODE_POSTAL
        defaultAddressShouldBeFound("codePostal.equals=" + DEFAULT_CODE_POSTAL);

        // Get all the addressList where codePostal equals to UPDATED_CODE_POSTAL
        defaultAddressShouldNotBeFound("codePostal.equals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllAddressesByCodePostalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where codePostal not equals to DEFAULT_CODE_POSTAL
        defaultAddressShouldNotBeFound("codePostal.notEquals=" + DEFAULT_CODE_POSTAL);

        // Get all the addressList where codePostal not equals to UPDATED_CODE_POSTAL
        defaultAddressShouldBeFound("codePostal.notEquals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllAddressesByCodePostalIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where codePostal in DEFAULT_CODE_POSTAL or UPDATED_CODE_POSTAL
        defaultAddressShouldBeFound("codePostal.in=" + DEFAULT_CODE_POSTAL + "," + UPDATED_CODE_POSTAL);

        // Get all the addressList where codePostal equals to UPDATED_CODE_POSTAL
        defaultAddressShouldNotBeFound("codePostal.in=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllAddressesByCodePostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where codePostal is not null
        defaultAddressShouldBeFound("codePostal.specified=true");

        // Get all the addressList where codePostal is null
        defaultAddressShouldNotBeFound("codePostal.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByCodePostalContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where codePostal contains DEFAULT_CODE_POSTAL
        defaultAddressShouldBeFound("codePostal.contains=" + DEFAULT_CODE_POSTAL);

        // Get all the addressList where codePostal contains UPDATED_CODE_POSTAL
        defaultAddressShouldNotBeFound("codePostal.contains=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllAddressesByCodePostalNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where codePostal does not contain DEFAULT_CODE_POSTAL
        defaultAddressShouldNotBeFound("codePostal.doesNotContain=" + DEFAULT_CODE_POSTAL);

        // Get all the addressList where codePostal does not contain UPDATED_CODE_POSTAL
        defaultAddressShouldBeFound("codePostal.doesNotContain=" + UPDATED_CODE_POSTAL);
    }


    @Test
    @Transactional
    public void getAllAddressesByWillayaIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where willaya equals to DEFAULT_WILLAYA
        defaultAddressShouldBeFound("willaya.equals=" + DEFAULT_WILLAYA);

        // Get all the addressList where willaya equals to UPDATED_WILLAYA
        defaultAddressShouldNotBeFound("willaya.equals=" + UPDATED_WILLAYA);
    }

    @Test
    @Transactional
    public void getAllAddressesByWillayaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where willaya not equals to DEFAULT_WILLAYA
        defaultAddressShouldNotBeFound("willaya.notEquals=" + DEFAULT_WILLAYA);

        // Get all the addressList where willaya not equals to UPDATED_WILLAYA
        defaultAddressShouldBeFound("willaya.notEquals=" + UPDATED_WILLAYA);
    }

    @Test
    @Transactional
    public void getAllAddressesByWillayaIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where willaya in DEFAULT_WILLAYA or UPDATED_WILLAYA
        defaultAddressShouldBeFound("willaya.in=" + DEFAULT_WILLAYA + "," + UPDATED_WILLAYA);

        // Get all the addressList where willaya equals to UPDATED_WILLAYA
        defaultAddressShouldNotBeFound("willaya.in=" + UPDATED_WILLAYA);
    }

    @Test
    @Transactional
    public void getAllAddressesByWillayaIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where willaya is not null
        defaultAddressShouldBeFound("willaya.specified=true");

        // Get all the addressList where willaya is null
        defaultAddressShouldNotBeFound("willaya.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByWillayaContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where willaya contains DEFAULT_WILLAYA
        defaultAddressShouldBeFound("willaya.contains=" + DEFAULT_WILLAYA);

        // Get all the addressList where willaya contains UPDATED_WILLAYA
        defaultAddressShouldNotBeFound("willaya.contains=" + UPDATED_WILLAYA);
    }

    @Test
    @Transactional
    public void getAllAddressesByWillayaNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where willaya does not contain DEFAULT_WILLAYA
        defaultAddressShouldNotBeFound("willaya.doesNotContain=" + DEFAULT_WILLAYA);

        // Get all the addressList where willaya does not contain UPDATED_WILLAYA
        defaultAddressShouldBeFound("willaya.doesNotContain=" + UPDATED_WILLAYA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomRue").value(hasItem(DEFAULT_NOM_RUE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].commun").value(hasItem(DEFAULT_COMMUN)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].willaya").value(hasItem(DEFAULT_WILLAYA)));

        // Check, that the count call also returns 1
        restAddressMockMvc.perform(get("/api/addresses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc.perform(get("/api/addresses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .nomRue(UPDATED_NOM_RUE)
            .ville(UPDATED_VILLE)
            .commun(UPDATED_COMMUN)
            .codePostal(UPDATED_CODE_POSTAL)
            .willaya(UPDATED_WILLAYA);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getNomRue()).isEqualTo(UPDATED_NOM_RUE);
        assertThat(testAddress.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAddress.getCommun()).isEqualTo(UPDATED_COMMUN);
        assertThat(testAddress.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testAddress.getWillaya()).isEqualTo(UPDATED_WILLAYA);
    }

    @Test
    @Transactional
    public void updateNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
