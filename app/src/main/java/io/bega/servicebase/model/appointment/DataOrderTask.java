package io.bega.servicebase.model.appointment;

import org.joda.time.DateTime;

import io.realm.RealmObject;

/**
 * Created by user on 4/27/16.
 */
public class DataOrderTask extends RealmObject {


    public String getExpID() {
        return expID;
    }

    public void setExpID(String expID) {
        this.expID = expID;
    }

    public String getExpCompany() {
        return expCompany;
    }

    public void setExpCompany(String expCompany) {
        this.expCompany = expCompany;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getInsuranceUserName() {
        return insuranceUserName;
    }

    public void setInsuranceUserName(String insuranceUserName) {
        this.insuranceUserName = insuranceUserName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public DataOrderTask()
    {
        
    }

    public String expID;

    public String expCompany;

    public String customerName;

    public String contractName;

    public String insuranceUserName;

    public String location;

    public String telephone;

    public String others;

    public String request;

    public String state;

    public String confirmDate;


}
