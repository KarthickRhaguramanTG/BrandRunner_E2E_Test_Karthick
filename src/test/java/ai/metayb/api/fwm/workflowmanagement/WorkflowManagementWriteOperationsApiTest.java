package ai.metayb.api.fwm.workflowmanagement;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Workflow Management" - write operations, covering 8 Postman requests
 * across 8 distinct routes: Create, AI Create Project, Duplicate by ID, Update by
 * ID, Delete by ID, Activate by ID, Upload Sales Logo, Upload QR Code.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause. Verified live for "Create" directly - returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Workflow Management")
public class WorkflowManagementWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/workflow/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/workflow/ai-create-project returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("AI Create Project")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void aiCreateProjectNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/workflow/duplicate/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Duplicate by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void duplicateByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/workflow/update/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: DELETE /web/workflow/delete/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void deleteByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PATCH /web/workflow/activate/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Activate by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void activateByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/workflow/upload-sales-logo returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Sales Logo")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void uploadSalesLogoNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/workflow/upload-qr-code returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload QR Code")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void uploadQrCodeNotAutomatedDueToEnvironmentIssue() {
    }
}
