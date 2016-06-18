package io.bega.servicebase.service.user;


/**
 * Created by user on 3/31/16.
 */
public class LoginResultData {

    private LoginResult result;

    private String message;

    private Exception ex;

    //private UserToken userToken;


    public LoginResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public Exception getEx() {
        return ex;
    }

    /*public UserToken getUserToken() {
        return userToken;
    }*/

    /*public LoginResultData(LoginResult result, String message, UserToken userToken, Exception ex)
    {
        this.userToken = userToken;
        this.result = result;
        this.message = message;
        this.ex = ex;
    } */


}
