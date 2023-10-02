import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;

import static Configuration.Endpoints.CREATE_ORDER;
import static Configuration.Endpoints.LOGIN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class OrderFromUserTest extends ConfigurationForTest {


    @DisplayName("Получение описания заказа с токеном авторизованного юзера")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа\n" +
            "4. Получение созданного заказа используя accessToken\n" +
            "5. Удаление юзера")
    @Test
    public void getAuthorizedUsersOrderTest() {

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

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .get(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @DisplayName("Получение описания заказа с токеном не авторизованного юзера")
    @Description("1. Создаем юзера \n" +
            "2. Логин созданным юзером\n" +
            "3. Создание заказа\n" +
            "4. Получение созданного заказа без accessToken\n" +
            "5. Удаление юзера")
    @Test
    public void getUnauthorizedUsersOrderTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(CREATE_ORDER)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }
}
