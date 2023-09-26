import Constructor.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class OrderFromUserTest {
    Random random = new Random();
    List<String> ingredients = Arrays.asList(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa70",
            "61c0c5a71d1f82001bdaaa71",
            "61c0c5a71d1f82001bdaaa72",
            "61c0c5a71d1f82001bdaaa6e",
            "61c0c5a71d1f82001bdaaa73",
            "61c0c5a71d1f82001bdaaa74",
            "61c0c5a71d1f82001bdaaa6c",
            "61c0c5a71d1f82001bdaaa75",
            "61c0c5a71d1f82001bdaaa76",
            "61c0c5a71d1f82001bdaaa77",
            "61c0c5a71d1f82001bdaaa78",
            "61c0c5a71d1f82001bdaaa79",
            "61c0c5a71d1f82001bdaaa7a"
    );
    String randomIngredient = ingredients.get(random.nextInt(ingredients.size()));


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }


    @DisplayName("Получение описания заказа с токеном авторизованного юзера")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа\n" +
            "4. Получение созданного заказа используя accessToken\n" +
            "5. Удаление юзера")
    @Test
    public void getAuthorizedUsersOrderTest() {
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


        accessToken = given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post("/api/auth/login")
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
                .header("Authorization", accessToken)
                .body("{\"ingredients\":[\"" + randomIngredient + "\",\"" + ingredients.get(random.nextInt(ingredients.size())) + "\" ]}")
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Получение описания заказа с токеном не авторизованного юзера")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа\n" +
            "4. Получение созданного заказа без accessToken\n" +
            "5. Удаление юзера")
    @Test
    public void getUnauthorizedUsersOrderTest() {
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


        accessToken = given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post("/api/auth/login")
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
                .header("Authorization", accessToken)
                .body("{\"ingredients\":[\"" + randomIngredient + "\",\"" + ingredients.get(random.nextInt(ingredients.size())) + "\" ]}")
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/orders")
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

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
