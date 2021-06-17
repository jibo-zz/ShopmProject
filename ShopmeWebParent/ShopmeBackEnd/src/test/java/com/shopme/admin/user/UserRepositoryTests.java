package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userMohaOM = new User("moha@example.com", "moha20", "Mohamed", "Omar");
        userMohaOM.addRole(roleAdmin);

        User savedUser = repo.save(userMohaOM);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userMarwa = new User("marwa@example.com", "marwa20", "Marwa", "Omar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userMarwa.addRole(roleEditor);
        userMarwa.addRole(roleAssistant);

        User savedUser = repo.save(userMarwa);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers =  repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userMoha  = repo.findById(1).get();
        System.out.println(userMoha);

        assertThat(userMoha).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userMoha  = repo.findById(1).get();
        userMoha.setEnabled(true);
        userMoha.setEmail("mirotawa@gmail.com");

        repo.save(userMoha);
    }

    @Test
    public void testUpdateUserRoles() {
        User userMarwa  = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSaleswoman = new Role(2);

        userMarwa.getRoles().remove(roleEditor);
        userMarwa.addRole(roleSaleswoman);

        repo.save(userMarwa);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        repo.deleteById(userId);
    }
}
