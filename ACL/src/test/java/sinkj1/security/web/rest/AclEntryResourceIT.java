package sinkj1.security.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sinkj1.security.IntegrationTest;
import sinkj1.security.domain.AclEntry;
import sinkj1.security.repository.AclEntryRepository;
import sinkj1.security.service.dto.AclEntryDTO;
import sinkj1.security.service.mapper.AclEntryMapper;

/**
 * Integration tests for the {@link AclEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclEntryResourceIT {

    private static final Boolean DEFAULT_GRANTING = false;
    private static final Boolean UPDATED_GRANTING = true;

    private static final String ENTITY_API_URL = "/api/acl-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclEntryRepository aclEntryRepository;

    @Autowired
    private AclEntryMapper aclEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclEntryMockMvc;

    private AclEntry aclEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclEntry createEntity(EntityManager em) {
        AclEntry aclEntry = new AclEntry().granting(DEFAULT_GRANTING);
        return aclEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclEntry createUpdatedEntity(EntityManager em) {
        AclEntry aclEntry = new AclEntry().granting(UPDATED_GRANTING);
        return aclEntry;
    }

    @BeforeEach
    public void initTest() {
        aclEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createAclEntry() throws Exception {
        int databaseSizeBeforeCreate = aclEntryRepository.findAll().size();
        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);
        restAclEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeCreate + 1);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getGranting()).isEqualTo(DEFAULT_GRANTING);
    }

    @Test
    @Transactional
    void createAclEntryWithExistingId() throws Exception {
        // Create the AclEntry with an existing ID
        aclEntry.setId(1L);
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        int databaseSizeBeforeCreate = aclEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAclEntries() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        // Get all the aclEntryList
        restAclEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].granting").value(hasItem(DEFAULT_GRANTING.booleanValue())));
    }

    @Test
    @Transactional
    void getAclEntry() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        // Get the aclEntry
        restAclEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, aclEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclEntry.getId().intValue()))
            .andExpect(jsonPath("$.granting").value(DEFAULT_GRANTING.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAclEntry() throws Exception {
        // Get the aclEntry
        restAclEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclEntry() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();

        // Update the aclEntry
        AclEntry updatedAclEntry = aclEntryRepository.findById(aclEntry.getId()).get();
        // Disconnect from session so that the updates on updatedAclEntry are not directly saved in db
        em.detach(updatedAclEntry);
        updatedAclEntry.granting(UPDATED_GRANTING);
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(updatedAclEntry);

        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getGranting()).isEqualTo(UPDATED_GRANTING);
    }

    @Test
    @Transactional
    void putNonExistingAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclEntryWithPatch() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();

        // Update the aclEntry using partial update
        AclEntry partialUpdatedAclEntry = new AclEntry();
        partialUpdatedAclEntry.setId(aclEntry.getId());

        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclEntry))
            )
            .andExpect(status().isOk());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getGranting()).isEqualTo(DEFAULT_GRANTING);
    }

    @Test
    @Transactional
    void fullUpdateAclEntryWithPatch() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();

        // Update the aclEntry using partial update
        AclEntry partialUpdatedAclEntry = new AclEntry();
        partialUpdatedAclEntry.setId(aclEntry.getId());

        partialUpdatedAclEntry.granting(UPDATED_GRANTING);

        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclEntry))
            )
            .andExpect(status().isOk());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
        AclEntry testAclEntry = aclEntryList.get(aclEntryList.size() - 1);
        assertThat(testAclEntry.getGranting()).isEqualTo(UPDATED_GRANTING);
    }

    @Test
    @Transactional
    void patchNonExistingAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclEntry() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryRepository.findAll().size();
        aclEntry.setId(count.incrementAndGet());

        // Create the AclEntry
        AclEntryDTO aclEntryDTO = aclEntryMapper.toDto(aclEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aclEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclEntry in the database
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclEntry() throws Exception {
        // Initialize the database
        aclEntryRepository.saveAndFlush(aclEntry);

        int databaseSizeBeforeDelete = aclEntryRepository.findAll().size();

        // Delete the aclEntry
        restAclEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclEntry> aclEntryList = aclEntryRepository.findAll();
        assertThat(aclEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
