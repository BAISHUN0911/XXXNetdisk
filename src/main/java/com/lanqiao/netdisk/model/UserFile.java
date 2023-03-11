package com.lanqiao.netdisk.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "userfile", uniqueConstraints = {
        @UniqueConstraint(name = "fileindex", columnNames = {"fileName", "filePath", "extendName", "userId"})})
@Entity
@TableName("userfile")
/**
 * @description: DO对象：UserFile，与数据库表userfile字段一一对应
 * @author: BAISHUN
 * @date: 2023/3/8
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '用户文件id'")
    @TableId(type = IdType.AUTO)
    private Long userFileId;

    @Column(columnDefinition = "bigint(20) comment '用户id'")
    private Long userId;

    @Column(columnDefinition="bigint(20) comment '文件id'")
    private Long fileId;

    @Column(columnDefinition="varchar(100) comment '文件名'")
    private String fileName;

    @Column(columnDefinition="varchar(500) comment '文件路径'")
    private String filePath;

    @Column(columnDefinition="varchar(100) comment '扩展名'")
    private String extendName;

    @Column(columnDefinition="int(1) comment '是否是目录 0-否, 1-是'")
    private Integer isDir;

    @Column(columnDefinition="varchar(25) comment '上传时间'")
    private String uploadTime;

    @Column(columnDefinition="int(11) comment '删除标志 0-未删除 1-已删除'")
    private Integer deleteFlag;

    @Column(columnDefinition="varchar(25) comment '删除时间'")
    private String deleteTime;

    @Column(columnDefinition = "varchar(50) comment '删除批次号'")
    private String deleteBatchNum;

}