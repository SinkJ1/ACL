package sinkj1.security.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sinkj1.security.repository.AclObjectIdentityRepository;
import sinkj1.security.service.AclObjectIdentityService;
import sinkj1.security.service.dto.AclObjectIdentityDTO;
import sinkj1.security.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sinkj1.security.domain.AclObjectIdentity}.
 */
@RestController
@RequestMapping("/api")
public class AclObjectIdentityResource {

    private final Logger log = LoggerFactory.getLogger(AclObjectIdentityResource.class);

    private static final String ENTITY_NAME = "aclObjectIdentity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclObjectIdentityService aclObjectIdentityService;

    private final AclObjectIdentityRepository aclObjectIdentityRepository;

    public AclObjectIdentityResource(
        AclObjectIdentityService aclObjectIdentityService,
        AclObjectIdentityRepository aclObjectIdentityRepository
    ) {
        this.aclObjectIdentityService = aclObjectIdentityService;
        this.aclObjectIdentityRepository = aclObjectIdentityRepository;
    }

    /**
     * {@code POST  /acl-object-identities} : Create a new aclObjectIdentity.
     *
     * @param aclObjectIdentityDTO the aclObjectIdentityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclObjectIdentityDTO, or with status {@code 400 (Bad Request)} if the aclObjectIdentity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-object-identities")
    public ResponseEntity<AclObjectIdentityDTO> createAclObjectIdentity(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @Valid @RequestBody AclObjectIdentityDTO aclObjectIdentityDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AclObjectIdentity : {}", aclObjectIdentityDTO);
        if (aclObjectIdentityDTO.getId() != null) {
            throw new BadRequestAlertException("A new aclObjectIdentity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclObjectIdentityDTO result = aclObjectIdentityService.save(aclObjectIdentityDTO);
        return ResponseEntity
            .created(new URI("/api/acl-object-identities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-object-identities/:id} : Updates an existing aclObjectIdentity.
     *
     * @param id the id of the aclObjectIdentityDTO to save.
     * @param aclObjectIdentityDTO the aclObjectIdentityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclObjectIdentityDTO,
     * or with status {@code 400 (Bad Request)} if the aclObjectIdentityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclObjectIdentityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-object-identities/{id}")
    public ResponseEntity<AclObjectIdentityDTO> updateAclObjectIdentity(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AclObjectIdentityDTO aclObjectIdentityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AclObjectIdentity : {}, {}", id, aclObjectIdentityDTO);
        if (aclObjectIdentityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclObjectIdentityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclObjectIdentityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclObjectIdentityDTO result = aclObjectIdentityService.save(aclObjectIdentityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclObjectIdentityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-object-identities/:id} : Partial updates given fields of an existing aclObjectIdentity, field will ignore if it is null
     *
     * @param id the id of the aclObjectIdentityDTO to save.
     * @param aclObjectIdentityDTO the aclObjectIdentityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclObjectIdentityDTO,
     * or with status {@code 400 (Bad Request)} if the aclObjectIdentityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aclObjectIdentityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclObjectIdentityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-object-identities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AclObjectIdentityDTO> partialUpdateAclObjectIdentity(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AclObjectIdentityDTO aclObjectIdentityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclObjectIdentity partially : {}, {}", id, aclObjectIdentityDTO);
        if (aclObjectIdentityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclObjectIdentityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclObjectIdentityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclObjectIdentityDTO> result = aclObjectIdentityService.partialUpdate(aclObjectIdentityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclObjectIdentityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-object-identities} : get all the aclObjectIdentities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclObjectIdentities in body.
     */
    @GetMapping("/acl-object-identities")
    public ResponseEntity<List<AclObjectIdentityDTO>> getAllAclObjectIdentities(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of AclObjectIdentities");
        Page<AclObjectIdentityDTO> page = aclObjectIdentityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acl-object-identities/:id} : get the "id" aclObjectIdentity.
     *
     * @param id the id of the aclObjectIdentityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclObjectIdentityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-object-identities/{id}")
    public ResponseEntity<AclObjectIdentityDTO> getAclObjectIdentity(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get AclObjectIdentity : {}", id);
        Optional<AclObjectIdentityDTO> aclObjectIdentityDTO = aclObjectIdentityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclObjectIdentityDTO);
    }

    /**
     * {@code DELETE  /acl-object-identities/:id} : delete the "id" aclObjectIdentity.
     *
     * @param id the id of the aclObjectIdentityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-object-identities/{id}")
    public ResponseEntity<Void> deleteAclObjectIdentity(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete AclObjectIdentity : {}", id);
        aclObjectIdentityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
