package org.sprintdragon.teleport.admin.entity.check;

import org.apache.commons.lang.StringUtils;
import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMongoBean;
import org.sprintdragon.teleport.admin.entity.domain.OrderTraceInfoMysqlBean;
import org.sprintdragon.teleport.common.utils.CommonTimeUtils;
import org.sprintdragon.teleport.core.migrate.ICheckMigrate;

import java.util.Date;

/**
 * Created by wangdi on 16-8-15.
 */
public class OrderTraceInfoMongoToMysqlCheckMigrate implements ICheckMigrate<OrderTraceInfoMongoBean, OrderTraceInfoMysqlBean> {

    @Override
    public OrderTraceInfoMysqlBean objTransform(OrderTraceInfoMongoBean from) {
        OrderTraceInfoMysqlBean to = new OrderTraceInfoMysqlBean();
        to.setOrderId(from.getOrderId());
        to.setVenderId(from.getVenderId());
        to.setCreated(from.getCreated() == null ? null : new Date(from.getCreated()));
        to.setModified(from.getModified() == null ? null : new Date(from.getModified()));
        to.setOrderCreateTime(from.getOrderCreateDate() == null ? null : new Date(from.getOrderCreateDate()));
        to.setClearanceTime(from.getClearanceTime() == null ? null : new Date(from.getClearanceTime()));
        to.setCustomsId(from.getCustomsId());
        to.setCustomsModel(from.getCustomsModel());
        to.setCustomsRegionCode(from.getCustomsregioncode());
        to.setErrorCode(from.getErrorCode());
        to.setMerchantId(from.getMerchantId());
        to.setParentPaymentNo(from.getParentPaymentNo());
        to.setPayEnumNo(from.getPayEnumNo());
        to.setPayPlatformType(from.getPayPlatformType());
        to.setPayTime(StringUtils.isEmpty(from.getPayTime()) ? null : CommonTimeUtils.getDateFromStr(from.getPayTime()));
        to.setPaymentNo(from.getPaymentNo());
        to.setProcessStatus(from.getProcessStatus());
        to.setProcessMsg(from.getProcessStatusMsg());
        to.setStorageShipStatus(from.getStorageAndShipStatus());
        to.setStorageShipMsg(from.getStorageAndShipMsg());
        to.setStoreId(from.getStoreId());
        to.setVenderType(from.getVenderType());
        to.setMsg(from.getMsg());
        return to;
    }

    @Override
    public OrderTraceInfoMysqlBean getQuerySingleDomain(OrderTraceInfoMysqlBean to) {
        OrderTraceInfoMysqlBean query = new OrderTraceInfoMysqlBean();
        query.setOrderId(to.getOrderId());
        return query;
    }


}
