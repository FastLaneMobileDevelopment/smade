package io.bega.servicebase.model.appointment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import android.net.Uri;
import android.text.format.Time;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import io.bega.servicebase.model.parcels.RealmListParcelConverter;
import io.bega.servicebase.model.items.ImageItem;
import io.bega.servicebase.model.photo.PhotoType;
import io.bega.servicebase.service.photo.InvalidateImageCache;
import io.realm.OrderTaskRealmProxy;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

import static io.bega.servicebase.activity.ActivityHelper.getActivity;

@Parcel(implementations = { OrderTaskRealmProxy.class }, value = Parcel.Serialization.BEAN, analyze = { OrderTask.class })
public class OrderTask extends RealmObject {


    @SerializedName("expected_date")
    public String ExpectedDate;

    @SerializedName("type")
    public String OrderType;

    @SerializedName("id")
    public String ID;

    @SerializedName("matrix_value")
    public String MatrixValue;

    @SerializedName("matrix_id")
    public String MatrixID;

    @SerializedName("repairman_value")
    public String RepairMainValue;

    @SerializedName("repairman_id")
    public String RepairMainID;

    @SerializedName("valuated_by_id")
    public String ValuatedByID;

    @SerializedName("valuated_by_value")
    public String ValuatedByValue;

    @SerializedName("transacted_by_value")
    public String TransactedByValue;

    @SerializedName("transacted_by_id")
    public int TransactedByID;

    @SerializedName("post_code_risk")
    public String PostCodeRisk;

    @SerializedName("team_value")
    public String TeamValue;

    @SerializedName("team_id")
    public String TeamID;

    @SerializedName("town_value")
    public String TownValue;

    @SerializedName("town_id")
    public int TownID;

    @SerializedName("tramitator_value")
    public String TramitatorValue;

    @SerializedName("tramitator_id")
    public int TramitatorID;

    @SerializedName("customer_value")
    public String CustomerValue;

    @SerializedName("customer_id")
    public int CustomerID;

    @SerializedName("origin_company_value")
    public String OriginCompanyValue;

    @SerializedName("origin_company_id")
    public int OriginCompanyID;

    @SerializedName("record_id_customer")
    public String RecordIDCustomer;

    @SerializedName("record_id_internal")
    public int RecordIDInternal;

    @SerializedName("record_id")
    public String RecordID;

    @SerializedName("StateCosts")
    public String StateCosts;

    @SerializedName("customer_personal_review")
    public String CustomerPersonalReview;

    @SerializedName("personal_review")
    public int PersonalReview;

    @SerializedName("personal_cost")
    public int PersonalCost;

    @SerializedName("HasOutstandingWork")
    public boolean HasOutStandingWork;

    @SerializedName("OutstandingWork")
    public String OutStandingWork;

    @SerializedName("Report")
    public String Report;

    @SerializedName("hasreport")
    public boolean HasReport;

    @SerializedName("created")
    public String Created;

    @SerializedName("modifiedby_id")
    public int ModifiedByID;

    @SerializedName("createdby_id")
    public int CreatedByID;

    @SerializedName("observations")
    public String Observations;

    @SerializedName("title")
    public String Title;

    @SerializedName("arrival_time")
    public String ArrivalTime;

    @SerializedName("timeout")
    public String TimeOut;

    @SerializedName("request")
    public String Request;

    @SerializedName("workforce")
    public double Workforce;

    @SerializedName("material")
    public double Material;

    @SerializedName("discount")
    public double Discount;

    @SerializedName("cash_collected")
    public double CashCollected;

    @SerializedName("paying")
    public double Paying;

    @SerializedName("state")
    public String State;

    @SerializedName("expiration")
    public String Expiration;

    @SerializedName("material_base_company")
    public double MaterialBaseCompany;

    @SerializedName("cash_delivered")
    public double CashDelivered;

    @SerializedName("location")
    public String Location;


    @SerializedName("order_id")
	private String orderID;

	@SerializedName("order_locator")
    private String orderLocator;

    @SerializedName("status")
    private String orderTaskStatus;

	//@SerializedName("extra")
	//private WorkDataOrderTask WorkData;

    @Ignore
    @SerializedName("status")
	private transient OrderTaskStatus status;


    public OrderTaskStatus getStatus() {
        if (orderTaskStatus == null)
        {
            return OrderTaskStatus.Descuento;
        }

        return OrderTaskStatus.valueOf(orderTaskStatus);
    }

    public void setStatus(OrderTaskStatus status) {
        this.status = status;
        orderTaskStatus = status.name();
    }

    @SerializedName("description")
	private String description;

    @SerializedName("date_created")
	private String dateCreated;

    @SerializedName("date_enter")
    private String dateEnter;

    public RealmList<TaskPhoto> getTaskPhotos() {
        return taskPhotos;
    }


    @ParcelPropertyConverter(RealmListParcelConverter.class)
    public void setTaskPhotos(RealmList<TaskPhoto> taskPhotos)
    {
        this.taskPhotos = taskPhotos;
    }

    private RealmList<TaskPhoto> taskPhotos;

    public void setDateEnter(DateTime dateTimeEnter)
    {
        dateEnter = dateTimeEnter.toString();
    }

    public String getDateEnter()
    {
        return dateEnter;
    }

	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dataCreated) {
		this.dateCreated = dataCreated;
	}



	public List<ImageItem> getTaskPhotos(PhotoType type) {
		List<ImageItem> resultTaskPhoto = new ArrayList<ImageItem>();
		for  (int i=0; i< this.getTaskPhotos().size(); i++)
		{
            TaskPhoto taskPhoto = this.getTaskPhotos().get(i);

			if (taskPhoto.getType() == type && taskPhoto.getOrderTaskID() == this.orderID)
			{
                ImageItem imageItem = new ImageItem()
                            .withId(taskPhoto.getId())
                            .withImage(taskPhoto.getPath())
                            .withName(taskPhoto.getDescription())
                            .withData(taskPhoto)
                            .withDescription(taskPhoto.getDescription());
                    resultTaskPhoto.add(imageItem);
			}
		}
		
		return resultTaskPhoto;
	}


	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getOrderLocator() {
		return orderLocator;
	}

	public void setOrderLocator(String orderLocator) {
		this.orderLocator = orderLocator;
	}



    public OrderTask()
    {
        this.orderID = UUID.randomUUID().toString();
        this.orderLocator = "";
        this.status = OrderTaskStatus.Cobrado;
        this.dateCreated = DateTime.now().toDateTime().toString();
        this.description = "";
		//this.taskPhotos = new RealmList<TaskPhoto>();
		this.status = OrderTaskStatus.Concretado;
		//this.WorkData = new WorkDataOrderTask();
    }

	public OrderTask(AppointmentData data, String orderId, String loc)
	{


        this.orderID = orderId;
		this.orderLocator = loc;
		this.dateCreated = DateTime.now().toDateTime().toString();
		this.status = OrderTaskStatus.Concretado;
        this.description = "";
		this.taskPhotos =  new RealmList<TaskPhoto>();
		this.setStatus(OrderTaskStatus.Concretado);
        this.Title = data.Title;
        this.RecordID = data.RecordID;
        this.PostCodeRisk = data.PostalCode;
        this.ExpectedDate = data.StartDate;
        this.Request = data.Description;
        this.Location = data.Location;
        this.ExpectedDate = data.StartDate;

	}

	public void addPhoto(TaskPhoto item) {
		taskPhotos.add(item);
	}

    public void deletePhoto(TaskPhoto item)
    {

        try {
            File file = new File(item.getPath());
            if (file.exists()) {
                file.deleteOnExit();
            }


           // Glide.get(getActivity()).clearDiskCache();
           // Glide.get(getActivity()).clearMemory();

            // invalidate the Glide cache,
            InvalidateImageCache invalidateImageCache = new InvalidateImageCache();
            invalidateImageCache.execute();


        }
        catch (Exception ex)
        {
            String p = ex.getMessage();

        }

        getTaskPhotos().remove(item);

    }

    @Override
    public int hashCode() {
        return this.orderID.hashCode() + this.orderLocator.hashCode();
    }




}
