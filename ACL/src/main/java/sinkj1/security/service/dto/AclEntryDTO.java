package sinkj1.security.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sinkj1.security.domain.AclEntry} entity.
 */
public class AclEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer aceOrder;

    private Integer mask;

    private Boolean granting;

    private Boolean auditSuccess;

    private Boolean auditFailure;

    private AclSidDTO aclSid;

    private AclObjectIdentityDTO aclObjectIdentity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAceOrder() {
        return aceOrder;
    }

    public void setAceOrder(Integer aceOrder) {
        this.aceOrder = aceOrder;
    }

    public Integer getMask() {
        return mask;
    }

    public void setMask(Integer mask) {
        this.mask = mask;
    }

    public Boolean getGranting() {
        return granting;
    }

    public void setGranting(Boolean granting) {
        this.granting = granting;
    }

    public Boolean getAuditSuccess() {
        return auditSuccess;
    }

    public void setAuditSuccess(Boolean auditSuccess) {
        this.auditSuccess = auditSuccess;
    }

    public Boolean getAuditFailure() {
        return auditFailure;
    }

    public void setAuditFailure(Boolean auditFailure) {
        this.auditFailure = auditFailure;
    }

    public AclSidDTO getAclSid() {
        return aclSid;
    }

    public void setAclSid(AclSidDTO aclSid) {
        this.aclSid = aclSid;
    }

    public AclObjectIdentityDTO getAclObjectIdentity() {
        return aclObjectIdentity;
    }

    public void setAclObjectIdentity(AclObjectIdentityDTO aclObjectIdentity) {
        this.aclObjectIdentity = aclObjectIdentity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclEntryDTO)) {
            return false;
        }

        AclEntryDTO aclEntryDTO = (AclEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aclEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclEntryDTO{" +
            "id=" + getId() +
            ", aceOrder=" + getAceOrder() +
            ", mask=" + getMask() +
            ", granting='" + getGranting() + "'" +
            ", auditSuccess='" + getAuditSuccess() + "'" +
            ", auditFailure='" + getAuditFailure() + "'" +
            ", aclSid=" + getAclSid() +
            ", aclObjectIdentity=" + getAclObjectIdentity() +
            "}";
    }
}
