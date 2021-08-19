package sinkj1.security.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import sinkj1.security.repository.AclMaskRepository;
import sinkj1.security.service.AclMaskService;
import sinkj1.security.service.dto.AclMaskDTO;
import sinkj1.security.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sinkj1.security.domain.AclMask}.
 */
@RestController
@RequestMapping("/api")
public class AclMaskResource {

    private final Logger log = LoggerFactory.getLogger(AclMaskResource.class);

    private static final String ENTITY_NAME = "aclMask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclMaskService aclMaskService;

    private final AclMaskRepository aclMaskRepository;

    public AclMaskResource(AclMaskService aclMaskService, AclMaskRepository aclMaskRepository) {
        this.aclMaskService = aclMaskService;
        this.aclMaskRepository = aclMaskRepository;
    }

    /**
     * {@code POST  /acl-masks} : Create a new aclMask.
     *
     * @param aclMaskDTO the aclMaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclMaskDTO, or with status {@code 400 (Bad Request)} if the aclMask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-masks")
    public ResponseEntity<AclMaskDTO> createAclMask(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @RequestBody AclMaskDTO aclMaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AclMask : {}", aclMaskDTO);
        if (aclMaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new aclMask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclMaskDTO result = aclMaskService.save(aclMaskDTO);
        return ResponseEntity
            .created(new URI("/api/acl-masks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-masks/:id} : Updates an existing aclMask.
     *
     * @param id the id of the aclMaskDTO to save.
     * @param aclMaskDTO the aclMaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclMaskDTO,
     * or with status {@code 400 (Bad Request)} if the aclMaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclMaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-masks/{id}")
    public ResponseEntity<AclMaskDTO> updateAclMask(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclMaskDTO aclMaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AclMask : {}, {}", id, aclMaskDTO);
        if (aclMaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclMaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclMaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclMaskDTO result = aclMaskService.save(aclMaskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclMaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-masks/:id} : Partial updates given fields of an existing aclMask, field will ignore if it is null
     *
     * @param id the id of the aclMaskDTO to save.
     * @param aclMaskDTO the aclMaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclMaskDTO,
     * or with status {@code 400 (Bad Request)} if the aclMaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aclMaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclMaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-masks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AclMaskDTO> partialUpdateAclMask(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclMaskDTO aclMaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclMask partially : {}, {}", id, aclMaskDTO);
        if (aclMaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclMaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclMaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclMaskDTO> result = aclMaskService.partialUpdate(aclMaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclMaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-masks} : get all the aclMasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclMasks in body.
     */
    @GetMapping("/acl-masks")
    public ResponseEntity<List<AclMaskDTO>> getAllAclMasks(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of AclMasks");
        Page<AclMaskDTO> page = aclMaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acl-masks/:id} : get the "id" aclMask.
     *
     * @param id the id of the aclMaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclMaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-masks/{id}")
    public ResponseEntity<AclMaskDTO> getAclMask(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get AclMask : {}", id);
        Optional<AclMaskDTO> aclMaskDTO = aclMaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclMaskDTO);
    }

    /**
     * {@code DELETE  /acl-masks/:id} : delete the "id" aclMask.
     *
     * @param id the id of the aclMaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-masks/{id}")
    public ResponseEntity<Void> deleteAclMask(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete AclMask : {}", id);
        aclMaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
