package ai.metayb.api.publicmaintenancecron.publicsales;

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
 * "07. Public, Maintenance &amp; Cron / Public Sales" - all 4 Postman requests.
 * All 4 are tagged "noauth" in Postman, but only 3 of the 4 genuinely behave as
 * public/share-link endpoints - verified live (not assumed):
 *
 * *** APPLICATION-SIDE FINDING *** "Get Sales Details View by Share Token"
 * (GET /web/public/sales-details/:shareToken/view) requires real authentication
 * despite being tagged "noauth" (401 "Authentication token missing" with none)
 * AND, even when called with a full valid Authorization + business_unit header,
 * returns HTTP 400 "Can't find /web/public/sales-details/:shareToken/view on this
 * server!!!" - i.e. this route is documented in the Postman collection but is not
 * actually registered on the backend. Documented as observed, not fixed (out of
 * this suite's scope).
 *
 * "Get Sales Details by Share Token" and "Get Stock Pickup by Share Token" both
 * genuinely validate the path's share token (401 "Invalid share link" for an
 * unknown one), independent of any auth headers - no real, currently-valid share
 * token/sales record is known to exist in this tenant (test data unavailable), so
 * the "found" positive path is not automated.
 *
 * "Get Sales Details" (query/param-less variant) genuinely supports EITHER a valid
 * share link OR authentication + workflow_id/user_id/form_data_id/cart_id query
 * params - verified live for both the fully-anonymous case and the
 * authenticated-but-missing-params case. Postman's own saved example has neither,
 * so both real observed validation errors are documented per Step 8 rather than
 * inventing IDs.
 */
@Epic("BrandRunners Web API")
@Feature("Public, Maintenance & Cron - Public Sales")
public class PublicSalesApiTest extends BaseApiTest {

    @Test(groups = { "regression", "negative"}, description = "Get Sales Details View by Share Token without authentication fails")
    @Story("Get Sales Details View by Share Token")
    @Description("Verified live: HTTP 401, message 'Authentication token missing' - despite being tagged 'noauth' in Postman, this route genuinely requires a valid token. See class Javadoc.")
    public void getSalesDetailsViewByShareTokenWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/public/sales-details/placeholder-token/view");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Sales Details View by Share Token, even fully authenticated, is not a registered backend route")
    @Story("Get Sales Details View by Share Token")
    @Description("Verified live: HTTP 400, message \"Can't find /web/public/sales-details/placeholder-token/view on this server!!!\" - a real Postman-vs-backend gap, documented as observed per Step 8. See class Javadoc.")
    public void getSalesDetailsViewByShareTokenNotARegisteredRoute() {
        Response response = given().spec(requestSpecification)
                .get("/web/public/sales-details/placeholder-token/view");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Can't find"),
                "Expected a route-not-found style message, got: " + response.jsonPath().getString("message"));
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Sales Details by Share Token with an unknown share token fails")
    @Story("Get Sales Details by Share Token")
    @Description("Verified live: HTTP 401, message 'Invalid share link' - genuinely validates the token itself, independent of auth. No real, currently-valid share token is known to exist in this tenant (test data unavailable) - see class Javadoc.")
    public void getSalesDetailsByShareTokenWithUnknownTokenFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/public/sales-details/placeholder-token");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid share link");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Sales Details, called exactly as Postman defines it (no share link, no auth), returns a validation error")
    @Story("Get Sales Details")
    @Description("Verified live: HTTP 401, message 'Authentication or a valid share link is required' - confirms this endpoint supports either mode. Postman's saved example has no query string, documented per Step 8.")
    public void getSalesDetailsWithoutShareLinkOrAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/public/sales-details");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication or a valid share link is required");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Sales Details, authenticated but missing the required identifying query params, returns a validation error")
    @Story("Get Sales Details")
    @Description("Verified live: HTTP 400, message 'workflow_id, user_id, form_data_id and cart_id are required for authenticated access' - proves the authenticated mode is real and validated. No real sales workflow data is known to exist to supply these IDs (test data unavailable).")
    public void getSalesDetailsAuthenticatedWithoutRequiredParamsFails() {
        Response response = given().spec(requestSpecification).get("/web/public/sales-details");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"),
                "workflow_id, user_id, form_data_id and cart_id are required for authenticated access");
    }

    @Test(groups = {"sanity", "regression"}, description = "Get Stock Pickup by Share Token with an unknown share token fails")
    @Story("Get Stock Pickup by Share Token")
    @Description("Verified live: HTTP 401, message 'Invalid share link'. No real, currently-valid share token is known to exist in this tenant (test data unavailable) - see class Javadoc.")
    public void getStockPickupByShareTokenWithUnknownTokenFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/public/stock-pickup/placeholder-token");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid share link");
    }
}
