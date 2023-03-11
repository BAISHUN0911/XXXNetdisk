package com.lanqiao.netdisk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "重命名文件DTO",required = true)
public class RenameFileDTO {

    @Schema(description = "用户文件ID")
    private Long userFileId;

    @Schema(description = "文件名")
    private String fileName;

}
