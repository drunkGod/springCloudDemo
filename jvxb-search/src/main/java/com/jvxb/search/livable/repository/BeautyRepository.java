package com.jvxb.search.livable.repository;

import com.jvxb.search.livable.entity.Beauty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author jvxb
 * @since 2020-04-05
 */
@Component
public interface BeautyRepository extends ElasticsearchRepository<Beauty, Long> {
}
