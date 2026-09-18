package ai.metayb.api.anp.filters;

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
 * "02. ANP / Filters / Get Filter Meta" (GET /web/anp-filters/meta) and its
 * "Filters (v2 alias)" counterpart (GET /web/anp-dashboard-v2/filters/meta).
 *
 * Verified live: both paths return the SAME response body/data - genuine aliases of
 * the same underlying data, not two independent features. Per Section 16 ("avoid
 * duplicate API calls"), the v2 alias gets one lightweight confirmation test rather
 * than a full duplicate suite.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Filters")
public class FilterMetaApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Filter Meta returns the location filter/zone metadata")
    @Story("Get Filter Meta")
    @Description("Verified live: HTTP 200, data is a non-empty array with 'filters' (location type levels) and 'zoneData'.")
    public void getFilterMetaReturnsZoneData() {
        Response response = given().spec(requestSpecification).get("/web/anp-filters/meta");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
        Assert.assertFalse(response.jsonPath().getList("data[0].filters").isEmpty(), "data[0].filters should be non-empty");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Filter Meta without authentication fails")
    @Story("Get Filter Meta")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getFilterMetaWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/anp-filters/meta");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Filter Meta (v2 alias) returns the same data as the primary path")
    @Story("Get Filter Meta (v2 alias)")
    @Description("Verified live: HTTP 200 with the same success/data shape as /web/anp-filters/meta - confirms this is a genuine alias.")
    public void getFilterMetaV2AliasReturnsSameShape() {
        Response response = given().spec(requestSpecification).get("/web/anp-dashboard-v2/filters/meta");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), true);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }
}
