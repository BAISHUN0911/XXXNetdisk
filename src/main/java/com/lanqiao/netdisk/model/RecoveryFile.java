package com.lanqiao.netdisk.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * @description: 通过JPA逆向生成数据库表
 * @author: BAISHUN
 * @date: 2023/2/23
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Data
@Table(name = "recoveryfile")
@Entity
@TableName("recoveryfile")
public class RecoveryFile {

    @Id
    @Column(columnDefinition = "bigint(20) comment '回收站文件id'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Long recoveryFileId;

    @Column(columnDefinition = "bigint(20) comment '用户文件id'")
    private Long userFileId;

    @Column(columnDefinition = "datetime comment '删除时间'")
    private Date deleteTime;

    @Column(columnDefinition = "varchar(36) comment '删除批次号'")
    private String deleteBatchNum;


}
