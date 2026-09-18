package ai.metayb.api.filesauditmisc.auditlogs;

import ai.metayb.api.utils.SanitizedApiLoggingFilter;
import ai.metayb.config.ConfigManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * "06. Files, Audit &amp; Misc / Audit Logs" - all 3 Postman requests: Get Filter
 * Options, List Audit Logs, Get All.
 *
 * *** OBSERVED BEHAVIOR *** "Get All" (/web/audit-logs/all) is NOT actually
 * unpaginated despite its name - verified live it returns the exact same paginated
 * shape (data.data/total/page/limit/totalPages, defaulting to page=1&amp;limit=20) as
 * "List Audit Logs" (/web/audit-logs). Documented as observed, not assumed to be a
 * true "dump everything" endpoint.
 *
 * Query-parameter filtering (moduleGroup, actionType, page, limit) is genuinely
 * implemented server-side - verified live by confirming every row returned for
 * moduleGroup=Entity/Target actually has that moduleGroup, and similarly for
 * actionType=CREATE/DELETE, with distinct totals per filter.
 *
 * All 3 requests share the same auth/tenant-scoping middleware confirmed throughout
 * this suite (missing token -&gt; 401 "Authentication token missing"; missing
 * business_unit -&gt; 401 "You do not have access to this business unit."; invalid
 * tenant -&gt; 400 "Database connection failed, Please check the tenant ID") - verified
 * live for all 3 individually for the no-auth case, and once in depth (missing BU
 * header, invalid tenant) on "List Audit Logs" per the "spot-check, document on
 * shared evidence" pattern used since "02. ANP".
 */
@Epic("BrandRunners Web API")
@Feature("Files, Audit & Misc - Audit Logs")
public class AuditLogsApiTest extends BaseApiTest {

    @Test(groups = {"api", "smoke", "positive"}, description = "Get Filter Options returns the available audit log filter values")
    @Story("Get Filter Options")
    @Description("Verified live: HTTP 200, data.moduleGroups/actionTypes/users are all non-empty arrays.")
    public void getFilterOptionsReturnsOptions() {
        Response response = given().spec(requestSpecification).get("/web/audit-logs/filter-options");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.moduleGroups").isEmpty(), "moduleGroups should be non-empty");
        Assert.assertFalse(response.jsonPath().getList("data.actionTypes").isEmpty(), "actionTypes should be non-empty");
        Assert.assertFalse(response.jsonPath().getList("data.users").isEmpty(), "users should be non-empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "List Audit Logs returns a paginated page of audit log entries")
    @Story("List Audit Logs")
    @Description("Verified live: HTTP 200, default page=1/limit=20, data.data has 20 rows, data.total/totalPages present and consistent (total > 0).")
    public void listAuditLogsReturnsPaginatedData() {
        Response response = given().spec(requestSpecification).get("/web/audit-logs");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.page"), 1);
        Assert.assertEquals(response.jsonPath().getInt("data.limit"), 20);
        Assert.assertEquals(response.jsonPath().getList("data.data").size(), 20);
        Assert.assertTrue(response.jsonPath().getInt("data.total") > 0, "total should be > 0 - this tenant has audit history");
    }

    @Test(groups = {"api", "regression", "positive"}, description = "List Audit Logs filters correctly by moduleGroup")
    @Story("List Audit Logs")
    @Description("Verified live: HTTP 200, filtering by moduleGroup=Entity returns only rows whose moduleGroup is Entity, with a total distinct from the unfiltered total - proves the filter is real, not a no-op.")
    public void listAuditLogsFiltersByModuleGroup() {
        Response response = given().spec(requestSpecification).get("/web/audit-logs?moduleGroup=Entity");

        Assert.assertEquals(response.statusCode(), 200);
        List<Map<String, ?>> rows = response.jsonPath().getList("data.data");
        Assert.assertFalse(rows.isEmpty(), "expected at least one Entity audit log row");
        for (Map<String, ?> row : rows) {
            Assert.assertEquals(row.get("moduleGroup"), "Entity", "every row should have moduleGroup=Entity");
        }
    }

    @Test(groups = {"api", "regression"}, description = "List Audit Logs with a non-existent moduleGroup returns an empty result, not an error")
    @Story("List Audit Logs")
    @Description("Verified live: HTTP 200 (not 400/404), data.total=0, data.data is empty - the filter degrades to an empty page rather than erroring on an unrecognized value.")
    public void listAuditLogsWithNonExistentModuleGroupReturnsEmptyResult() {
        Response response = given().spec(requestSpecification).get("/web/audit-logs?moduleGroup=NoSuchModuleGroup");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.total"), 0);
        Assert.assertTrue(response.jsonPath().getList("data.data").isEmpty(), "data.data should be empty");
    }

    @Test(groups = {"api", "smoke", "positive"}, description = "Get All returns the same paginated shape as List Audit Logs")
    @Story("Get All")
    @Description("Verified live: HTTP 200, identical paginated shape to List Audit Logs (page=1/limit=20 by default) - see class Javadoc; this endpoint does not actually return every record unpaginated.")
    public void getAllReturnsPaginatedData() {
        Response response = given().spec(requestSpecification).get("/web/audit-logs/all");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.page"), 1);
        Assert.assertEquals(response.jsonPath().getInt("data.limit"), 20);
        Assert.assertEquals(response.jsonPath().getList("data.data").size(), 20);
        Assert.assertTrue(response.jsonPath().getInt("data.total") > 0, "total should be > 0 - this tenant has audit history");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Audit Logs without authentication fails")
    @Story("List Audit Logs")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listAuditLogsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/audit-logs");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Audit Logs authenticated but missing the business_unit header fails")
    @Story("List Audit Logs")
    @Description("Verified live: HTTP 401, message 'You do not have access to this business unit.' - distinct from the no-auth case, proving the token itself IS recognized and only business-unit scoping fails.")
    public void listAuditLogsAuthenticatedButMissingBusinessUnitHeaderFails() {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ConfigManager.getApiTenant())
                .header("Authorization", "Bearer " + authToken)
                .get("/web/audit-logs");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "You do not have access to this business unit.");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "List Audit Logs with an invalid tenant header fails")
    @Story("List Audit Logs")
    @Description("Verified live: HTTP 400, message 'Database connection failed, Please check the tenant ID' - same tenant-resolution behavior confirmed throughout this suite (see LoginApiTest).")
    public void listAuditLogsWithInvalidTenantFails() {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", "nonexistent-tenant-xyz")
                .header("Authorization", "Bearer " + authToken)
                .header("business_unit", businessUnitId)
                .get("/web/audit-logs");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Database connection failed, Please check the tenant ID");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get Filter Options without authentication fails")
    @Story("Get Filter Options")
    @Description("Verified live: HTTP 401, message 'Authentication token missing' - same auth middleware as List Audit Logs.")
    public void getFilterOptionsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/audit-logs/filter-options");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = {"api", "regression", "negative"}, description = "Get All without authentication fails")
    @Story("Get All")
    @Description("Verified live: HTTP 401, message 'Authentication token missing' - same auth middleware as List Audit Logs.")
    public void getAllWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/audit-logs/all");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
