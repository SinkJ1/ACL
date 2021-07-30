package sinkj1.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclClass.class);
        AclClass aclClass1 = new AclClass();
        aclClass1.setId(1L);
        AclClass aclClass2 = new AclClass();
        aclClass2.setId(aclClass1.getId());
        assertThat(aclClass1).isEqualTo(aclClass2);
        aclClass2.setId(2L);
        assertThat(aclClass1).isNotEqualTo(aclClass2);
        aclClass1.setId(null);
        assertThat(aclClass1).isNotEqualTo(aclClass2);
    }
}
