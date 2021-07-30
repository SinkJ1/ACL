package sinkj1.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclObjectIdentityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclObjectIdentity.class);
        AclObjectIdentity aclObjectIdentity1 = new AclObjectIdentity();
        aclObjectIdentity1.setId(1L);
        AclObjectIdentity aclObjectIdentity2 = new AclObjectIdentity();
        aclObjectIdentity2.setId(aclObjectIdentity1.getId());
        assertThat(aclObjectIdentity1).isEqualTo(aclObjectIdentity2);
        aclObjectIdentity2.setId(2L);
        assertThat(aclObjectIdentity1).isNotEqualTo(aclObjectIdentity2);
        aclObjectIdentity1.setId(null);
        assertThat(aclObjectIdentity1).isNotEqualTo(aclObjectIdentity2);
    }
}
