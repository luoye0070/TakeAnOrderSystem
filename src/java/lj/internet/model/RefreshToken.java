package lj.internet.model;

public class RefreshToken {
    @Override
    public String toString() {
        return "RefreshToken [access_token=" + access_token + ", expires_in="
                + expires_in + ", refresh_token=" + refresh_token + ", scope="
                + scope + ", session_key=" + session_key + ", session_secret="
                + session_secret + "]";
    }
    public String access_token="";
    public long expires_in=0;//access_token有效期，单位秒
    public String refresh_token="";
    public String scope="";//Access Token最终的访问范围
    public String session_key="";
    public String session_secret="";
    public RefreshToken(String access_token, long expires_in,
                        String refresh_token, String scope, String session_key,
                        String session_secret) {
        super();
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.scope = scope;
        this.session_key = session_key;
        this.session_secret = session_secret;
    }
    public RefreshToken() {
        super();
    }

}
