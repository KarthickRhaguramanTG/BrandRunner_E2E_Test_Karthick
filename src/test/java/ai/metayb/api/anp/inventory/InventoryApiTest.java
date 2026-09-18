package ai.metayb.api.anp.inventory;

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
 * "02. ANP / Inventory" - all 3 Postman requests. Only 3 requests total, kept in one
 * class per Section 16 ("avoid unnecessary classes for simple APIs").
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Inventory")
public class InventoryApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get All returns the inventory listing")
    @Story("Get All")
    @Description("Verified live: HTTP 200, 'Inventory details fetched successfully', data.data is an array (empty in this QA tenant - asserted as observed).")
    public void getAllReturnsInventoryListing() {
        Response response = given().spec(requestSpecification).get("/web/inventory/get-all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "Inventory details fetched successfully");
        Assert.assertNotNull(response.jsonPath().getList("data.data"), "data.data should be a list (may be empty)");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Logs by Asset ID returns log history for a real asset")
    @Story("Get Logs by Asset ID")
    @Description("Verified live: HTTP 200 for assetId=9, 'Inventory logs fetched successfully', data.asset.assetId matches.")
    public void getLogsByAssetIdReturnsHistory() {
        Response response = given().spec(requestSpecification).get("/web/inventory/9/logs");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.asset.assetId"), 9);
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get All without authentication fails")
    @Story("Get All")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getAllWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/inventory/get-all");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: PUT /web/inventory/stock returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Stock")
    @Description("Blocked by environment - same systemic CloudFront/S3 SPA-fallback root cause confirmed " +
            "throughout this folder. Directly verified live. Not executed.")
    public void updateStockNotAutomatedDueToEnvironmentIssue() {
    }
}
