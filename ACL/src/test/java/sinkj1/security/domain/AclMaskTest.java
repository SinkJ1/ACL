package sinkj1.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclMaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclMask.class);
        AclMask aclMask1 = new AclMask();
        aclMask1.setId(1L);
        AclMask aclMask2 = new AclMask();
        aclMask2.setId(aclMask1.getId());
        assertThat(aclMask1).isEqualTo(aclMask2);
        aclMask2.setId(2L);
        assertThat(aclMask1).isNotEqualTo(aclMask2);
        aclMask1.setId(null);
        assertThat(aclMask1).isNotEqualTo(aclMask2);
    }
}
