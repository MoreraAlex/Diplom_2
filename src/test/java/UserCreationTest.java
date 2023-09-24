import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.ParameterizedTest;
//import java.util.stream.Stream;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

//Создание пользователя + удаление пользователя. Импортировал junit.jupiter чтобы сделать отдельный параметризованный тест в рамках одного тестового класса, а не создавать отдельный класс для параметризованных тестов

public class UserCreationTest {

    @Before
    public void setUp()
    {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @DisplayName("Создание нового юзера")
    @Description("1. Создаем юзера \n" +
            "2. Удаляем юзера")
    @Test
    public void createUserTest()
    {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String name = java.util.UUID.randomUUID().toString();

        String accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," +
                        "\"password\":\"1234\"," +
                        "\"name\":\"" + name + "\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(CoreMatchers.containsString("user"))
                .body(CoreMatchers.containsString("accessToken"))
                .body(CoreMatchers.containsString("refreshToken"))
                .extract()
                .path("accessToken");



        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Создание нового юзера с существующими данными")
    @Description("1. Создаем юзера_1 \n" +
            "2. Попытка создать юзера_2 с данными уже созданного юзера_1\n" +
            "3.Удаляем юзера_1")
    @Test
    public void createUserThatAlreadyExistsTest()
    {
        String email = java.util.UUID.randomUUID() + "@gmail.com";
        String name = java.util.UUID.randomUUID().toString();

        String accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," +
                        "\"password\":\"1234\"," +
                        "\"name\":\"" + name + "\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200)
                .body(CoreMatchers.containsString("success"))
                .body(CoreMatchers.containsString("user"))
                .body(CoreMatchers.containsString("accessToken"))
                .body(CoreMatchers.containsString("refreshToken"))
                .extract()
                .path("accessToken");


        given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," +
                        "\"password\":\"1234\"," +
                        "\"name\":\"" + name + "\"}")
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403)
                .body("message", equalTo("User already exists"));

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


