package ai.metayb.api.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

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
 */
public class SanitizedApiLoggingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(SanitizedApiLoggingFilter.class);
    private static final Set<String> SENSITIVE_HEADERS = Set.of("authorization", "cookie", "set-cookie");
    private static final String MASK = "****";

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        logger.info("--> {} {}", requestSpec.getMethod(), requestSpec.getURI());
        requestSpec.getHeaders().forEach(h -> logger.info("    {}: {}", h.getName(), mask(h.getName(), h.getValue())));

        Response response = ctx.next(requestSpec, responseSpec);

        logger.info("<-- {} {} {}", response.getStatusCode(), requestSpec.getMethod(), requestSpec.getURI());
        response.getHeaders().forEach(h -> logger.info("    {}: {}", h.getName(), mask(h.getName(), h.getValue())));
        logger.info("    body: {}", response.getBody().asString());

        return response;
    }

    private static String mask(String headerName, String value) {
        return SENSITIVE_HEADERS.contains(headerName.toLowerCase()) ? MASK : value;
    }
}
