package ai.metayb.api.controlsettings.locations;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "01. Control Settings / Locations" - ALL write operations, covering 9 Postman requests
 * across 7 distinct routes:
 *   - Create Location: "Root location", "Child location" (POST /web/location)
 *   - Hierarchy (POST /web/location/hierarchy)
 *   - Reset Hierarchy by Business Unit ID (DELETE /web/location/hierarchy/reset/:businessUnitId)
 *   - Bulk (POST /web/location/bulk)
 *   - Update Location (PUT /web/location/:id)
 *   - Delete Location (DELETE /web/location/:id)
 *   - Apply Location (POST /web/location/apply-location)
 *   - Upload Bulk (POST /web/location/bulk-upload)
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause confirmed across every other folder/sub-area. Verified live for every route
 * above individually with realistic bodies and safe non-existent ids (999999) for
 * Update/Delete/Reset - every call returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("Control Settings - Locations")
public class LocationWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/location (Root location / Child location) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Location")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void createLocationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: POST /web/location/hierarchy returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Hierarchy")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void createHierarchyNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"},
            description = "BLOCKED: DELETE /web/location/hierarchy/reset/:businessUnitId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Reset Hierarchy by Business Unit ID")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void resetHierarchyNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/location/bulk returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Bulk")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void bulkCreateNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: PUT /web/location/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Location")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void updateLocationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: DELETE /web/location/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Location")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void deleteLocationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/location/apply-location returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Apply Location")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void applyLocationNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"},
            description = "BLOCKED: POST /web/location/bulk-upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Bulk")
    @Description("Blocked by environment: see class Javadoc. Not executed.")
    public void uploadBulkNotAutomatedDueToEnvironmentIssue() {
    }
}
