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
 * 系统权限表
 * </p>
 *
 * @author jvxb
 * @since 2020-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
@ApiModel(value="SysPermission对象", description="系统权限表")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "权限id")
        @TableId(value = "perm_id", type = IdType.AUTO)
    private Integer permId;

        @ApiModelProperty(value = "权限名称")
        private String permName;

        @ApiModelProperty(value = "权限描述")
        private String permDesc;

        @ApiModelProperty(value = "父权限id")
        private Integer permPid;

        @ApiModelProperty(value = "权限路径")
        private String permUrl;

        @ApiModelProperty(value = "排序")
        private Integer sort;

        @ApiModelProperty(value = "是否显示 0不显示 1显示")
        private String isShow;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;


    public static final String PERM_ID = "perm_id";

    public static final String PERM_NAME = "perm_name";

    public static final String PERM_DESC = "perm_desc";

    public static final String PERM_PID = "perm_pid";

    public static final String PERM_URL = "perm_url";

    public static final String SORT = "sort";

    public static final String IS_SHOW = "is_show";

    public static final String CREATE_TIME = "create_time";

}
