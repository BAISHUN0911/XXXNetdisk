package com.lanqiao.netdisk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 删除文件DTO
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Data
@Schema(name = "删除文件DTO",required = true)
public class DeleteFileDTO {
    @Schema(description = "用户文件id")
    private Long userFileId;
    @Schema(description = "文件路径")
    private String filePath;
    @Schema(description = "文件名")
    private String fileName;
    @Schema(description = "是否是目录：0否1是")
    private Integer isDir;
}
