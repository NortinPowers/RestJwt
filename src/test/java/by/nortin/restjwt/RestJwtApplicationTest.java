package by.nortin.restjwt;

import by.nortin.restjwt.controller.AdminController;
import by.nortin.restjwt.controller.AuthController;
import by.nortin.restjwt.controller.BookController;
import by.nortin.restjwt.controller.MainController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestJwtApplicationTest {

    @Autowired
    private AdminController adminController;

    @Autowired
    private AuthController authController;

    @Autowired
    private BookController bookController;

    @Autowired
    private MainController mainController;

    @Test
    void test_adminController_notNull() {
        Assertions.assertThat(adminController).isNotNull();
    }

    @Test
    void test_authController_notNull() {
        Assertions.assertThat(authController).isNotNull();
    }

    @Test
    void test_bookController_notNull() {
        Assertions.assertThat(bookController).isNotNull();
    }

    @Test
    void test_mainController_notNull() {
        Assertions.assertThat(mainController).isNotNull();
    }
}
