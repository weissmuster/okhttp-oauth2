package org.twittig.okhttp.oauth2;

public class Token {
  protected Long expires_in;
  protected String token_type;
  protected String refresh_token;
  protected String access_token;
  protected String scope;

  public Long getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(Long expires_in) {
    this.expires_in = expires_in;
  }

  public String getToken_type() {
    return token_type;
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
