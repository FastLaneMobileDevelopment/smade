package io.bega.servicebase.model.event;


public class PhotoMessageBusEvent<T> {

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

    private PhotoMessageBusEvent()
    {

    }

    public PhotoMessageBusEvent(int message, T  data)
    {
        this.message = message;
        this.data = data;
    }



}