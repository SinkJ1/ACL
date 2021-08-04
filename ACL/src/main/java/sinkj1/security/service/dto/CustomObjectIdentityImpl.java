package sinkj1.security.service.dto;

import org.springframework.security.acls.domain.ObjectIdentityImpl;

import java.io.Serializable;

public class CustomObjectIdentityImpl extends ObjectIdentityImpl {

    public String type;
    public Serializable identifier;

    public CustomObjectIdentityImpl(String type, Serializable identifier) {
        super(type, identifier);
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
