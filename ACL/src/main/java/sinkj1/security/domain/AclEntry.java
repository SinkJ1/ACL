package sinkj1.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AclEntry.
 */
@Entity
@Table(name = "acl_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "granting")
    private Boolean granting;

    @ManyToOne
    @JoinColumn(name = "sid")
    @JsonIgnoreProperties(value = { "aclEntries" }, allowSetters = true)
    private AclSid aclSid;

    @ManyToOne
    @JoinColumn(name = "acl_object_identity")
    @JsonIgnoreProperties(value = { "aclEntries", "aclClass" }, allowSetters = true)
    private AclObjectIdentity aclObjectIdentity;

    @ManyToOne
    @JoinColumn(name = "mask")
    @JsonIgnoreProperties(value = { "aclEntries" }, allowSetters = true)
    private AclMask aclMask;

    public AclEntry() {}

    public AclEntry(Boolean granting, AclSid aclSid, AclObjectIdentity aclObjectIdentity, AclMask aclMask) {
        this.granting = granting;
        this.aclSid = aclSid;
        this.aclObjectIdentity = aclObjectIdentity;
        this.aclMask = aclMask;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclEntry id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getGranting() {
        return this.granting;
    }

    public AclEntry granting(Boolean granting) {
        this.granting = granting;
        return this;
    }

    public void setGranting(Boolean granting) {
        this.granting = granting;
    }

    public AclSid getAclSid() {
        return this.aclSid;
    }

    public AclEntry aclSid(AclSid aclSid) {
        this.setAclSid(aclSid);
        return this;
    }

    public void setAclSid(AclSid aclSid) {
        this.aclSid = aclSid;
    }

    public AclObjectIdentity getAclObjectIdentity() {
        return this.aclObjectIdentity;
    }

    public AclEntry aclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
        this.setAclObjectIdentity(aclObjectIdentity);
        return this;
    }

    public void setAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
        this.aclObjectIdentity = aclObjectIdentity;
    }

    public AclMask getAclMask() {
        return this.aclMask;
    }

    public AclEntry aclMask(AclMask aclMask) {
        this.setAclMask(aclMask);
        return this;
    }

    public void setAclMask(AclMask aclMask) {
        this.aclMask = aclMask;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclEntry)) {
            return false;
        }
        return id != null && id.equals(((AclEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclEntry{" +
            "id=" + getId() +
            ", granting='" + getGranting() + "'" +
            "}";
    }
}
