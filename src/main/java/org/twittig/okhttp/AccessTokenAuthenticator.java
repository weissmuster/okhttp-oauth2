package org.twittig.okhttp;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccessTokenAuthenticator implements Authenticator {

  private final AccessTokenProvider accessTokenProvider;

  public AccessTokenAuthenticator(AccessTokenProvider accessTokenProvider) {
    this.accessTokenProvider = accessTokenProvider;
  }

  @Nullable
  @Override
  public Request authenticate(@Nullable Route route, @NotNull Response response) {

    // We need to have a token in order to refresh it.
    String token = accessTokenProvider.getToken() == null ? null : accessTokenProvider.getToken();

    synchronized (this) {
      String newToken = accessTokenProvider.getToken();

      // Check if the request made was previously made as an authenticated request.
      if (response.request().header("Authorization") != null) {

        // If the token has changed since the request was made, use the new token.
        if (!newToken.equals(token)) {
          return response
              .request()
              .newBuilder()
              .removeHeader("Authorization")
              .addHeader("Authorization", "Bearer " + newToken)
              .build();
        }

        String updatedToken =
            accessTokenProvider.getRefreshToken() == null ? null : accessTokenProvider.getRefreshToken();

        // Retry the request with the new token.
        return response
            .request()
            .newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer " + updatedToken)
            .build();
      }
    }

    return null;
  }
}
