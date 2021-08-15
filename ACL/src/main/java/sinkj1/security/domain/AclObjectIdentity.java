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
 * A AclObjectIdentity.
 */
@Entity
@Table(name = "acl_object_identity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclObjectIdentity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "object_id_identity", nullable = false)
    private Integer objectIdIdentity;

    @NotNull
    @Column(name = "parent_object", nullable = false)
    private Integer parentObject;

    @Column(name = "owner_sid")
    private Integer ownerSid;

    @Column(name = "entries_inheriting")
    private Boolean entriesInheriting;

    @OneToMany(mappedBy = "aclObjectIdentity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aclSid", "aclObjectIdentity", "aclMask" }, allowSetters = true)
    private Set<AclEntry> aclEntries = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "aclObjectIdentities" }, allowSetters = true)
    private AclClass aclClass;

    public AclObjectIdentity() {}

    public AclObjectIdentity(Integer objectIdIdentity, Integer parentObject, Boolean entriesInheriting, AclClass aclClass) {
        this.objectIdIdentity = objectIdIdentity;
        this.parentObject = parentObject;
        this.entriesInheriting = entriesInheriting;
        this.aclClass = aclClass;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclObjectIdentity id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getObjectIdIdentity() {
        return this.objectIdIdentity;
    }

    public AclObjectIdentity objectIdIdentity(Integer objectIdIdentity) {
        this.objectIdIdentity = objectIdIdentity;
        return this;
    }

    public void setObjectIdIdentity(Integer objectIdIdentity) {
        this.objectIdIdentity = objectIdIdentity;
    }

    public Integer getParentObject() {
        return this.parentObject;
    }

    public AclObjectIdentity parentObject(Integer parentObject) {
        this.parentObject = parentObject;
        return this;
    }

    public void setParentObject(Integer parentObject) {
        this.parentObject = parentObject;
    }

    public Integer getOwnerSid() {
        return this.ownerSid;
    }

    public AclObjectIdentity ownerSid(Integer ownerSid) {
        this.ownerSid = ownerSid;
        return this;
    }

    public void setOwnerSid(Integer ownerSid) {
        this.ownerSid = ownerSid;
    }

    public Boolean getEntriesInheriting() {
        return this.entriesInheriting;
    }

    public AclObjectIdentity entriesInheriting(Boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
        return this;
    }

    public void setEntriesInheriting(Boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
    }

    public Set<AclEntry> getAclEntries() {
        return this.aclEntries;
    }

    public AclObjectIdentity aclEntries(Set<AclEntry> aclEntries) {
        this.setAclEntries(aclEntries);
        return this;
    }

    public AclObjectIdentity addAclEntry(AclEntry aclEntry) {
        this.aclEntries.add(aclEntry);
        aclEntry.setAclObjectIdentity(this);
        return this;
    }

    public AclObjectIdentity removeAclEntry(AclEntry aclEntry) {
        this.aclEntries.remove(aclEntry);
        aclEntry.setAclObjectIdentity(null);
        return this;
    }

    public void setAclEntries(Set<AclEntry> aclEntries) {
        if (this.aclEntries != null) {
            this.aclEntries.forEach(i -> i.setAclObjectIdentity(null));
        }
        if (aclEntries != null) {
            aclEntries.forEach(i -> i.setAclObjectIdentity(this));
        }
        this.aclEntries = aclEntries;
    }

    public AclClass getAclClass() {
        return this.aclClass;
    }

    public AclObjectIdentity aclClass(AclClass aclClass) {
        this.setAclClass(aclClass);
        return this;
    }

    public void setAclClass(AclClass aclClass) {
        this.aclClass = aclClass;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclObjectIdentity)) {
            return false;
        }
        return id != null && id.equals(((AclObjectIdentity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclObjectIdentity{" +
            "id=" + getId() +
            ", objectIdIdentity=" + getObjectIdIdentity() +
            ", parentObject=" + getParentObject() +
            ", ownerSid=" + getOwnerSid() +
            ", entriesInheriting='" + getEntriesInheriting() + "'" +
            "}";
    }
}
