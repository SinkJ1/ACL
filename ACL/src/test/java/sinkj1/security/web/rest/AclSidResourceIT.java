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
import sinkj1.security.domain.AclSid;
import sinkj1.security.repository.AclSidRepository;
import sinkj1.security.service.dto.AclSidDTO;
import sinkj1.security.service.mapper.AclSidMapper;

/**
 * Integration tests for the {@link AclSidResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclSidResourceIT {

    private static final String DEFAULT_SID = "AAAAAAAAAA";
    private static final String UPDATED_SID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acl-sids";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclSidRepository aclSidRepository;

    @Autowired
    private AclSidMapper aclSidMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclSidMockMvc;

    private AclSid aclSid;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclSid createEntity(EntityManager em) {
        AclSid aclSid = new AclSid().sid(DEFAULT_SID);
        return aclSid;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclSid createUpdatedEntity(EntityManager em) {
        AclSid aclSid = new AclSid().sid(UPDATED_SID);
        return aclSid;
    }

    @BeforeEach
    public void initTest() {
        aclSid = createEntity(em);
    }

    @Test
    @Transactional
    void createAclSid() throws Exception {
        int databaseSizeBeforeCreate = aclSidRepository.findAll().size();
        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);
        restAclSidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSidDTO)))
            .andExpect(status().isCreated());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeCreate + 1);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSid()).isEqualTo(DEFAULT_SID);
    }

    @Test
    @Transactional
    void createAclSidWithExistingId() throws Exception {
        // Create the AclSid with an existing ID
        aclSid.setId(1L);
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        int databaseSizeBeforeCreate = aclSidRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclSidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSidDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSidIsRequired() throws Exception {
        int databaseSizeBeforeTest = aclSidRepository.findAll().size();
        // set the field null
        aclSid.setSid(null);

        // Create the AclSid, which fails.
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        restAclSidMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSidDTO)))
            .andExpect(status().isBadRequest());

        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAclSids() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        // Get all the aclSidList
        restAclSidMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclSid.getId().intValue())))
            .andExpect(jsonPath("$.[*].sid").value(hasItem(DEFAULT_SID)));
    }

    @Test
    @Transactional
    void getAclSid() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        // Get the aclSid
        restAclSidMockMvc
            .perform(get(ENTITY_API_URL_ID, aclSid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclSid.getId().intValue()))
            .andExpect(jsonPath("$.sid").value(DEFAULT_SID));
    }

    @Test
    @Transactional
    void getNonExistingAclSid() throws Exception {
        // Get the aclSid
        restAclSidMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclSid() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();

        // Update the aclSid
        AclSid updatedAclSid = aclSidRepository.findById(aclSid.getId()).get();
        // Disconnect from session so that the updates on updatedAclSid are not directly saved in db
        em.detach(updatedAclSid);
        updatedAclSid.sid(UPDATED_SID);
        AclSidDTO aclSidDTO = aclSidMapper.toDto(updatedAclSid);

        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclSidDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclSidDTO))
            )
            .andExpect(status().isOk());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSid()).isEqualTo(UPDATED_SID);
    }

    @Test
    @Transactional
    void putNonExistingAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclSidDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclSidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclSidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclSidDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclSidWithPatch() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();

        // Update the aclSid using partial update
        AclSid partialUpdatedAclSid = new AclSid();
        partialUpdatedAclSid.setId(aclSid.getId());

        partialUpdatedAclSid.sid(UPDATED_SID);

        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclSid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclSid))
            )
            .andExpect(status().isOk());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSid()).isEqualTo(UPDATED_SID);
    }

    @Test
    @Transactional
    void fullUpdateAclSidWithPatch() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();

        // Update the aclSid using partial update
        AclSid partialUpdatedAclSid = new AclSid();
        partialUpdatedAclSid.setId(aclSid.getId());

        partialUpdatedAclSid.sid(UPDATED_SID);

        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclSid.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclSid))
            )
            .andExpect(status().isOk());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
        AclSid testAclSid = aclSidList.get(aclSidList.size() - 1);
        assertThat(testAclSid.getSid()).isEqualTo(UPDATED_SID);
    }

    @Test
    @Transactional
    void patchNonExistingAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclSidDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclSidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclSidDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclSid() throws Exception {
        int databaseSizeBeforeUpdate = aclSidRepository.findAll().size();
        aclSid.setId(count.incrementAndGet());

        // Create the AclSid
        AclSidDTO aclSidDTO = aclSidMapper.toDto(aclSid);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclSidMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aclSidDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclSid in the database
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclSid() throws Exception {
        // Initialize the database
        aclSidRepository.saveAndFlush(aclSid);

        int databaseSizeBeforeDelete = aclSidRepository.findAll().size();

        // Delete the aclSid
        restAclSidMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclSid.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclSid> aclSidList = aclSidRepository.findAll();
        assertThat(aclSidList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
