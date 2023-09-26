import Constructor.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @DisplayName("Проверка логина юзера с валидными данными\n")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером \n" +
            "3. Удаляем юзера\n")
    @Test
    public void userLoginTest() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);
        User userEmailAndPass = new User(email, password);

        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");

        given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(CoreMatchers.containsString("user"))
                .body(CoreMatchers.containsString("accessToken"))
                .body(CoreMatchers.containsString("refreshToken"));


        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Проверка логина с пустым полем 'пароль'")
    @Description("1. Создаем юзера \n" +
            "2. Попытка логина созданным юзером с пустым полем пароль\n" +
            "3. Удаляем юзера\n")
    @Test
    public void userLoginWithEmptyPasswordTest() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);

        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," + "\"password\":\"\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));


        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Проверка логина с пустым полем 'email'")
    @Description("1. Создаем юзера \n" +
            "2. Попытка логина созданным юзером с пустым полем email\n" +
            "3. Удаляем юзера")
    @Test
    public void userLoginWithEmptyEmailTest() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);

        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"\"," + "\"password\":\"1234\"}")
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));


        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }


}
