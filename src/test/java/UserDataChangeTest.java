import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static Configuration.Endpoints.USER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDataChangeTest extends ConfigurationForTest {


    @DisplayName("Изменить имя авторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера \n" + "3. Проверяем, что данные изменились \n" + "4. Удаляем юзера")
    @Test
    public void changeAuthorizedUserName() {
        given()
                .contentType(ContentType.JSON).header("Authorization", accessToken)
                .body("{\"email\":\"" + email + "\"," + "\"name\":\"1234\"}")
                .when()
                .patch(USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get(USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("user.name", equalTo("1234"));
    }

    @DisplayName("Изменить email авторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера \n" + "3. Проверяем, что данные изменились \n" + "4. Удаляем юзера \n")
    @Test
    public void changeAuthorizedUserEmail() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .body("{\"email\":\"" + email + "changed\"," + "\"name\":\"" + name + "\"}")
                .when()
                .patch(USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get(USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("user.email", equalTo(email + "changed"));

    }

    @DisplayName("Изменить имя неавторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера без токена \n" + "3. Проверяем, что данные не изменились \n" + "4. Удаляем юзера")
    @Test
    public void changeUnauthorizedUserName() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," + "\"name\":\"1234\"}")
                .when()
                .patch(USER)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get(USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("user.name", equalTo(name));

    }

    @DisplayName("Изменить email неавторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера без токена \n" + "3. Проверяем, что данные не изменились \n" + "4. Удаляем юзера")
    @Test
    public void changeUnauthorizedUserEmail() {

        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"CHANGE@testing.com\"," + "\"name\":\"" + name + "\"}")
                .when()
                .patch(USER)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get(USER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("user.email", equalTo(email));

    }


}
