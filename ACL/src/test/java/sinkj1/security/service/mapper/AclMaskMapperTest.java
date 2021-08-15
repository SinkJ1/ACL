package sinkj1.security.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AclMaskMapperTest {

    private AclMaskMapper aclMaskMapper;

    @BeforeEach
    public void setUp() {
        aclMaskMapper = new AclMaskMapperImpl();
    }
}
