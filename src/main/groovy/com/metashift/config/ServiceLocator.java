package com.metashift.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ServiceLocator implements ApplicationContextAware{
	
	private static ServiceLocator serviceLocator = null;

	private ApplicationContext applicationContext =null;

	public static ServiceLocator getCurrentInstance(){
		if(serviceLocator == null){
			serviceLocator = new ServiceLocator();
		}
		return serviceLocator;
	}
	
	
	/**
	 * Returns a "live" instance of the given class from the "current" ServiceLocator. This will either come from the spring container or be created.
	 * @param clazz the class of the object to be created 
	 * @return The instance of the given class. 
	 * @throws {@link IllegalStateException} if there is a problem getting the class from the Spring Application Context.
	 */
	public static <T> T locateCurrent( Class<T> clazz)throws IllegalStateException{
		return getCurrentInstance().locate(clazz);
	}
	
	/* (non-Javadoc)
	 * @see com.headflow.service.impl.IServiceLocator#getConcreteClass(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T locate( Class<T> clazz)throws IllegalStateException{
		try {

			T object = applicationContext.getBean(clazz);
			
			if(object == null){
				throw new IllegalStateException("No Class Found For Name " + clazz.getName() + " in Spring Context");
			}
			
			return (T)object;
		} catch (BeansException e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.headflow.service.impl.IServiceLocator#getBean(java.lang.String)
	 */
	public Object getBean(String beanName)throws IllegalStateException{
		try {
			Object service = null;

			service = applicationContext.getBean(beanName);
			
			if(service == null){
				throw new IllegalStateException("No bean for name " + beanName + " found in Spring Context");
			}
			
			return service;
		} catch (BeansException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private ServiceLocator(){
		
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
