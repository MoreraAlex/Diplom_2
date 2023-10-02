import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static Configuration.Endpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest extends ConfigurationForTest {

    @DisplayName("Проверка логина юзера с валидными данными\n")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером \n" +
            "3. Удаляем юзера\n")
    @Test
    public void userLoginTest() {

        given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(CoreMatchers.containsString("user"))
                .body(CoreMatchers.containsString("accessToken"))
                .body(CoreMatchers.containsString("refreshToken"));

    }

    @DisplayName("Проверка логина с пустым полем 'пароль'")
    @Description("1. Создаем юзера \n" +
            "2. Попытка логина созданным юзером с пустым полем пароль\n" +
            "3. Удаляем юзера\n")
    @Test
    public void userLoginWithEmptyPasswordTest() {

        given()
                .contentType(ContentType.JSON)
                .body(userWithEmptyPassword)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

    }

    @DisplayName("Проверка логина с пустым полем 'email'")
    @Description("1. Создаем юзера \n" +
            "2. Попытка логина созданным юзером с пустым полем email\n" +
            "3. Удаляем юзера")
    @Test
    public void userLoginWithEmptyEmailTest() {

        given()
                .contentType(ContentType.JSON)
                .body(userWithEmptyEmail)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));


    }


}
