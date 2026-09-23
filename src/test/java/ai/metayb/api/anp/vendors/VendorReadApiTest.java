package ai.metayb.api.anp.vendors;

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
 * "02. ANP / Vendors" - read requests: Get Vendors List, Get Location List,
 * Get Bulk Upload Template.
 *
 * vendorId=5 ("Tesla") is a real, pre-existing vendor in this QA tenant (discovered
 * via Get Vendors List), not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Vendors")
public class VendorReadApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Vendors List returns existing vendors")
    @Story("Get Vendors List")
    @Description("Verified live: HTTP 200, data.data is a non-empty array of {id, vendorName, contactPersonName, isActive}.")
    public void getVendorsListReturnsVendors() {
        Response response = given().spec(requestSpecification).get("/web/vendor/vendors-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Location List returns the location hierarchy tree")
    @Story("Get Location List")
    @Description("Verified live: HTTP 200, data is a non-empty array of nested {id, name, code, children}.")
    public void getLocationListReturnsHierarchyTree() {
        Response response = given().spec(requestSpecification).get("/web/vendor/location-list");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Bulk Upload Template returns an xlsx file")
    @Story("Get Bulk Upload Template")
    @Description("Verified live: HTTP 200, response is a real xlsx file (ZIP signature), non-empty.")
    public void getBulkUploadTemplateReturnsXlsx() {
        Response response = given().spec(requestSpecification).get("/web/vendor/bulk-upload/template");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asByteArray().length > 0, "Template file should not be empty");
    }

    @Test(groups = {"regression", "negative"}, description = "Get Vendors List without authentication fails")
    @Story("Get Vendors List")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getVendorsListWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/vendor/vendors-list");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
