package ai.metayb.api.auth_session.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * POST /web/auth/refresh - "00. Auth & Session / Auth / Refresh Token".
 * Unauthenticated in Postman (noauth) - the refresh token itself is the credential.
 */
@Epic("BrandRunners Web API")
@Feature("Authentication")
public class RefreshTokenApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Refreshing with a valid refresh token succeeds")
    @Story("Token Refresh")
    @Description("Verified live: reuses the refreshToken captured at suite login. Confirmed safe to reuse - " +
            "this API does not rotate or invalidate refresh tokens on use, and does not invalidate the prior " +
            "access token either, so this does not interfere with other tests sharing the suite-level session.")
    public void refreshWithValidRefreshTokenSucceeds() throws Exception {
        Allure.step("POST /web/auth/refresh (refreshToken=***masked***)");
        Response response = refresh(refreshToken);

        Assert.assertEquals(response.statusCode(), 200, "Valid refresh token should return HTTP 200");
        Assert.assertEquals(response.jsonPath().getString("message"), "Tokens refreshed successfully");
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Refreshing with an invalid refresh token fails")
    @Story("Token Refresh")
    @Description("Verified live: HTTP 500, message 'invalid token'. Documented as observed behavior - " +
            "a malformed/garbage token surfacing as 500 rather than 401 is an application-side finding, " +
            "not something this automation can correct.")
    public void refreshWithInvalidRefreshTokenFails() throws Exception {
        Response response = refresh("invalid.refresh.token");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("message"), "invalid token");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Refreshing with the refreshToken field missing fails")
    @Story("Token Refresh")
    @Description("Verified live: HTTP 401, message 'Refresh token missing'.")
    public void refreshWithMissingRefreshTokenFieldFails() throws Exception {
        Response response = given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body(new ObjectMapper().writeValueAsString(Collections.emptyMap()))
                .post("/web/auth/refresh");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Refresh token missing");
    }

    private static Response refresh(String refreshTokenValue) throws Exception {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("refreshToken", refreshTokenValue);
        return given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body(new ObjectMapper().writeValueAsString(body))
                .post("/web/auth/refresh");
    }

}
