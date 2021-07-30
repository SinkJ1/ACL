package sinkj1.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclSidTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclSid.class);
        AclSid aclSid1 = new AclSid();
        aclSid1.setId(1L);
        AclSid aclSid2 = new AclSid();
        aclSid2.setId(aclSid1.getId());
        assertThat(aclSid1).isEqualTo(aclSid2);
        aclSid2.setId(2L);
        assertThat(aclSid1).isNotEqualTo(aclSid2);
        aclSid1.setId(null);
        assertThat(aclSid1).isNotEqualTo(aclSid2);
    }
}
