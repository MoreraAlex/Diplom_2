import Constructor.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static Configuration.Endpoints.REGISTER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UserCreationWithEmptyFieldTest extends ConfigurationForTest {

    private String email;
    private String password;
    private String name;
    private int expectedStatusCode;
    private String expectedErrorMessage;

    public UserCreationWithEmptyFieldTest(String email, String password, String name, int expectedStatusCode, String expectedErrorMessage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][]{
                {"", "1234567", "Alex", 403, "Email, password and name are required fields"},
                {"test@yandex.com", "", "Alex", 403, "Email, password and name are required fields"},
                {"test@yandex.com", "1234", "", 403, "Email, password and name are required fields"},
        };
    }

    @DisplayName("Создание юзера с отсутствующим обязательным полем")
    @Description("1. Попытка создать юзера с отсутствующим обязательным полем")
    @Test
    public void createUserWithEmptyFieldTest() {
        User user = new User(email, password, name);

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .assertThat()
                .statusCode(expectedStatusCode)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedErrorMessage));
    }
}
