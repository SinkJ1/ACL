package sinkj1.security.multi_tenancy.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Tenant {

    @Id
    @Size(max = 30)
    @Column(name = "tenant_id")
    private String tenantId;

    @Size(max = 30)
    @Column(name = "schema")
    private String schema;

    public Tenant(){

    }

    public Tenant(String tenantId, String schema) {
        this.tenantId = tenantId;
        this.schema = schema;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
