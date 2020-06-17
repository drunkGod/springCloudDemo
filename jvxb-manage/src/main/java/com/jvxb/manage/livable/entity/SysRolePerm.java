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
 * 角色-权限映射表
 * </p>
 *
 * @author jvxb
 * @since 2020-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_perm")
@ApiModel(value="SysRolePerm对象", description="角色-权限映射表")
public class SysRolePerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "角色id")
        private Integer roleId;

        @ApiModelProperty(value = "权限id")
        private Integer permId;

    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String ROLE_ID = "role_id";

    public static final String PERM_ID = "perm_id";

    public static final String CREATE_TIME = "create_time";

}
