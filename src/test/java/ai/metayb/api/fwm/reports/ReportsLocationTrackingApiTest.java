package ai.metayb.api.fwm.reports;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "03. FWM / Reports - Location Tracking" - all 4 Postman requests, all POST:
 * Get Live Tracking Users (2 variants), Get User Activity Listing, Get User Live
 * Location.
 *
 * NOT AUTOMATED in this QA environment - same systemic CloudFront/S3 SPA-fallback
 * root cause confirmed throughout this folder. Verified live for "Get Live
 * Tracking Users" directly - returned HTML instead of JSON.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Reports - Location Tracking")
public class ReportsLocationTrackingApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/user-location-tracking/getUsers (2 variants) returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get Live Tracking Users")
    @Description("Blocked by environment - directly verified live. See class Javadoc. Not executed.")
    public void getLiveTrackingUsersNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/user-location-tracking/getUserActivityListing returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get User Activity Listing")
    @Description("Blocked by environment - same route family. Not executed.")
    public void getUserActivityListingNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/user-location-tracking/getUserLiveLocation returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Get User Live Location")
    @Description("Blocked by environment - same route family. Not executed.")
    public void getUserLiveLocationNotAutomatedDueToEnvironmentIssue() {
    }
}
