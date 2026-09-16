package ai.metayb.api.auth;

import ai.metayb.ui.core.DataReader;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * POST /web/auth/login - "BrandRunners Web APIs" Postman collection,
 * folder "00. Auth & Session / Auth", request "Login".
 *
 * Every other request in the collection depends on what this one produces
 * (accessToken cookie -> Authorization: Bearer header, X-Amz-Tenant-Id), so
 * it is the first API automated against this collection.
 */
@Epic("BrandRunners Web API")
@Feature("Authentication")
public class LoginApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke"}, description = "Login with valid QA credentials returns an access token and business unit")
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Reproduces the Postman collection's Login test script: asserts HTTP 200 " +
            "(pm.test('Login succeeded')), then confirms the accessToken cookie and " +
            "data.user.businessInfo.id that every other authenticated request depends on are present.")
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

        // businessInfo.id is a UUID string in the real response, not numeric - Postman's own
        // script captures it with no type assumption (json?.data?.user?.businessInfo?.id).
        String businessUnitId = response.jsonPath().getString("data.user.businessInfo.id");
        Assert.assertNotNull(businessUnitId, "Response should include data.user.businessInfo.id");
        Assert.assertFalse(businessUnitId.isBlank(), "data.user.businessInfo.id must not be blank");
        Allure.step("data.user.businessInfo.id: " + businessUnitId);
    }

    private static String maskEmail(String email) {
        int at = (email == null) ? -1 : email.indexOf('@');
        if (at <= 0) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}
