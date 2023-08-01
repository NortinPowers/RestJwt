package by.nortin.restjwt.repository;

import static by.nortin.restjwt.utils.Constants.TEST_PROPERTY_SOURCE_LOCATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.nortin.restjwt.domain.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = TEST_PROPERTY_SOURCE_LOCATIONS)
@Sql(value = "classpath:sql/role/role-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:sql/role/role-repository-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private String testRole;

    {
        testRole = "ROLE_USER";
    }

    @Test
    void test_findByName_isPresent() {
        Optional<Role> roleByRole = roleRepository.findByName(testRole);

        assertTrue(roleByRole.isPresent());
        assertEquals(testRole, roleByRole.get().getName());
    }

    @Test
    void test_findByName_isNotPresent() {
        testRole = "NotExistRole";

        Optional<Role> roleByRole = roleRepository.findByName(testRole);

        assertFalse(roleByRole.isPresent());
    }
}
