package io.bega.servicebase.model.appointment;

import io.bega.servicebase.model.photo.PhotoType;

/**
 * Created by usuario on 12/06/14.
 */
public interface IOrderTaskPhotoCreator {

    void createTaskPhoto(TaskPhoto taskPhoto);
    void deleteTaskPhoto(TaskPhoto taskPhoto);
    void refreshTaskPhoto(PhotoType type);
    void changeTaskPhoto(TaskPhoto taskPhoto);

}
