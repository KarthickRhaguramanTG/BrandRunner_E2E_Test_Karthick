package ai.metayb.api.controlsettings.users;

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
 * "01. Control Settings / Users" - core listing/lookup requests:
 * List Users (GET /web/user), List All Users (GET /web/user/all),
 * List Unique Users (GET /web/user/all/unique), Get User by ID (GET /web/user/:id).
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Users")
public class UserListApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "List Users returns a paginated list of users")
    @Story("List Users")
    @Description("Verified live: HTTP 200, data.data is a non-empty array of users.")
    public void listUsersReturnsPaginatedUsers() {
        Response response = given().spec(requestSpecification).get("/web/user?page=1&limit=20&search=");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty for this tenant");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Users without authentication fails")
    @Story("List Users")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listUsersWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/user?page=1&limit=20&search=");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "List All Users returns the full user array")
    @Story("List All Users")
    @Description("Verified live: HTTP 200, data is a non-empty array.")
    public void listAllUsersReturnsAllUsers() {
        Response response = given().spec(requestSpecification).get("/web/user/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty for this tenant");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "List Unique Users returns a de-duplicated user array")
    @Story("List Unique Users")
    @Description("Verified live: HTTP 200, data is a non-empty array.")
    public void listUniqueUsersReturnsUsers() {
        Response response = given().spec(requestSpecification).get("/web/user/all/unique");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty for this tenant");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get User by ID returns the user's details")
    @Story("Get User by ID")
    @Description("Verified live: HTTP 200 for the logged-in user's own id (1), with name/email present.")
    public void getUserByIdReturnsUserDetails() {
        Response response = given().spec(requestSpecification).get("/web/user/1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().getString("data.name"));
        Assert.assertNotNull(response.jsonPath().getString("data.email"));
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get User by ID with a non-existent id fails")
    @Story("Get User by ID")
    @Description("Verified live: HTTP 400, message 'User not found' (not 404).")
    public void getUserByIdWithNonExistentIdFails() {
        Response response = given().spec(requestSpecification).get("/web/user/999999");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found");
    }
}
