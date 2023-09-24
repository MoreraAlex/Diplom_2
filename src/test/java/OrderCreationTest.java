import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class OrderCreationTest {
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
    public void setUp()
    {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @After
    public void tearDown() {
        if (Status.FAILED.equals(Allure.getLifecycle().getCurrentTestCase())) {
            // Добавьте скриншоты, логи или другие аттачи в случае ошибки
        }
    }

    @DisplayName("Проверка создания заказа при полученном accessToken")
    @Description("1. Создаем юзера\n " +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа вместе с accessToken\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithTokenTest() {
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
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");


        accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," + "\"password\":\"1234\"}")
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
                .body("{\"ingredients\":[\"" + randomIngredient +  "\",\"" + ingredients.get(random.nextInt(ingredients.size())) +"\" ]}")
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
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Проверка создания заказа без accessToken")
    @Description("1. Создание заказа без accessToken")
    @Test
    public void createOrderWithoutTokenTest() {

        given()
                .contentType(ContentType.JSON)
                .body("{\"ingredients\":[\"" + randomIngredient +  "\",\"" + ingredients.get(random.nextInt(ingredients.size())) +"\" ]}")
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());

    }

    @DisplayName("Проверка создания заказа с добавлением ингредиентов")
    @Description("1. Создаем юзера\n " +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа добавляя ингредиенты\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithIngredientsTest() {
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
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");


        accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," + "\"password\":\"1234\"}")
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
                .body("{\"ingredients\":[\"" + randomIngredient +  "\",\"" + ingredients.get(random.nextInt(ingredients.size())) +"\" ]}")
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
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Проверка создания заказа без добавления ингредиентов")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа без ингредиентов\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithoutIngredientsTest() {
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
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");


        accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," + "\"password\":\"1234\"}")
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
                .body("{\"ingredients\":[]}")
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));



        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202)
                .body("message", equalTo("User successfully removed"));
    }

    @DisplayName("Проверка создания заказа с добавлением несуществующих ID ингредиентов")
    @Description("1. Создаем юзера\n " +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа используя несуществующие ID в ингредиентах\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithInvalidIngredientIDTest() {
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
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");


        accessToken = given()
                .contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\"," + "\"password\":\"1234\"}")
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
                .body("{\"ingredients\":[\"testid0123\"]}")
                .when()
                .post("/api/orders")
                .then()
                .statusCode(500);



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
