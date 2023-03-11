package com.lanqiao.netdisk.service;


import com.lanqiao.netdisk.dto.DownloadFileDTO;
import com.lanqiao.netdisk.dto.UploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @description: 文件传输相关业务接口，文件传输：上传文件、下载文件、获取用户空间等。
 * @author: BAISHUN
 * @date: 2023/3/8
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
public interface FiletransferService {

    /**
     * 上传文件
     * @param request
     * @param uploadFileDTO 文件上传DTO对象
     * @param userId 用户ID
     */
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDTO, Long userId);

    /**
     * 下载文件
     * @param response
     * @param downloadFileDTO 文件下载DTO对象
     */
    void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO);

    /**
     * 根据用户ID去获取该用户网盘容量信息
     * @param userId 用户ID
     * @return
     */
    Long selectStorageSizeByUserId(Long userId);


}
