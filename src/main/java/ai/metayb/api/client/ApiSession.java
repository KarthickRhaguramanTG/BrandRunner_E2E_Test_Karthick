package ai.metayb.api.client;

/**
 * A single authenticated session's identity: the bearer token and business
 * unit id every authenticated BrandRunners Web API call needs. Deliberately
 * holds nothing else (no email/password) - this is the thing that gets passed
 * around after login, never the credentials that produced it.
 */
public record ApiSession(String authToken, String businessUnitId) {
}
