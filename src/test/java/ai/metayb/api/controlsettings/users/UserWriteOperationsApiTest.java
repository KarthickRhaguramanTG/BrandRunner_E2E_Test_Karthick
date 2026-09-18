package ai.metayb.api.controlsettings.users;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "01. Control Settings / Users" - ALL write operations, covering 9 Postman requests
 * across 5 distinct routes:
 *   - Create User: "Create · SMS login", "Create · EMAIL login", "Create · BOTH login" (POST /web/user)
 *   - Export Users: "All users", "Inactive search" (POST /web/user/export)
 *   - Update User: "Update · locations + designations", "Deactivate" (PUT /web/user/:id)
 *   - Delete User (DELETE /web/user/:id)
 *   - Upload Users Bulk File (POST /web/user/bulk/upload)
 *
 * NOT AUTOMATED in this QA environment - same systemic root cause already confirmed
 * across multiple folders/sub-areas (Auth & Session's Switch Profile, Control Settings'
 * Save App Settings, and a diagnostic check against Create Business Unit / Create
 * Designation): every POST/PUT/DELETE route outside "/web/auth/*" tested so far in this
 * environment returns HTTP 200 with the CloudFront/S3 SPA fallback page instead of JSON.
 * Verified live for every route above individually, including with realistic non-empty
 * bodies and safely non-existent ids (999999) for Update/Delete so no real QA data was
 * ever at risk - every call returned the same HTML fallback.
 *
 * Because Create itself never reaches the real API, the "create test data, then
 * update/delete it" approach has nothing to build on for this sub-area.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Users")
public class UserWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/user (Create · SMS/EMAIL/BOTH login) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create User")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void createUserNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/user/export (All users / Inactive search) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Export Users")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void exportUsersNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: PUT /web/user/:id (locations+designations / Deactivate) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update User")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void updateUserNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: DELETE /web/user/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete User")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void deleteUserNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/user/bulk/upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Users Bulk File")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void uploadUsersBulkFileNotAutomatedDueToEnvironmentIssue() {
    }
}
