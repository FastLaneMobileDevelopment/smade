package io.bega.servicebase.model.appointment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 6/1/16.
 */

public class AppointmentData {

    @SerializedName("appointmentid")
    public String AppointmentID;

    @SerializedName("location")
    public String Location;

    @SerializedName("postal_code")
    public String PostalCode;

    @SerializedName("town")
    public String Town;

    @SerializedName("country")
    public String Country;

    @SerializedName("start_date")
    public String StartDate;

    @SerializedName("end_date")
    public String EndDate;

    @SerializedName("gps_location.lat")
    public double Latitude;

    @SerializedName("gps_location.lon")
    public double Longitud;

    @SerializedName("title")
    public String Title;

    @SerializedName("description")
    public String Description;

    @SerializedName("id")
    public String Id;

    @SerializedName("operatorid")
    public String OperatorID;

    @SerializedName("insured")
    public String Insured;

    @SerializedName("telephone")
    public String Telephone;

    @SerializedName("record_id")
    public String RecordID;

    @Override
    public String toString() {
        return  Title + "\n" + Location + " (" + Town + ")";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public String getLocation()
    {
        return Location  + " (" + Town + ")";
    }
}
