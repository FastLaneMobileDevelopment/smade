package io.bega.servicebase.model.appointment;

public interface IOrderTaskCreator {
	void createNewTask(OrderTask newTask);
    void deleteTask(OrderTask task);
    void selectTask(OrderTask task);
}
