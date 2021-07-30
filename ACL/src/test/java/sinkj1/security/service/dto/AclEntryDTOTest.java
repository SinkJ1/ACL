package sinkj1.security.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sinkj1.security.web.rest.TestUtil;

class AclEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclEntryDTO.class);
        AclEntryDTO aclEntryDTO1 = new AclEntryDTO();
        aclEntryDTO1.setId(1L);
        AclEntryDTO aclEntryDTO2 = new AclEntryDTO();
        assertThat(aclEntryDTO1).isNotEqualTo(aclEntryDTO2);
        aclEntryDTO2.setId(aclEntryDTO1.getId());
        assertThat(aclEntryDTO1).isEqualTo(aclEntryDTO2);
        aclEntryDTO2.setId(2L);
        assertThat(aclEntryDTO1).isNotEqualTo(aclEntryDTO2);
        aclEntryDTO1.setId(null);
        assertThat(aclEntryDTO1).isNotEqualTo(aclEntryDTO2);
    }
}
