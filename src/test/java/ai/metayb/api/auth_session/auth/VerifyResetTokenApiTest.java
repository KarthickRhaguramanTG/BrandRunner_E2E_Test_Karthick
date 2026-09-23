package ai.metayb.api.auth_session.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * POST /web/auth/verify-reset-token - "00. Auth & Session / Auth / Verify Reset Token".
 *
 * No positive scenario is automated: a valid reset token is only ever delivered by
 * email (via Forgot Password), and this environment provides no safe/programmatic
 * way to retrieve that email. Per Step 8, this is documented rather than invented.
 */
@Epic("BrandRunners Web API")
@Feature("Authentication")
public class VerifyResetTokenApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "negative"}, description = "Verifying an invalid reset token fails")
    @Story("Verify Reset Token")
    @Description("Verified live: HTTP 400, message 'Invalid or expired reset token'.")
    public void verifyResetTokenWithInvalidTokenFails() throws Exception {
        Response response = verifyResetToken("invalid-token-xyz");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid or expired reset token");
    }

    @Test(groups = {"sanity", "regression", "negative"}, description = "Verifying with the token field missing fails")
    @Story("Verify Reset Token")
    @Description("Verified live: HTTP 400, message 'Token is required'.")
    public void verifyResetTokenWithMissingTokenFieldFails() throws Exception {
        Response response = given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body(new ObjectMapper().writeValueAsString(Collections.emptyMap()))
                .post("/web/auth/verify-reset-token");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Token is required");
    }

    private static Response verifyResetToken(String token) throws Exception {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("token", token);
        return given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body(new ObjectMapper().writeValueAsString(body))
                .post("/web/auth/verify-reset-token");
    }
}
