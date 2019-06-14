package com.landsky.videoimageshot.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/***
 ** @category 字符串工具类...
 ** @author qing.yunhui
 ** @email: qingyunhui@landsky.cn
 ** @createTime:2018-12-12 09:34
 **/
public class StringUtil {

    /**
     * <p>判断给定的对象是否为null或空</p>
     * @param obj 待校验的对象
     * @return boolean
     * */
    public static boolean isEmpty(Object obj){
        if(null==obj){return true;}
        if(obj instanceof Map){
            return ((Map)obj).isEmpty();
        }else if(obj instanceof Collection){
            return ((Collection)obj).isEmpty();
        }else if(obj instanceof String){
            if(((String)obj).trim().length()==0){return true;}
            if("[]".equals(obj)){return true;}
        }else if(obj.getClass().isArray()){
            return Array.getLength(obj)==0;
        }
        return false;
    }



    /**
     * <p>判断给定的对象判断是否为null或空</p>
     * @param objects 待校验的对象
     * @return  boolean
     * */
    public static boolean isEmptys(Object...objects){
        if(null==objects || objects.length<1){return true;}
        for(Object obj:objects){
            boolean isEmpty=isEmpty(obj);
            if(isEmpty) {return true;}
        }
        return false;
    }

    /**
     * <p>判断给定的对象集判断是否都为null或都为空</p>
     * @param objects 待校验的对象集
     * @return  boolean
     * */
    public static boolean isAllEmptys(Object ...objects){
        if(null==objects || objects.length<1){return true;}
        int count=0;
        for(int i=0;i<objects.length;i++){
            boolean isEmpty=isEmpty(objects[i]);
            if(isEmpty) {
                count++;
            }
        }
        return count==objects.length;
    }

    /**
     * <p>判断给定的字符判断是否为null或空</p>
     * @param objects
     * @return  boolean
     * */
    public static boolean isNotEmptys(Object ...objects){
        return !isEmptys(objects);
    }

    /***
     * 首字母转换
     * @param str 待转换的字符
     * @param isUpper true:首字母转换大写 , false:首字母转换小写
     * @return 转换后的字符
     * **/
    public static String firstLetterConvert(String str,boolean isUpper) {
        StringBuffer buff = new StringBuffer(str);
        buff.replace(0, 1, String.valueOf(isUpper?Character.toUpperCase(str.charAt(0)):Character.toLowerCase(str.charAt(0))));
        return buff.toString();
    }

}
