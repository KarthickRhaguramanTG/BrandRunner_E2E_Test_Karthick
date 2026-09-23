package ai.metayb.api.anp.budgetworkflow;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Budget Workflow" - ALL write operations, covering 7 Postman requests
 * across 7 distinct routes: Workflow Bulk Action, Submit by Budget ID, Workflow
 * Action by Budget ID, Submit Actuals by Budget ID, Workflow Comment by Budget ID,
 * Acknowledge Activity Performance by Budget ID, Upload Actuals by Budget ID.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Workflow Bulk Action" directly - returned HTML instead of
 * JSON. The other 6 routes are documented on that same evidence, per the "spot-check
 * per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget Workflow")
public class BudgetWorkflowWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget-workflow/workflow/bulk-action returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Workflow Bulk Action")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void workflowBulkActionNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget-workflow/:budgetId/submit returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Submit by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void submitByBudgetIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/budget-workflow/:budgetId/workflow/action returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Workflow Action by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void workflowActionNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/budget-workflow/:budgetId/submit-actuals returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Submit Actuals by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void submitActualsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget-workflow/:budgetId/workflow/comment returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Workflow Comment by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void workflowCommentNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget-workflow/:budgetId/acknowledgeActivityPerformance returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Acknowledge Activity Performance by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void acknowledgeActivityPerformanceNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget-workflow/:budgetId/upload-actuals returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Actuals by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void uploadActualsNotAutomatedDueToEnvironmentIssue() {
    }
}
