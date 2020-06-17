package com.jvxb.manage.livable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author jvxb
 * @since 2020-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value="SysRole对象", description="系统角色表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "角色id")
        @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

        @ApiModelProperty(value = "角色名称")
        private String roleName;

        @ApiModelProperty(value = "角色描述")
        private String roleDesc;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;


    public static final String ROLE_ID = "role_id";

    public static final String ROLE_NAME = "role_name";

    public static final String ROLE_DESC = "role_desc";

    public static final String CREATE_TIME = "create_time";

}
