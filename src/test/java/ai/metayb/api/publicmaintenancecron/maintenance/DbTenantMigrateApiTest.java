package ai.metayb.api.publicmaintenancecron.maintenance;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import testUtils.BaseApiTest;

/**
 * "07. Public, Maintenance &amp; Cron / Maintenance / Db Tenant Migrate"
 * (POST /web/maintenance/db/tenant-migrate, body {"tenantId": "ng", "operation": "migrate"}).
 *
 * NOT AUTOMATED - intentionally, for safety, not because of the systemic
 * CloudFront/S3 write-blocking pattern seen elsewhere. This is a genuine database
 * migration operation, not a routine CRUD write: even a live call using a
 * deliberately-nonexistent placeholder tenant ID (instead of Postman's real-looking
 * "ng" value) to probe whether this route is also CloudFront-blocked like every
 * other write endpoint in this suite was refused by this session's own safety
 * guardrails as a "Modify Shared Resources" action, before any request was sent.
 *
 * This is correctly marked Blocked / Requires Explicit Authorization rather than
 * silently skipped, per Step 3. It should only be exercised, if ever, with explicit
 * human sign-off, a confirmed-safe/disposable target tenant, and ideally a
 * dedicated non-shared environment - not as part of an unattended regression suite.
 */
@Epic("BrandRunners Web API")
@Feature("Public, Maintenance & Cron - Maintenance")
public class DbTenantMigrateApiTest extends BaseApiTest {

    @Test(enabled = false, groups = {"sanity", "regression"}, description = "BLOCKED: POST /web/maintenance/db/tenant-migrate is a real DB migration operation - not exercised without explicit human authorization")
    @Story("Db Tenant Migrate")
    @Description("Blocked for safety, not by the environment - see class Javadoc. Not executed.")
    public void dbTenantMigrateNotAutomatedForSafety() {
    }
}
