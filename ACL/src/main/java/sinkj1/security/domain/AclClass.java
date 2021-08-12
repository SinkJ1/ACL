package sinkj1.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AclClass.
 */
@Entity
@Table(name = "acl_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "class", nullable = false)
    private String className;

    @NotNull
    @Column(name = "class_id_type", nullable = false)
    private String classIdType;

    @OneToMany(mappedBy = "aclClass")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aclEntries", "aclClass" }, allowSetters = true)
    private Set<AclObjectIdentity> aclObjectIdentities = new HashSet<>();


    public AclClass() {
    }

    public AclClass(String className) {
        this.className = className;
        this.classIdType = "";
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclClass id(Long id) {
        this.id = id;
        return this;
    }

    public String getClassName() {
        return this.className;
    }

    public AclClass className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassIdType() {
        return this.classIdType;
    }

    public AclClass classIdType(String classIdType) {
        this.classIdType = classIdType;
        return this;
    }

    public void setClassIdType(String classIdType) {
        this.classIdType = classIdType;
    }

    public Set<AclObjectIdentity> getAclObjectIdentities() {
        return this.aclObjectIdentities;
    }

    public AclClass aclObjectIdentities(Set<AclObjectIdentity> aclObjectIdentities) {
        this.setAclObjectIdentities(aclObjectIdentities);
        return this;
    }

    public AclClass addAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
        this.aclObjectIdentities.add(aclObjectIdentity);
        aclObjectIdentity.setAclClass(this);
        return this;
    }

    public AclClass removeAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
        this.aclObjectIdentities.remove(aclObjectIdentity);
        aclObjectIdentity.setAclClass(null);
        return this;
    }

    public void setAclObjectIdentities(Set<AclObjectIdentity> aclObjectIdentities) {
        if (this.aclObjectIdentities != null) {
            this.aclObjectIdentities.forEach(i -> i.setAclClass(null));
        }
        if (aclObjectIdentities != null) {
            aclObjectIdentities.forEach(i -> i.setAclClass(this));
        }
        this.aclObjectIdentities = aclObjectIdentities;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclClass)) {
            return false;
        }
        return id != null && id.equals(((AclClass) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclClass{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", classIdType='" + getClassIdType() + "'" +
            "}";
    }
}
