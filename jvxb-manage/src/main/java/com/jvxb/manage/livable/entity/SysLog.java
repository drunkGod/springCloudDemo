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
 * 系统日志表
 * </p>
 *
 * @author jvxb
 * @since 2020-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
@ApiModel(value="SysLog对象", description="系统日志表")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "系统日志主键")
        @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "操作内容")
        private String content;

    private String uri;

        @ApiModelProperty(value = "操作参数")
        private String parameter;

        @ApiModelProperty(value = "操作者id")
        private Integer operatorId;

        @ApiModelProperty(value = "操作人")
        private String operatorName;

        @ApiModelProperty(value = "操作时间")
        private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String CONTENT = "content";

    public static final String URI = "uri";

    public static final String PARAMETER = "parameter";

    public static final String OPERATOR_ID = "operator_id";

    public static final String OPERATOR_NAME = "operator_name";

    public static final String CREATE_TIME = "create_time";

}
