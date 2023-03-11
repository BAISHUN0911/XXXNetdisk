package com.lanqiao.netdisk.controller;

import com.lanqiao.netdisk.common.RestResult;
import com.lanqiao.netdisk.dto.DownloadFileDTO;
import com.lanqiao.netdisk.dto.UploadFileDTO;
import com.lanqiao.netdisk.model.File;
import com.lanqiao.netdisk.model.Storage;
import com.lanqiao.netdisk.model.User;
import com.lanqiao.netdisk.model.UserFile;
import com.lanqiao.netdisk.service.FileService;
import com.lanqiao.netdisk.service.FiletransferService;
import com.lanqiao.netdisk.service.UserFileService;
import com.lanqiao.netdisk.service.UserService;
import com.lanqiao.netdisk.util.DateUtil;
import com.lanqiao.netdisk.util.FileUtil;
import com.lanqiao.netdisk.vo.UploadFileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


/**
 * @description: 文件传输类接口：主要职责为文件上传，下载及删除
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Tag(name = "filetransfer",description = "该接口为文件传输接口，主要用来做文件的上传和下载")
@RestController
@RequestMapping("/filetransfer")
public class FiletransferController {

    @Resource
    UserService userService;
    @Resource
    FileService fileService;
    @Resource
    UserFileService userFileService;
    @Resource
    FiletransferService filetransferService;

    @Operation(summary = "极速上传",
            description = "校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法",
            tags = {"filetransfer"}
    )
    @GetMapping(value="/uploadfile")
    @ResponseBody
    public RestResult<UploadFileVO> uploadFileSpeed(UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null){
            return RestResult.fail().message("未登录,请先登录");
        }
        UploadFileVO uploadFileVO = new UploadFileVO();
        HashMap<String, Object> param = new HashMap<>();
        //前端生成的文件md5码传给服务器
        param.put("identifier",uploadFileDto.getIdentifier());
        //对给定类加锁，进入同步代码前需要获得当前class的锁
        synchronized (FiletransferController.class){
            //mybatisplus提供Service方法，这里通过文件的md5查询文件
            List<File> list = fileService.listByMap(param);
            if (list != null && !list.isEmpty()) {
                //通过md5查到了已存在的文件，获取这个文件
                File file = list.get(0);
                //注意数据库中的userfile表，明白这个表的含义。为了避免使用外键，使用userfile表关联user和file表
                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setUserId(sessionUser.getUserId());
                userFile.setFilePath(uploadFileDto.getFilePath());
                String filename = uploadFileDto.getFilename();
                userFile.setFileName(filename.substring(0,filename.lastIndexOf(".")));
                userFile.setExtendName(FileUtil.getFileExtendName(filename));
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFile.setDeleteFlag(0);
                userFileService.save(userFile);
                // fileService.increaseFilePointCount(file.getFileId());
                uploadFileVO.setSkipUpload(true);
            }else {
                //如果查询出的文件集合为空，证明这个文件没上传过，不能文件秒传，只能通过普通方式上传。
                uploadFileVO.setSkipUpload(false);
            }
        }
        return RestResult.success().data(uploadFileVO);
    }

    @Operation(summary = "上传文件", description = "真正的上传文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<UploadFileVO> uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null){
            return RestResult.fail().message("未登录,请先登录");
        }

        filetransferService.uploadFile(request, uploadFileDto, sessionUser.getUserId());
        UploadFileVO uploadFileVo = new UploadFileVO();
        return RestResult.success().data(uploadFileVo);

    }

    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO) {
        filetransferService.downloadFile(response, downloadFileDTO);
    }

    @Operation(summary = "获取存储信息", description = "获取存储信息，返回值单位为字节（B）", tags = {"filetransfer"})
    @RequestMapping(value = "/getstorage", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<Long> getStorage(@RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        Storage storageBean = new Storage();
        Long storageSize = filetransferService.selectStorageSizeByUserId(sessionUser.getUserId());
        return RestResult.success().data(storageSize);
    }



}
