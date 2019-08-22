package com.jca.systemset.task;

import java.util.concurrent.Callable;

import com.jca.systemset.service.operator.TFOperatorService;

public class TaskService<V> implements Callable<V> {
	
	private V v;
	@Override
	public V call() throws Exception {
		System.out.println(Thread.currentThread().getName());
		return v;
	}
	
	public V get() throws Exception {		
		return v;
	}
}
