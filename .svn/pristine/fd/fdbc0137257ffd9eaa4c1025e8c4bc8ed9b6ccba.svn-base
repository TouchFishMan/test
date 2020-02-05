package com.telek.hemsipc.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
 
/**
 * spring配置文件加载 
 * @Class Name：PdiresSpringContext    
 * @Class Description：    
 * @Creater：telek    
 * @Create Time：2019年5月9日上午9:50:47    
 * @Modifier：telek    
 * @Modification Time：2019年5月9日上午9:50:47    
 * @Remarks：
 */
public class HemsipcSpringContext {
 
	private static ApplicationContext applicationContext;
 
	public static void initSpringContext() {
		String[] configs = { "classpath:hemsipc-applicationContext.xml"};
		applicationContext = new FileSystemXmlApplicationContext(configs);
	}
 
	
	public static <T> T getBean(Class<T> c){
		return applicationContext.getBean(c);
	}


	public static <T> T getBean(String name,Class<T> clazz){
		return applicationContext.getBean(name,clazz);
	}
}
