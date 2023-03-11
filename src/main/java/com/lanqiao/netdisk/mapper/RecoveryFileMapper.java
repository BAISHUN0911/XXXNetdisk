package com.lanqiao.netdisk.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanqiao.netdisk.model.RecoveryFile;
import com.lanqiao.netdisk.vo.RecoveryFileListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @description: DAO层，回收文件相关数据访问操作
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Mapper
public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {
    /**
     * 查询已回收文件列表
     * @return
     */
    List<RecoveryFileListVO> selectRecoveryFileList();
}
