package ai.metayb.api.fwm.gallery;

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
 * "03. FWM / Gallery" - all 4 Postman requests, all GET, all automated. Query
 * params (locationIds=1, standardActivityId=1) mirror the Postman collection's own
 * example values exactly.
 *
 * *** APPLICATION-SIDE FINDING *** "Get User Info" returns HTTP 500 with a raw SQL
 * query fragment in the error message - the same class of bug already found at
 * "03. FWM / Form Configuration / Get Form Configuration" (see FormConfigurationApiTest).
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Gallery")
public class GalleryApiTest extends BaseApiTest {

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Standardactivity returns standard activity data for a location")
    @Story("Get Standardactivity")
    @Description("Verified live: HTTP 200 for locationIds=1, 'Standard activity data fetched successfully', data is a list (empty - asserted as observed).")
    public void getStandardactivityReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/gallery/standardactivity?locationIds=1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list");
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Activityform returns activity form data for a standard activity")
    @Story("Get Activityform")
    @Description("Verified live: HTTP 200 for standardActivityId=1, 'Activity form data fetched successfully', data is a list (empty - asserted as observed).")
    public void getActivityformReturnsData() {
        Response response = given().spec(requestSpecification).get("/web/gallery/activityform?standardActivityId=1");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list");
    }

    @Test(groups = { "regression"}, description = "Get User Info, called exactly as the Postman collection defines it, currently errors server-side")
    @Story("Get User Info")
    @Description("Verified live: HTTP 500 with a raw SQL query fragment in the error message - a real backend " +
            "bug (see class Javadoc and FormConfigurationApiTest for the sibling instance), documented as " +
            "observed behavior per Step 8.")
    public void getUserInfoAsDefinedInPostmanErrors() {
        Response response = given().spec(requestSpecification).get("/web/gallery/userInfo?locationIds=1");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getBoolean("success"), false);
    }

    @Test(groups = { "regression"}, description = "Get Gallery Data, called exactly as the Postman collection defines it, returns a validation error")
    @Story("Get Gallery Data")
    @Description("Verified live: HTTP 500, message 'startDate and endDate are required' - Postman's saved " +
            "request for this one has no date params, documented per Step 8 rather than inventing them.")
    public void getGalleryDataAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification)
                .get("/web/gallery/galleryData?locationIds=1&standardActivityId=1&page=1&limit=20");

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("message"), "startDate and endDate are required");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Standardactivity without authentication fails")
    @Story("Get Standardactivity")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void getStandardactivityWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/gallery/standardactivity?locationIds=1");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
