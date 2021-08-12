package sinkj1.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

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

    @NotNull
    @Column(name = "ace_order", nullable = false)
    private Integer aceOrder;

    @ManyToOne
    @JoinColumn(name = "mask")
    @JsonIgnoreProperties(value = {"aclEntries"}, allowSetters = true)
    private AclMask aclMask;

    @Column(name = "granting")
    private Boolean granting;

    @Column(name = "audit_success")
    private Boolean auditSuccess;

    @Column(name = "audit_failure")
    private Boolean auditFailure;

    @ManyToOne
    @JoinColumn(name = "sid")
    @JsonIgnoreProperties(value = {"aclEntries"}, allowSetters = true)
    private AclSid aclSid;

    @ManyToOne
    @JoinColumn(name = "acl_object_identity")
    @JsonIgnoreProperties(value = {"aclEntries", "aclClass"}, allowSetters = true)
    private AclObjectIdentity aclObjectIdentity;

    public AclEntry() {
    }

    public AclEntry(Integer aceOrder, AclMask aclMask, Boolean granting, Boolean auditSuccess, Boolean auditFailure, AclSid aclSid, AclObjectIdentity aclObjectIdentity) {
        this.aceOrder = aceOrder;
        this.aclMask = aclMask;
        this.granting = granting;
        this.auditSuccess = auditSuccess;
        this.auditFailure = auditFailure;
        this.aclSid = aclSid;
        this.aclObjectIdentity = aclObjectIdentity;
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

    public Integer getAceOrder() {
        return this.aceOrder;
    }

    public AclEntry aceOrder(Integer aceOrder) {
        this.aceOrder = aceOrder;
        return this;
    }

    public void setAceOrder(Integer aceOrder) {
        this.aceOrder = aceOrder;
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

    public Boolean getAuditSuccess() {
        return this.auditSuccess;
    }

    public AclEntry auditSuccess(Boolean auditSuccess) {
        this.auditSuccess = auditSuccess;
        return this;
    }

    public void setAuditSuccess(Boolean auditSuccess) {
        this.auditSuccess = auditSuccess;
    }

    public Boolean getAuditFailure() {
        return this.auditFailure;
    }

    public AclEntry auditFailure(Boolean auditFailure) {
        this.auditFailure = auditFailure;
        return this;
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

    public void setAuditFailure(Boolean auditFailure) {
        this.auditFailure = auditFailure;
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
            ", aceOrder=" + getAceOrder() +
            ", granting='" + getGranting() + "'" +
            ", auditSuccess='" + getAuditSuccess() + "'" +
            ", auditFailure='" + getAuditFailure() + "'" +
            "}";
    }
}
