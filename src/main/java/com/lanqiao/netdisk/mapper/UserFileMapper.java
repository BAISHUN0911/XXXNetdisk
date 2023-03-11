package com.lanqiao.netdisk.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanqiao.netdisk.model.UserFile;
import com.lanqiao.netdisk.vo.UserfileListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @description: 用户文件DAO，数据访问层，操作数据库表userfile
 * 这里使用了MyBatis plus技术，继承了BaseMapper<T>类，可以直接使用一些包装好的方法：例如常见的增删改查。
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Mapper
public interface UserFileMapper extends BaseMapper<UserFile> {

    /**
     * 搜索用户的文件
     * @param userFile 用户文件
     * @param beginCount 开始索引
     * @param pageCount 每页数据量
     * @return
     */
    List<UserfileListVO> userfileList (UserFile userFile, Long beginCount, Long pageCount);

    /**
     *        在已知扩展名中查询文件
     * @param fileNameList 文件名字列表
     * @param beginCount    用来分页，定位到哪一页
     * @param pageCount     每页显示的数据
     * @param userId        用户id
     * @return              查询到有扩展名的文件集合
     */
    List<UserfileListVO> selectFileByExtendName(List<String> fileNameList,Long beginCount,Long pageCount,long userId);


    /**
     *        查询未知扩展名的文件
     * @param fileNameList      文件名字列表
     * @param beginCount        用来分页，定位到哪一页
     * @param pageCount         每页显示的数据
     * @param userId            用户id
     * @return                  查询到没有扩展名的文件集合
     */
    List<UserfileListVO> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    //没有扩展名文件的总数，大概率是文件夹
    Long selectCountNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);
    //有扩展名文件的总数
    Long selectCountByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    //移动文件
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, Long userId);

    void replaceFilePath(@Param("filePath") String filePath, @Param("oldFilePath") String oldFilePath, @Param("userId") Long userId);

    /**
     * 根据用户id获取这个用户存储的文件总大小
     * @param userId 用户ID
     * @return
     */
    Long selectStorageSizeByUserId(@Param("userId") Long userId);

    /**
     * 删除文件夹
     * @param filePath 文件夹目录
     * @param deleteBatchNum 删除标识
     * @param userId 用户ID
     */
    void deleteDir(@Param("filePath") String filePath, @Param("deleteBatchNum") String deleteBatchNum, @Param("userId") Long userId);
}
