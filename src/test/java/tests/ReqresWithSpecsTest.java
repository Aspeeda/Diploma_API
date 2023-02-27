package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import models.LoginBodyModel;
import models.LoginResponseModel;
import models.NewAccModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.CreateSpecs.createRequestSpec;
import static specs.CreateSpecs.createResponseSpec;
import static specs.LoginSpecs.*;

@Epic("Diploma project")
@Story("Testing API")
@Owner("Darya Melyanovskaya")

public class ReqresWithSpecsTest extends TestBase {

    @Test
    @DisplayName("Log in with correct data")
    void loginTest() {
        LoginBodyModel data = new LoginBodyModel();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("cityslicka");

        LoginResponseModel response = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    @DisplayName("Log in with missing e-mail")
    void missingEmailOrUsernameTest() {
        LoginBodyModel data = new LoginBodyModel();
        data.setEmail("");
        data.setPassword("cityslicka");

        LoginResponseModel response = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpecMissingData)
                .extract().as(LoginResponseModel.class);

        assertThat(response.getError()).isEqualTo("Missing email or username");
    }

    @Test
    @DisplayName("Log in with missing password")
    void missingPasswordTest() {
        LoginBodyModel data = new LoginBodyModel();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("");

        LoginResponseModel response = given(loginRequestSpec)
                .body(data)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpecMissingData)
                .extract().as(LoginResponseModel.class);

        assertThat(response.getError()).isEqualTo("Missing password");
    }

    @Test
    @DisplayName("bad request")
    void NotFound404ResourceTest() {

        get("/unknown/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create new account")
    void createNewAccountTest() {

        NewAccModel newAccModel = new NewAccModel();
        newAccModel.setName("morpheus");
        newAccModel.setJob("leader");


        NewAccModel response = given(createRequestSpec)
                .body(newAccModel)
                .when()
                .post("/users")
                .then()
                .spec(createResponseSpec)
                .extract().as(NewAccModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
    }

    @Test
    @Disabled("Требуется доработка")
    @DisplayName("try to enter with unsupported media")
    void unSupportedMediaTypeTest() {
        LoginBodyModel data = new LoginBodyModel();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("cityslicka");

        LoginResponseModel response = given(loginRequestSpecNonTyped)
                .when()
                .post("/login")
                .then()
                .spec(loginResponseSpecUnsupportedMedia)
                .extract().as(LoginResponseModel.class);

        assertThat(response.getError()).isEqualTo("Unsupported Media Type");
    }
}
