package in.bananaa.pass.dto.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import in.bananaa.pass.dto.GenericResponse;

public class LoginResponse extends GenericResponse implements Serializable {

    @SerializedName("user")
    private User user;

    @SerializedName("accessToken")
    private String accessToken;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
