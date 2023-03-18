package com.lanqiao.netdisk.service;

import com.alibaba.fastjson.JSON;
import com.lanqiao.netdisk.model.UserFile;
import com.lanqiao.netdisk.service.impl.UserFileServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @description: 用户文件业务类测试
 * @author: BAISHUN
 * @date: 2023/3/9
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@SpringBootTest
public class UserFileServiceTest {

    private Logger logger = LoggerFactory.getLogger(UserFileServiceTest.class);

    /**byName注入，在Spring的IoC容器中查找是否存在name为userFileService的bean，
     * 因为我们在UserFileServiceImpl类使用@Service注解时直接给bean命名了，所以这里会给出连接
     * @Service注解也可以不去命名，会默认取名为：将类首字母小写
     * */
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
        dir.setUpdateTime(new Date());
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
        file.setUpdateTime(new Date());
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
        file2.setUpdateTime(new Date());
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
        dir.setFilePath("/文件夹2/文件夹2-1/文件夹编号:20");
        dir.setIsDir(1);
        dir.setUpdateTime(new Date());
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
        file.setFilePath("/文件夹2/文件夹2-1/文件夹编号:20");
        file.setIsDir(0);
        file.setFileId(11L);
        file.setUpdateTime(new Date());
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
        //数据库中用户根目录下有一个文件夹2，文件夹2下有20个文件夹，在文件夹2下有个文件夹2-1，在文件夹2-1中有个编号20的文件夹，
        // 现在要删除这个编号20的文件夹：不仅要删除这个编号20的文件夹下的内容，还要删除这个编号20文件夹本身
        CompletableFuture<String> future = userFileService.updateFileDeleteStateByFilePath(1514L, "文件夹1-1", "/文件夹1/", 30L);
        System.out.println("当前线程名："+Thread.currentThread().getName());
        future.exceptionally(ex -> {
            System.out.println("删除文件夹的异步方法执行发生异常：" + ex.getMessage());
            return null;
        });
        try {
            //可以通过get()方法获取异步方法的返回值
            String s = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("测试方法执行完");
    }

    @Test
    void select_fileTree() {
        List<UserFile> userFiles = userFileService.selectFileTreeListLikeFilePath("/文件夹1/", 30L);

        logger.info("该目录下文件夹信息：{}", JSON.toJSONString(userFiles));
    }

    @Test
    void test_batchDeleteFiles2() {
        CompletableFuture<String> future = userFileService.updateFileDeleteStateByFilePath2("/文件夹1/", "20230312141100", 30L);
        future.exceptionally(ex -> {
            System.out.println("删除文件夹的异步方法执行发生异常：" + ex.getMessage());
            return null;
        });
        try {
            //可以通过get()方法获取异步方法的返回值
            String s = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        logger.info("测试方法执行完");
    }

    @Test
    void deleteFile() {
        userFileService.deleteUserFile(1515L, 30L);
    }


}
