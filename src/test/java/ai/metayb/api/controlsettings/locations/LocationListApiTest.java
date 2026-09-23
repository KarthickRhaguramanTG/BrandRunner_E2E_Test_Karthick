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
 * "01. Control Settings / Locations" - core listing requests: List Locations,
 * Get All, Get All Location, Download Sample, Get Templates.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Locations")
public class LocationListApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression"}, description = "List Locations, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("List Locations")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected string, received undefined, ...'. " +
            "The Postman collection's saved request has no query string, so this is what literally replaying " +
            "it produces - documented per Step 8 rather than inventing required query parameters.")
    public void listLocationsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/location");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("expected string, received undefined"));
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get All returns the location grid columns and data")
    @Story("Get All")
    @Description("Verified live: HTTP 200, data.data.columns present (COUNTRY/STATE/... grid columns).")
    public void getAllReturnsLocationGrid() {
        Response response = given().spec(requestSpecification).get("/web/location/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertNotNull(response.jsonPath().get("data.data.columns"), "Response should include data.data.columns");
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get All Location returns the hierarchy definitions")
    @Story("Get All Location")
    @Description("Verified live: HTTP 200, data.hierarchies is a non-empty array.")
    public void getAllLocationReturnsHierarchies() {
        Response response = given().spec(requestSpecification).get("/web/location/get-all-location");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.hierarchies").isEmpty(), "data.hierarchies should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Download Sample returns an xlsx file")
    @Story("Download Sample")
    @Description("Verified live: HTTP 200, Content-Type application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, non-empty body.")
    public void downloadSampleReturnsXlsx() {
        Response response = given().spec(requestSpecification).get("/web/location/download-sample");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.contentType().contains("spreadsheetml"), "Expected an xlsx content type, got: " + response.contentType());
        Assert.assertTrue(response.getBody().asByteArray().length > 0, "Sample file should not be empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Templates returns available location templates")
    @Story("Get Templates")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, templateName, templateConfig}.")
    public void getTemplatesReturnsTemplateList() {
        Response response = given().spec(requestSpecification).get("/web/location/templates");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = { "regression", "negative"}, description = "Get All Location without authentication fails")
    @Story("Get All Location")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getAllLocationWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/location/get-all-location");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
