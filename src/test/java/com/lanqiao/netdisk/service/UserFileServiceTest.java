package com.lanqiao.netdisk.service;

import com.lanqiao.netdisk.model.UserFile;
import com.lanqiao.netdisk.service.impl.UserFileServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 用户文件业务类测试
 * @author: BAISHUN
 * @date: 2023/3/9
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@SpringBootTest
public class UserFileServiceTest {

    private Logger logger = LoggerFactory.getLogger(UserFileServiceTest.class);

    @Resource
    UserFileService userFileService;

    @Test
    void test_selectFileList() {
        List<UserFile> userFiles = userFileService.selectFileListByFilePath("/文件夹2", 30L);
        for (UserFile userFile : userFiles) {
            logger.info("文件名：" + userFile.getFileName());
        }
        logger.info("该目录下所有文件扫描完毕");
    }

    @Test
    void test_insertFilesAndDirs() {
        //模拟向数据库插入50个文件夹信息，50个文件信息
        UserFile dir = new UserFile();
        dir.setUserId(30L);
        dir.setFilePath("/");
        dir.setIsDir(1);
        dir.setUploadTime("2023-3-9");
        dir.setDeleteFlag(0);
        for (int i = 1; i <= 20; i++) {
            dir.setFileName("文件夹编号:"+i);
            userFileService.save(dir);
        }
        UserFile file = new UserFile();
        file.setUserId(30L);
        file.setFilePath("/");
        file.setIsDir(0);
        file.setFileId(11L);
        file.setUploadTime("2023-3-9");
        file.setExtendName("txt");
        file.setDeleteFlag(0);
        for (int i = 1; i <= 100; i++) {
            file.setFileName("文件编号:" + i);
            userFileService.save(file);
        }
        UserFile file2 = new UserFile();
        file2.setUserId(30L);
        file2.setFilePath("/文件夹2");
        file2.setIsDir(0);
        file2.setFileId(12L);
        file2.setUploadTime("2023-3-9");
        file2.setExtendName("word");
        file2.setDeleteFlag(0);
        for (int i = 1; i <= 200; i++) {
            file2.setFileName("文件编号:" + i);
            userFileService.save(file2);
        }
    }

    @Test
    void test_insertDirs() {
        UserFile dir = new UserFile();
        dir.setUserId(30L);
        dir.setFilePath("/文件夹2");
        dir.setIsDir(1);
        dir.setUploadTime("2023-3-9");
        dir.setDeleteFlag(0);
        for (int i = 1; i <= 20; i++) {
            dir.setFileName("文件夹编号:" + i);
            userFileService.save(dir);
        }
    }

    @Test
    void test_insertFiles() {
        UserFile file = new UserFile();
        file.setUserId(30L);
        file.setFilePath("/文件夹3");
        file.setIsDir(0);
        file.setFileId(11L);
        file.setUploadTime("2023-3-9");
        file.setExtendName("txt");
        file.setDeleteFlag(0);
        for (int i = 1; i <= 100; i++) {
            file.setFileName("文件编号:" + i);
            userFileService.save(file);
        }
    }

    @Test
    void test_batchDeleteFiles() {
        //测试updateFileDeleteStateByFilePath，删除文件夹并删除该文件下所有文件
        userFileService.updateFileDeleteStateByFilePath("/", "20230309170900", 30L);
    }


}
