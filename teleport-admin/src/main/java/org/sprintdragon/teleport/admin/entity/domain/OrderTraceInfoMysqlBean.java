package org.sprintdragon.teleport.admin.entity.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wangdi on 16-8-16.
 */
public class OrderTraceInfoMysqlBean {

    /**
     * 主键Id(全局唯一)
     */
    @NotNull
    private Long id;

    /**
     * 订单号
     */
    @NotNull
    private Long orderId;

    /**
     * 商家Id
     */
    @NotNull
    private Long venderId;

    /**
     * 海关订单的入库时间
     */
    private Date created;

    /**
     * 海关订单的更新时间
     */
    private Date modified;

    /**
     * 京东订单的创建时间
     */
    @NotNull
    private Date orderCreateTime;

    /**
     * 清关时间
     */
    private Date clearanceTime;

    /**
     * 保税区Id
     */
    @NotNull
    private String customsId;

    /**
     * 保税区模式
     */
    @NotNull
    private String customsModel;

    /**
     * 关区代码
     */
    private String customsRegionCode;

    /**
     * 错误码
     */
    private Integer errorCode;


    /**
     * 服务商Id
     */
    private String merchantId;

    /**
     * 父支付单号(交易流水号)
     */
    private String parentPaymentNo;

    /**
     * 支付枚举号
     */
    private String payEnumNo;

    /**
     * 支付平台类型
     */
    private String payPlatformType;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付单号
     */
    private String paymentNo;

    /**
     * 订单的状态
     */
    @NotNull
    private Integer processStatus;

    /**
     * 订单状态描述
     */
    @NotNull
    private String processMsg;

    /**
     * 订单出库发货状态
     */
    private Integer storageShipStatus;

    /**
     * 订单出库发货状态描述
     */
    private String storageShipMsg;

    /**
     * 库房Id
     */
    private Long storeId;

    /**
     * 订单类型
     */
    private String venderType;

    /**
     * 存储供商家查看的友好提示信息
     */
    private String msg;

    @Override
    public String toString() {
        return "OrderTraceInfoMysqlBean{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", venderId=" + venderId +
                ", created=" + created +
                ", modified=" + modified +
                ", orderCreateTime=" + orderCreateTime +
                ", clearanceTime=" + clearanceTime +
                ", customsId='" + customsId + '\'' +
                ", customsModel='" + customsModel + '\'' +
                ", customsRegionCode='" + customsRegionCode + '\'' +
                ", errorCode=" + errorCode +
                ", merchantId='" + merchantId + '\'' +
                ", parentPaymentNo='" + parentPaymentNo + '\'' +
                ", payEnumNo='" + payEnumNo + '\'' +
                ", payPlatformType='" + payPlatformType + '\'' +
                ", payTime=" + payTime +
                ", paymentNo='" + paymentNo + '\'' +
                ", processStatus=" + processStatus +
                ", processMsg='" + processMsg + '\'' +
                ", storageShipStatus=" + storageShipStatus +
                ", storageShipMsg='" + storageShipMsg + '\'' +
                ", storeId=" + storeId +
                ", venderType='" + venderType + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getClearanceTime() {
        return clearanceTime;
    }

    public void setClearanceTime(Date clearanceTime) {
        this.clearanceTime = clearanceTime;
    }

    public String getCustomsId() {
        return customsId;
    }

    public void setCustomsId(String customsId) {
        this.customsId = customsId;
    }

    public String getCustomsModel() {
        return customsModel;
    }

    public void setCustomsModel(String customsModel) {
        this.customsModel = customsModel;
    }

    public String getCustomsRegionCode() {
        return customsRegionCode;
    }

    public void setCustomsRegionCode(String customsRegionCode) {
        this.customsRegionCode = customsRegionCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getParentPaymentNo() {
        return parentPaymentNo;
    }

    public void setParentPaymentNo(String parentPaymentNo) {
        this.parentPaymentNo = parentPaymentNo;
    }

    public String getPayEnumNo() {
        return payEnumNo;
    }

    public void setPayEnumNo(String payEnumNo) {
        this.payEnumNo = payEnumNo;
    }

    public String getPayPlatformType() {
        return payPlatformType;
    }

    public void setPayPlatformType(String payPlatformType) {
        this.payPlatformType = payPlatformType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessMsg() {
        return processMsg;
    }

    public void setProcessMsg(String processMsg) {
        this.processMsg = processMsg;
    }

    public Integer getStorageShipStatus() {
        return storageShipStatus;
    }

    public void setStorageShipStatus(Integer storageShipStatus) {
        this.storageShipStatus = storageShipStatus;
    }

    public String getStorageShipMsg() {
        return storageShipMsg;
    }

    public void setStorageShipMsg(String storageShipMsg) {
        this.storageShipMsg = storageShipMsg;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getVenderType() {
        return venderType;
    }

    public void setVenderType(String venderType) {
        this.venderType = venderType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
