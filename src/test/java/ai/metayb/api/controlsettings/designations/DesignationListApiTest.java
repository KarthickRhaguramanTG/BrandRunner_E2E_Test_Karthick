package ai.metayb.api.controlsettings.designations;

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
 * "01. Control Settings / Designations" - read requests: Get Permissions List,
 * Get Designation List All, Get Designation List, Get Location Hierarchy List,
 * Get Bulk Upload Template.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Designations")
public class DesignationListApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Permissions List returns the available permission catalog")
    @Story("Get Permissions List")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, code, name, service_code}.")
    public void getPermissionsListReturnsPermissions() {
        Response response = given().spec(requestSpecification).get("/web/designation/permissions-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Designation List All returns every designation")
    @Story("Get Designation List All")
    @Description("Verified live: HTTP 200, data.data is a non-empty array, data.budgetCreatorIds present.")
    public void getDesignationListAllReturnsDesignations() {
        Response response = given().spec(requestSpecification).get("/web/designation/designation-list/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
        Assert.assertNotNull(response.jsonPath().get("data.budgetCreatorIds"));
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Designation List returns a paginated designation list")
    @Story("Get Designation List")
    @Description("Verified live: HTTP 200, data.data is a non-empty array (page=1, limit=20).")
    public void getDesignationListReturnsPaginatedDesignations() {
        Response response = given().spec(requestSpecification).get("/web/designation/designation-list?page=1&limit=20&search=");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Location Hierarchy List returns the hierarchy levels")
    @Story("Get Location Hierarchy List")
    @Description("Verified live: HTTP 200, 'Location hierarchies fetched successfully', data is a non-empty array of {id, code, level}.")
    public void getLocationHierarchyListReturnsLevels() {
        Response response = given().spec(requestSpecification).get("/web/designation/location-hierarchy-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Location hierarchies fetched successfully");
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Bulk Upload Template returns an xlsx file")
    @Story("Get Bulk Upload Template")
    @Description("Verified live: HTTP 200, Content-Type application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, non-empty body.")
    public void getBulkUploadTemplateReturnsXlsx() {
        Response response = given().spec(requestSpecification).get("/web/designation/bulk-upload/template");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.contentType().contains("spreadsheetml"), "Expected an xlsx content type, got: " + response.contentType());
        Assert.assertTrue(response.getBody().asByteArray().length > 0, "Template file should not be empty");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Permissions List without authentication fails")
    @Story("Get Permissions List")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getPermissionsListWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/designation/permissions-list");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
