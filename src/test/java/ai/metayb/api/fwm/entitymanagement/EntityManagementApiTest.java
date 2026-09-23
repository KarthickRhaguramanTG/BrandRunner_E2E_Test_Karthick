package ai.metayb.api.fwm.entitymanagement;

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
 * "03. FWM / Entity Management" - read requests: List Entity Management, Export,
 * Get Media Download Url, Get Entity Management by ID, Get Line Items by ID, Get
 * Logs by ID (fully automated); Get Data Values by ID (documented observed behavior).
 *
 * id=133 ("Dooms day") is a real, pre-existing entity in this QA tenant, not fabricated.
 *
 * SECURITY NOTE: "Get Media Download Url" returns a presigned S3 URL carrying live
 * AWS STS credentials in its query string (same class of finding as ANP's
 * Get Bulk Jobs). SanitizedApiLoggingFilter already masks this - the test below only
 * asserts the field exists, never logs or prints the URL.
 */
@Epic("BrandRunners Web API")
@Feature("FWM - Entity Management")
public class EntityManagementApiTest extends BaseApiTest {

    @Test(groups = {"regression", "sanity", "positive"}, description = "List Entity Management returns existing entities")
    @Story("List Entity Management")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, name, entityRefId, status}.")
    public void listEntityManagementReturnsEntities() {
        Response response = given().spec(requestSpecification).get("/web/entity");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Export returns the entity export data")
    @Story("Export")
    @Description("Verified live: HTTP 200, data is a non-empty array of {id, ouid, entityId, entityName}.")
    public void exportReturnsEntityData() {
        Response response = given().spec(requestSpecification).get("/web/entity/export");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("data").isEmpty(), "data should be non-empty");
    }

    @Test(groups = {"sanity", "regression", "positive"}, description = "Get Media Download Url returns a presigned download URL")
    @Story("Get Media Download Url")
    @Description("Verified live: HTTP 200, response includes a downloadUrl field. The URL's value is never " +
            "logged or asserted beyond presence - see class Javadoc security note.")
    public void getMediaDownloadUrlReturnsUrl() {
        Response response = given().spec(requestSpecification).get("/web/entity/media/download-url");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("downloadUrl"), "Response should include a downloadUrl");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Entity Management by ID returns a real entity's details")
    @Story("Get Entity Management by ID")
    @Description("Verified live: HTTP 200 for id=133, data.entity.name/data.entity.status present.")
    public void getEntityManagementByIdReturnsDetails() {
        Response response = given().spec(requestSpecification).get("/web/entity/133");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("data.entity.id"), 133);
        Assert.assertNotNull(response.jsonPath().getString("data.entity.name"));
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Line Items by ID returns line items for a real entity")
    @Story("Get Line Items by ID")
    @Description("Verified live: HTTP 200 for id=133, 'Line items fetched successfully', data.items present (empty - asserted as observed).")
    public void getLineItemsByIdReturnsItems() {
        Response response = given().spec(requestSpecification).get("/web/entity/133/line-items");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data.items"), "data.items should be a list");
    }

    @Test(groups = {"regression", "sanity", "positive"}, description = "Get Logs by ID returns audit logs for a real entity")
    @Story("Get Logs by ID")
    @Description("Verified live: HTTP 200 for id=133, 'Entity logs fetched successfully', data present (empty array - asserted as observed).")
    public void getLogsByIdReturnsLogs() {
        Response response = given().spec(requestSpecification).get("/web/entity/133/logs");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getList("data"), "data should be a list");
    }

    @Test(groups = { "regression"}, description = "Get Data Values by ID, called with only the real entity id, returns a validation error")
    @Story("Get Data Values by ID")
    @Description("Verified live: HTTP 400, message 'Invalid input: expected string, received undefined' - " +
            "Postman's saved request for this one has no extra query params either, documented per Step 8.")
    public void getDataValuesByIdAsDefinedInPostmanReturnsValidationError() {
        Response response = given().spec(requestSpecification).get("/web/entity/133/data-values");

        Assert.assertEquals(response.statusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("expected string"));
    }

    @Test(groups = { "regression", "negative"}, description = "List Entity Management without authentication fails")
    @Story("List Entity Management")
    @Description("Verified live: HTTP 401, message 'Authentication token missing'.")
    public void listEntityManagementWithoutAuthFails() {
        Response response = given().spec(noAuthRequestSpecification).get("/web/entity");

        Assert.assertEquals(response.statusCode(), 401);
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication token missing");
    }
}
