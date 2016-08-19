package org.sprintdragon.teleport.admin.entity.check;

import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMongoBean;
import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMysqlBean;
import org.sprintdragon.teleport.core.migrate.ICheckMigrate;

/**
 * Created by wangdi on 16-8-15.
 */
public class OrderTraceInfoMongoToMysqlCheckMigrate implements ICheckMigrate<OrderTraceInfoMongoBean, OrderTraceInfoMysqlBean> {

    @Override
    public OrderTraceInfoMysqlBean objTransform(OrderTraceInfoMongoBean from) {
        OrderTraceInfoMysqlBean to = new OrderTraceInfoMysqlBean();
        to.setOrderId(from.getOrderId());
        to.setVenderId(from.getVenderId());
        to.setCreated(from.getCreated());
        to.setCreatedTime(from.getCreatedTime());
        to.setModified(from.getModified());
        to.setModifiedTime(from.getModifiedTime());
        to.setType(from.getType());
        to.setStatus(from.getStatus());
        to.setMsg(from.getMsg());
        to.setOrderCreateDate(from.getOrderCreateDate());
        to.setOrderCreateTime(from.getOrderCreateTime());
        to.setStorageAndShipStatus(from.getStorageAndShipStatus());
        to.setStorageAndShipMsg(from.getStorageAndShipMsg());
        to.setCustomsId(from.getCustomsId());
        to.setCustomsModel(from.getCustomsModel());
        to.setPaymentNo(from.getPaymentNo());
        to.setPayPlatformType(from.getPayPlatformType());
        to.setPayEnumNo(from.getPayEnumNo());
        to.setFenFaStatus(from.getFenFaStatus());
        to.setFenFaRemark(from.getFenFaRemark());
        to.setProcessStatus(from.getProcessStatus());
        to.setProcessStatusMsg(from.getProcessStatusMsg());
        to.setClearanceTime(from.getClearanceTime());
        to.setVenderType(from.getVenderType());
        to.setStoreId(from.getStoreId());
        to.setMerchantId(from.getMerchantId());
        to.setPayTime(from.getPayTime());
        to.setPayPlatformType(from.getPayPlatformType());
        to.setErrorCode(from.getErrorCode());
        return to;
    }

    @Override
    public OrderTraceInfoMysqlBean getQuerySingleDomain(OrderTraceInfoMysqlBean to) {
        OrderTraceInfoMysqlBean query = new OrderTraceInfoMysqlBean();
        query.setOrderId(to.getOrderId());
        return query;
    }


}
