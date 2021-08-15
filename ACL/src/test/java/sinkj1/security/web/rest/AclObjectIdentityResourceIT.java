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
import sinkj1.security.domain.AclObjectIdentity;
import sinkj1.security.repository.AclObjectIdentityRepository;
import sinkj1.security.service.dto.AclObjectIdentityDTO;
import sinkj1.security.service.mapper.AclObjectIdentityMapper;

/**
 * Integration tests for the {@link AclObjectIdentityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclObjectIdentityResourceIT {

    private static final Integer DEFAULT_OBJECT_ID_IDENTITY = 1;
    private static final Integer UPDATED_OBJECT_ID_IDENTITY = 2;

    private static final Integer DEFAULT_PARENT_OBJECT = 1;
    private static final Integer UPDATED_PARENT_OBJECT = 2;

    private static final Integer DEFAULT_OWNER_SID = 1;
    private static final Integer UPDATED_OWNER_SID = 2;

    private static final Boolean DEFAULT_ENTRIES_INHERITING = false;
    private static final Boolean UPDATED_ENTRIES_INHERITING = true;

    private static final String ENTITY_API_URL = "/api/acl-object-identities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclObjectIdentityRepository aclObjectIdentityRepository;

    @Autowired
    private AclObjectIdentityMapper aclObjectIdentityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclObjectIdentityMockMvc;

    private AclObjectIdentity aclObjectIdentity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclObjectIdentity createEntity(EntityManager em) {
        AclObjectIdentity aclObjectIdentity = new AclObjectIdentity()
            .objectIdIdentity(DEFAULT_OBJECT_ID_IDENTITY)
            .parentObject(DEFAULT_PARENT_OBJECT)
            .ownerSid(DEFAULT_OWNER_SID)
            .entriesInheriting(DEFAULT_ENTRIES_INHERITING);
        return aclObjectIdentity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclObjectIdentity createUpdatedEntity(EntityManager em) {
        AclObjectIdentity aclObjectIdentity = new AclObjectIdentity()
            .objectIdIdentity(UPDATED_OBJECT_ID_IDENTITY)
            .parentObject(UPDATED_PARENT_OBJECT)
            .ownerSid(UPDATED_OWNER_SID)
            .entriesInheriting(UPDATED_ENTRIES_INHERITING);
        return aclObjectIdentity;
    }

    @BeforeEach
    public void initTest() {
        aclObjectIdentity = createEntity(em);
    }

    @Test
    @Transactional
    void createAclObjectIdentity() throws Exception {
        int databaseSizeBeforeCreate = aclObjectIdentityRepository.findAll().size();
        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);
        restAclObjectIdentityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeCreate + 1);
        AclObjectIdentity testAclObjectIdentity = aclObjectIdentityList.get(aclObjectIdentityList.size() - 1);
        assertThat(testAclObjectIdentity.getObjectIdIdentity()).isEqualTo(DEFAULT_OBJECT_ID_IDENTITY);
        assertThat(testAclObjectIdentity.getParentObject()).isEqualTo(DEFAULT_PARENT_OBJECT);
        assertThat(testAclObjectIdentity.getOwnerSid()).isEqualTo(DEFAULT_OWNER_SID);
        assertThat(testAclObjectIdentity.getEntriesInheriting()).isEqualTo(DEFAULT_ENTRIES_INHERITING);
    }

    @Test
    @Transactional
    void createAclObjectIdentityWithExistingId() throws Exception {
        // Create the AclObjectIdentity with an existing ID
        aclObjectIdentity.setId(1L);
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        int databaseSizeBeforeCreate = aclObjectIdentityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclObjectIdentityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkObjectIdIdentityIsRequired() throws Exception {
        int databaseSizeBeforeTest = aclObjectIdentityRepository.findAll().size();
        // set the field null
        aclObjectIdentity.setObjectIdIdentity(null);

        // Create the AclObjectIdentity, which fails.
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        restAclObjectIdentityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParentObjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = aclObjectIdentityRepository.findAll().size();
        // set the field null
        aclObjectIdentity.setParentObject(null);

        // Create the AclObjectIdentity, which fails.
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        restAclObjectIdentityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAclObjectIdentities() throws Exception {
        // Initialize the database
        aclObjectIdentityRepository.saveAndFlush(aclObjectIdentity);

        // Get all the aclObjectIdentityList
        restAclObjectIdentityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclObjectIdentity.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectIdIdentity").value(hasItem(DEFAULT_OBJECT_ID_IDENTITY)))
            .andExpect(jsonPath("$.[*].parentObject").value(hasItem(DEFAULT_PARENT_OBJECT)))
            .andExpect(jsonPath("$.[*].ownerSid").value(hasItem(DEFAULT_OWNER_SID)))
            .andExpect(jsonPath("$.[*].entriesInheriting").value(hasItem(DEFAULT_ENTRIES_INHERITING.booleanValue())));
    }

    @Test
    @Transactional
    void getAclObjectIdentity() throws Exception {
        // Initialize the database
        aclObjectIdentityRepository.saveAndFlush(aclObjectIdentity);

        // Get the aclObjectIdentity
        restAclObjectIdentityMockMvc
            .perform(get(ENTITY_API_URL_ID, aclObjectIdentity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclObjectIdentity.getId().intValue()))
            .andExpect(jsonPath("$.objectIdIdentity").value(DEFAULT_OBJECT_ID_IDENTITY))
            .andExpect(jsonPath("$.parentObject").value(DEFAULT_PARENT_OBJECT))
            .andExpect(jsonPath("$.ownerSid").value(DEFAULT_OWNER_SID))
            .andExpect(jsonPath("$.entriesInheriting").value(DEFAULT_ENTRIES_INHERITING.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAclObjectIdentity() throws Exception {
        // Get the aclObjectIdentity
        restAclObjectIdentityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclObjectIdentity() throws Exception {
        // Initialize the database
        aclObjectIdentityRepository.saveAndFlush(aclObjectIdentity);

        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();

        // Update the aclObjectIdentity
        AclObjectIdentity updatedAclObjectIdentity = aclObjectIdentityRepository.findById(aclObjectIdentity.getId()).get();
        // Disconnect from session so that the updates on updatedAclObjectIdentity are not directly saved in db
        em.detach(updatedAclObjectIdentity);
        updatedAclObjectIdentity
            .objectIdIdentity(UPDATED_OBJECT_ID_IDENTITY)
            .parentObject(UPDATED_PARENT_OBJECT)
            .ownerSid(UPDATED_OWNER_SID)
            .entriesInheriting(UPDATED_ENTRIES_INHERITING);
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(updatedAclObjectIdentity);

        restAclObjectIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclObjectIdentityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isOk());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
        AclObjectIdentity testAclObjectIdentity = aclObjectIdentityList.get(aclObjectIdentityList.size() - 1);
        assertThat(testAclObjectIdentity.getObjectIdIdentity()).isEqualTo(UPDATED_OBJECT_ID_IDENTITY);
        assertThat(testAclObjectIdentity.getParentObject()).isEqualTo(UPDATED_PARENT_OBJECT);
        assertThat(testAclObjectIdentity.getOwnerSid()).isEqualTo(UPDATED_OWNER_SID);
        assertThat(testAclObjectIdentity.getEntriesInheriting()).isEqualTo(UPDATED_ENTRIES_INHERITING);
    }

    @Test
    @Transactional
    void putNonExistingAclObjectIdentity() throws Exception {
        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();
        aclObjectIdentity.setId(count.incrementAndGet());

        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclObjectIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclObjectIdentityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclObjectIdentity() throws Exception {
        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();
        aclObjectIdentity.setId(count.incrementAndGet());

        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclObjectIdentityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclObjectIdentity() throws Exception {
        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();
        aclObjectIdentity.setId(count.incrementAndGet());

        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclObjectIdentityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclObjectIdentityWithPatch() throws Exception {
        // Initialize the database
        aclObjectIdentityRepository.saveAndFlush(aclObjectIdentity);

        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();

        // Update the aclObjectIdentity using partial update
        AclObjectIdentity partialUpdatedAclObjectIdentity = new AclObjectIdentity();
        partialUpdatedAclObjectIdentity.setId(aclObjectIdentity.getId());

        partialUpdatedAclObjectIdentity.ownerSid(UPDATED_OWNER_SID);

        restAclObjectIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclObjectIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclObjectIdentity))
            )
            .andExpect(status().isOk());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
        AclObjectIdentity testAclObjectIdentity = aclObjectIdentityList.get(aclObjectIdentityList.size() - 1);
        assertThat(testAclObjectIdentity.getObjectIdIdentity()).isEqualTo(DEFAULT_OBJECT_ID_IDENTITY);
        assertThat(testAclObjectIdentity.getParentObject()).isEqualTo(DEFAULT_PARENT_OBJECT);
        assertThat(testAclObjectIdentity.getOwnerSid()).isEqualTo(UPDATED_OWNER_SID);
        assertThat(testAclObjectIdentity.getEntriesInheriting()).isEqualTo(DEFAULT_ENTRIES_INHERITING);
    }

    @Test
    @Transactional
    void fullUpdateAclObjectIdentityWithPatch() throws Exception {
        // Initialize the database
        aclObjectIdentityRepository.saveAndFlush(aclObjectIdentity);

        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();

        // Update the aclObjectIdentity using partial update
        AclObjectIdentity partialUpdatedAclObjectIdentity = new AclObjectIdentity();
        partialUpdatedAclObjectIdentity.setId(aclObjectIdentity.getId());

        partialUpdatedAclObjectIdentity
            .objectIdIdentity(UPDATED_OBJECT_ID_IDENTITY)
            .parentObject(UPDATED_PARENT_OBJECT)
            .ownerSid(UPDATED_OWNER_SID)
            .entriesInheriting(UPDATED_ENTRIES_INHERITING);

        restAclObjectIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclObjectIdentity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclObjectIdentity))
            )
            .andExpect(status().isOk());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
        AclObjectIdentity testAclObjectIdentity = aclObjectIdentityList.get(aclObjectIdentityList.size() - 1);
        assertThat(testAclObjectIdentity.getObjectIdIdentity()).isEqualTo(UPDATED_OBJECT_ID_IDENTITY);
        assertThat(testAclObjectIdentity.getParentObject()).isEqualTo(UPDATED_PARENT_OBJECT);
        assertThat(testAclObjectIdentity.getOwnerSid()).isEqualTo(UPDATED_OWNER_SID);
        assertThat(testAclObjectIdentity.getEntriesInheriting()).isEqualTo(UPDATED_ENTRIES_INHERITING);
    }

    @Test
    @Transactional
    void patchNonExistingAclObjectIdentity() throws Exception {
        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();
        aclObjectIdentity.setId(count.incrementAndGet());

        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclObjectIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclObjectIdentityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclObjectIdentity() throws Exception {
        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();
        aclObjectIdentity.setId(count.incrementAndGet());

        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclObjectIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclObjectIdentity() throws Exception {
        int databaseSizeBeforeUpdate = aclObjectIdentityRepository.findAll().size();
        aclObjectIdentity.setId(count.incrementAndGet());

        // Create the AclObjectIdentity
        AclObjectIdentityDTO aclObjectIdentityDTO = aclObjectIdentityMapper.toDto(aclObjectIdentity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclObjectIdentityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclObjectIdentityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclObjectIdentity in the database
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclObjectIdentity() throws Exception {
        // Initialize the database
        aclObjectIdentityRepository.saveAndFlush(aclObjectIdentity);

        int databaseSizeBeforeDelete = aclObjectIdentityRepository.findAll().size();

        // Delete the aclObjectIdentity
        restAclObjectIdentityMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclObjectIdentity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclObjectIdentity> aclObjectIdentityList = aclObjectIdentityRepository.findAll();
        assertThat(aclObjectIdentityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
