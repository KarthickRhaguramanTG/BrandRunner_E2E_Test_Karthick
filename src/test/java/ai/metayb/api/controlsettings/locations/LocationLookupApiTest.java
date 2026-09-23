package ai.metayb.api.controlsettings.locations;

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
 * "01. Control Settings / Locations" - supporting lookup requests: Get Business Units,
 * Get Business Location, Get User Location, Get All Business, Get Business By ID,
 * Get Location By Business Unit, Get Location Template, Get By Level, Get Selected Location.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Locations")
public class LocationLookupApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Business Units returns the tenant's business units")
    @Story("Get Business Units")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, uuid, name}.")
    public void getBusinessUnitsReturnsUnits() {
        Response response = given().spec(requestSpecification).get("/web/location/business-units");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Business Location returns the caller's accessible business entries")
    @Story("Get Business Location")
    @Description("Verified live: HTTP 200, data is a non-empty array of {dbId, id, name}.")
    public void getBusinessLocationReturnsEntries() {
        Response response = given().spec(requestSpecification).get("/web/location/get-business-location");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get User Location returns the hierarchy definitions for the logged-in user")
    @Story("Get User Location")
    @Description("Verified live: HTTP 200, data.hierarchies is a non-empty array.")
    public void getUserLocationReturnsHierarchies() {
        Response response = given().spec(requestSpecification).get("/web/location/get-user-location");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.hierarchies").isEmpty(), "data.hierarchies should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get All Business returns a dropdown of business names")
    @Story("Get All Business")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, label}.")
    public void getAllBusinessReturnsDropdown() {
        Response response = given().spec(requestSpecification).get("/web/location/get-all-business");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Business By ID returns the caller's own business unit as a dropdown entry")
    @Story("Get Business By ID")
    @Description("Verified live: HTTP 200, data is a single-entry array [{id: 1, label: 'Maggie'}].")
    public void getBusinessByIdReturnsOwnBusinessUnit() {
        Response response = given().spec(requestSpecification).get("/web/location/get-business-by-id");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Location By Business Unit returns the BU's location list")
    @Story("Get Location By Business Unit")
    @Description("Verified live: HTTP 200, data is a non-empty array of {locationId, code, level, name}.")
    public void getLocationByBusinessUnitReturnsLocations() {
        Response response = given().spec(requestSpecification).get("/web/location/location-by-business-unit");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Location Template returns available country templates")
    @Story("Get Location Template")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, label}.")
    public void getLocationTemplateReturnsTemplates() {
        Response response = given().spec(requestSpecification).get("/web/location/get-location-template");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = { "regression"}, description = "Get By Level, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get By Level")
    @Description("Verified live: HTTP 400, message 'Business unit ID is required'.")
    public void getByLevelAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/location/by-level");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Business unit ID is required");
    }

    @Test(groups = { "regression"}, description = "Get Selected Location, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get Selected Location")
    @Description("Verified live: HTTP 400, message 'Business ID is required'.")
    public void getSelectedLocationAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/location/get-selected-location");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Business ID is required");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Business Units without authentication fails")
    @Story("Get Business Units")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getBusinessUnitsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/location/business-units");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
