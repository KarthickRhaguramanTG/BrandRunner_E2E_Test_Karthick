package ai.metayb.api.fwm.continuousstandardactivities;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Continuous Standard Activities" - write operations, covering 5
 * Postman requests across 5 distinct routes: Assign Users by ID, Update Assignment
 * Groups by ID / Config Group ID, Delete Assignment Groups by ID / Config Group ID,
 * Update Assignments by ID, Delete Assignments by ID.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause. Verified live for "Assign Users by ID" directly - returned HTML
 * instead of JSON. Real ids exist for the others (assignmentId=170, configGroupId=
 * "71d04e7e-8bde-492c-a3d4-33ad465e2791", both discovered via Get Assignments by
 * ID), but the route itself is unreachable regardless of id validity.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Continuous Standard Activities")
public class ContinuousStandardActivitiesWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/continuous-standard-activities/:id/assign-users returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Assign Users by ID")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void assignUsersByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: PATCH /web/continuous-standard-activities/:id/assignment-groups/:configGroupId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Assignment Groups by ID / Config Group ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateAssignmentGroupsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: DELETE /web/continuous-standard-activities/:id/assignment-groups/:configGroupId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Assignment Groups by ID / Config Group ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteAssignmentGroupsNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: PATCH /web/continuous-standard-activities/:id/assignments/:assignmentId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Assignments by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateAssignmentsByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: DELETE /web/continuous-standard-activities/:id/assignments/:assignmentId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Assignments by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteAssignmentsByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
