package ai.metayb.api.auth_session.auth;

import ai.metayb.api.utils.SanitizedApiLoggingFilter;
import ai.metayb.config.ConfigManager;
import ai.metayb.ui.core.DataReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * POST /web/auth/login - "BrandRunners Web APIs" Postman collection,
 * folder "00. Auth & Session / Auth", request "Login".
 *
 * Every other request in the collection depends on what this one produces
 * (accessToken cookie -> Authorization: Bearer header, X-Amz-Tenant-Id), so
 * it is the first API automated against this collection.
 *
 * Negative cases below were verified live against the real QA environment
 * (not assumed) - see each test's comment for the exact observed status/message.
 */
@Epic("BrandRunners Web API")
@Feature("Authentication")
public class LoginApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Login with valid QA credentials returns an access token and business unit")
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Reproduces the Postman collection's Login test script: asserts HTTP 200 " +
            "(pm.test('Login succeeded')), then confirms the accessToken cookie and " +
            "data.user.businessInfo[0].id that every other authenticated request depends on are present.")
    public void loginReturnsAccessTokenAndBusinessUnit() throws Exception {
        DataReader credentials = new DataReader();

        Allure.step("POST /web/auth/login (email=" + maskEmail(credentials.apiEmail) + ", password=***)");
        Response response = performLogin(credentials.apiEmail, credentials.apiPassword);

        Allure.step("Response status: " + response.statusCode());
        Assert.assertEquals(response.statusCode(), 200, "Login should return HTTP 200");

        String accessToken = response.getCookie("accessToken");
        Assert.assertNotNull(accessToken, "Login response must set an 'accessToken' cookie");
        Assert.assertFalse(accessToken.isBlank(), "'accessToken' cookie must not be blank");
        Allure.step("accessToken cookie present: ***masked***");

        // businessInfo is an ARRAY in the real response (verified live) - Postman's own script
        // makes the same assumption (json?.data?.user?.businessInfo?.id would actually resolve
        // to undefined on a real array without [0], but that's Postman's script, not ours).
        String businessUnitId = response.jsonPath().getString("data.user.businessInfo[0].id");
        Assert.assertNotNull(businessUnitId, "Response should include data.user.businessInfo[0].id");
        Assert.assertFalse(businessUnitId.isBlank(), "data.user.businessInfo[0].id must not be blank");
        Allure.step("data.user.businessInfo[0].id: " + businessUnitId);
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with a wrong password is rejected")
    @Story("User Login")
    @Description("Verified live: wrong password returns HTTP 400 with message 'Login Failed' " +
            "(not 401 - this API uses 400 uniformly for auth failures).")
    public void loginWithWrongPasswordIsRejected() throws Exception {
        DataReader credentials = new DataReader();
        Response response = performLogin(credentials.apiEmail, "WrongPassword@999");

        Assert.assertEquals(response.statusCode(), 400, "Wrong password should return HTTP 400");
        Assert.assertEquals(response.jsonPath().getString("message"), "Login Failed");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with a non-existent email is rejected")
    @Story("User Login")
    @Description("Verified live: an unregistered email returns the SAME HTTP 400 'Login Failed' " +
            "message as a wrong password - this API does not reveal whether an email is registered.")
    public void loginWithNonExistentEmailIsRejected() throws Exception {
        Response response = performLogin("nonexistent-user-xyz@metayb.ai", "Whatever@123");

        Assert.assertEquals(response.statusCode(), 400, "Non-existent email should return HTTP 400");
        Assert.assertEquals(response.jsonPath().getString("message"), "Login Failed");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with a missing password field is rejected with a validation error")
    @Story("User Login")
    @Description("Verified live: HTTP 400, message 'Validation Error', errors.password contains 'Password is required'.")
    public void loginWithMissingPasswordFieldIsRejected() throws Exception {
        DataReader credentials = new DataReader();
        Response response = loginRequest(credentials.apiEmail, null, ConfigManager.getApiTenant());

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Validation Error");
        Assert.assertTrue(response.jsonPath().getList("errors.password", String.class).contains("Password is required"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with a missing email field is rejected with a validation error")
    @Story("User Login")
    @Description("Verified live: HTTP 400, message 'Validation Error', errors.email contains 'Email is required'.")
    public void loginWithMissingEmailFieldIsRejected() throws Exception {
        DataReader credentials = new DataReader();
        Response response = loginRequest(null, credentials.apiPassword, ConfigManager.getApiTenant());

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Validation Error");
        Assert.assertTrue(response.jsonPath().getList("errors.email", String.class).contains("Email is required"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with a malformed email format is rejected with a validation error")
    @Story("User Login")
    @Description("Verified live: HTTP 400, message 'Validation Error', errors.email contains 'Email must be a email'.")
    public void loginWithMalformedEmailIsRejected() throws Exception {
        DataReader credentials = new DataReader();
        Response response = loginRequest("not-an-email", credentials.apiPassword, ConfigManager.getApiTenant());

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Validation Error");
        Assert.assertTrue(response.jsonPath().getList("errors.email", String.class).contains("Email must be a email"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with the tenant header missing is rejected")
    @Story("User Login")
    @Description("Verified live: HTTP 400, message explains tenancy is required " +
            "('The invoked function is enabled with tenancy configuration...').")
    public void loginWithMissingTenantHeaderIsRejected() throws Exception {
        DataReader credentials = new DataReader();
        Response response = loginRequest(credentials.apiEmail, credentials.apiPassword, null);

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("tenant"),
                "Expected a tenancy-related message, got: " + response.jsonPath().getString("message"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with an invalid tenant header is rejected")
    @Story("User Login")
    @Description("Verified live: HTTP 400, message 'Database connection failed, Please check the tenant ID' " +
            "- this app resolves tenants to per-tenant database connections.")
    public void loginWithInvalidTenantHeaderIsRejected() throws Exception {
        DataReader credentials = new DataReader();
        Response response = loginRequest(credentials.apiEmail, credentials.apiPassword, "nonexistent-tenant-xyz");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Database connection failed, Please check the tenant ID");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Login with a malformed JSON body fails")
    @Story("User Login")
    @Description("Verified live: HTTP 500 with a raw JSON-parser error message - the API does not " +
            "handle malformed JSON gracefully (returns 500, not 400, and leaks parser internals). " +
            "Documented here as observed behavior; flagged separately as an application-side finding, " +
            "not something this automation framework can or should fix.")
    public void loginWithMalformedJsonBodyFails() throws Exception {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ConfigManager.getApiTenant())
                .contentType("application/json")
                .body("{not valid json")
                .post("/web/auth/login");

        Assert.assertEquals(response.statusCode(), 500, "Malformed JSON currently returns HTTP 500 (observed, not by design)");
    }

    /** Builds a login request with any of email/password/tenant omitted (null = header/field not sent). */
    private static Response loginRequest(String email, String password, String tenant) throws Exception {
        var req = given().filter(new SanitizedApiLoggingFilter()).baseUri(baseURI).contentType("application/json");
        if (tenant != null) {
            req = req.header("X-Amz-Tenant-Id", tenant);
        }
        Map<String, String> body = new LinkedHashMap<>();
        if (email != null) body.put("email", email);
        if (password != null) body.put("password", password);
        return req.body(new ObjectMapper().writeValueAsString(body)).post("/web/auth/login");
    }

    private static String maskEmail(String email) {
        int at = (email == null) ? -1 : email.indexOf('@');
        if (at <= 0) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}
