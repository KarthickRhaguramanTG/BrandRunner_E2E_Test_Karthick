package ai.metayb.api.anp.dashboard;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Approver Dashboard" - 33 Postman requests across 4 distinct routes
 * (each with 6-9 filter-combination variants):
 *   - My Approvals (POST /web/anp-dashboard-v2/approver/my-approvals) - 6 variants
 *   - Approval Status (POST .../approver/approval-status) - 9 variants
 *   - Approver Pending Aging (POST .../approver/pending-approval-aging) - 9 variants
 *   - Recent Pending Requests (POST .../approver/recent-pending-requests) - 9 variants
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "My Approvals" (Budget - all locations) directly - returned
 * HTML instead of JSON. The other 3 routes are documented on that same evidence, per
 * the "spot-check per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Approver Dashboard")
public class ApproverDashboardWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/approver/my-approvals (6 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("My Approvals")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void myApprovalsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/approver/approval-status (9 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Approval Status")
    @Description("Blocked by environment - same route family as My Approvals. See class Javadoc. Not executed.")
    public void approvalStatusNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/approver/pending-approval-aging (9 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Approver Pending Aging")
    @Description("Blocked by environment - same route family as My Approvals. See class Javadoc. Not executed.")
    public void approverPendingAgingNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/anp-dashboard-v2/approver/recent-pending-requests (9 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Recent Pending Requests")
    @Description("Blocked by environment - same route family as My Approvals. See class Javadoc. Not executed.")
    public void recentPendingRequestsNotAutomatedDueToEnvironmentIssue() {
    }
}
