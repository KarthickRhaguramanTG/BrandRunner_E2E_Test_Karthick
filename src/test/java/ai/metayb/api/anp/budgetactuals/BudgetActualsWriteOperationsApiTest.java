package ai.metayb.api.anp.budgetactuals;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Budget Actuals" - ALL write operations, covering 6 Postman requests
 * across 3 distinct routes:
 *   - Submit Budget Actuals: "Create actuals", "Update existing actuals" (POST
 *     /web/budget-actuals/:budgetId/actuals/submit) - 2 variants
 *   - Preview Budget Actuals: "Single date", "Date range" (POST
 *     /web/budget-actuals/:budgetId/actuals-preview) - 2 variants
 *   - Finalize Actuals Approval: "Approve", "Reject" (POST
 *     /web/budget-actuals/actuals/:actualMasterId/finalize-approval) - 2 variants
 *     (this route additionally needs a real actualMasterId, which doesn't exist
 *     either - see BudgetActualsApiTest - so it is doubly unautomatable here)
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Submit Budget Actuals" directly - returned HTML instead of
 * JSON. The other 2 routes are documented on that same evidence, per the "spot-check
 * per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget Actuals")
public class BudgetActualsWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/budget-actuals/:budgetId/actuals/submit returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Submit Budget Actuals")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void submitBudgetActualsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/budget-actuals/:budgetId/actuals-preview returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Preview Budget Actuals")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void previewBudgetActualsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/budget-actuals/actuals/:actualMasterId/finalize-approval returns the CloudFront/S3 SPA fallback, not JSON - also requires an actualMasterId that doesn't exist in this environment")
    @Story("Finalize Actuals Approval")
    @Description("Blocked by environment AND by unavailable test data (no real actualMasterId exists). See class Javadoc. Not executed.")
    public void finalizeActualsApprovalNotAutomatedDueToEnvironmentIssue() {
    }
}
