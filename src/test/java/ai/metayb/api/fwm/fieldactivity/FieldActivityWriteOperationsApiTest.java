package ai.metayb.api.fwm.fieldactivity;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Field Activity" - ALL write operations, covering 26 Postman requests
 * across 15 distinct routes:
 *   - List Monthwise Activities: "no city/status", "selected cities + status" (POST /web/field-activity) - 2
 *   - Get Campaigns Table: "All locations", "Selected locations + status" (POST /web/field-activity/campaigns-table) - 2
 *   - Assign Field Users: 7 variants (POST /web/field-activity/:id/assign)
 *   - Update Field Assignment: 4 variants (PATCH /web/field-activity/:id/assign)
 *   - Delete by Budget ID / Date (DELETE /web/field-activity/delete/:budgetId/:date)
 *   - Upload Media (POST /web/field-activity/media/upload)
 *   - Attendance Regularization (POST /web/field-activity/attendance-regularization)
 *   - Supervisor Reconcile Send/Verify OTP, Submit (POST .../supervisor-reconcile/*) - 3
 *   - Promoter Close Sale Send/Verify OTP, Submit (POST .../promoter-close-sale/*) - 3
 *   - Update Field Activity (PATCH /web/field-activity/:id)
 *   - Field Users (POST /web/field-activity/field-users)
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause confirmed across every folder worked on so far. Verified live for "List
 * Monthwise Activities", "Assign Field Users", "Delete by Budget ID / Date",
 * "Update Field Activity", "Field Users", and "Supervisor Reconcile Send OTP"
 * directly - all returned HTML instead of JSON. The other routes are documented on
 * that same evidence, per the "spot-check per sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Field Activity")
public class FieldActivityWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/field-activity (List Monthwise Activities, 2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("List Monthwise Activities")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void listMonthwiseActivitiesNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/field-activity/campaigns-table (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Campaigns Table")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void getCampaignsTableNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/field-activity/:id/assign (Assign Field Users, 7 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Assign Field Users")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void assignFieldUsersNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: PATCH /web/field-activity/:id/assign (Update Field Assignment, 4 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Field Assignment")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateFieldAssignmentNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: DELETE /web/field-activity/delete/:budgetId/:date returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete by Budget ID / Date")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void deleteByBudgetIdDateNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/field-activity/media/upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Media")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void uploadMediaNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: POST /web/field-activity/attendance-regularization returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Attendance Regularization")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void attendanceRegularizationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: POST .../supervisor-reconcile/send-otp, verify-otp, submit return the CloudFront/S3 SPA fallback, not JSON")
    @Story("Supervisor Reconcile (Send OTP / Verify OTP / Submit)")
    @Description("Blocked by environment - directly verified live for send-otp. See class Javadoc. Not executed.")
    public void supervisorReconcileNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: POST .../promoter-close-sale/send-otp, verify-otp, submit return the CloudFront/S3 SPA fallback, not JSON")
    @Story("Promoter Close Sale (Send OTP / Verify OTP / Submit)")
    @Description("Blocked by environment - same route family as Supervisor Reconcile. Not executed.")
    public void promoterCloseSaleNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: PATCH /web/field-activity/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Field Activity")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void updateFieldActivityNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/field-activity/field-users returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Field Users")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void fieldUsersNotAutomatedDueToEnvironmentIssue() {
    }
}
