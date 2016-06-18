package io.bega.servicebase.util.flow;

public interface TakesResult<T> {
	void receive(T result);
}
