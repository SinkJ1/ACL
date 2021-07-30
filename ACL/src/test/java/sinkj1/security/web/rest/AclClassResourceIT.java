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
import sinkj1.security.domain.AclClass;
import sinkj1.security.repository.AclClassRepository;
import sinkj1.security.service.dto.AclClassDTO;
import sinkj1.security.service.mapper.AclClassMapper;

/**
 * Integration tests for the {@link AclClassResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclClassResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_ID_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_ID_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/acl-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclClassRepository aclClassRepository;

    @Autowired
    private AclClassMapper aclClassMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclClassMockMvc;

    private AclClass aclClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclClass createEntity(EntityManager em) {
        AclClass aclClass = new AclClass().className(DEFAULT_CLASS_NAME).classIdType(DEFAULT_CLASS_ID_TYPE);
        return aclClass;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclClass createUpdatedEntity(EntityManager em) {
        AclClass aclClass = new AclClass().className(UPDATED_CLASS_NAME).classIdType(UPDATED_CLASS_ID_TYPE);
        return aclClass;
    }

    @BeforeEach
    public void initTest() {
        aclClass = createEntity(em);
    }

    @Test
    @Transactional
    void createAclClass() throws Exception {
        int databaseSizeBeforeCreate = aclClassRepository.findAll().size();
        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);
        restAclClassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclClassDTO)))
            .andExpect(status().isCreated());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeCreate + 1);
        AclClass testAclClass = aclClassList.get(aclClassList.size() - 1);
        assertThat(testAclClass.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testAclClass.getClassIdType()).isEqualTo(DEFAULT_CLASS_ID_TYPE);
    }

    @Test
    @Transactional
    void createAclClassWithExistingId() throws Exception {
        // Create the AclClass with an existing ID
        aclClass.setId(1L);
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        int databaseSizeBeforeCreate = aclClassRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclClassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclClassDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aclClassRepository.findAll().size();
        // set the field null
        aclClass.setClassName(null);

        // Create the AclClass, which fails.
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        restAclClassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclClassDTO)))
            .andExpect(status().isBadRequest());

        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassIdTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = aclClassRepository.findAll().size();
        // set the field null
        aclClass.setClassIdType(null);

        // Create the AclClass, which fails.
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        restAclClassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclClassDTO)))
            .andExpect(status().isBadRequest());

        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAclClasses() throws Exception {
        // Initialize the database
        aclClassRepository.saveAndFlush(aclClass);

        // Get all the aclClassList
        restAclClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].classIdType").value(hasItem(DEFAULT_CLASS_ID_TYPE)));
    }

    @Test
    @Transactional
    void getAclClass() throws Exception {
        // Initialize the database
        aclClassRepository.saveAndFlush(aclClass);

        // Get the aclClass
        restAclClassMockMvc
            .perform(get(ENTITY_API_URL_ID, aclClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclClass.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME))
            .andExpect(jsonPath("$.classIdType").value(DEFAULT_CLASS_ID_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingAclClass() throws Exception {
        // Get the aclClass
        restAclClassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAclClass() throws Exception {
        // Initialize the database
        aclClassRepository.saveAndFlush(aclClass);

        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();

        // Update the aclClass
        AclClass updatedAclClass = aclClassRepository.findById(aclClass.getId()).get();
        // Disconnect from session so that the updates on updatedAclClass are not directly saved in db
        em.detach(updatedAclClass);
        updatedAclClass.className(UPDATED_CLASS_NAME).classIdType(UPDATED_CLASS_ID_TYPE);
        AclClassDTO aclClassDTO = aclClassMapper.toDto(updatedAclClass);

        restAclClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclClassDTO))
            )
            .andExpect(status().isOk());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
        AclClass testAclClass = aclClassList.get(aclClassList.size() - 1);
        assertThat(testAclClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testAclClass.getClassIdType()).isEqualTo(UPDATED_CLASS_ID_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAclClass() throws Exception {
        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();
        aclClass.setId(count.incrementAndGet());

        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclClass() throws Exception {
        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();
        aclClass.setId(count.incrementAndGet());

        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclClass() throws Exception {
        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();
        aclClass.setId(count.incrementAndGet());

        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclClassMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aclClassDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclClassWithPatch() throws Exception {
        // Initialize the database
        aclClassRepository.saveAndFlush(aclClass);

        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();

        // Update the aclClass using partial update
        AclClass partialUpdatedAclClass = new AclClass();
        partialUpdatedAclClass.setId(aclClass.getId());

        partialUpdatedAclClass.className(UPDATED_CLASS_NAME).classIdType(UPDATED_CLASS_ID_TYPE);

        restAclClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclClass))
            )
            .andExpect(status().isOk());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
        AclClass testAclClass = aclClassList.get(aclClassList.size() - 1);
        assertThat(testAclClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testAclClass.getClassIdType()).isEqualTo(UPDATED_CLASS_ID_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAclClassWithPatch() throws Exception {
        // Initialize the database
        aclClassRepository.saveAndFlush(aclClass);

        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();

        // Update the aclClass using partial update
        AclClass partialUpdatedAclClass = new AclClass();
        partialUpdatedAclClass.setId(aclClass.getId());

        partialUpdatedAclClass.className(UPDATED_CLASS_NAME).classIdType(UPDATED_CLASS_ID_TYPE);

        restAclClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclClass))
            )
            .andExpect(status().isOk());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
        AclClass testAclClass = aclClassList.get(aclClassList.size() - 1);
        assertThat(testAclClass.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testAclClass.getClassIdType()).isEqualTo(UPDATED_CLASS_ID_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAclClass() throws Exception {
        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();
        aclClass.setId(count.incrementAndGet());

        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclClassDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclClass() throws Exception {
        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();
        aclClass.setId(count.incrementAndGet());

        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclClass() throws Exception {
        int databaseSizeBeforeUpdate = aclClassRepository.findAll().size();
        aclClass.setId(count.incrementAndGet());

        // Create the AclClass
        AclClassDTO aclClassDTO = aclClassMapper.toDto(aclClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclClassMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aclClassDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclClass in the database
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclClass() throws Exception {
        // Initialize the database
        aclClassRepository.saveAndFlush(aclClass);

        int databaseSizeBeforeDelete = aclClassRepository.findAll().size();

        // Delete the aclClass
        restAclClassMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclClass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclClass> aclClassList = aclClassRepository.findAll();
        assertThat(aclClassList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
