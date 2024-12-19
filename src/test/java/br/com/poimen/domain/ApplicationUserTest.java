package br.com.poimen.domain;

import static br.com.poimen.domain.ApplicationUserTestSamples.*;
import static br.com.poimen.domain.ChurchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.poimen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void churchTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Church churchBack = getChurchRandomSampleGenerator();

        applicationUser.setChurch(churchBack);
        assertThat(applicationUser.getChurch()).isEqualTo(churchBack);

        applicationUser.church(null);
        assertThat(applicationUser.getChurch()).isNull();
    }
}
