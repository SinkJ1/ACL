package sinkj1.security.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclSidDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclSidDTO.class);
        AclSidDTO aclSidDTO1 = new AclSidDTO();
        aclSidDTO1.setId(1L);
        AclSidDTO aclSidDTO2 = new AclSidDTO();
        assertThat(aclSidDTO1).isNotEqualTo(aclSidDTO2);
        aclSidDTO2.setId(aclSidDTO1.getId());
        assertThat(aclSidDTO1).isEqualTo(aclSidDTO2);
        aclSidDTO2.setId(2L);
        assertThat(aclSidDTO1).isNotEqualTo(aclSidDTO2);
        aclSidDTO1.setId(null);
        assertThat(aclSidDTO1).isNotEqualTo(aclSidDTO2);
    }
}
