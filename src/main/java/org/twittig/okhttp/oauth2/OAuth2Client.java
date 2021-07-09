package org.twittig.okhttp.oauth2;

import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OAuth2Client {
  private final String clientId;
  private final String clientSecret;
  private final String tokenUri;
  private final OkHttpClient okHttpClient;

  private final String scope;
  private final String username;
  private final String password;
  private final Map<String, String> parameters;
  private String grantType;

  private OAuth2Client(Builder builder) {
    this.username = builder.username;
    this.password = builder.password;
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
    this.tokenUri = builder.site;
    this.scope = builder.scope;
    this.grantType = builder.grantType;
    this.okHttpClient = builder.okHttpClient;
    this.parameters = builder.parameters;
  }

  protected String getScope() {
    return scope;
  }

  protected String getGrantType() {
    return grantType;
  }

  protected String getClientId() {
    return clientId;
  }

  protected String getClientSecret() {
    return clientSecret;
  }

  protected String getTokenUri() {
    return tokenUri;
  }

  protected String getUsername() {
    return username;
  }

  protected String getPassword() {
    return password;
  }

  public OAuthResponse refreshAccessToken(String refreshToken) throws IOException {
    if (this.grantType == null) this.grantType = Constants.GRANT_TYPE_REFRESH;
    return Access.refreshAccessToken(refreshToken, this);
  }

  public void refreshAccessToken(final String refreshToken, final OAuthResponseCallback callback) {
    new Thread(
            () -> {
              OAuthResponse response;
              try {
                response = refreshAccessToken(refreshToken);
                callback.onResponse(response);
              } catch (Exception e) {
                response = new OAuthResponse(e);
                callback.onResponse(response);
              }
            })
        .start();
  }

  public OAuthResponse requestAccessToken() throws IOException {
    if (this.grantType == null) this.grantType = Constants.GRANT_TYPE_PASSWORD;
    return Access.getToken(this);
  }

  public void requestAccessToken(final OAuthResponseCallback callback) {
    new Thread(
            () -> {
              OAuthResponse response;
              try {
                response = requestAccessToken();
                callback.onResponse(response);
              } catch (Exception e) {
                response = new OAuthResponse(e);
                callback.onResponse(response);
              }
            })
        .start();
  }

  protected OkHttpClient getOkHttpClient() {
    return Objects.requireNonNullElseGet(this.okHttpClient, OkHttpClient::new);
  }

  protected Map<String, String> getParameters() {
    return Objects.requireNonNullElseGet(parameters, HashMap::new);
  }

  protected Map<String, String> getFieldsAsMap() {
    Map<String, String> oAuthParams = new HashMap<>();
    oAuthParams.put(Constants.POST_CLIENT_ID, getClientId());
    oAuthParams.put(Constants.POST_CLIENT_SECRET, getClientSecret());
    oAuthParams.put(Constants.POST_GRANT_TYPE, getGrantType());
    oAuthParams.put(Constants.POST_SCOPE, getScope());
    oAuthParams.put(Constants.POST_USERNAME, getUsername());
    oAuthParams.put(Constants.POST_PASSWORD, getPassword());
    return oAuthParams;
  }

  public static class Builder {
    private final String clientId;
    private final String clientSecret;
    private final String site;

    private String scope;
    private String grantType;

    private String username;
    private String password;

    private OkHttpClient okHttpClient;

    private Map<String, String> parameters;

    public Builder(
        String username, String password, String clientId, String clientSecret, String site) {
      this.username = username;
      this.password = password;
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.site = site;
      this.okHttpClient = null;
    }

    public Builder(String clientId, String clientSecret, String site) {
      this.username = null;
      this.password = null;
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.site = site;
      this.okHttpClient = null;
    }

    public Builder grantType(String grantType) {
      this.grantType = grantType;
      return this;
    }

    public Builder scope(String scope) {
      this.scope = scope;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder okHttpClient(OkHttpClient client) {
      this.okHttpClient = client;
      return this;
    }

    public Builder parameters(Map<String, String> parameters) {
      this.parameters = parameters;
      return this;
    }

    public OAuth2Client build() {
      return new OAuth2Client(this);
    }
  }
}
