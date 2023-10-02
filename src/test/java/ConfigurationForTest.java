import Constructor.IngredientsList;
import Constructor.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;

import static Configuration.Endpoints.REGISTER;
import static Configuration.Endpoints.USER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class ConfigurationForTest {

    String email = java.util.UUID.randomUUID() + "@gmail.com";
    String password = java.util.UUID.randomUUID().toString();
    String name = java.util.UUID.randomUUID().toString();

    User user = new User(email, password, name);
    User userEmailAndPass = new User(email, password);
    User userWithEmptyPassword = new User(email, "");
    User userWithEmptyEmail = new User("", password);

    String accessToken;
    String jsonIngredients = IngredientsList.ingredients();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        accessToken = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body(containsString("user"))
                .body(containsString("accessToken"))
                .body(containsString("refreshToken"))
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", accessToken)
                    .when()
                    .delete(USER)
                    .then()
                    .assertThat()
                    .statusCode(202)
                    .body("message", equalTo("User successfully removed"));

        }
    }
}
