package com.jvxb.search.livable.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * <p>
 *
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
@Data
@ApiModel(value = "Beauty对象", description = "")
@Document(indexName = "index_beauty", type = "beauty")
@AllArgsConstructor
@NoArgsConstructor
public class Beauty implements Serializable {

    private Integer id;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "票数")
    private Integer ps;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String IMG = "img";

    public static final String PS = "ps";
}
