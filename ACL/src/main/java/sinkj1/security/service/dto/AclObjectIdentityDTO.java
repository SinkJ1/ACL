package sinkj1.security.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sinkj1.security.domain.AclObjectIdentity} entity.
 */
public class AclObjectIdentityDTO implements Serializable {

    private Long id;

    @NotNull
    private String objectIdIdentity;

    @NotNull
    private Integer parentObject;

    private Integer ownerSid;

    private Boolean entriesInheriting;

    private AclClassDTO aclClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectIdIdentity() {
        return objectIdIdentity;
    }

    public void setObjectIdIdentity(String objectIdIdentity) {
        this.objectIdIdentity = objectIdIdentity;
    }

    public Integer getParentObject() {
        return parentObject;
    }

    public void setParentObject(Integer parentObject) {
        this.parentObject = parentObject;
    }

    public Integer getOwnerSid() {
        return ownerSid;
    }

    public void setOwnerSid(Integer ownerSid) {
        this.ownerSid = ownerSid;
    }

    public Boolean getEntriesInheriting() {
        return entriesInheriting;
    }

    public void setEntriesInheriting(Boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
    }

    public AclClassDTO getAclClass() {
        return aclClass;
    }

    public void setAclClass(AclClassDTO aclClass) {
        this.aclClass = aclClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclObjectIdentityDTO)) {
            return false;
        }

        AclObjectIdentityDTO aclObjectIdentityDTO = (AclObjectIdentityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aclObjectIdentityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclObjectIdentityDTO{" +
            "id=" + getId() +
            ", objectIdIdentity='" + getObjectIdIdentity() + "'" +
            ", parentObject=" + getParentObject() +
            ", ownerSid=" + getOwnerSid() +
            ", entriesInheriting='" + getEntriesInheriting() + "'" +
            ", aclClass=" + getAclClass() +
            "}";
    }
}
