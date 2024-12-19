package br.com.poimen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.poimen.domain.ApplicationUser;
import br.com.poimen.domain.User;
import br.com.poimen.domain.enumeration.UserStatus;
import br.com.poimen.repository.ApplicationUserRepository;
import br.com.poimen.repository.UserRepository;
import br.com.poimen.security.SecurityUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ApplicationUserServiceIT {

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setLogin("testuser");
        user.setPassword("password");
        user.setActivated(true);
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        userRepository.saveAndFlush(user);

        Mockito.when(SecurityUtils.getCurrentUserLogin()).thenReturn(Optional.of(user.getLogin()));
    }

    @Test
    public void testCreateApplicationUserForLoggedInUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setName("Test Application User");
        applicationUser.setDescription("Test Description");
        applicationUser.setStatus(UserStatus.ACTIVE); // Defina o status

        ApplicationUser savedApplicationUser = applicationUserService.save(applicationUser);

        assertThat(savedApplicationUser).isNotNull();
        assertThat(savedApplicationUser.getInternalUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void testCreateApplicationUserForUserWithExistingApplicationUser() {
        ApplicationUser existingApplicationUser = new ApplicationUser();
        existingApplicationUser.setInternalUser(user);
        applicationUserRepository.saveAndFlush(existingApplicationUser);

        ApplicationUser newApplicationUser = new ApplicationUser();

        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setName("Test Application User");
        applicationUser.setDescription("Test Description");
        applicationUser.setStatus(UserStatus.ACTIVE); // Defina o status

        assertThatThrownBy(() -> applicationUserService.save(newApplicationUser))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("User already has an ApplicationUser");
    }
}
