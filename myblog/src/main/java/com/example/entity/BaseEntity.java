package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:统一的实体
 * @Author: huzhuo
 * @Date: Created in 2021/4/12 23:02
 */

@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)

    private Long id;
    private Date created;
    private Date modified;

}
