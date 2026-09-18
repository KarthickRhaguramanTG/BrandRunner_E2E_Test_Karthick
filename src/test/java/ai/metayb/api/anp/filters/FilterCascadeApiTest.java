package ai.metayb.api.anp.filters;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Filters / Get Filter Locations" and "Get Filter Activities" (POST
 * /web/anp-filters/locations, POST /web/anp-filters/activities), and their "Filters
 * (v2 alias)" counterparts (POST /web/anp-dashboard-v2/filters/locations,
 * POST /web/anp-dashboard-v2/filters/activities) - 15 Postman requests total across
 * 4 distinct routes:
 *   - Get Filter Locations (primary): "ZONE - all permitted", "SUBZONE - from selected
 *     ZONE", "STATE - from selected SUBZONE", "CITY - from selected STATE",
 *     "CITY - all permitted", "LGA - from selected CITY" (6 variants)
 *   - Get Filter Locations (v2 alias): same 5 variants minus LGA
 *   - Get Filter Activities (primary): "All permitted leaves", "Selected locations" (2)
 *   - Get Filter Activities (v2 alias): same 2 variants
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed across every folder/sub-area worked on so far, now confirmed
 * in this folder too across 3 distinct route namespaces (anp-filters, anp-dashboard-v2
 * filters, and separately anp-dashboard-v2 requester/summary and budget/all - see
 * BudgetGridApiTest and RequesterSummaryApiTest for those). Verified live for both the
 * primary and v2-alias paths - both return HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Filters")
public class FilterCascadeApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-filters/locations (all 6 cascade variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Filter Locations")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void getFilterLocationsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-filters/activities (both variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Filter Activities")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void getFilterActivitiesNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/filters/locations (v2 alias, all 5 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Filter Locations (v2 alias)")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void getFilterLocationsV2AliasNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/filters/activities (v2 alias, both variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Filter Activities (v2 alias)")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void getFilterActivitiesV2AliasNotAutomatedDueToEnvironmentIssue() {
    }
}
