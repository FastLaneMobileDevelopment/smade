package io.bega.servicebase.model.appointment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;


import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IIdentifyable;


import org.parceler.Parcel;

import java.io.File;
import java.util.UUID;

import io.bega.servicebase.model.items.ImageItem;
import io.bega.servicebase.model.photo.PhotoType;
import io.bega.servicebase.screen.appointment.photo.HelperImage;
import io.realm.OrderTaskRealmProxy;
import io.realm.RealmObject;
import io.realm.TaskPhotoRealmProxy;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


@Parcel(implementations = { TaskPhotoRealmProxy.class }, value = Parcel.Serialization.BEAN, analyze = { TaskPhoto.class })
public class TaskPhoto extends RealmObject  {
;
    public static final String AUTO_ID = "_id";
    public static final String ID = "photo_id";
    public static final String PATH = "order_id";
    public static final String PHOTO_TYPE = "order_locator";


    @PrimaryKey
    @SerializedName("photo_id")
    private String id;

    @SerializedName("photo_path")
	private String path;

    @SerializedName("photo_type")
    private String rawType;

    @SerializedName("photo_description")
    private String mDescription;

    @SerializedName("photo_title")
    private String mTitle;

    @SerializedName("photo_order_task_id")
    private String orderTaskID;

    @Ignore
	private transient  PhotoType type;

    //@Ignore
    //private transient Bitmap small_bitmap;

    public TaskPhoto()
    {
        this.id = "";
        this.path = "";
        this.orderTaskID = "";
        this.type = PhotoType.Form;

    }

    public TaskPhoto withId(String id)
    {
        this.id = id;
        return this;
    }

    public TaskPhoto withPath(String path)
    {
        this.path = path;
        return this;
    }

    public TaskPhoto withType(PhotoType type)
    {
        this.type = type;
        return this;
    }

    public TaskPhoto withOrderID(String orderID)
    {
        this.orderTaskID = orderID;
        return this;
    }

    public TaskPhoto withTitle(String title)
    {
        this.mTitle = title;
        return this;
    }

    public TaskPhoto withDescriptino(String description)
    {
        this.mDescription = description;
        return this;
    }

    public TaskPhoto(String orderID, String path, PhotoType photoType) {
        this.id =  UUID.randomUUID().toString();
        this.path = path;
        this.orderTaskID = orderID;
        this.type = photoType;
        this.rawType = photoType.toString();
    }

    public void setType(PhotoType type)
    {
        this.rawType = type.toString();
        this.type = type;
    }


    public String getId() { return this.id; }


	public String getPath()
	{
		return this.path;
	}

    public String getTitle()
    {
        return this.mTitle;
    }

    public String getDescription()
    {
        return this.mDescription;
    }

    public String getOrderTaskID() {
        return this.orderTaskID;
    }

	public PhotoType getType()
	{
        if (this.rawType != null) {
            PhotoType photoType = PhotoType.valueOf(this.rawType);
            return photoType;
        }
		return this.type;
	}


    @Override public boolean equals(Object o) {
        // Return true if the objects are identical.
        // (This is just an optimization, not required for correctness.)
        if (this == o) {
            return true;
        }

        // Return false if the other object has the wrong type.
        // This type may be an interface depending on the interface's specification.
        if (!(o instanceof TaskPhoto)) {
            return false;
        }

        // Cast to the appropriate type.
        // This will succeed because of the instanceof, and lets us access private fields.
        TaskPhoto lhs = (TaskPhoto) o;

        // Check each field. Primitive fields, reference fields, and nullable reference
        // fields are all treated differently.
        return getId() == lhs.getId();
    }

    @Override
    public int hashCode() {
        return  getId().hashCode();
    }

    @Override public String toString()
    {
        return getClass().getName() + " [getId() = " + this.getId() + ", Path =  " + this.getPath()
                + ", Type = " + this.getType() + "]";
    }

}
