import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static Configuration.Endpoints.CREATE_ORDER;
import static Configuration.Endpoints.LOGIN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class OrderCreationTest extends ConfigurationForTest {


    @DisplayName("Проверка создания заказа при полученном accessToken")
    @Description("1. Создаем юзера\n " +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа вместе с accessToken\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithTokenTest() {

        accessToken = given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
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
                .body(jsonIngredients)
                .when()
                .post(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @DisplayName("Проверка создания заказа без accessToken")
    @Description("1. Создание заказа без accessToken")
    //В документации нет ничего про ограничение на создание заказа неавторизованным пользователем,
    // однако, есть ощущение, что мы не должны иметь возможность создавать заказы без токена.
    // Поэтому исправил на ожидаемый код 401 Unauthorized хоть это и приводит к падению теста
    @Test
    public void createOrderWithoutTokenTest() {

        given()
                .contentType(ContentType.JSON)
                .body(jsonIngredients)
                .when()
                .post(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false));

    }

    @DisplayName("Проверка создания заказа с добавлением ингредиентов")
    @Description("1. Создаем юзера\n " +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа добавляя ингредиенты\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithIngredientsTest() {


        accessToken = given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
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
                .body(jsonIngredients)
                .when()
                .post(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @DisplayName("Проверка создания заказа без добавления ингредиентов")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа без ингредиентов\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithoutIngredientsTest() {

        accessToken = given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
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
                .post(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));


    }

    @DisplayName("Проверка создания заказа с добавлением несуществующих ID ингредиентов")
    @Description("1. Создаем юзера\n " +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа используя несуществующие ID в ингредиентах\n" +
            "4. Удаление юзера")
    @Test
    public void createOrderWithInvalidIngredientIDTest() {

        accessToken = given()
                .contentType(ContentType.JSON)
                .body(userEmailAndPass)
                .when()
                .post(LOGIN)
                .then()
                .assertThat()
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
                .post(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(500);


    }


}
