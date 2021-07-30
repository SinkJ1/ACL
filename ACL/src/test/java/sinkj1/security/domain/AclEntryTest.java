package sinkj1.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclEntry.class);
        AclEntry aclEntry1 = new AclEntry();
        aclEntry1.setId(1L);
        AclEntry aclEntry2 = new AclEntry();
        aclEntry2.setId(aclEntry1.getId());
        assertThat(aclEntry1).isEqualTo(aclEntry2);
        aclEntry2.setId(2L);
        assertThat(aclEntry1).isNotEqualTo(aclEntry2);
        aclEntry1.setId(null);
        assertThat(aclEntry1).isNotEqualTo(aclEntry2);
    }
}
