package trackup.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trackup.entity.Goal;
import trackup.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository; // Asegúrate de tener este repositorio también

    private User testUser;

    @BeforeEach
    void setUp() {
        // Crear y guardar un usuario para asociarlo con los objetivos
        testUser = new User();
        testUser.setUsername("Juan Pérez"); // Ajusta el nombre del usuario según sea necesario
        userRepository.save(testUser);  // Guardar el usuario en la base de datos
    }

    @Test
    @DisplayName("Test para crear y guardar un Goal correctamente")
    void testCreateGoal() {
        // Crear un Goal con los datos correctos
        Goal goal = new Goal(
                null,               // El ID es autogenerado por la base de datos, lo dejamos como null
                "Ejercicio",        // Nombre del objetivo
                "Hacer ejercicio regularmente", // Descripción del objetivo
                testUser            // Usuario asociado con este objetivo
        );

        Goal savedGoal = goalRepository.save(goal);  // Guardamos el Goal

        // Verificamos que el objetivo ha sido guardado correctamente
        assertThat(savedGoal).isNotNull();
        assertThat(savedGoal.getId()).isNotNull(); // El ID debe haberse generado
        assertThat(savedGoal.getName()).isEqualTo("Ejercicio");
        assertThat(savedGoal.getDescription()).isEqualTo("Hacer ejercicio regularmente");
        assertThat(savedGoal.getUser()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("Test para buscar un Goal por su ID")
    void testFindGoalById() {
        // Crear y guardar un Goal asociado al usuario
        Goal goal = new Goal(
                null,               // El ID es autogenerado
                "Leer libros",      // Nombre del objetivo
                "Leer un libro al mes", // Descripción del objetivo
                testUser            // Usuario asociado
        );
        Goal savedGoal = goalRepository.save(goal);  // Guardamos el Goal

        // Buscar el Goal por su ID
        Goal foundGoal = goalRepository.findById(savedGoal.getId()).orElse(null);

        // Verificamos que el Goal se haya encontrado correctamente
        assertThat(foundGoal).isNotNull();
        assertThat(foundGoal.getId()).isEqualTo(savedGoal.getId());
        assertThat(foundGoal.getName()).isEqualTo("Leer libros");
        assertThat(foundGoal.getDescription()).isEqualTo("Leer un libro al mes");
        assertThat(foundGoal.getUser()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("Test para eliminar un Goal")
    void testDeleteGoal() {
        // Crear y guardar un Goal
        Goal goal = new Goal(
                null,               // El ID es autogenerado
                "Aprender Java",    // Nombre del objetivo
                "Aprender Java en 6 meses", // Descripción del objetivo
                testUser            // Usuario asociado
        );
        Goal savedGoal = goalRepository.save(goal);  // Guardamos el Goal

        // Eliminar el Goal
        goalRepository.delete(savedGoal);

        // Verificamos que el Goal fue eliminado
        Goal deletedGoal = goalRepository.findById(savedGoal.getId()).orElse(null);
        assertThat(deletedGoal).isNull();  // El Goal ya no debe existir
    }

}