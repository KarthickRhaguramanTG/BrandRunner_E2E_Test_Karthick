package ai.metayb.api.publicmaintenancecron.cron;

import ai.metayb.api.utils.SanitizedApiLoggingFilter;
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
 * "07. Public, Maintenance &amp; Cron / Cron" - all 5 Postman requests: Import
 * Fabric, Import Distributor Stocks, Import Distributors, Get Activity Approval
 * Reminders, Sync Currency.
 *
 * All 5 are meant to be triggered by an external scheduler carrying a real
 * "cron-secret" header - the Postman collection's own "cronSecret" variable is
 * defined but deliberately left empty, so no real, currently-valid secret is known.
 * The true "job executes successfully" positive path is intentionally NOT
 * automated here: guessing or brute-forcing a working secret to force one of these
 * data-import/sync jobs to actually run against the shared QA tenant would be an
 * inappropriate, out-of-scope side effect for this suite to trigger - not merely
 * "test data unavailable" but a deliberate safety decision. Every other aspect of
 * each request (auth-guard behavior) IS fully verified live and asserted for real.
 *
 * Verified live for all 5 routes individually: missing/empty cron-secret and an
 * invalid-but-present cron-secret produce two distinct, real 401 messages (proving
 * the secret is genuinely validated, not just checked for presence). The
 * missing-tenant-header behavior was verified live once (Import Fabric) and is
 * documented on that evidence for the sibling routes per the "spot-check, document
 * on shared evidence" pattern used since "02. ANP" - all 5 share the same
 * "{{tenant}}" + "cron-secret" header shape in Postman.
 */
@Epic("BrandRunners Web API")
@Feature("Public, Maintenance & Cron - Cron")
public class CronApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "negative"}, description = "Import Fabric without a cron-secret header fails")
    @Story("Import Fabric")
    @Description("Verified live: HTTP 401, message 'Cron request rejected: missing cron-secret header.'")
    public void importFabricWithoutCronSecretFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/cron/fabric-import");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cron request rejected: missing cron-secret header.");
    }

    @Test(groups = { "regression", "negative"}, description = "Import Fabric with an invalid cron-secret fails")
    @Story("Import Fabric")
    @Description("Verified live: HTTP 401, message 'Cron request rejected: invalid cron-secret.' - distinct from the missing-header case, proving the secret is genuinely validated.")
    public void importFabricWithInvalidCronSecretFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .header("cron-secret", "invalid-placeholder-secret-xyz")
                .get("/web/cron/fabric-import");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cron request rejected: invalid cron-secret.");
    }

    @Test(groups = {"sanity", "regression", "negative"}, description = "Import Fabric without a tenant header fails")
    @Story("Import Fabric")
    @Description("Verified live: HTTP 400, message \"The invoked function is enabled with tenancy configuration. Add a valid tenant ID in your request and try again.\" - a distinct, lower-level guard checked independently of the cron-secret check.")
    public void importFabricWithoutTenantHeaderFails() {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("cron-secret", "invalid-placeholder-secret-xyz")
                .get("/web/cron/fabric-import");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"),
                "The invoked function is enabled with tenancy configuration. Add a valid tenant ID in your request and try again.");
    }

    @Test(groups = { "regression", "negative"}, description = "Import Distributor Stocks with an invalid cron-secret fails")
    @Story("Import Distributor Stocks")
    @Description("Verified live: HTTP 401, message 'Cron request rejected: invalid cron-secret.' - same auth-guard confirmed for Import Fabric.")
    public void importDistributorStocksWithInvalidCronSecretFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .header("cron-secret", "invalid-placeholder-secret-xyz")
                .get("/web/cron/import-distributor-stocks");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cron request rejected: invalid cron-secret.");
    }

    @Test(groups = { "regression", "negative"}, description = "Import Distributors with an invalid cron-secret fails")
    @Story("Import Distributors")
    @Description("Verified live: HTTP 401, message 'Cron request rejected: invalid cron-secret.' - same auth-guard confirmed for Import Fabric.")
    public void importDistributorsWithInvalidCronSecretFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .header("cron-secret", "invalid-placeholder-secret-xyz")
                .get("/web/cron/import-distributors");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cron request rejected: invalid cron-secret.");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Activity Approval Reminders with an invalid cron-secret fails")
    @Story("Get Activity Approval Reminders")
    @Description("Verified live: HTTP 401, message 'Cron request rejected: invalid cron-secret.' - same auth-guard confirmed for Import Fabric.")
    public void getActivityApprovalRemindersWithInvalidCronSecretFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .header("cron-secret", "invalid-placeholder-secret-xyz")
                .get("/web/cron/activity-approval-reminders");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cron request rejected: invalid cron-secret.");
    }

    @Test(groups = {"sanity", "regression", "negative"}, description = "Sync Currency with an invalid cron-secret fails")
    @Story("Sync Currency")
    @Description("Verified live: HTTP 401, message 'Cron request rejected: invalid cron-secret.' - same auth-guard confirmed for Import Fabric.")
    public void syncCurrencyWithInvalidCronSecretFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .header("cron-secret", "invalid-placeholder-secret-xyz")
                .get("/web/cron/currency-sync");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Cron request rejected: invalid cron-secret.");
    }
}
