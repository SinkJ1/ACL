package sinkj1.security.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclClassDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclClassDTO.class);
        AclClassDTO aclClassDTO1 = new AclClassDTO();
        aclClassDTO1.setId(1L);
        AclClassDTO aclClassDTO2 = new AclClassDTO();
        assertThat(aclClassDTO1).isNotEqualTo(aclClassDTO2);
        aclClassDTO2.setId(aclClassDTO1.getId());
        assertThat(aclClassDTO1).isEqualTo(aclClassDTO2);
        aclClassDTO2.setId(2L);
        assertThat(aclClassDTO1).isNotEqualTo(aclClassDTO2);
        aclClassDTO1.setId(null);
        assertThat(aclClassDTO1).isNotEqualTo(aclClassDTO2);
    }
}
