package sinkj1.security.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AclObjectIdentityMapperTest {

    private AclObjectIdentityMapper aclObjectIdentityMapper;

    @BeforeEach
    public void setUp() {
        aclObjectIdentityMapper = new AclObjectIdentityMapperImpl();
    }
}
