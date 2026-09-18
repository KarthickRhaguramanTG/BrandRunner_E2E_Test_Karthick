package ai.metayb.api.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Replaces REST Assured's stock RequestLoggingFilter/ResponseLoggingFilter.
 *
 * Those filters were found (by direct observation of real BrandRunners API test
 * output) to print header values verbatim regardless of RestAssuredConfig's
 * LogConfig.blacklistHeader(...) - the bearer token and Set-Cookie values appeared
 * in plain text in the console log even with blacklistHeader("Authorization")
 * configured on the request specification. This filter masks sensitive header
 * values itself, through the project's existing Log4j2 logger, instead of relying
 * on that RestAssured mechanism.
 *
 * Also masks sensitive JSON fields in RESPONSE bodies (key/token/secret/password in
 * the field name) - found necessary when "01. Control Settings / Tenant & BU
 * Settings / Get Tenant Settings" and "Get Google Map API Key" turned out to return
 * real third-party API keys and integration tokens (SMS/WhatsApp/email providers,
 * Google Maps) directly in the response body, not just in headers.
 *
 * Additionally masks by VALUE shape, not just field name:
 *   - Get Tenant Settings returned an AWS Access Key ID (format AKIA + 16 chars) under
 *     a field named "emailUser" (an SES SMTP username), which the field-name pattern
 *     above does not catch since "emailUser" contains none of key/token/secret/password.
 *   - Users / Get Bulk Jobs returned a presigned S3 URL (under "resultUrl", again not a
 *     "sensitive" field name) carrying temporary AWS STS credentials in its query
 *     string - an ASIA-prefixed access key id (STS temporary keys use ASIA, not AKIA)
 *     plus X-Amz-Security-Token/X-Amz-Signature, which together grant real (if
 *     short-lived) download access.
 *   - "07. Public, Maintenance & Cron / Cron" authenticates via a "cron-secret"
 *     request header (not Authorization) - masked pre-emptively even though this
 *     suite's own Cron tests never send a real value, since a real one could be
 *     supplied later (e.g. via config) and this is exactly the kind of credential
 *     header Section 11 requires masked.
 */
public class SanitizedApiLoggingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(SanitizedApiLoggingFilter.class);
    private static final Set<String> SENSITIVE_HEADERS = Set.of("authorization", "cookie", "set-cookie", "cron-secret");
    private static final Pattern SENSITIVE_JSON_FIELD = Pattern.compile(
            "(\"[^\"]*(?:key|token|secret|password)[^\"]*\"\\s*:\\s*)\"[^\"]*\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern AWS_ACCESS_KEY_ID = Pattern.compile("(?:AKIA|ASIA)[0-9A-Z]{16}");
    private static final Pattern PRESIGNED_URL_CREDENTIAL = Pattern.compile(
            "(X-Amz-(?:Security-Token|Signature|Credential)=)[^&\"\\\\]+", Pattern.CASE_INSENSITIVE);
    private static final String MASK = "****";

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        logger.info("--> {} {}", requestSpec.getMethod(), requestSpec.getURI());
        requestSpec.getHeaders().forEach(h -> logger.info("    {}: {}", h.getName(), maskHeader(h.getName(), h.getValue())));

        Response response = ctx.next(requestSpec, responseSpec);

        logger.info("<-- {} {} {}", response.getStatusCode(), requestSpec.getMethod(), requestSpec.getURI());
        response.getHeaders().forEach(h -> logger.info("    {}: {}", h.getName(), maskHeader(h.getName(), h.getValue())));
        if (isTextual(response.getContentType())) {
            logger.info("    body: {}", maskJsonBody(response.getBody().asString()));
        } else {
            logger.info("    body: [{} bytes, content-type={}, not logged]", response.getBody().asByteArray().length, response.getContentType());
        }

        return response;
    }

    private static boolean isTextual(String contentType) {
        if (contentType == null) {
            return true;
        }
        String lower = contentType.toLowerCase();
        return lower.contains("json") || lower.contains("text") || lower.contains("xml");
    }

    private static String maskHeader(String headerName, String value) {
        return SENSITIVE_HEADERS.contains(headerName.toLowerCase()) ? MASK : value;
    }

    private static String maskJsonBody(String body) {
        if (body == null || body.isEmpty()) {
            return body;
        }
        String maskedByField = SENSITIVE_JSON_FIELD.matcher(body).replaceAll("$1\"" + MASK + "\"");
        String maskedKeys = AWS_ACCESS_KEY_ID.matcher(maskedByField).replaceAll(MASK);
        return PRESIGNED_URL_CREDENTIAL.matcher(maskedKeys).replaceAll("$1" + MASK);
    }
}
