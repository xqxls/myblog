package com.example.search.repository;

import com.example.search.model.PostDocment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:方便对ES进行增删改查
 * @Author: huzhuo
 * @Date: Created in 2021/5/8 22:49
 */

@Repository
public interface PostRepository extends ElasticsearchRepository<PostDocment, Long> {

    // 符合jpa命名规范的接口


}
