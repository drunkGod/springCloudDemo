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
 * 系统用户表
 * </p>
 *
 * @author jvxb
 * @since 2020-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="SysUser对象", description="系统用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "所属角色id")
        private Integer roleId;

        @ApiModelProperty(value = "所属角色 (冗余设计)")
        private String roleName;

        @ApiModelProperty(value = "系統用户名")
        private String username;

        @ApiModelProperty(value = "系统用户真实姓名")
        private String realName;

        @ApiModelProperty(value = "系统用户密码")
        private String password;

        @ApiModelProperty(value = "系统用户手机号码")
        private String phone;

        @ApiModelProperty(value = "帐号是否有效 0 无效 1有效")
        private String valid;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String ROLE_ID = "role_id";

    public static final String ROLE_NAME = "role_name";

    public static final String USERNAME = "username";

    public static final String REAL_NAME = "real_name";

    public static final String PASSWORD = "password";

    public static final String PHONE = "phone";

    public static final String VALID = "valid";

    public static final String CREATE_TIME = "create_time";

}
