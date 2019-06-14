package com.landsky.camera.entity;

import io.reactivex.annotations.NonNull;

@FunctionalInterface
public interface RxNullHandler<T> {

	@NonNull
	T handleNull();
}
