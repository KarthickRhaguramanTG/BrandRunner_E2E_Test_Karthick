package ai.metayb.api.anp.budget;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Budget" - ALL write/mutating operations, covering 18 Postman requests
 * across 12 distinct routes:
 *   - Get Budget Grid (POST /web/budget/all) - 7 variants
 *   - Actual by Budget ID (POST /web/budget/:budgetId/actual)
 *   - Add Actual by Budget ID (POST /web/budget/:budgetId/add-actual)
 *   - Create Budget (POST /web/budget/create-budget)
 *   - Bulk Create Budget (POST /web/budget/bulk-create-budget)
 *   - Update Group Status by Budget Group ID (PATCH /web/budget/group/:budgetGroupId/status)
 *   - Update by Budget Group ID (PUT /web/budget/update/:budgetGroupId)
 *   - Submit by Budget ID (PATCH /web/budget/:budgetId/submit)
 *   - Delete Budget (DELETE /web/budget/:budgetId)
 *   - Pre Update Activity by Budget ID (POST /web/budget/:budgetId/pre-update-activity)
 *   - Export (POST /web/budget/export)
 *   - Upload Media (POST /web/budget/media/upload)
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause confirmed across every folder/sub-area worked on so far. Verified live for
 * "Get Budget Grid" (Default grid) and "Create Budget" directly - both returned HTML
 * instead of JSON. The other 10 routes are documented on that same evidence, per the
 * "spot-check per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Budget")
public class BudgetWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget/all (Get Budget Grid, 7 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Budget Grid")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void getBudgetGridNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget/:budgetId/actual and /add-actual return the CloudFront/S3 SPA fallback, not JSON")
    @Story("Actual by Budget ID / Add Actual by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void actualByBudgetIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget/create-budget returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Budget")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createBudgetNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget/bulk-create-budget returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Bulk Create Budget")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void bulkCreateBudgetNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: PATCH /web/budget/group/:budgetGroupId/status returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Group Status by Budget Group ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateGroupStatusNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: PUT /web/budget/update/:budgetGroupId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update by Budget Group ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateByBudgetGroupIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: PATCH /web/budget/:budgetId/submit returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Submit by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void submitByBudgetIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: DELETE /web/budget/:budgetId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Budget")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void deleteBudgetNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/budget/:budgetId/pre-update-activity returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Pre Update Activity by Budget ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void preUpdateActivityNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/budget/export returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Export")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void exportNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"regression"},
            description = "BLOCKED: POST /web/budget/media/upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Media")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void uploadMediaNotAutomatedDueToEnvironmentIssue() {
    }
}
