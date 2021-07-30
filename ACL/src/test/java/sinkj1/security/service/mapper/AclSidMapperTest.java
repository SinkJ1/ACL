package sinkj1.security.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AclSidMapperTest {

    private AclSidMapper aclSidMapper;

    @BeforeEach
    public void setUp() {
        aclSidMapper = new AclSidMapperImpl();
    }
}
