package sinkj1.security.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sinkj1.security.repository.AclClassRepository;
import sinkj1.security.service.AclClassService;
import sinkj1.security.service.dto.AclClassDTO;
import sinkj1.security.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sinkj1.security.domain.AclClass}.
 */
@RestController
@RequestMapping("/api")
public class AclClassResource {

    private final Logger log = LoggerFactory.getLogger(AclClassResource.class);

    private static final String ENTITY_NAME = "aclClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclClassService aclClassService;

    private final DataSource dataSource;

    private final AclClassRepository aclClassRepository;

    public AclClassResource(AclClassService aclClassService, DataSource dataSource, AclClassRepository aclClassRepository) {
        this.aclClassService = aclClassService;
        this.dataSource = dataSource;
        this.aclClassRepository = aclClassRepository;
    }

    /**
     * {@code POST  /acl-classes} : Create a new aclClass.
     *
     * @param aclClassDTO the aclClassDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclClassDTO, or with status {@code 400 (Bad Request)} if the aclClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-classes")
    public ResponseEntity<AclClassDTO> createAclClass(@Valid @RequestBody AclClassDTO aclClassDTO) throws URISyntaxException {
        log.debug("REST request to save AclClass : {}", aclClassDTO);
        if (aclClassDTO.getId() != null) {
            throw new BadRequestAlertException("A new aclClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclClassDTO result = aclClassService.save(aclClassDTO);
        return ResponseEntity
            .created(new URI("/api/acl-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-classes/:id} : Updates an existing aclClass.
     *
     * @param id the id of the aclClassDTO to save.
     * @param aclClassDTO the aclClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclClassDTO,
     * or with status {@code 400 (Bad Request)} if the aclClassDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-classes/{id}")
    public ResponseEntity<AclClassDTO> updateAclClass(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AclClassDTO aclClassDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AclClass : {}, {}", id, aclClassDTO);
        if (aclClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclClassDTO result = aclClassService.save(aclClassDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclClassDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-classes/:id} : Partial updates given fields of an existing aclClass, field will ignore if it is null
     *
     * @param id the id of the aclClassDTO to save.
     * @param aclClassDTO the aclClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclClassDTO,
     * or with status {@code 400 (Bad Request)} if the aclClassDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aclClassDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-classes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AclClassDTO> partialUpdateAclClass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AclClassDTO aclClassDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclClass partially : {}, {}", id, aclClassDTO);
        if (aclClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclClassDTO> result = aclClassService.partialUpdate(aclClassDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclClassDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-classes} : get all the aclClasses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclClasses in body.
     */
    @GetMapping("/acl-classes")
    public ResponseEntity<List<AclClassDTO>> getAllAclClasses(Pageable pageable) {
        log.debug("REST request to get a page of AclClasses");
        Page<AclClassDTO> page = aclClassService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acl-classes/:id} : get the "id" aclClass.
     *
     * @param id the id of the aclClassDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclClassDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-classes/{id}")
    public ResponseEntity<AclClassDTO> getAclClass(@PathVariable Long id) {
        log.debug("REST request to get AclClass : {}", id);
        Optional<AclClassDTO> aclClassDTO = aclClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclClassDTO);
    }

    /**
     * {@code DELETE  /acl-classes/:id} : delete the "id" aclClass.
     *
     * @param id the id of the aclClassDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-classes/{id}")
    public ResponseEntity<Void> deleteAclClass(@PathVariable Long id) {
        log.debug("REST request to delete AclClass : {}", id);
        aclClassService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
