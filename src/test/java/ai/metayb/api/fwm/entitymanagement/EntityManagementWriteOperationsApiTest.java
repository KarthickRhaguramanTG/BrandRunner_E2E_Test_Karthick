package ai.metayb.api.fwm.entitymanagement;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Entity Management" - write operations, covering 8 Postman requests
 * across 8 distinct routes: Create, Upload Media, Update Entity Management, Delete
 * Entity Management, Line Items Bulk by ID, Line Items by ID, Update Line Items by
 * ID, Delete Line Items by ID.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause. Verified live for "Create" directly - returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Entity Management")
public class EntityManagementWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/entity/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/entity/media/upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Media")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void uploadMediaNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/entity/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Entity Management")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateEntityManagementNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: DELETE /web/entity/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Entity Management")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteEntityManagementNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/entity/:id/line-items/bulk returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Line Items Bulk by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void lineItemsBulkByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: POST /web/entity/:id/line-items returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Line Items by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void lineItemsByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: PUT /web/entity/:id/line-items/:lineItemId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Line Items by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateLineItemsByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"}, description = "BLOCKED: DELETE /web/entity/:id/line-items/:lineItemId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Line Items by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteLineItemsByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
