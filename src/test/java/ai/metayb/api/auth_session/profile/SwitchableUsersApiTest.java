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
 * GET /web/profile/user-list - "00. Auth & Session / Switch Profile / List Switchable Users".
 */
@Epic("BrandRunners Web API")
@Feature("Profile")
public class SwitchableUsersApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "List switchable users returns the accounts this user can switch into")
    @Story("Switchable Users")
    @Description("Verified live: HTTP 200 with a data.users array (each with id/uuid/name/email/isActive/businessUnitId).")
    public void listSwitchableUsersReturnsUserList() {
        Response response = given().spec(requestSpecification).get("/web/profile/user-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.users"), "Response should include a data.users list");
    }

    @Test(groups = {"regression", "sanitys", "negative"}, description = "List switchable users without authentication fails")
    @Story("Switchable Users")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listSwitchableUsersWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/profile/user-list");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
