package io.bega.servicebase.model.service;

import com.google.gson.annotations.SerializedName;

import io.bega.servicebase.model.User;
import io.bega.servicebase.model.UserDefinition;
import io.bega.servicebase.model.UserWithPassword;

/**
 * Created by user on 4/8/16.
 */
public class Operator extends UserWithPassword {

    @SerializedName("photoUrl")
    public String PhotoURL;

    @SerializedName("OperatorID")
    public String OperatorID;

    @SerializedName("parentOperatorID")
    public String ParentOperatorID;

    @SerializedName("guild")
    public String Guild;

    @SerializedName("preferedGuild")
    public String PreferedGuild;

    @SerializedName("hasKeys")
    public boolean HasKeys;

    @SerializedName("realm")
    public String Realm;

    @SerializedName("username")
    public String UserName;

    @SerializedName("created")
    public String OperatorCreated;

    @SerializedName("lastUpdated")
    public String OperatorLastUpdated;


    public Operator(String name, String email, String password) {
        super(name, email, password);
    }

}
