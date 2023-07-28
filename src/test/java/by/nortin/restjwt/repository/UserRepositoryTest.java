package by.nortin.restjwt.repository;

import static by.nortin.restjwt.Utils.Constants.TEST_PROPERTY_SOURCE_LOCATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import by.nortin.restjwt.domain.User;
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
@Sql(value = "classpath:sql/user/user-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:sql/user/user-repository-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private String username;

    {
        username = "admin";
    }

    @Test
    void test_findByUserName_isPresent() {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        assertTrue(optionalUser.isPresent());
        assertEquals(username, optionalUser.get().getUserName());
    }

    @Test
    void test_findByUserName_isNotPresent() {
        username = "nonExistUser";
        Optional<User> optionalUser = userRepository.findByUserName(username);

        assertFalse(optionalUser.isPresent());
    }
}
