package sinkj1.security.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sinkj1.security.domain.AclMask} entity.
 */
public class AclMaskDTO implements Serializable {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclMaskDTO)) {
            return false;
        }

        AclMaskDTO aclMaskDTO = (AclMaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aclMaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclMaskDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
