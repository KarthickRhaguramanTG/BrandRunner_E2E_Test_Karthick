package ai.metayb.api.fwm.locationmaster;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Location Master" - write operations, covering 6 Postman requests across
 * 6 distinct routes: Create, Update Location Master, Delete Location Master, Items
 * by ID, Update Items by ID, Delete Items by ID.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause. Verified live for "Create" directly - returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Location Master")
public class LocationMasterWriteOperationsApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/location-master/create returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void createNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: PUT /web/location-master/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Location Master")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateLocationMasterNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: DELETE /web/location-master/:id returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Location Master")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteLocationMasterNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/location-master/:id/items returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Items by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void itemsByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: PUT /web/location-master/:id/items/:itemId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Update Items by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void updateItemsByIdNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: DELETE /web/location-master/:id/items/:itemId returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Items by ID")
    @Description("Blocked by environment - same systemic root cause. Not executed.")
    public void deleteItemsByIdNotAutomatedDueToEnvironmentIssue() {
    }
}
