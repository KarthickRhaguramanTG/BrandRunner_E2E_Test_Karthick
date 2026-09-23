package ai.metayb.api.anp.dashboard;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Requester Dashboard" - 36 Postman requests across 6 distinct routes
 * (each with 3-9 filter-combination variants: all locations / selected filters /
 * multi-month, sometimes x requestType null|budget|contingency):
 *   - Requester Summary (POST /web/anp-dashboard-v2/requester/summary) - 6 variants
 *   - Requester Approval Pipeline (POST .../requester/approval-pipeline) - 9 variants
 *   - Requests Requiring Attention (POST .../requester/requests-requiring-attention) - 9 variants
 *   - Activity Financial Breakdown (POST .../requester/activity-financial-breakdown) - 3 variants
 *   - Budget Trend (POST .../requester/budget-trend) - 6 variants
 *   - Pending Actual Submission (POST .../requester/pending-actual-submission) - 3 variants
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause confirmed across every folder/sub-area worked on so far (Auth & Session's
 * Switch Profile, all of Control Settings' write endpoints, this folder's Filters and
 * Get Budget Grid). Verified live for "Requester Summary" (Budget - all locations)
 * directly - returned HTML instead of JSON, exact same symptom as every other blocked
 * route. The other 5 routes in this sub-area are documented on that same evidence
 * rather than re-verified individually, per the "spot-check per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Requester Dashboard")
public class RequesterDashboardWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/requester/summary (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Requester Summary")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void requesterSummaryNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/requester/approval-pipeline (9 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Requester Approval Pipeline")
    @Description("Blocked by environment - same route family as Requester Summary. See class Javadoc. Not executed.")
    public void requesterApprovalPipelineNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/requester/requests-requiring-attention (9 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Requests Requiring Attention")
    @Description("Blocked by environment - same route family as Requester Summary. See class Javadoc. Not executed.")
    public void requestsRequiringAttentionNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/requester/activity-financial-breakdown (3 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Activity Financial Breakdown")
    @Description("Blocked by environment - same route family as Requester Summary. See class Javadoc. Not executed.")
    public void activityFinancialBreakdownNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/requester/budget-trend (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Budget Trend")
    @Description("Blocked by environment - same route family as Requester Summary. See class Javadoc. Not executed.")
    public void budgetTrendNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/requester/pending-actual-submission (3 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Pending Actual Submission")
    @Description("Blocked by environment - same route family as Requester Summary. See class Javadoc. Not executed.")
    public void pendingActualSubmissionNotAutomatedDueToEnvironmentIssue() {
    }
}
