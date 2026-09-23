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

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * POST /web/auth/reset-password - "00. Auth & Session / Auth / Reset Password".
 *
 * No positive scenario is automated: a valid reset token is only ever delivered by
 * email (via Forgot Password), and this environment provides no safe/programmatic
 * way to retrieve that email, nor is it safe to actually change this shared QA
 * account's password from an automated suite. Per Step 8, this is documented
 * rather than invented.
 */
@Epic("BrandRunners Web API")
@Feature("Authentication")
public class ResetPasswordApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "negative"}, description = "Resetting a password with an invalid token fails")
    @Story("Reset Password")
    @Description("Verified live: HTTP 400, message 'Invalid or expired reset token' - same message/status as " +
            "Verify Reset Token's equivalent case, consistent with shared token-validation logic.")
    public void resetPasswordWithInvalidTokenFails() throws Exception {
        Response response = resetPassword("invalid-token-xyz", "NewPassword@123");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid or expired reset token");
    }

    @Test(groups = { "regression", "negative"}, description = "Resetting a password with the password field missing is rejected")
    @Story("Reset Password")
    @Description("Verified live: HTTP 400, message 'Validation Error', errors.password contains 'Password is required'.")
    public void resetPasswordWithMissingPasswordFieldIsRejected() throws Exception {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("token", "invalid-token-xyz");
        Response response = given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body(new ObjectMapper().writeValueAsString(body))
                .post("/web/auth/reset-password");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Validation Error");
        Assert.assertTrue(response.jsonPath().getList("errors.password", String.class).contains("Password is required"));
    }

    private static Response resetPassword(String token, String newPassword) throws Exception {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("token", token);
        body.put("password", newPassword);
        return given().spec(noAuthRequestSpecification)
                .contentType("application/json")
                .body(new ObjectMapper().writeValueAsString(body))
                .post("/web/auth/reset-password");
    }
}
