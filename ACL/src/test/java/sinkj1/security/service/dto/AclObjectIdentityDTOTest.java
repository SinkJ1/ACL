package sinkj1.security.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclObjectIdentityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclObjectIdentityDTO.class);
        AclObjectIdentityDTO aclObjectIdentityDTO1 = new AclObjectIdentityDTO();
        aclObjectIdentityDTO1.setId(1L);
        AclObjectIdentityDTO aclObjectIdentityDTO2 = new AclObjectIdentityDTO();
        assertThat(aclObjectIdentityDTO1).isNotEqualTo(aclObjectIdentityDTO2);
        aclObjectIdentityDTO2.setId(aclObjectIdentityDTO1.getId());
        assertThat(aclObjectIdentityDTO1).isEqualTo(aclObjectIdentityDTO2);
        aclObjectIdentityDTO2.setId(2L);
        assertThat(aclObjectIdentityDTO1).isNotEqualTo(aclObjectIdentityDTO2);
        aclObjectIdentityDTO1.setId(null);
        assertThat(aclObjectIdentityDTO1).isNotEqualTo(aclObjectIdentityDTO2);
    }
}
