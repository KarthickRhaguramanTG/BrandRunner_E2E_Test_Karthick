package ai.metayb.api.anp.dashboard;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Legacy Dashboard v1" - 8 Postman requests across 4 distinct routes
 * (each with "All locations" / "Selected locations" variants):
 *   - Dashboard Overview (POST /web/anp-dashboard/overview) - 2 variants
 *   - Dashboard Pending Actions (POST /web/anp-dashboard/pending-actions) - 2 variants
 *   - Dashboard Vendors (POST /web/anp-dashboard/vendors) - 2 variants
 *   - Dashboard All - Legacy (POST /web/anp-dashboard/all) - 2 variants
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Dashboard Overview" (All locations) directly - returned
 * HTML instead of JSON. The other 3 routes are documented on that same evidence, per
 * the "spot-check per sub-area" approach. Note this is a DIFFERENT base path
 * ("/web/anp-dashboard/*", the v1 legacy API) than the v2 dashboard cluster
 * ("/web/anp-dashboard-v2/*") - both suffer the identical symptom regardless.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Legacy Dashboard v1")
public class LegacyDashboardWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard/overview (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Dashboard Overview")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void dashboardOverviewNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/anp-dashboard/pending-actions (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Dashboard Pending Actions")
    @Description("Blocked by environment - same route family as Dashboard Overview. See class Javadoc. Not executed.")
    public void dashboardPendingActionsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/anp-dashboard/vendors (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Dashboard Vendors")
    @Description("Blocked by environment - same route family as Dashboard Overview. See class Javadoc. Not executed.")
    public void dashboardVendorsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"regression"},
            description = "BLOCKED: POST /web/anp-dashboard/all (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Dashboard All (Legacy)")
    @Description("Blocked by environment - same route family as Dashboard Overview. See class Javadoc. Not executed.")
    public void dashboardAllLegacyNotAutomatedDueToEnvironmentIssue() {
    }
}
