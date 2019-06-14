package com.landsky.camera.service;

import com.arcsoft.face.FaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.landsky.camera.entity.BaseLibrary;
import com.landsky.camera.entity.DistinguishResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Musk
 * @since 2019-05-08
 */
public interface IBaseLibraryService extends IService<BaseLibrary> {

    void push(String base64img);

    List<DistinguishResult> contrast(String base64img);

    boolean isHaveFaceFeature(String base64);

    List<FaceInfo> getFaceInfo(String base64);

}
