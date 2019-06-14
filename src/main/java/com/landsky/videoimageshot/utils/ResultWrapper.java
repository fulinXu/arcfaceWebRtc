package com.landsky.videoimageshot.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 操作结果返回包装类
 *
 * @author tangh
 */
@SuppressWarnings("unchecked")
public class ResultWrapper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String SUFFIX_SUCCESS = "成功";
    public static final String SUFFIX_FAILURE = "失败";

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 操作信息
     */
    private String message;

    /**
     * 附帶数据
     */
    private Object object;

    public ResultWrapper() {

    }

    public ResultWrapper(boolean success) {
        this.success = success;
    }

    public static ResultWrapper debug() {
        return success(false).message("调试中");
    }

    public static ResultWrapper success(boolean success) {
        return new ResultWrapper(success);
    }

    public static ResultWrapper success() {
        return success(true);
    }

    public static ResultWrapper failure() {
        return success(false);
    }

    public <T extends ResultWrapper> T message(String message) {
        this.message = message;
        return (T) this;
    }

    /**
     * 传入的信息根据是否成功附带后缀
     *
     * @param message
     * @return
     */
    public <T extends ResultWrapper> T submessage(String message) {
        if (success) {
            this.message = message + SUFFIX_SUCCESS;
        } else {
            this.message = message + SUFFIX_FAILURE;
        }
        return (T) this;
    }

    public <T extends ResultWrapper> T messageAdd() {
        return submessage("添加");
    }

    public <T extends ResultWrapper> T messageEdit() {
        return submessage("编辑");
    }

    public <T extends ResultWrapper> T messageDelete() {
        return submessage("删除");
    }

    public <T extends ResultWrapper> T messageQuery() {
        return submessage("查询");
    }

    public <T extends ResultWrapper> T messageExist(String msg) {
        return message(msg + "已存在");
    }

    public <T extends ResultWrapper> T messageNotExist(String msg) {
        return message(msg + "不存在");
    }

    public <T extends ResultWrapper> T messageNotEmpty(String msg) {
        return message(msg + "不能为空");
    }

    public <T extends ResultWrapper> T object(Object object) {
        this.object = object;
        return (T) this;
    }

    public <T extends ResultWrapper> T object(Object... object) {
        this.object = object;
        return (T) this;
    }

    public <T extends ResultWrapper> T transaction() {
        if (success) {
            return (T) this;
        } else {
            throw new RuntimeException(message);
        }
    }

    public <T extends ResultWrapper> T cache(boolean success, CacheHandler ch) {
        if (this.success == success && ch != null) {
            ch.cache();
        }
        return (T) this;
    }

    public <T extends ResultWrapper> T cacheSuccess(CacheHandler ch) {
        return cache(true, ch);
    }

    public <T extends ResultWrapper> T cacheFailure(CacheHandler ch) {
        return cache(false, ch);
    }

    public static ResultWrapper build(Object object) {
        ResultWrapper resultWrapper = new ResultWrapper();
        resultWrapper.object = object;
        if (!StringUtil.isEmpty(object)) {
            resultWrapper.success = Boolean.TRUE;
        }
        resultWrapper.messageQuery();
        return resultWrapper;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getObject() {
        return object;
    }

    @JsonIgnore
    public <T> T getObjectCastSafety() {
        return (T) object;
    }

    @JsonIgnore
    public <T> T getObjectCastUnsafe() {
        try {
            return (T) object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "ResultWrapper [success=" + success + ", message=" + message + ", object=" + object + "]";
    }

    @FunctionalInterface
    public interface CacheHandler {
        void cache();
    }
}
