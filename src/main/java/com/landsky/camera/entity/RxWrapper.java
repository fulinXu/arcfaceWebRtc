package com.landsky.camera.entity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.reactivex.Single;

public interface RxWrapper<T> {

	default Single<QueryWrapper<T>> rxEmptyWrapper() {
		return Single.just(Wrappers.emptyWrapper());
	}
	
	default Single<LambdaQueryWrapper<T>> rxLambdaQueryWrapper() {
		return rxLambdaQueryWrapper(Wrappers.lambdaQuery());
	}
	
	default Single<LambdaQueryWrapper<T>> rxLambdaQueryWrapper(T entity) {
		return rxLambdaQueryWrapper(Wrappers.lambdaQuery(entity));
	}
	
	default Single<LambdaQueryWrapper<T>> rxLambdaQueryWrapper(LambdaQueryWrapper<T> wrapper) {
		return Single.just(wrapper);
	}
	
	default Single<QueryWrapper<T>> rxQueryWrapper() {
		return rxQueryWrapper(Wrappers.query());
	}
	
	default Single<QueryWrapper<T>> rxQueryWrapper(T entity) {
		return rxQueryWrapper(Wrappers.query(entity));
	}
	
	default Single<QueryWrapper<T>> rxQueryWrapper(QueryWrapper<T> wrapper) {
		return Single.just(wrapper);
	}
	
	default Single<LambdaUpdateWrapper<T>> rxLambdaUpdateWrapper() {
		return rxLambdaUpdateWrapper(Wrappers.lambdaUpdate());
	}
	
	default Single<LambdaUpdateWrapper<T>> rxLambdaUpdateWrapper(T entity) {
		return rxLambdaUpdateWrapper(Wrappers.lambdaUpdate(entity));
	}
	
	default Single<LambdaUpdateWrapper<T>> rxLambdaUpdateWrapper(LambdaUpdateWrapper<T> wrapper) {
		return Single.just(wrapper);
	}
	
	default Single<UpdateWrapper<T>> rxUpdateWrapper() {
		return rxUpdateWrapper(Wrappers.update());
	}
	
	default Single<UpdateWrapper<T>> rxUpdateWrapper(T entity) {
		return rxUpdateWrapper(Wrappers.update(entity));
	}
	
	default Single<UpdateWrapper<T>> rxUpdateWrapper(UpdateWrapper<T> wrapper) {
		return Single.just(wrapper);
	}
	
	default <K> Single<QueryWrapper<K>> rxEmptyWrapper2() {
		return Single.just(Wrappers.emptyWrapper());
	}
	
	default <K> Single<LambdaQueryWrapper<K>> rxLambdaQueryWrapper2() {
		return rxLambdaQueryWrapper2(Wrappers.lambdaQuery());
	}
	
	default <K> Single<LambdaQueryWrapper<K>> rxLambdaQueryWrapper2(K entity) {
		return rxLambdaQueryWrapper2(Wrappers.lambdaQuery(entity));
	}
	
	default <K> Single<LambdaQueryWrapper<K>> rxLambdaQueryWrapper2(LambdaQueryWrapper<K> wrapper) {
		return Single.just(wrapper);
	}
	
	default <K> Single<QueryWrapper<K>> rxQueryWrapper2() {
		return rxQueryWrapper2(Wrappers.query());
	}
	
	default <K> Single<QueryWrapper<K>> rxQueryWrapper2(K entity) {
		return rxQueryWrapper2(Wrappers.query(entity));
	}
	
	default <K> Single<QueryWrapper<K>> rxQueryWrapper2(QueryWrapper<K> wrapper) {
		return Single.just(wrapper);
	}
	
	default <K> Single<LambdaUpdateWrapper<K>> rxLambdaUpdateWrapper2() {
		return rxLambdaUpdateWrapper2(Wrappers.lambdaUpdate());
	}
	
	default <K> Single<LambdaUpdateWrapper<K>> rxLambdaUpdateWrapper2(K entity) {
		return rxLambdaUpdateWrapper2(Wrappers.lambdaUpdate(entity));
	}
	
	default <K> Single<LambdaUpdateWrapper<K>> rxLambdaUpdateWrapper2(LambdaUpdateWrapper<K> wrapper) {
		return Single.just(wrapper);
	}
	
	default <K> Single<UpdateWrapper<K>> rxUpdateWrapper2() {
		return rxUpdateWrapper2(Wrappers.update());
	}
	
	default <K> Single<UpdateWrapper<K>> rxUpdateWrapper2(K entity) {
		return rxUpdateWrapper2(Wrappers.update(entity));
	}
	
	default <K> Single<UpdateWrapper<K>> rxUpdateWrapper2(UpdateWrapper<K> wrapper) {
		return Single.just(wrapper);
	}
}
