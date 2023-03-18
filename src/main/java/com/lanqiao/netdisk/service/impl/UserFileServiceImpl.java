package com.lanqiao.netdisk.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanqiao.netdisk.constant.FileConstant;
import com.lanqiao.netdisk.mapper.FileMapper;
import com.lanqiao.netdisk.mapper.RecoveryFileMapper;
import com.lanqiao.netdisk.mapper.UserFileMapper;
import com.lanqiao.netdisk.model.RecoveryFile;
import com.lanqiao.netdisk.model.UserFile;
import com.lanqiao.netdisk.service.UserFileService;
import com.lanqiao.netdisk.util.DateUtil;
import com.lanqiao.netdisk.vo.DeleteFileVO;
import com.lanqiao.netdisk.vo.UserfileListVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * @description: 用户文件业务实现类
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Service("userFileService")
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements UserFileService {

    private Logger logger = LoggerFactory.getLogger(UserFileServiceImpl.class);

    @Resource
    UserFileMapper userFileMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    RecoveryFileMapper recoveryFileMapper;

    @Resource(name = "taskExecutor")
    ThreadPoolTaskExecutor executor;

    @Override
    public List<UserfileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage-1) * pageCount;
        UserFile userFile = new UserFile();
        userFile.setUserId(userId);
        userFile.setFilePath(filePath);
        logger.info("文件目录：{}", filePath);
        List<UserfileListVO> fileList = userFileMapper.userfileList(userFile, beginCount, pageCount);
        return fileList;
    }

    @Override
    public Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId) {
        Long beginCount = (currentPage-1)*pageCount;
        List<UserfileListVO> fileList;              //
        Long total;
        if(fileType== FileConstant.OTHER_TYPE){
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.addAll(Arrays.asList(FileConstant.DOC_FILE));
            arrayList.addAll(Arrays.asList(FileConstant.IMG_FILE));
            arrayList.addAll(Arrays.asList(FileConstant.VIDEO_FILE));
            arrayList.addAll(Arrays.asList(FileConstant.MUSIC_FILE));
            userFileMapper.selectFileNotInExtendNames(arrayList,beginCount,pageCount,userId);
            total=userFileMapper.selectCountNotInExtendNames(arrayList,beginCount,pageCount,userId);    //计算这类文件的数量，目前不知道啥用
        }else {
            List<String> fileExtends = null;
            if(fileType==FileConstant.IMAGE_TYPE){
                fileExtends=Arrays.asList(FileConstant.IMG_FILE);
            }else if (fileType == FileConstant.DOC_TYPE) {
                fileExtends = Arrays.asList(FileConstant.DOC_FILE);
            } else if (fileType == FileConstant.VIDEO_TYPE) {
                fileExtends = Arrays.asList(FileConstant.VIDEO_FILE);
            } else if (fileType == FileConstant.MUSIC_TYPE) {
                fileExtends = Arrays.asList(FileConstant.MUSIC_FILE);
            }
            fileList=userFileMapper.selectFileByExtendName(fileExtends,beginCount,pageCount,userId);
            total=userFileMapper.selectCountByExtendName(fileExtends, beginCount, pageCount,userId);    //计算这类文件的数量
            HashMap<String, Object> map = new HashMap<>();
            map.put("list",fileList);
            map.put("total",total);
            return map;

        }
        return null;
    }

    @Override
    public void deleteUserFile(Long userFileId, Long sessionUserId) {
        String uuid = UUID.randomUUID().toString();
        Date now = new Date();
        logger.info("用户：{}执行删除文件操作，用户文件id：{}，删除批次号：{}", sessionUserId, userFileId, uuid);
        LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        userFileLambdaUpdateWrapper.set(UserFile::getDeleteFlag,1)
                .set(UserFile::getDeleteTime, now)
                .set(UserFile::getDeleteBatchNum, uuid)
                .eq(UserFile::getUserFileId,userFileId);
        userFileMapper.update(null,userFileLambdaUpdateWrapper);
        logger.info("文件删除成功，文件id：{}", userFileId);
        //将这次删除的文件添加到recoveryfile表记录
        RecoveryFile recoveryFile = new RecoveryFile();
        recoveryFile.setUserFileId(userFileId);
        recoveryFile.setDeleteTime(now);
        recoveryFile.setDeleteBatchNum(uuid);
        recoveryFileMapper.insert(recoveryFile);
        logger.info("被删除的文件已记录在回收站");
    }

    @Override
    @Async
    public CompletableFuture<String> updateFileDeleteStateByFilePath(Long userFileId, String fileName, String filePath, Long userId) {
        String uuid = UUID.randomUUID().toString();
        String deletePath = filePath + fileName + "/";
        logger.info("用户：{}执行删除文件夹操作，删除的目录为：{}，删除批次号：{}", userId, deletePath, uuid);
        logger.info("当前执行删除任务的线程：" + Thread.currentThread().getName());
        DeleteFileVO deleteFileVO = new DeleteFileVO();
        deleteFileVO.setUserId(userId);
        deleteFileVO.setUserFileId(userFileId);
        deleteFileVO.setDeletePath(deletePath);
        deleteFileVO.setDeleteBatchNum(uuid);
        userFileMapper.deleteDir(deleteFileVO);
        logger.info(filePath + "下的" + fileName + "文件夹删除成功");
        return CompletableFuture.completedFuture("异步方法执行完成");
    }

    @Override
    @Async
    public CompletableFuture<String> updateFileDeleteStateByFilePath2(String filePath, String deleteBatchNum, Long userId) {
        logger.info("用户：{}执行删除文件夹操作，删除的目录为：{}，删除批次号：{}", userId, filePath, deleteBatchNum);
        List<UserFile> fileList = selectFileTreeListLikeFilePath(filePath, userId);
        //有个问题，这里的length指的是文件数还是这个filepath下的文件+文件夹数
        int length = fileList.size();
        if(length >= 0) {
            //模拟异步方法发生运行时异常
            throw new RuntimeException("异步方法执行发生异常");
        }
        logger.info("该文件夹所包含文件数：{}", length);
        for (int i = 0; i < fileList.size(); i++) {
            UserFile userFileTemp = fileList.get(i);
            logger.info("当前执行删除任务的线程：" + Thread.currentThread().getName() + "目标删除文件：" + userFileTemp.getFileName());
            //标记删除标志
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper.set(UserFile::getDeleteFlag, 1)
                    .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                    .set(UserFile::getDeleteBatchNum, deleteBatchNum)
                    .eq(UserFile::getUserFileId, userFileTemp.getUserFileId())
                    .eq(UserFile::getDeleteFlag, 0);
            userFileMapper.update(null, userFileLambdaUpdateWrapper);
        }
        logger.info(filePath + "文件夹删除成功");
        return CompletableFuture.completedFuture("异步方法执行完成");
    }

    @Override
    public List<UserFile> selectFileListByFilePath(String filePath, long userId) {
        logger.info("根据用户ID和目录查找文件，用户ID：" + userId + "路径：" + filePath);
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getUserId, userId).eq(UserFile::getFilePath, filePath);
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId) {
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");

        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        logger.info("查询的目录为：" + filePath);

        lambdaQueryWrapper.eq(UserFile::getUserId, userId)
                .eq(UserFile::getFilePath, filePath);
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<UserFile> selectFilePathTreeByUserId(Long userId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getUserId,userId)
                .eq(UserFile::getIsDir,1);
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long userId) {
        if ("null".equals(extendName)){
            extendName=null;
        }
        LambdaUpdateWrapper<UserFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(UserFile::getFilePath,newfilePath)
                .eq(UserFile::getFilePath,oldfilePath)
                .eq(UserFile::getFileName,fileName)
                .eq(UserFile::getUserId,userId);

        if (StringUtils.isNotEmpty(extendName)){
            lambdaUpdateWrapper.eq(UserFile::getExtendName,extendName);
        }else {
            lambdaUpdateWrapper.isNull(UserFile::getExtendName);
        }

        userFileMapper.update(null,lambdaUpdateWrapper);
        //移动子目录
        oldfilePath=oldfilePath+fileName+"/";
        newfilePath=newfilePath+fileName+"/";
        oldfilePath = oldfilePath.replace("\\", "\\\\\\\\");
        oldfilePath = oldfilePath.replace("'", "\\'");
        oldfilePath = oldfilePath.replace("%", "\\%");
        oldfilePath = oldfilePath.replace("_", "\\_");
        if (extendName==null){
            //没扩展名，一般是目录，需要移动子目录
            userFileMapper.updateFilepathByFilepath(oldfilePath,newfilePath,userId);
        }
    }

    @Override
    public List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileName,fileName)
                .eq(UserFile::getFilePath,filePath)
                .eq(UserFile::getUserId,userId)
                .eq(UserFile::getDeleteFlag,"0");
        return userFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void replaceUserFilePath(String filePath, String oldFilePath, Long userId) {
        userFileMapper.replaceFilePath(filePath,oldFilePath,userId);
    }


}
