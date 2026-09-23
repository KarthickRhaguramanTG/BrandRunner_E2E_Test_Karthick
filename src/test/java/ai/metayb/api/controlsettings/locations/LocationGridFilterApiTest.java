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
 * "01. Control Settings / Locations" - grid filter requests: Get Grid Filter Hierarchies,
 * Get Grid Filter Options, Get Grid Filter Chain.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Locations")
public class LocationGridFilterApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Grid Filter Hierarchies returns hierarchy filter options")
    @Story("Get Grid Filter Hierarchies")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, code, level, label}.")
    public void getGridFilterHierarchiesReturnsOptions() {
        Response response = given().spec(requestSpecification).get("/web/location/grid-filter/hierarchies");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Grid Filter Options, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get Grid Filter Options")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected number, received NaN'.")
    public void getGridFilterOptionsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/location/grid-filter/options");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid input: expected number, received NaN");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Grid Filter Chain, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get Grid Filter Chain")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected number, received NaN' - same gap as Get Grid Filter Options.")
    public void getGridFilterChainAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/location/grid-filter/chain");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid input: expected number, received NaN");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Grid Filter Hierarchies without authentication fails")
    @Story("Get Grid Filter Hierarchies")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getGridFilterHierarchiesWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/location/grid-filter/hierarchies");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
