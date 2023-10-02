import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import static Configuration.Endpoints.REGISTER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;


public class UserCreationTest extends ConfigurationForTest {


    @DisplayName("Создание нового юзера")
    @Description("1. Создаем юзера \n" +
            " 2. Проверяем, что accessToken получен \n" +
            "3. Удаляем юзера")
    @Test
    public void createUserTest() {
        Assert.assertTrue("Токен отсутствует, пользователь не создан", accessToken != null);
    }

    @DisplayName("Создание нового юзера с существующими данными")
    @Description("1. Создаем юзера_1 \n" +
            "2. Попытка создать юзера_2 с данными уже созданного юзера_1\n" +
            "3.Удаляем юзера_1")
    @Test
    public void createUserThatAlreadyExistsTest() {

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo("User already exists"));

    }

}


