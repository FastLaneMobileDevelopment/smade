package io.bega.servicebase.model.appointment;

import io.realm.RealmObject;

/**
 * Created by user on 4/27/16.
 */
public class WorkDataOrderTask extends RealmObject {


    public double getWorkForce() {
        return WorkForce;
    }

    public void setWorkForce(double workForce) {
        WorkForce = workForce;
    }

    public double getMaterialWorker() {
        return MaterialWorker;
    }

    public void setMaterialWorker(double materialWorker) {
        MaterialWorker = materialWorker;
    }

    public double getMaterialCompany() {
        return MaterialCompany;
    }

    public void setMaterialCompany(double materialCompany) {
        MaterialCompany = materialCompany;
    }

    public double getCash() {
        return Cash;
    }

    public void setCash(double cash) {
        Cash = cash;
    }

    public boolean isCashInCompany() {
        return CashInCompany;
    }

    public void setCashInCompany(boolean cashInCompany) {
        CashInCompany = cashInCompany;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    private double WorkForce = 0;

    private double MaterialWorker = 0;

    private double MaterialCompany = 0;

    private double Cash = 0;

    private boolean CashInCompany = false;

    private String Notes;

    public WorkDataOrderTask withNotes(String notes)
    {
        this.Notes = notes;
        return this;
    }


    public WorkDataOrderTask withCashInCompany(boolean withCashInCompany)
    {
        this.CashInCompany = withCashInCompany;
        return this;
    }

    public WorkDataOrderTask withCash(float cash)
    {
        this.Cash = cash;
        return this;
    }

    public WorkDataOrderTask withMaterialCompany(float materialCompany)
    {
        this.MaterialCompany = materialCompany;
        return this;
    }

    public WorkDataOrderTask withMaterialWorker(float materialWorker)
    {
        this.MaterialCompany = materialWorker;
        return this;
    }

    public WorkDataOrderTask withWorkForce(float workForce)
    {
        this.WorkForce = workForce;
        return this;
    }
}
