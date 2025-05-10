package trackup.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trackup.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // por defecto levanta H2 en memoria
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("guardar y recuperar un usuario por ID")
    void whenSaveUser_thenFindById() {
        User u = new User();
        u.setUsername("juan");
        u.setEmail("juan@example.com");
        u.setPassword("secret123");           // <-- ahora seteamos password

        User saved = userRepository.saveAndFlush(u);

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("juan");
    }

    @Test
    @DisplayName("buscar por username existente")
    void whenFindByUsername_thenReturnUser() {
        User u = new User();
        u.setUsername("maria");
        u.setEmail("maria@example.com");
        u.setPassword("pw-maria");           // <-- seteamos password

        userRepository.saveAndFlush(u);

        Optional<User> found = userRepository.findByUsername("maria");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("maria@example.com");
    }

    @Test
    @DisplayName("buscar por username no existente devuelve vacío")
    void whenFindByUnknownUsername_thenEmpty() {
        Optional<User> found = userRepository.findByUsername("inexistente");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("buscar por email existente")
    void whenFindByEmail_thenReturnUser() {
        User u = new User();
        u.setUsername("pedro");
        u.setEmail("pedro@example.com");
        u.setPassword("pw-pedro");           // <-- seteamos password

        userRepository.saveAndFlush(u);

        Optional<User> found = userRepository.findByEmail("pedro@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("pedro");
    }

    @Test
    @DisplayName("buscar por email no existente devuelve vacío")
    void whenFindByUnknownEmail_thenEmpty() {
        Optional<User> found = userRepository.findByEmail("no@existe.com");
        assertThat(found).isEmpty();
    }

}