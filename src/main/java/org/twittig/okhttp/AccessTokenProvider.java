package org.twittig.okhttp;

import okhttp3.OkHttpClient;
import org.twittig.okhttp.oauth2.OAuth2Client;
import org.twittig.okhttp.oauth2.OAuthResponse;

import java.io.IOException;

public class AccessTokenProvider {

  private final OkHttpClient okHttpClient;

  private final String clientId;
  private final String clientSecret;
  private final String tokenUri;
  private final String clientCredentials;

  private String token;
  private String refreshToken;

  public AccessTokenProvider(
      OkHttpClient okHttpClient,
      String clientId,
      String clientSecret,
      String tokenUri,
      String clientCredentials) {
    this.okHttpClient = okHttpClient;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.tokenUri = tokenUri;
    this.clientCredentials = clientCredentials;

    initTokens();
  }

  public String getToken() {
    return token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  private void initTokens() {

    OAuth2Client oAuth2Client =
        new OAuth2Client.Builder(clientId, clientSecret, tokenUri)
            .grantType(clientCredentials)
            .okHttpClient(okHttpClient)
            .build();

    try {
      OAuthResponse oAuthResponse = oAuth2Client.requestAccessToken();

      this.token = oAuthResponse.getAccessToken();
      this.refreshToken = oAuthResponse.getRefreshToken();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
