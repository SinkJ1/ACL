package sinkj1.security.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sinkj1.security.config.MultiTenantManager;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/tenants")
public class TenantResource {

    private final MultiTenantManager tenantManager;

    private final Logger log = LoggerFactory.getLogger(TenantResource.class);

    public TenantResource(MultiTenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    /**
     * Get list of all tenants in the local storage
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tenantManager.getTenantList());
    }

    /**
     * Add the new tenant on the fly
     *
     * @param dbProperty Map with tenantId and related datasource properties
     */
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, String> dbProperty) throws Exception {

        log.info("[i] Received add new tenant params request {}", dbProperty);

        String tenantId = dbProperty.get("tenantId");
        String url = dbProperty.get("url");
        String username = dbProperty.get("username");
        String password = dbProperty.get("password");

        if (tenantId == null || url == null || username == null || password == null) {
            log.error("[!] Received database params are incorrect or not full!");
            throw new Exception();
        }

        try {
            tenantManager.addTenant(tenantId, url, username, password);
            log.info("[i] Loaded DataSource for tenant '{}'.", tenantId);
            return ResponseEntity.ok(dbProperty);
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }


}
