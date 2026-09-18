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
 * "01. Control Settings / Users" - supporting lookup/dropdown requests: List Designation
 * Dropdown, List Users by Designation, List User Locations, List Users by Location,
 * List Users by Permission, List Location Hierarchies.
 *
 * Two of these were found to NOT succeed even when called exactly as the Postman
 * collection defines them (no query params) - documented per Step 8 rather than
 * inventing query parameters the collection doesn't specify.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Users")
public class UserLookupApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "List Designation Dropdown returns designation options")
    @Story("List Designation Dropdown")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, label}.")
    public void listDesignationDropdownReturnsOptions() {
        Response response = given().spec(requestSpecification).get("/web/user/designations");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty for this tenant");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "List Users by Designation returns users for a designation id")
    @Story("List Users by Designation")
    @Description("Verified live: HTTP 200 for designation id=1, data.users present.")
    public void listUsersByDesignationReturnsUsers() {
        Response response = given().spec(requestSpecification).get("/web/user/designations/1/users");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("data.users"), "Response should include data.users");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "List User Locations returns the location tree")
    @Story("List User Locations")
    @Description("Verified live: HTTP 200, data is a non-empty array.")
    public void listUserLocationsReturnsLocations() {
        Response response = given().spec(requestSpecification).get("/web/user/locations");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty for this tenant");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Designation Dropdown without authentication fails")
    @Story("List Designation Dropdown")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listDesignationDropdownWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/user/designations");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = {"api", "regression"}, description = "List Users by Location, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("List Users by Location")
    @Description("Verified live: HTTP 400, message 'Invalid query parameters'. The Postman collection's saved " +
            "request for this endpoint has no query string at all, so this is what literally replaying it " +
            "produces - the collection's own example appears incomplete for a successful call. No query " +
            "parameter values are invented here; this documents the gap per Step 8 instead.")
    public void listUsersByLocationAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/user/by-location");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid query parameters");
    }

    @Test(groups = {"api", "regression"}, description = "List Users by Permission, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("List Users by Permission")
    @Description("Verified live: HTTP 400, message 'Invalid query parameters' - same gap as List Users by Location.")
    public void listUsersByPermissionAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/user/by-permission");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid query parameters");
    }

    @Test(groups = {"api", "regression"}, description = "List Location Hierarchies, called exactly as the Postman collection defines it, currently errors server-side")
    @Story("List Location Hierarchies")
    @Description("Verified live: HTTP 500, message 'Cannot read properties of undefined (reading '0')' - a real " +
            "backend crash when called with only the standard tenant/business_unit headers Postman's saved " +
            "request specifies. Documented as an application-side robustness finding (worth reporting to the " +
            "BrandRunners backend team), not something this automation framework can fix.")
    public void listLocationHierarchiesAsDefinedInPostmanErrors() {
        Response response = given().spec(requestSpecification).get("/web/user/location-hierarchies");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cannot read properties of undefined (reading '0')");
    }
}
