package ai.metayb.canary;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * Resolves Canary login credentials from an AWS Secrets Manager secret.
 *
 * Only invoked when a secret id is actually configured (see CanaryConfig) -
 * this class, and the AWS SDK classes it touches, are never loaded during a
 * local run that doesn't set CANARY_SECRET_ID, so local validation never
 * needs AWS credentials just to exercise the API flow.
 *
 * Expected secret structure (a single JSON-format secret, not two separate
 * secrets):
 * <pre>
 * {
 *   "username": "canary@example.com",
 *   "password": "..."
 * }
 * </pre>
 * "username" is optional - CANARY_USERNAME may be used instead if the login
 * email itself isn't considered sensitive (see CanaryConfig's precedence).
 * "password" is required whenever a secret id is configured.
 */
final class SecretsManagerCredentialResolver {

    private SecretsManagerCredentialResolver() {
    }

    record Credentials(String username, String password) {
    }

    static Credentials resolve(String secretId) {
        try (SecretsManagerClient client = SecretsManagerClient.create()) {
            GetSecretValueResponse response = client.getSecretValue(
                    GetSecretValueRequest.builder().secretId(secretId).build());

            JsonNode node = new ObjectMapper().readTree(response.secretString());
            String username = node.hasNonNull("username") ? node.get("username").asText() : null;
            String password = node.hasNonNull("password") ? node.get("password").asText() : null;

            if (password == null || password.isBlank()) {
                throw new IllegalStateException("Secrets Manager secret '" + secretId + "' has no 'password' field");
            }
            return new Credentials(username, password);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            // Deliberately logs only the exception type, never e.getMessage() verbatim -
            // AWS SDK exceptions here describe auth/permission/not-found failures, not
            // secret content, but there's no reason to take that on faith in a canary log.
            throw new IllegalStateException(
                    "Failed to resolve credentials from Secrets Manager secret '" + secretId + "': "
                            + e.getClass().getSimpleName(), e);
        }
    }
}
