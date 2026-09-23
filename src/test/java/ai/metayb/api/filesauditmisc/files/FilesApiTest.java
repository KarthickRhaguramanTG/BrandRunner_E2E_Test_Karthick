package ai.metayb.api.filesauditmisc.files;

import ai.metayb.api.utils.SanitizedApiLoggingFilter;
import ai.metayb.config.ConfigManager;
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
 * "06. Files, Audit &amp; Misc / Public Files" and "/ Protected Files" - both Postman
 * requests, kept in one class per the established pattern for small sub-areas.
 *
 * Neither request's saved Postman example defines a real {{folderName}}/{{filename}}
 * (not present anywhere as a collection variable), so there is no known real,
 * previously-uploaded file in this tenant to exercise the true "file found" path -
 * test data unavailable, same class of gap as "Get Vehicles By Location" in
 * "05. Master Data". Both endpoints are called with a placeholder folder/filename,
 * documenting the real observed "not found" behavior, per Step 8, while every
 * auth/tenant-scoping behavior below IS fully verified live and asserted for real.
 *
 * Confirmed live: "Public Files" (tagged "noauth" in Postman) genuinely accepts
 * anonymous calls; "Protected Files" (no explicit auth override - inherits the
 * collection's bearer auth) genuinely requires both a valid token AND the
 * "business_unit" header, with distinct error messages for each missing piece -
 * the same two-stage auth/tenant-scoping check seen throughout this suite
 * (e.g. LogoutApiTest).
 */
@Epic("BrandRunners Web API")
@Feature("Files, Audit & Misc - Files")
public class FilesApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Public File by Folder Name/Filename works without any authentication")
    @Story("Get Public File by Folder Name / Filename")
    @Description("Verified live: HTTP 400, message 'Image Not Found', called with a placeholder folder/filename " +
            "(no real uploaded file's path is known - see class Javadoc). Confirms the endpoint is genuinely " +
            "public - no Authorization header or business_unit header is sent at all.")
    public void getPublicFileWithoutAuthReturnsNotFoundForPlaceholderFile() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/file/placeholder-folder/placeholder-file.png");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Image Not Found");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Protected File by Folder Name/Filename without any authentication fails")
    @Story("Get Protected File by Folder Name / Filename")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'. Unlike 'Public Files', this " +
            "route inherits the collection's bearer auth in Postman and genuinely enforces it server-side.")
    public void getProtectedFileWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification)
                .get("/web/files/placeholder-folder/placeholder-file.png");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }

    @Test(groups = { "regression", "negative"}, description = "Get Protected File by Folder Name/Filename authenticated but missing the business_unit header fails")
    @Story("Get Protected File by Folder Name / Filename")
    @Description("Verified live: HTTP 401, message 'You do not have access to this business unit.' - distinct " +
            "from the no-auth case, proving the token itself IS recognized and only business-unit scoping fails.")
    public void getProtectedFileAuthenticatedButMissingBusinessUnitHeaderFails() {
        Response response = given()
                .filter(new SanitizedApiLoggingFilter())
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ConfigManager.getApiTenant())
                .header("Authorization", "Bearer " + authToken)
                .get("/web/files/placeholder-folder/placeholder-file.png");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "You do not have access to this business unit.");
    }

    @Test(groups = { "regression"}, description = "Get Protected File by Folder Name/Filename, fully authenticated, returns not-found for a placeholder file")
    @Story("Get Protected File by Folder Name / Filename")
    @Description("Verified live: HTTP 400, message 'Image Not Found', with full auth + tenant + business_unit " +
            "headers. Called with a placeholder folder/filename - see class Javadoc for why no real file is used.")
    public void getProtectedFileWithFullAuthReturnsNotFoundForPlaceholderFile() {
        Response response = given().spec(requestSpecification)
                .get("/web/files/placeholder-folder/placeholder-file.png");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), "Image Not Found");
    }
}
