package com.landsky.camera.config;

import com.landsky.camera.entity.BaseLibrary;
import com.landsky.camera.service.IBaseLibraryService;
import com.landsky.camera.service.impl.BaseLibraryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaseLibraryInitRunner implements ApplicationRunner {

	private static Logger logger = LoggerFactory.getLogger(BaseLibraryInitRunner.class);
	@Autowired
	IBaseLibraryService iBaseLibraryService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<BaseLibrary> list = iBaseLibraryService.list();
		BaseLibraryServiceImpl.faceList = list;

	}
}
