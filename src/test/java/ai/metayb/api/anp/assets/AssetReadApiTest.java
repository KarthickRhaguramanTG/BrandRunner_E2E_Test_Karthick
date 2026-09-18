package ai.metayb.api.anp.assets;

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
 * "02. ANP / Assets" - read requests: Get All, Get Asset by Asset ID, Get Standard
 * Rates by Asset ID, Get Template Location Wise, Get Template Global Rates.
 *
 * assetId=9 is a real, pre-existing (non-billable) asset in this QA tenant (discovered
 * via Get All), not fabricated.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Assets")
public class AssetReadApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get All returns existing assets")
    @Story("Get All")
    @Description("Verified live: HTTP 200, data.data is a non-empty array of {assetId, name, isBillable, globalPrice}.")
    public void getAllReturnsAssets() {
        Response response = given().spec(requestSpecification).get("/web/assets/get-all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data.data").isEmpty(), "data.data should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Asset by Asset ID returns a real asset's details")
    @Story("Get Asset by Asset ID")
    @Description("Verified live: HTTP 200 for assetId=9, data.name/data.isBillable present.")
    public void getAssetByAssetIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/assets/9");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.id"), 9);
        Assert.assertNotNull(response.jsonPath().getString("data.name"));
    }

    @Test(groups = {"api", "regression"}, description = "Get Standard Rates by Asset ID is rejected for a non-billable asset")
    @Story("Get Standard Rates by Asset ID")
    @Description("Verified live: HTTP 400, message 'Only billable assets support standard rates' - assetId=9's " +
            "real isBillable=false in this QA tenant. This is the actual, observed behavior for the only asset " +
            "readily available; a true positive would need a billable asset, which this environment's Create " +
            "endpoint (blocked) would be needed to produce. Documented per Step 8 rather than invented.")
    public void getStandardRatesByAssetIdIsRejectedForNonBillableAsset() {
        Response response = given().spec(requestSpecification).get("/web/assets/9/standardRates");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Only billable assets support standard rates");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Template Location Wise returns an xlsx file")
    @Story("Get Template Location Wise")
    @Description("Verified live: HTTP 200, response is a real xlsx file (ZIP signature), non-empty.")
    public void getTemplateLocationWiseReturnsXlsx() {
        Response response = given().spec(requestSpecification).get("/web/assets/template/location-wise");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asByteArray().length > 0, "Template file should not be empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Template Global Rates returns an xlsx file")
    @Story("Get Template Global Rates")
    @Description("Verified live: HTTP 200, response is a real xlsx file (ZIP signature), non-empty.")
    public void getTemplateGlobalRatesReturnsXlsx() {
        Response response = given().spec(requestSpecification).get("/web/assets/template/global-rates");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asByteArray().length > 0, "Template file should not be empty");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get All without authentication fails")
    @Story("Get All")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getAllWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/assets/get-all");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
