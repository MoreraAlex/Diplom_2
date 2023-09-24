import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UserCreationWithEmptyFieldTest {
    @Before
    public void setUp()
    {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

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
                {"", "1234", "Alex", 403, "Email, password and name are required fields"},
                {"test@yandex,com", "", "Alex", 403, "Email, password and name are required fields"},
                {"test@yandex,com", "1234", "", 403, "Email, password and name are required fields"},
        };
    }

    @DisplayName("Создание юзера с отсутствующим обязательным полем")
    @Description("1. Попытка создать юзера с отсутствующим обязательным полем")
    @Test
    public void createUserWithEmptyFieldTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," +
                        "\"password\":\"" + password + "\"," +
                        "\"name\":\"" + name + "\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(expectedStatusCode)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedErrorMessage));
    }
}
