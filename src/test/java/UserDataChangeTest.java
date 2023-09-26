import Constructor.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDataChangeTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @DisplayName("Изменить имя авторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера \n" + "3. Проверяем, что данные изменились \n" + "4. Удаляем юзера")
    @Test
    public void changeAuthorizedUserName() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);

        String accessToken = given().contentType(ContentType.JSON).body(user).when().post("/api/auth/register").then().statusCode(200).body("success", equalTo(true)).body(containsString("user")).body(containsString("accessToken")).body(containsString("refreshToken")).extract().path("accessToken");

        given().contentType(ContentType.JSON).header("Authorization", accessToken).body("{\"email\":\"" + email + "\"," + "\"name\":\"1234\"}").when().patch("/api/auth/user").then().statusCode(200).body("success", equalTo(true));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().get("/api/auth/user").then().statusCode(200).body("user.name", equalTo("1234"));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().delete("/api/auth/user").then().statusCode(202).body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Изменить email авторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера \n" + "3. Проверяем, что данные изменились \n" + "4. Удаляем юзера \n")
    @Test
    public void changeAuthorizedUserEmail() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);

        String accessToken = given().contentType(ContentType.JSON).body(user).when().post("/api/auth/register").then().statusCode(200).body("success", equalTo(true)).body(containsString("user")).body(containsString("accessToken")).body(containsString("refreshToken")).extract().path("accessToken");

        given().contentType(ContentType.JSON).header("Authorization", accessToken).body("{\"email\":\"" + email + "changed\"," + "\"name\":\"" + name + "\"}").when().patch("/api/auth/user").then().statusCode(200).body("success", equalTo(true));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().get("/api/auth/user").then().statusCode(200).body("user.email", equalTo(email + "changed"));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().delete("/api/auth/user").then().statusCode(202).body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Изменить имя неавторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера без токена \n" + "3. Проверяем, что данные не изменились \n" + "4. Удаляем юзера")
    @Test
    public void changeUnauthorizedUserName() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);

        String accessToken = given().contentType(ContentType.JSON).body(user).when().post("/api/auth/register").then().statusCode(200).body("success", equalTo(true)).body(containsString("user")).body(containsString("accessToken")).body(containsString("refreshToken")).extract().path("accessToken");

        given().contentType(ContentType.JSON).body("{\"email\":\"" + email + "\"," + "\"name\":\"1234\"}").when().patch("/api/auth/user").then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("You should be authorised"));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().get("/api/auth/user").then().statusCode(200).body("user.name", equalTo(name));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().delete("/api/auth/user").then().statusCode(202).body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Изменить имя неавторизованного юзера")
    @Description("1. Создаем юзера \n" + "2. Пробуем изменить данные юзера без токена \n" + "3. Проверяем, что данные не изменились \n" + "4. Удаляем юзера")


    @Test
    public void changeUnauthorizedUserEmail() {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String password = java.util.UUID.randomUUID().toString();
        String name = java.util.UUID.randomUUID().toString();

        User user = new User(email, password, name);

        String accessToken = given().contentType(ContentType.JSON).body(user).when().post("/api/auth/register").then().statusCode(200).body("success", equalTo(true)).body(containsString("user")).body(containsString("accessToken")).body(containsString("refreshToken")).extract().path("accessToken");

        given().contentType(ContentType.JSON).body("{\"email\":\"CHANGE@testing.com\"," + "\"name\":\"" + name + "\"}").when().patch("/api/auth/user").then().statusCode(401).body("success", equalTo(false)).body("message", equalTo("You should be authorised"));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().get("/api/auth/user").then().statusCode(200).body("user.email", equalTo(email));

        given().contentType(ContentType.JSON).header("Authorization", accessToken).when().delete("/api/auth/user").then().statusCode(202).body("message", equalTo("User successfully removed"));
    }


}
