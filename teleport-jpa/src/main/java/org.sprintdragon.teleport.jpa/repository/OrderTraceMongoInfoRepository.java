package org.sprintdragon.teleport.jpa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.sprintdragon.teleport.jpa.OrderTraceInfoMongoInfo;

/**
 * Created by wangdi on 16-8-22.
 */
public interface OrderTraceMongoInfoRepository extends PagingAndSortingRepository<OrderTraceInfoMongoInfo, Long> {
}
