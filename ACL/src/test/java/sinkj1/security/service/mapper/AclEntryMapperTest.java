package sinkj1.security.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AclEntryMapperTest {

    private AclEntryMapper aclEntryMapper;

    @BeforeEach
    public void setUp() {
        aclEntryMapper = new AclEntryMapperImpl();
    }
}
