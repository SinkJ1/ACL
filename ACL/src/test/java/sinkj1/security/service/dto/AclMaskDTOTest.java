package sinkj1.security.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclMaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclMaskDTO.class);
        AclMaskDTO aclMaskDTO1 = new AclMaskDTO();
        aclMaskDTO1.setId(1L);
        AclMaskDTO aclMaskDTO2 = new AclMaskDTO();
        assertThat(aclMaskDTO1).isNotEqualTo(aclMaskDTO2);
        aclMaskDTO2.setId(aclMaskDTO1.getId());
        assertThat(aclMaskDTO1).isEqualTo(aclMaskDTO2);
        aclMaskDTO2.setId(2L);
        assertThat(aclMaskDTO1).isNotEqualTo(aclMaskDTO2);
        aclMaskDTO1.setId(null);
        assertThat(aclMaskDTO1).isNotEqualTo(aclMaskDTO2);
    }
}
