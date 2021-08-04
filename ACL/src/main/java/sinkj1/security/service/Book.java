package sinkj1.security.service;

import sinkj1.security.domain.BaseEntity;

public class Book implements BaseEntity {

    Long id = 1L;

    @Override
    public Long getId() {
        return id;
    }
}
