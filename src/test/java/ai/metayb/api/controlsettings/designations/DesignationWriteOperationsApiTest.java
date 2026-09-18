package ai.metayb.api.controlsettings.designations;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "01. Control Settings / Designations" - write requests: Create Designation
 * (POST /web/designation/create-designation), Update Designation
 * (PUT /web/designation/update-designation), Upload Bulk
 * (POST /web/designation/bulk-upload), Delete List (DELETE /web/designation/delete-list).
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed across every other folder/sub-area worked on so far. Verified
 * live with realistic bodies and a safe non-existent id (999999) for Update - every
 * call returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Designations")
public class DesignationWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/designation/create-designation returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Designation")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void createDesignationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: PUT /web/designation/update-designation returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Designation")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void updateDesignationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/designation/bulk-upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Bulk")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void uploadBulkNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: DELETE /web/designation/delete-list returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete List")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void deleteListNotAutomatedDueToEnvironmentIssue() {
    }
}
