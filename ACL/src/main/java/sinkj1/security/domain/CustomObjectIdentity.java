package sinkj1.security.domain;

import org.springframework.security.acls.model.ObjectIdentity;

import java.io.Serializable;

public class CustomObjectIdentity implements ObjectIdentity {
    public String type;
    public Serializable identifier;

    public CustomObjectIdentity() {
    }

    public CustomObjectIdentity(String type, Serializable identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    @Override
    public Serializable getIdentifier() {
        return identifier;
    }

    @Override
    public String getType() {
        return type;
    }
}
