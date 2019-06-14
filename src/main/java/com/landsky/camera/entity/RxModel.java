package com.landsky.camera.entity;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.reactivex.Single;

import java.io.Serializable;
import java.util.List;

public class RxModel<T extends RxModel<T>> extends Model<T> implements RxWrapper<T>, RxResult<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * 插入（字段选择插入）
	 * </p>
	 */
	public Single<Boolean> rxInsert() {
		return Single.<Boolean>create(se -> se.onSuccess(insert()));
	}

	/**
	 * <p>
	 * 插入 OR 更新
	 * </p>
	 */
	public Single<Boolean> rxInsertOrUpdate() {
		return Single.<Boolean>create(se -> se.onSuccess(insertOrUpdate()));
	}

	/**
	 * <p>
	 * 根据 ID 删除
	 * </p>
	 *
	 * @param id
	 *            主键ID
	 */
	public Single<Boolean> rxDeleteById(Serializable id) {
		return Single.<Boolean>create(se -> se.onSuccess(deleteById(id)));
	}

	/**
	 * <p>
	 * 根据主键删除
	 * </p>
	 */
	public Single<Boolean> rxDeleteById() {
		return Single.<Boolean>create(se -> se.onSuccess(deleteById()));
	}

	/**
	 * <p>
	 * 删除记录
	 * </p>
	 *
	 * @param queryWrapper
	 *            实体对象封装操作类（可以为 null）
	 */
	public Single<Boolean> rxDelete(Wrapper<T> queryWrapper) {
		return Single.<Boolean>create(se -> se.onSuccess(delete(queryWrapper)));
	}

	/**
	 * <p>
	 * 更新（字段选择更新）
	 * </p>
	 */
	public Single<Boolean> rxUpdateById() {
		return Single.<Boolean>create(se -> se.onSuccess(updateById()));
	}

	/**
	 * <p>
	 * 执行 SQL 更新
	 * </p>
	 *
	 * @param updateWrapper
	 *            实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
	 */
	public Single<Boolean> rxUpdate(Wrapper<T> updateWrapper) {
		return Single.<Boolean>create(se -> se.onSuccess(update(updateWrapper)));
	}

	/**
	 * <p>
	 * 查询所有
	 * </p>
	 */
	public Single<List<T>> rxSelectAll() {
		return Single.<List<T>>create(se -> se.onSuccess(selectAll()));
	}

	/**
	 * <p>
	 * 根据 ID 查询
	 * </p>
	 *
	 * @param id
	 *            主键ID
	 */
	public Single<T> rxSelectById(Serializable id, RxNullHandler<T> handler) {
		T one = selectById(id);
		if (one == null && handler == null) {
			throw new RuntimeException("Entity or RxNullHandler cant be null");
		}
		return Single.<T>create(se -> se.onSuccess(one == null ? handler.handleNull() : one));
	}

	/**
	 * <p>
	 * 根据主键查询
	 * </p>
	 */
	public Single<T> rxSelectById(RxNullHandler<T> handler) {
		T one = selectById();
		if (one == null && handler == null) {
			throw new RuntimeException("Entity or RxNullHandler cant be null");
		}
		return Single.<T>create(se -> se.onSuccess(one == null ? handler.handleNull() : one));
	}

	/**
	 * <p>
	 * 查询总记录数
	 * </p>
	 *
	 * @param queryWrapper
	 *            实体对象封装操作类（可以为 null）
	 */

	public Single<List<T>> rxSelectList(Wrapper<T> queryWrapper) {
		return Single.<List<T>>create(se -> se.onSuccess(selectList(queryWrapper)));
	}

	/**
	 * <p>
	 * 查询一条记录
	 * </p>
	 *
	 * @param queryWrapper
	 *            实体对象封装操作类（可以为 null）
	 */
	public Single<T> rxSelectOne(Wrapper<T> queryWrapper, RxNullHandler<T> handler) {
		T one = selectOne(queryWrapper);
		if (one == null && handler == null) {
			throw new RuntimeException("Entity or RxNullHandler cant be null");
		}
		return Single.<T>create(se -> se.onSuccess(one == null ? handler.handleNull() : one));
	}

	/**
	 * <p>
	 * 翻页查询
	 * </p>
	 *
	 * @param page
	 *            翻页查询条件
	 * @param queryWrapper
	 *            实体对象封装操作类（可以为 null）
	 */
	public Single<IPage<T>> rxSelectPage(IPage<T> page, Wrapper<T> queryWrapper) {
		return Single.<IPage<T>>create(se -> se.onSuccess(selectPage(page, queryWrapper)));
	}

	/**
	 * <p>
	 * 查询总数
	 * </p>
	 *
	 * @param queryWrapper
	 *            实体对象封装操作类（可以为 null）
	 */
	public Single<Integer> rxSelectCount(Wrapper<T> queryWrapper) {
		return Single.<Integer>create(se -> se.onSuccess(selectCount(queryWrapper)));
	}
}
