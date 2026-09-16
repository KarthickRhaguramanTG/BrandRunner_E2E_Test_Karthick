package testUtils;

import ai.metayb.config.ConfigManager;
import ai.metayb.ui.core.DataReader;
import ai.metayb.utils.PerformanceRecorder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

@Listeners({AllureTestNg.class})
public class BaseApiTest {

    private static final Logger logger = LogManager.getLogger(BaseApiTest.class);

    // Authorization carries the bearer token, Cookie/Set-Cookie carry accessToken/csrf -
    // none of these may appear in plain text in console output or Allure attachments.
    // RestAssured's blacklistHeader still logs the header name but replaces its value.
    private static final RestAssuredConfig SANITIZED_LOG_CONFIG = RestAssuredConfig.config()
            .logConfig(LogConfig.logConfig()
                    .blacklistHeader("Authorization")
                    .blacklistHeader("Cookie")
                    .blacklistHeader("Set-Cookie"));

    protected static final PerformanceRecorder perfRecorder = new PerformanceRecorder();
    protected static String baseURI;
    protected static String authToken;
    protected static RequestSpecification requestSpecification;
    protected static RequestSpecification noAuthRequestSpecification;

    @BeforeSuite
    public void globalSetup() throws Exception {
        baseURI = ConfigManager.getApiBaseUrl();
        logger.info("Base URI initialized for environment: {} (tenant: {})", baseURI, ConfigManager.getApiTenant());

        DataReader credentials = new DataReader();
        Response loginResponse = performLogin(credentials.apiEmail, credentials.apiPassword);

        // Per the BrandRunners Web APIs Postman collection's Login test script, the session
        // token is returned as the "accessToken" cookie, not a field in the JSON body.
        authToken = loginResponse.getCookie("accessToken");
        if (authToken == null || authToken.isEmpty()) {
            throw new RuntimeException("Login failed - no accessToken cookie in response. Full response:\n" + loginResponse.asString());
        }
        logger.info("Auth token acquired successfully.");

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setConfig(SANITIZED_LOG_CONFIG)
                .addHeader("X-Amz-Tenant-Id", ConfigManager.getApiTenant())
                .addHeader("Authorization", "Bearer " + authToken)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        noAuthRequestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setConfig(SANITIZED_LOG_CONFIG)
                .addHeader("X-Amz-Tenant-Id", ConfigManager.getApiTenant())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    @AfterMethod
    public void recordMetrics() {
        perfRecorder.generateReport();
    }

    protected void recordResponseTime(long startTime, String endpoint) {
        long responseTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        perfRecorder.record(endpoint, responseTime);
    }

    /**
     * POST /web/auth/login (BrandRunners Web APIs Postman collection, "00. Auth & Session/Auth/Login").
     * Unauthenticated by design - never attach the Authorization-bearing requestSpecification here.
     */
    protected static Response performLogin(String email, String password) throws Exception {
        return given()
                .config(SANITIZED_LOG_CONFIG)
                .baseUri(baseURI)
                .header("X-Amz-Tenant-Id", ConfigManager.getApiTenant())
                .contentType("application/json")
                .body(loginPayload(email, password))
                .post("/web/auth/login");
    }

    private static String loginPayload(String email, String password) throws Exception {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        return new ObjectMapper().writeValueAsString(payload);
    }
}
