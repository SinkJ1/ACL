package sinkj1.security.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sinkj1.security.domain.AclEntry} entity.
 */
public class AclEntryDTO implements Serializable {

    private Long id;

    private AclMaskDTO mask;

    private Boolean granting;

    private AclSidDTO aclSid;

    private AclObjectIdentityDTO aclObjectIdentity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclMaskDTO getMask() {
        return mask;
    }

    public void setMask(AclMaskDTO mask) {
        this.mask = mask;
    }

    public Boolean getGranting() {
        return granting;
    }

    public void setGranting(Boolean granting) {
        this.granting = granting;
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
            ", mask=" + getMask() +
            ", granting='" + getGranting() + "'" +
            ", aclSid=" + getAclSid() +
            ", aclObjectIdentity=" + getAclObjectIdentity() +
            "}";
    }
}
