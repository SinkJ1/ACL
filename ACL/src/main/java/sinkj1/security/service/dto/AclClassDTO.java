package sinkj1.security.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sinkj1.security.domain.AclClass} entity.
 */
public class AclClassDTO implements Serializable {

    private Long id;

    @NotNull
    private String className;

    @NotNull
    private String classIdType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassIdType() {
        return classIdType;
    }

    public void setClassIdType(String classIdType) {
        this.classIdType = classIdType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclClassDTO)) {
            return false;
        }

        AclClassDTO aclClassDTO = (AclClassDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aclClassDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclClassDTO{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", classIdType='" + getClassIdType() + "'" +
            "}";
    }
}
