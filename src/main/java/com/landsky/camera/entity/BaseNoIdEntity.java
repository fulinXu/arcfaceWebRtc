package com.landsky.camera.entity;

/**
 * entity基类
 */

import com.baomidou.mybatisplus.annotation.TableLogic;

import java.util.Date;

public class BaseNoIdEntity<T extends ExtendModel<T>> extends ExtendModel<T> {

    private static final long serialVersionUID = 1L;

//	/**
//	 * 共用字段id
//	 */
//	@TableId(type = IdType.UUID)
//	private String id;

    /**
     * 共用字段创建时间
     */
    private Date createTime;

    /**
     * 共用字段更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除，sql拼接自动过滤，不需要手动传，可在properties配置true和false对应的int值
     */
    @TableLogic
    private Boolean deleted;

    //	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @SuppressWarnings("unchecked")
    public <K extends BaseNoIdEntity<T>> K createState() {
        Date now = new Date();
        setCreateTime(now);
        setUpdateTime(now);
        setDeleted(false);
        return (K) this;
    }

    @SuppressWarnings("unchecked")
    public <K extends BaseNoIdEntity<T>> K updateState() {
        setCreateTime(null);
        setUpdateTime(new Date());
        setDeleted(null);
        return (K) this;
    }

    @Override
    public String toString() {
        return "BaseEntity [createTime=" + createTime + ", updateTime=" + updateTime + ", deleted="
                + deleted + "]";
    }
}
