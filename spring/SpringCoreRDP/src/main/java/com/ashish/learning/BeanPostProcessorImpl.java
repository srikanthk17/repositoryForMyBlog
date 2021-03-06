package com.ashish.learning;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

public class BeanPostProcessorImpl implements BeanPostProcessor, Ordered{

	/**
	 * This gets executed after initialization of the bean
	 * arg0 - Actual bean instance
	 * arg1 - Bean name
	 */
	@Override
	public Object postProcessAfterInitialization(Object arg0, String arg1)
			throws BeansException {
		System.out.println("BeanPostProcessorImpl: This gets executed after initialization of the bean: " + arg1);
		return arg0;
	}

	/**
	 * This gets executed before initialization of the bean
	 * arg0 - Actual bean instance
	 * arg1 - Bean name
	 */
	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1)
			throws BeansException {
		System.out.println("BeanPostProcessorImpl: This gets executed before initialization of the bean: " + arg1);
		return arg0;
	}

	/**
	 * Assign the little lower priority to this post processor to ensure 
	 * post processor 2 gets executed before post processor
	 * return 1 means it has the little lesser priority than 0
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
