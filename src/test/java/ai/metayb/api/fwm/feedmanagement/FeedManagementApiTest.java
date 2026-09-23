package ai.metayb.api.fwm.feedmanagement;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

import static io.restassured.RestAssured.given;

/**
 * "03. FWM / Feed Management" - all 5 Postman requests. Only List Feeds is GET; the
 * other 4 (Upload Media, Create Feed, Edit Feed, Delete Feed) are all POST -
 * including Delete, which uses POST rather than the DELETE verb.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Feed Management")
public class FeedManagementApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "List Feeds returns existing feed posts")
    @Story("List Feeds")
    @Description("Verified live: HTTP 200 (page=1, limit=10), data.feeds is a non-empty array of {id, description, createdTime}.")
    public void listFeedsReturnsFeeds() {
        Response response = given().spec(requestSpecification).get("/web/feed/getFeeds?page=1&limit=10");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data.feeds").isEmpty(), "data.feeds should be non-empty");
    }

    @Test(groups = { "regression", "negative"}, description = "List Feeds without authentication fails")
    @Story("List Feeds")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listFeedsWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/feed/getFeeds?page=1&limit=10");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: POST /web/feed/media/upload returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Upload Media")
    @Description("Blocked by the same systemic CloudFront/S3 SPA-fallback root cause confirmed throughout this folder. Not executed.")
    public void uploadMediaNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/feed/postFeed returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Create Feed")
    @Description("Blocked by environment - directly verified live. Not executed.")
    public void createFeedNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/feed/editFeed returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Edit Feed")
    @Description("Blocked by environment - same route family. Not executed.")
    public void editFeedNotAutomatedDueToEnvironmentIssue() {
    }

    @Test(enabled = false, groups = { "regression"}, description = "BLOCKED: POST /web/feed/deleteFeed returns the CloudFront/S3 SPA fallback, not JSON")
    @Story("Delete Feed")
    @Description("Blocked by environment - same route family (note: Delete uses POST, not the DELETE verb). Not executed.")
    public void deleteFeedNotAutomatedDueToEnvironmentIssue() {
    }
}
