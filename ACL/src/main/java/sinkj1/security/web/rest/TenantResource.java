package sinkj1.security.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sinkj1.security.service.TenantManagementService;

@Controller
@RequestMapping("/api")
public class TenantResource {

    @Autowired
    private TenantManagementService tenantManagementService;

    public TenantResource(TenantManagementService tenantManagementService) {
        this.tenantManagementService = tenantManagementService;
    }

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestParam String tenantId, @RequestParam String schema) {
        this.tenantManagementService.createTenant(tenantId, schema);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
