package io.bega.servicebase.model.appointment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersTasks  {

	private  List<OrderTask> taskOrders = new ArrayList<OrderTask>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public  Map<String, OrderTask> taskOrdersMap = new HashMap<String, OrderTask>();
	
	public List<OrderTask> getOrderTask()
	{
		return this.taskOrders;
	}
	
	public OrderTask getOrderTaskByPosition(int position)
	{
		return this.taskOrders.get(position);
	}
	
	public OrderTask getOrderTaskById(String id)
	{
		return this.taskOrdersMap.get(id);
	}


	public void addNewTask(OrderTask newTask)
	{
		if (taskOrdersMap.containsKey(newTask.getOrderID()))
		{
			return;
		}
		
		if (taskOrders.contains(newTask))
		{
			return;
		}
		
		taskOrdersMap.put(newTask.getOrderID(), newTask);
		taskOrders.add(newTask);
		
		
	}
	
	public void deleteTask(OrderTask task)
	{
		taskOrders.remove(task);
		taskOrdersMap.remove(task.getOrderID());
	}

	
}
