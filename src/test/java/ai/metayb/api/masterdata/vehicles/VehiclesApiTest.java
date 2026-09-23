package ai.metayb.api.masterdata.vehicles;

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
 * "05. Master Data / Vehicles" - read requests: Get Vehicle Types, Get All, Get
 * Vehicle by ID (fully automated); Get Vehicles By Location (documented observed
 * behavior - same "incomplete Postman example" pattern established elsewhere).
 *
 * id=77 is a real, pre-existing vehicle in this QA tenant, discovered via Get All,
 * not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("Master Data - Vehicles")
public class VehiclesApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Vehicle Types returns the configured vehicle types")
    @Story("Get Vehicle Types")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, name, status}.")
    public void getVehicleTypesReturnsTypes() {
        Response response = given().spec(requestSpecification).get("/web/vehicle/vehicle-types");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get All returns existing vehicles")
    @Story("Get All")
    @Description("Verified live: HTTP 200, data.data is a non-empty array of {id, vehicleNumber, vehicleOwner, status}.")
    public void getAllReturnsVehicles() {
        Response response = given().spec(requestSpecification).get("/web/vehicle/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Vehicle by ID returns a real vehicle's details")
    @Story("Get Vehicle by ID")
    @Description("Verified live: HTTP 200 for id=77, data.vehicleNumber present.")
    public void getVehicleByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/vehicle/77");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.id"), 77);
        Assert.assertNotNull(response.jsonPath().getString("data.vehicleNumber"));
    }

    @Test(groups = { "regression", "negative"}, description = "Get Vehicle by ID with a non-existent id fails")
    @Story("Get Vehicle by ID")
    @Description("Verified live: HTTP 400, message 'Vehicle with ID 999999 not found'.")
    public void getVehicleByIdWithNonExistentIdFails() {
        Response response = given().spec(requestSpecification).get("/web/vehicle/999999");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Vehicle with ID 999999 not found");
    }

    @Test(groups = {"regression"}, description = "Get Vehicles By Location, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get Vehicles By Location")
    @Description("Verified live: HTTP 400, message 'Invalid query parameters'. The Postman collection's saved " +
            "request for this endpoint has no query string, so this is what literally replaying it produces - " +
            "documented per Step 8 rather than inventing required query parameters.")
    public void getVehiclesByLocationAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/vehicle/vehicles-by-location");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid query parameters");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Vehicle Types without authentication fails")
    @Story("Get Vehicle Types")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getVehicleTypesWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/vehicle/vehicle-types");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
