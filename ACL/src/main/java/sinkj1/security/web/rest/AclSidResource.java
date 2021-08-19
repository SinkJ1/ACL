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
import sinkj1.security.repository.AclSidRepository;
import sinkj1.security.service.AclSidService;
import sinkj1.security.service.dto.AclSidDTO;
import sinkj1.security.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sinkj1.security.domain.AclSid}.
 */
@RestController
@RequestMapping("/api")
public class AclSidResource {

    private final Logger log = LoggerFactory.getLogger(AclSidResource.class);

    private static final String ENTITY_NAME = "aclSid";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclSidService aclSidService;

    private final AclSidRepository aclSidRepository;

    public AclSidResource(AclSidService aclSidService, AclSidRepository aclSidRepository) {
        this.aclSidService = aclSidService;
        this.aclSidRepository = aclSidRepository;
    }

    /**
     * {@code POST  /acl-sids} : Create a new aclSid.
     *
     * @param aclSidDTO the aclSidDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclSidDTO, or with status {@code 400 (Bad Request)} if the aclSid has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-sids")
    public ResponseEntity<AclSidDTO> createAclSid(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @Valid @RequestBody AclSidDTO aclSidDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AclSid : {}", aclSidDTO);
        if (aclSidDTO.getId() != null) {
            throw new BadRequestAlertException("A new aclSid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclSidDTO result = aclSidService.save(aclSidDTO);
        return ResponseEntity
            .created(new URI("/api/acl-sids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-sids/:id} : Updates an existing aclSid.
     *
     * @param id the id of the aclSidDTO to save.
     * @param aclSidDTO the aclSidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclSidDTO,
     * or with status {@code 400 (Bad Request)} if the aclSidDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclSidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-sids/{id}")
    public ResponseEntity<AclSidDTO> updateAclSid(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AclSidDTO aclSidDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AclSid : {}, {}", id, aclSidDTO);
        if (aclSidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclSidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclSidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclSidDTO result = aclSidService.save(aclSidDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclSidDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-sids/:id} : Partial updates given fields of an existing aclSid, field will ignore if it is null
     *
     * @param id the id of the aclSidDTO to save.
     * @param aclSidDTO the aclSidDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclSidDTO,
     * or with status {@code 400 (Bad Request)} if the aclSidDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aclSidDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclSidDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-sids/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AclSidDTO> partialUpdateAclSid(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AclSidDTO aclSidDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclSid partially : {}, {}", id, aclSidDTO);
        if (aclSidDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclSidDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclSidRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclSidDTO> result = aclSidService.partialUpdate(aclSidDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclSidDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-sids} : get all the aclSids.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclSids in body.
     */
    @GetMapping("/acl-sids")
    public ResponseEntity<List<AclSidDTO>> getAllAclSids(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of AclSids");
        Page<AclSidDTO> page = aclSidService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acl-sids/:id} : get the "id" aclSid.
     *
     * @param id the id of the aclSidDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclSidDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-sids/{id}")
    public ResponseEntity<AclSidDTO> getAclSid(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get AclSid : {}", id);
        Optional<AclSidDTO> aclSidDTO = aclSidService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclSidDTO);
    }

    /**
     * {@code DELETE  /acl-sids/:id} : delete the "id" aclSid.
     *
     * @param id the id of the aclSidDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-sids/{id}")
    public ResponseEntity<Void> deleteAclSid(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete AclSid : {}", id);
        aclSidService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
