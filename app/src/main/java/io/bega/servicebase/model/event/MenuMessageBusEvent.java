package io.bega.servicebase.model.event;


public class MenuMessageBusEvent<T> {

    private int message;

    private T data;

    public int getMessage()
    {
        return this.message;
    }

    public T getData()
    {
        return this.data;
    }

    private MenuMessageBusEvent()
    {

    }

    public MenuMessageBusEvent(int message, T  data)
    {
        this.message = message;
        this.data = data;
    }



}