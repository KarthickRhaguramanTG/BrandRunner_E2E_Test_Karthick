package ai.metayb.api.anp.dashboard;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Executive Dashboard" - 21 Postman requests across 4 distinct routes:
 *   - Executive Pending Aging (POST /web/anp-dashboard-v2/executive/pending-approval-aging) - 3 variants
 *   - Regional Spend Heat Map (POST .../executive/regional-spend-heat-map) - 6 variants
 *   - Top Locations (POST .../executive/top-location) - 6 variants
 *   - Top Activities (POST .../executive/top-activities) - 6 variants
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Regional Spend Heat Map" (approved - all filters) directly -
 * returned HTML instead of JSON. The other 3 routes are documented on that same
 * evidence, per the "spot-check per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Executive Dashboard")
public class ExecutiveDashboardWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/executive/pending-approval-aging (3 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Executive Pending Aging")
    @Description("Blocked by environment - same route family as Regional Spend Heat Map. See class Javadoc. Not executed.")
    public void executivePendingAgingNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/executive/regional-spend-heat-map (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Regional Spend Heat Map")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void regionalSpendHeatMapNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/executive/top-location (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Top Locations")
    @Description("Blocked by environment - same route family as Regional Spend Heat Map. See class Javadoc. Not executed.")
    public void topLocationsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/executive/top-activities (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Top Activities")
    @Description("Blocked by environment - same route family as Regional Spend Heat Map. See class Javadoc. Not executed.")
    public void topActivitiesNotAutomatedDueToEnvironmentIssue() {
    }
}
