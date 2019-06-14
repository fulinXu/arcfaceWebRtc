package com.landsky.videoimageshot.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/***
 ** @category 在spring初始化时候能获得spring的context，等于可以快速取到spring的context
 ** @author qing.yunhui
 ** @email: qingyunhui@landsky.cn
 ** @createTime: 2019/1/8-11:04
 **/
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    /**
     * <p>
     * 获取上下文
     * </p>
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return appContext;
    }

    /**
     * <p>
     * 根据给定beanName获取对应的bean
     * </p>
     *
     * @param beanName
     *            待获取的beanName
     * @return 获取到的bean
     */
    public static Object getBean(String beanName) {
        return appContext.getBean(beanName);
    }

    /**
     * <p>
     * 根据给定beanName获取对应的bean
     * </p>
     *
     * @param clz
     *            待获取的 clz
     * @return 获取到的bean
     */
    public static <T> T getBean(Class<T> clz) {
        return appContext.getBean(clz);
    }

    /**
     * <p>
     * 根据给定beanName、和type 获取对应的bean
     * </p>
     *
     * @param beanName
     *            待获取的 beanName
     * @param type
     *            bean的类型(这里通过指的是bean的超类，或接口）
     */
    public static <T> T getBean(String beanName, Class<T> type) {
        return appContext.getBean(beanName, type);
    }

    public static <T> T getBean(Class<T> type, Object... args) {
        return appContext.getBean(type, args);
    }
}
