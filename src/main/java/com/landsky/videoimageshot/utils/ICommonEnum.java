package com.landsky.videoimageshot.utils;

/***
 ** @category 枚举类键值通用接口...
 ** @author qing.yunhui
 ** @email: qingyunhui@landsky.cn
 ** @createTime:2018-12-12 09:53
 **/
public interface ICommonEnum {

    /**当key值为数字类型时，在调用getCode时会将字符类型转换成Integer类型方便调用*/
    String getKey();

    /**value */
    String getValue();

    /**code(key) */
    Integer getCode();

}
