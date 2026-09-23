package ai.metayb.api.fwm.targetmanagement;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Target Management" - write operations, covering 3 Postman requests
 * across 3 distinct routes: Create, Update by ID, Delete by ID.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause. Verified live for "Create" directly - returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Target Management")
public class TargetManagementWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/targetmanagement/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: PUT /web/targetmanagement/update/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: DELETE /web/targetmanagement/delete/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
