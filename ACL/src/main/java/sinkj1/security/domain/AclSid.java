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
 * A AclSid.
 */
@Entity
@Table(name = "acl_sid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AclSid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    @NotNull
    @Column(name = "sid", nullable = false)
    private String sid;

    @OneToMany(mappedBy = "aclSid")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aclSid", "aclObjectIdentity" }, allowSetters = true)
    private Set<AclEntry> aclEntries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclSid id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getPrincipal() {
        return this.principal;
    }

    public AclSid principal(Boolean principal) {
        this.principal = principal;
        return this;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public String getSid() {
        return this.sid;
    }

    public AclSid sid(String sid) {
        this.sid = sid;
        return this;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Set<AclEntry> getAclEntries() {
        return this.aclEntries;
    }

    public AclSid aclEntries(Set<AclEntry> aclEntries) {
        this.setAclEntries(aclEntries);
        return this;
    }

    public AclSid addAclEntry(AclEntry aclEntry) {
        this.aclEntries.add(aclEntry);
        aclEntry.setAclSid(this);
        return this;
    }

    public AclSid removeAclEntry(AclEntry aclEntry) {
        this.aclEntries.remove(aclEntry);
        aclEntry.setAclSid(null);
        return this;
    }

    public void setAclEntries(Set<AclEntry> aclEntries) {
        if (this.aclEntries != null) {
            this.aclEntries.forEach(i -> i.setAclSid(null));
        }
        if (aclEntries != null) {
            aclEntries.forEach(i -> i.setAclSid(this));
        }
        this.aclEntries = aclEntries;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclSid)) {
            return false;
        }
        return id != null && id.equals(((AclSid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclSid{" +
            "id=" + getId() +
            ", principal='" + getPrincipal() + "'" +
            ", sid='" + getSid() + "'" +
            "}";
    }
}
