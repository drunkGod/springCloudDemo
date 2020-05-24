package com.jvxb.beauty.livable.entity;

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
 * 
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("beauty")
@ApiModel(value="Beauty对象", description="")
public class Beauty implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "名字")
        private String name;

        @ApiModelProperty(value = "图片")
        private String img;

        @ApiModelProperty(value = "票数")
        private Integer ps;

        @ApiModelProperty(value = "来源")
        private String origin;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createtime;

        @ApiModelProperty(value = "更新时间")
        private LocalDateTime updatetime;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String IMG = "img";

    public static final String PS = "ps";

    public static final String ORIGIN = "origin";

    public static final String CREATETIME = "createtime";

    public static final String UPDATETIME = "updatetime";

}
