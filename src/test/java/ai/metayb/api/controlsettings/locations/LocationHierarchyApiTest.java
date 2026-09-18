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
 * "01. Control Settings / Locations" - hierarchy/tree traversal requests: Get Hierarchy,
 * Get By Hierarchy, Get Children by Parent ID, Get Tree by Business Unit ID, Get Parents.
 *
 * hierarchyId=26 (COUNTRY) and parentId=63 (India) are real, pre-existing location ids
 * discovered via GET (Get Hierarchy / Get By Hierarchy), not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Locations")
public class LocationHierarchyApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Hierarchy returns the business unit's hierarchy levels")
    @Story("Get Hierarchy")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, businessUnitId, code, level, count}.")
    public void getHierarchyReturnsLevels() {
        Response response = given().spec(requestSpecification).get("/web/location/hierarchy");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get By Hierarchy returns locations at a given hierarchy level")
    @Story("Get By Hierarchy")
    @Description("Verified live: HTTP 200 for hierarchyId=26 (COUNTRY), data is a non-empty array.")
    public void getByHierarchyReturnsLocations() {
        Response response = given().spec(requestSpecification).get("/web/location/by-hierarchy/26");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Children by Parent ID returns child locations")
    @Story("Get Children by Parent ID")
    @Description("Verified live: HTTP 200 for parentId=63 (India), data is a non-empty array.")
    public void getChildrenByParentIdReturnsChildren() {
        Response response = given().spec(requestSpecification).get("/web/location/children/63");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"api", "regression"}, description = "Get Tree by Business Unit ID, called with businessUnitId=1 as the collection's default variable value, is rejected")
    @Story("Get Tree by Business Unit ID")
    @Description("Verified live: HTTP 400, message 'Permission denied: business unit mismatch'. The collection's " +
            "own default businessUnitId variable (1, numeric) does not match the UUID-form business_unit header " +
            "this account actually has - documented as observed behavior rather than guessing the correct id.")
    public void getTreeByBusinessUnitIdWithCollectionDefaultIsRejected() {
        Response response = given().spec(requestSpecification).get("/web/location/tree/1");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Permission denied: business unit mismatch");
    }

    @Test(groups = {"api", "regression"}, description = "Get Parents, called exactly as the Postman collection defines it (no query params), returns a validation error")
    @Story("Get Parents")
    @Description("Verified live: HTTP 400, message 'Business ID is required, Hierarchy ID is required'.")
    public void getParentsAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/location/parents");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Business ID is required, Hierarchy ID is required");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Hierarchy without authentication fails")
    @Story("Get Hierarchy")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getHierarchyWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/location/hierarchy");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
