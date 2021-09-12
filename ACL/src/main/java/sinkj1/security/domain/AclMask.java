package sinkj1.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AclMask.
 */
@Entity
@Table(name = "acl_mask")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclMask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "aclMask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aclSid", "aclObjectIdentity", "aclMask" }, allowSetters = true)
    private Set<AclEntry> aclEntries = new HashSet<>();

    public AclMask() {}

    public AclMask(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclMask id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public AclMask name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AclEntry> getAclEntries() {
        return this.aclEntries;
    }

    public AclMask aclEntries(Set<AclEntry> aclEntries) {
        this.setAclEntries(aclEntries);
        return this;
    }

    public AclMask addAclEntry(AclEntry aclEntry) {
        this.aclEntries.add(aclEntry);
        aclEntry.setAclMask(this);
        return this;
    }

    public AclMask removeAclEntry(AclEntry aclEntry) {
        this.aclEntries.remove(aclEntry);
        aclEntry.setAclMask(null);
        return this;
    }

    public void setAclEntries(Set<AclEntry> aclEntries) {
        if (this.aclEntries != null) {
            this.aclEntries.forEach(i -> i.setAclMask(null));
        }
        if (aclEntries != null) {
            aclEntries.forEach(i -> i.setAclMask(this));
        }
        this.aclEntries = aclEntries;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclMask)) {
            return false;
        }
        return id != null && id.equals(((AclMask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclMask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
