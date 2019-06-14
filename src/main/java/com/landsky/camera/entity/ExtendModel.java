package com.landsky.camera.entity;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.reactivex.Single;

import java.util.List;

/**
 * mybatis plus model扩展类
 * 
 * @author tangh
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class ExtendModel<T extends ExtendModel<T>> extends RxModel<T> {

	private static final long serialVersionUID = 1L;

	public List<T> listAsWrapper() {
		return selectList((Wrapper<T>) Wrappers.query(this));
	}

	public IPage<T> pageAsWrapper(IPage<T> page) {
		return selectPage(page, (Wrapper<T>) Wrappers.query(this));
	}

	public boolean updateAsWrapper() {
		return update((Wrapper<T>) Wrappers.query(this));
	}

	public boolean deleteAsWrapper() {
		return delete((Wrapper<T>) Wrappers.query(this));
	}

	public int countAsWrapper() {
		return selectCount((Wrapper<T>) Wrappers.query(this));
	}

	public Single<List<T>> rxListAsWrapper() {
		return rxSelectList((Wrapper<T>) Wrappers.query(this));
	}

	public Single<IPage<T>> rxPageAsWrapper(IPage<T> page) {
		return rxSelectPage(page, (Wrapper<T>) Wrappers.query(this));
	}

	public Single<Boolean> rxUpdateAsWrapper() {
		return rxUpdate((Wrapper<T>) Wrappers.query(this));
	}

	public Single<Boolean> rxDeleteAsWrapper() {
		return rxDelete((Wrapper<T>) Wrappers.query(this));
	}

	public Single<Integer> rxCountAsWrapper() {
		return rxSelectCount((Wrapper<T>) Wrappers.query(this));
	}
}
