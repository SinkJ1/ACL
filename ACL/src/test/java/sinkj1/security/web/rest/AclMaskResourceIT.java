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
import sinkj1.security.domain.AclMask;
import sinkj1.security.repository.AclMaskRepository;
import sinkj1.security.service.dto.AclMaskDTO;
import sinkj1.security.service.mapper.AclMaskMapper;

/**
 * Integration tests for the {@link AclMaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclMaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acl-masks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclMaskRepository aclMaskRepository;

    @Autowired
    private AclMaskMapper aclMaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclMaskMockMvc;

    private AclMask aclMask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclMask createEntity(EntityManager em) {
        AclMask aclMask = new AclMask().name(DEFAULT_NAME);
        return aclMask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclMask createUpdatedEntity(EntityManager em) {
        AclMask aclMask = new AclMask().name(UPDATED_NAME);
        return aclMask;
    }

    @BeforeEach
    public void initTest() {
        aclMask = createEntity(em);
    }

    @Test
    @Transactional
    void createAclMask() throws Exception {
        int databaseSizeBeforeCreate = aclMaskRepository.findAll().size();
        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);
        restAclMaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclMaskDTO)))
            .andExpect(status().isCreated());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeCreate + 1);
        AclMask testAclMask = aclMaskList.get(aclMaskList.size() - 1);
        assertThat(testAclMask.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createAclMaskWithExistingId() throws Exception {
        // Create the AclMask with an existing ID
        aclMask.setId(1L);
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        int databaseSizeBeforeCreate = aclMaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclMaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclMaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAclMasks() throws Exception {
        // Initialize the database
        aclMaskRepository.saveAndFlush(aclMask);

        // Get all the aclMaskList
        restAclMaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclMask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAclMask() throws Exception {
        // Initialize the database
        aclMaskRepository.saveAndFlush(aclMask);

        // Get the aclMask
        restAclMaskMockMvc
            .perform(get(ENTITY_API_URL_ID, aclMask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclMask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAclMask() throws Exception {
        // Get the aclMask
        restAclMaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclMask() throws Exception {
        // Initialize the database
        aclMaskRepository.saveAndFlush(aclMask);

        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();

        // Update the aclMask
        AclMask updatedAclMask = aclMaskRepository.findById(aclMask.getId()).get();
        // Disconnect from session so that the updates on updatedAclMask are not directly saved in db
        em.detach(updatedAclMask);
        updatedAclMask.name(UPDATED_NAME);
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(updatedAclMask);

        restAclMaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclMaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclMaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
        AclMask testAclMask = aclMaskList.get(aclMaskList.size() - 1);
        assertThat(testAclMask.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAclMask() throws Exception {
        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();
        aclMask.setId(count.incrementAndGet());

        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclMaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclMaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclMaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclMask() throws Exception {
        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();
        aclMask.setId(count.incrementAndGet());

        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclMaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclMaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclMask() throws Exception {
        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();
        aclMask.setId(count.incrementAndGet());

        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclMaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclMaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclMaskWithPatch() throws Exception {
        // Initialize the database
        aclMaskRepository.saveAndFlush(aclMask);

        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();

        // Update the aclMask using partial update
        AclMask partialUpdatedAclMask = new AclMask();
        partialUpdatedAclMask.setId(aclMask.getId());

        restAclMaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclMask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclMask))
            )
            .andExpect(status().isOk());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
        AclMask testAclMask = aclMaskList.get(aclMaskList.size() - 1);
        assertThat(testAclMask.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAclMaskWithPatch() throws Exception {
        // Initialize the database
        aclMaskRepository.saveAndFlush(aclMask);

        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();

        // Update the aclMask using partial update
        AclMask partialUpdatedAclMask = new AclMask();
        partialUpdatedAclMask.setId(aclMask.getId());

        partialUpdatedAclMask.name(UPDATED_NAME);

        restAclMaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclMask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclMask))
            )
            .andExpect(status().isOk());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
        AclMask testAclMask = aclMaskList.get(aclMaskList.size() - 1);
        assertThat(testAclMask.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAclMask() throws Exception {
        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();
        aclMask.setId(count.incrementAndGet());

        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclMaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclMaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclMaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclMask() throws Exception {
        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();
        aclMask.setId(count.incrementAndGet());

        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclMaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclMaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclMask() throws Exception {
        int databaseSizeBeforeUpdate = aclMaskRepository.findAll().size();
        aclMask.setId(count.incrementAndGet());

        // Create the AclMask
        AclMaskDTO aclMaskDTO = aclMaskMapper.toDto(aclMask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclMaskMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aclMaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclMask in the database
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclMask() throws Exception {
        // Initialize the database
        aclMaskRepository.saveAndFlush(aclMask);

        int databaseSizeBeforeDelete = aclMaskRepository.findAll().size();

        // Delete the aclMask
        restAclMaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclMask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclMask> aclMaskList = aclMaskRepository.findAll();
        assertThat(aclMaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
