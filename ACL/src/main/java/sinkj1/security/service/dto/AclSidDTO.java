package sinkj1.security.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sinkj1.security.domain.AclSid} entity.
 */
public class AclSidDTO implements Serializable {

    private Long id;

    @NotNull
    private String sid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclSidDTO)) {
            return false;
        }

        AclSidDTO aclSidDTO = (AclSidDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aclSidDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclSidDTO{" +
            "id=" + getId() +
            ", sid='" + getSid() + "'" +
            "}";
    }
}
