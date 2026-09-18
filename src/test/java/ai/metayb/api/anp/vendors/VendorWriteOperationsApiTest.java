package ai.metayb.api.anp.vendors;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "02. ANP / Vendors" - ALL write operations, covering 4 Postman requests across 4
 * distinct routes: Create Vendor, Update Vendor, Delete by ID, Upload Bulk.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback root
 * cause. Verified live for "Create Vendor" directly - returned HTML instead of JSON.
 * The other 3 routes are documented on that same evidence, per the "spot-check per
 * sub-area" approach.
 */
@Epic("BrandRunners Web API")
@Feature("ANP - Vendors")
public class VendorWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/vendor/create-vendor returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Vendor")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createVendorNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: PUT /web/vendor/update-vendor returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Vendor")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void updateVendorNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: DELETE /web/vendor/delete/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete by ID")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void deleteByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"api", "regression"},
            description = "BLOCKED: POST /web/vendor/bulk-upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Bulk")
    @Description("Blocked by environment - same route family. See class Javadoc. Not executed.")
    public void uploadBulkNotAutomatedDueToEnvironmentIssue() {
    }
}
