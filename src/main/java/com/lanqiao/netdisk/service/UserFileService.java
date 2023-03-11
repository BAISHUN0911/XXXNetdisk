package com.lanqiao.netdisk.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lanqiao.netdisk.model.UserFile;
import com.lanqiao.netdisk.vo.UserfileListVO;

import java.util.List;
import java.util.Map;
/**
 * @description: 用户文件业务
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
public interface UserFileService extends IService<UserFile> {

    /**
     * 根据路径获取用户文件
     * @param filePath 文件目录
     * @param userId 用户ID
     * @param currentPage 当前页
     * @param pageCount 总页数
     * @return
     */
    List<UserfileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount);

    /**
     * 根据文件类型查看文件
     * @param fileType 文件类型：1图像2文档3视频4音频5其它
     * @param currentPage 当前页
     * @param pageCount 总页数
     * @param userId 用户ID
     * @return
     */
    Map<String,Object> getUserFileByType(int fileType,Long currentPage,Long pageCount,Long userId);

    /**
     * 删除文件
     * @param userFileId 用户文件ID
     * @param sessionUserId
     */
    void deleteUserFile(Long userFileId, Long sessionUserId);

    /**
     * 通过文件目录标志文件删除状态
     * 删除目录时需要将该文件目录下的所有文件都放入回收站，而代码实现则是通过一个删除标志来实现，
     * 为了防止文件目录下文件特别多，因此这里需要创建一个新的线程去执行，防止出现阻塞
     * @param filePath 文件目录
     * @param deleteBatchNum 删除批次号
     * @param userId 用户ID
     */
    void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, Long userId);

    @Deprecated
    void updateFileDeleteStateByFilePath2(String filePath, String deleteBatchNum, Long userId);

    /**
     * 根据用户ID和目录查询该目录下的所有文件（文件+文件夹）
     * @param filePath
     * @param userId
     * @return
     */
    List<UserFile> selectFileListByFilePath(String filePath, long userId);

    /**
     * 根据文件路径获取该文件树
     * @param filePath 文件路径
     * @param userId 用户ID
     * @return
     */
    List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId);

    /**
     * 通过用户id获取该用户的文件树
     * 根据用户ID获取该用户的所有文件夹
     * @param userId 用户ID
     * @return
     */
    List<UserFile> selectFilePathTreeByUserId(Long userId);

    /**
     * 通过新路径去更新文件或目录的路径
     * @param oldfilePath 原文件目录
     * @param newfilePath 新文件目录
     * @param fileName 文件名
     * @param extendName 文件扩展名
     * @param userId 用户ID
     */
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long userId);

    /**
     * 根据文件名和路径来查找文件,用来检测想要更新的文件名是否已存在
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param userId 用户ID
     * @return
     */
    List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId);

    /**
     * 更改文件路径
     * @param filePath 文件路径
     * @param oldFilePath 移动前文件路径
     * @param userId 用户ID
     */
    void replaceUserFilePath(String filePath, String oldFilePath, Long userId);
}
