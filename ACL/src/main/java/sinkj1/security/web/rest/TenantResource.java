package sinkj1.security.web.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sinkj1.security.repository.TenantRepository;
import sinkj1.security.service.TenantService;
import sinkj1.security.service.dto.TenantDTO;
import sinkj1.security.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@Controller
@RequestMapping("/api")
public class TenantResource {

    private final Logger log = LoggerFactory.getLogger(TenantResource.class);

    private static final String ENTITY_NAME = "tenant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantService tenantService;

    private final TenantRepository tenantRepository;

    public TenantResource(TenantService tenantService, TenantRepository tenantRepository) {
        this.tenantService = tenantService;
        this.tenantRepository = tenantRepository;
    }

    /**
     * {@code POST  /tenants} : Create a new tenant.
     *
     * @param tenantDTO the tenantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantDTO, or with status {@code 400 (Bad Request)} if the tenant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tenants")
    public ResponseEntity<TenantDTO> createTenant(@RequestBody TenantDTO tenantDTO) throws URISyntaxException {
        log.debug("REST request to save Tenant : {}", tenantDTO);
        if (tenantDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TenantDTO result = tenantService.createTenant(tenantDTO);
        return ResponseEntity
            .created(new URI("/api/tenants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tenants/:id} : Updates an existing tenant.
     *
     * @param id the id of the tenantDTO to save.
     * @param tenantDTO the tenantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantDTO,
     * or with status {@code 400 (Bad Request)} if the tenantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tenants/{id}")
    public ResponseEntity<TenantDTO> updateTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TenantDTO tenantDTO
    ) throws URISyntaxException, SQLException {
        log.debug("REST request to update Tenant : {}, {}", id, tenantDTO);
        if (tenantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TenantDTO result = tenantService.save(tenantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tenantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tenants/:id} : Partial updates given fields of an existing tenant, field will ignore if it is null
     *
     * @param id the id of the tenantDTO to save.
     * @param tenantDTO the tenantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantDTO,
     * or with status {@code 400 (Bad Request)} if the tenantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tenantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tenants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TenantDTO> partialUpdateTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TenantDTO tenantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tenant partially : {}, {}", id, tenantDTO);
        if (tenantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantDTO> result = tenantService.partialUpdate(tenantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tenantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tenants} : get all the tenants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenants in body.
     */
    @GetMapping("/tenants")
    public ResponseEntity<List<TenantDTO>> getAllTenants(Pageable pageable) {
        log.debug("REST request to get a page of Tenants");
        Page<TenantDTO> page = tenantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tenants/:id} : get the "id" tenant.
     *
     * @param id the id of the tenantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tenants/{id}")
    public ResponseEntity<TenantDTO> getTenant(@PathVariable Long id) {
        log.debug("REST request to get Tenant : {}", id);
        Optional<TenantDTO> tenantDTO = tenantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tenantDTO);
    }

    /**
     * {@code DELETE  /tenants/:id} : delete the "id" tenant.
     *
     * @param id the id of the tenantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tenants/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) throws SQLException {
        log.debug("REST request to delete Tenant : {}", id);
        tenantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
