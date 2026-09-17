package ai.metayb.api.auth_session.profile;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import static io.restassured.RestAssured.given;

/**
 * GET /web/profile/my-roles - "00. Auth & Session / Switch Profile / Get My Roles".
 */
@Epic("BrandRunners Web API")
@Feature("Profile")
public class MyRolesApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get my roles returns the logged-in user's role designations")
    @Story("My Roles")
    @Description("Verified live: HTTP 200 with a non-empty data.roles array, each entry carrying designationId/designationName.")
    public void getMyRolesReturnsRoleDesignations() {
        Response response = given().spec(requestSpecification).get("/web/profile/my-roles");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data.roles").isEmpty(), "data.roles should be non-empty for this account");
        Assert.assertNotNull(response.jsonPath().getString("data.roles[0].designationName"), "Each role should carry a designationName");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get my roles without authentication fails")
    @Story("My Roles")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getMyRolesWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/profile/my-roles");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
