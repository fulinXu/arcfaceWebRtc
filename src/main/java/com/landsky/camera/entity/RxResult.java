package com.landsky.camera.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.landsky.videoimageshot.utils.ResultWrapper;
import io.reactivex.Single;

import java.util.*;

public interface RxResult<T> {

	default Single<ResultWrapper> rxResultWrapper(boolean success) {
		return rxResultWrapper(ResultWrapper.success(success));
	}

	default Single<ResultWrapper> rxResultWrapper(ResultWrapper wrapper) {
		return Single.just(wrapper);
	}

	default Single<IPage<T>> rxEmptyPage() {
		return Single.just(new Page<>());
	}

	default <K> Single<IPage<K>> rxEmptyPage2() {
		return Single.just(new Page<>());
	}

	default Single<IPage<T>> rxEmptyPage(IPage<T> page) {
		return Single.just(page);
	}

	default <K> Single<IPage<K>> rxEmptyPage2(IPage<K> page) {
		return Single.just(page);
	}

	default Single<Collection<T>> rxEmptyCollection() {
		return Single.just(Collections.emptyList());
	}

	default <K> Single<Collection<K>> rxEmptyCollection2() {
		return Single.just(Collections.emptyList());
	}

	default Single<List<T>> rxEmptyList() {
		return Single.just(Collections.emptyList());
	}

	default <K> Single<List<K>> rxEmptyList2() {
		return Single.just(Collections.emptyList());
	}

	default Single<Set<T>> rxEmptySet() {
		return Single.just(Collections.emptySet());
	}

	default <K> Single<Set<K>> rxEmptySet2() {
		return Single.just(Collections.emptySet());
	}

	default <K, V> Single<Map<K, V>> rxEmptyMap() {
		return Single.just(Collections.emptyMap());
	}
}
