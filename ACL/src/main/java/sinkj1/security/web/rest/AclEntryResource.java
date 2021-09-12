package sinkj1.security.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sinkj1.security.domain.AclEntry;
import sinkj1.security.domain.MaskAndObject;
import sinkj1.security.repository.AclEntryRepository;
import sinkj1.security.service.AclEntryService;
import sinkj1.security.service.dto.AclEntryDTO;
import sinkj1.security.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sinkj1.security.domain.AclEntry}.
 */
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT })
@RestController
@RequestMapping("/api")
public class AclEntryResource {

    private final Logger log = LoggerFactory.getLogger(AclEntryResource.class);

    private static final String ENTITY_NAME = "aclEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclEntryService aclEntryService;

    private final AclEntryRepository aclEntryRepository;
    private final DataSource dataSource;

    public AclEntryResource(AclEntryService aclEntryService, AclEntryRepository aclEntryRepository, DataSource dataSource) {
        this.aclEntryService = aclEntryService;
        this.aclEntryRepository = aclEntryRepository;
        this.dataSource = dataSource;
    }

    /**
     * {@code POST  /acl-entries} : Create a new aclEntry.
     *
     * @param aclEntryDTO the aclEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclEntryDTO, or with status {@code 400 (Bad Request)} if the aclEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-entries")
    public ResponseEntity<AclEntryDTO> createAclEntry(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @Valid @RequestBody AclEntryDTO aclEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AclEntry : {}", aclEntryDTO);
        if (aclEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new aclEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclEntryDTO result = aclEntryService.save(aclEntryDTO);
        return ResponseEntity
            .created(new URI("/api/acl-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-entries/:id} : Updates an existing aclEntry.
     *
     * @param id the id of the aclEntryDTO to save.
     * @param aclEntryDTO the aclEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclEntryDTO,
     * or with status {@code 400 (Bad Request)} if the aclEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-entries/{id}")
    public ResponseEntity<AclEntryDTO> updateAclEntry(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AclEntryDTO aclEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AclEntry : {}, {}", id, aclEntryDTO);
        if (aclEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclEntryDTO result = aclEntryService.save(aclEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-entries/:id} : Partial updates given fields of an existing aclEntry, field will ignore if it is null
     *
     * @param id the id of the aclEntryDTO to save.
     * @param aclEntryDTO the aclEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclEntryDTO,
     * or with status {@code 400 (Bad Request)} if the aclEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aclEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-entries/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AclEntryDTO> partialUpdateAclEntry(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AclEntryDTO aclEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclEntry partially : {}, {}", id, aclEntryDTO);
        if (aclEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclEntryDTO> result = aclEntryService.partialUpdate(aclEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-entries} : get all the aclEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclEntries in body.
     */
    @GetMapping("/acl-entries")
    public ResponseEntity<List<AclEntryDTO>> getAllAclEntries(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of AclEntries");
        Page<AclEntryDTO> page = aclEntryService.findAll(pageable);
        for (AclEntryDTO aclEntry : page) {
            System.out.println(aclEntry);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acl-entries/:id} : get the "id" aclEntry.
     *
     * @param id the id of the aclEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-entries/{id}")
    public ResponseEntity<AclEntryDTO> getAclEntry(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) throws SQLException {
        log.debug("REST request to get AclEntry : {}", id);
        Optional<AclEntryDTO> aclEntryDTO = aclEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aclEntryDTO);
    }

    /**
     * {@code DELETE  /acl-entries/:id} : delete the "id" aclEntry.
     *
     * @param id the id of the aclEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-entries/{id}")
    public ResponseEntity<Void> deleteAclEntry(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete AclEntry : {}", id);
        aclEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/get-acl-entries-by-user/{name}")
    public ResponseEntity<List<MaskAndObject>> getMaskObjByName(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @RequestParam("objE") String objE,
        @PathVariable String name
    ) {
        return ResponseEntity.ok(aclEntryService.getMaskAndObjectIdByUser(objE, name));
    }

    @GetMapping("/get-acl-entries")
    public ResponseEntity<List<MaskAndObject>> getMaskObj(
        @RequestHeader(value = "X-TENANT-ID", required = false) String tenantId,
        @RequestParam("objE") String objE
    ) {
        return ResponseEntity.ok(aclEntryService.getMaskAndObjectId(objE));
    }

    @GetMapping("/check-role")
    public ResponseEntity<Boolean> check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        List<String> authoritiesStrings = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(authoritiesStrings.contains("ROLE_ADMIN"));
    }
}
