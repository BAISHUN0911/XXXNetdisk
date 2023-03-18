package com.lanqiao.netdisk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 删除文件操作
 * @author: BAISHUN
 * @date: 2023/3/12
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Schema(description="删除文件/文件夹VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFileVO {
    /** 用户id */
    private Long userId;
    /** 用户文件id */
    private Long userFileId;
    /** 需要删除的目录 */
    private String deletePath;
    /** 删除批次号 */
    private String deleteBatchNum;

}
