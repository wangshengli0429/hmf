package com.hmf.web.entity;

import com.hmf.web.utils.enums.IsPaidEnum;
import com.hmf.web.utils.enums.isMainOrderEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PeOrders extends BaseEntity implements Serializable {
    private Integer id;

    private Integer customerId;

    private Integer companiesId;

    private Integer caseId;

    private String orderNum;

    private Byte orderType;

    private Integer productId;

    private String productName;

    private Integer number;

    private Integer month;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private String contactName;

    private String contactPhone;

    private Integer mainOrderId;

    private isMainOrderEnum isMainOrder;

    private IsPaidEnum isPaid;

    private Date startDate;

    private Date expireDate;

    private Byte status;

    private Byte payWay;

    private Byte payType;

    private Integer cusAddressId;

    private String customerNote;

    private Date paidTime;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;


}