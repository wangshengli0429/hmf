package com.hmf.web.utils.enums;

/**
 * @Author
 * @Description 添加描述
 * @Param
 * @return
 **/
public enum HttpCodeEnum {

    CODE_0000("0", "成功"),
    CODE_6000("6000","传参异常，缺少必填参数"),

    CUSTOMER_CODE_0001("0001", "客户地址新增成功"),
    CUSTOMER_CODE_1001("1001", "客户地址已存在，不可以重复新增或修改！"),
    CUSTOMER_CODE_0002("0002", "客户地址新增失败"),
    CUSTOMER_CODE_0003("0003", "客户地址删除成功"),
    CUSTOMER_CODE_0004("0004", "客户地址删除失败"),
    CUSTOMER_CODE_0005("0005", "客户地址修改成功"),
    CUSTOMER_CODE_0006("0006", "客户地址修改失败"),
    CUSTOMER_CODE_0007("0007","客户地址列表没有数据"),

    CUSTOMER_CODE_0008("0008","客户列表没有数据"),
    CUSTOMER_CODE_0009("0009","客户新增失败"),
    CUSTOMER_CODE_0010("0010","客户信息查询失败"),
    CUSTOMER_CODE_0011("0011","客户修改失败"),

    CUSTOMER_CODE_0012("0012","客户备忘录查询没有数据"),
    CUSTOMER_CODE_0013("0013","客户备忘录新增失败"),
    CUSTOMER_CODE_0014("0014","客户备忘录修改失败"),
    COMPANY_CODE_1500("1500","根据公司Id,获取公司名称为空！"),
    COMPANY_CODE_1900("1900","未能查到贵公司税号，若公司名称无误请手动输入！"),
    CUSTOMER_ADDRESS_CODE_0015("0015","客户新增地址不能为空"),
    CONTACT_ADD_CODE_0017("0017","客户新增联系人失败,请联系后台！"),

    ACC_FLOW_CODE_2001("2001","客户账号流水没有数据"),
    ACC_FLOW_CODE_2002("2002","客户账号流水申请发票状态失败"),
    ACC_FLOW_CODE_2003("2003","发票记录获取信息异常"),
    ACC_FLOW_CODE_2004("2004","钱包充值流水记录新增失败，请联系后台！"),

    PAYINFO_CODE_2700("2700","钱包充值支付信息记录新增失败，请联系后台！"),

    BILL_RECORD_CODE_1000("1000","申请发票新增记录失败"),
    BILL_RECORD_CODE_1001("1001","发票申请状态修改失败"),
    BILL_RECORD_CODE_1002("1002","我的发票列表传参异常"),
    BILL_RECORD_CODE_1003("1003","如果您选择电子发票邮箱地址不能为空！"),
    BILL_RECORD_CODE_1004("1004","如果您选择纸质发票请填写收件地址！"),
    BILL_RECORD_CODE_1005("1005","如果您选择增值税专用发票,不可以勾选个人/非企业用户"),
    BILL_RECORD_CODE_1006("1006","如果您选择增值税专用发票,开户行不能为空!"),
    BILL_RECORD_CODE_1007("1007","如果您选择增值税专用发票,开户行电话不能为空!"),
    BILL_RECORD_CODE_1008("1008","发票列表目前没有数据！"),
    BILL_RECORD_CODE_1009("1009","发票详情获取异常！"),
    BILL_RECORD_CODE_1010("1010","发票开具失败！"),
    BILL_RECORD_CODE_1011("1011","发票类型不能为空！"),
    BILL_RECORD_CODE_1012("1012","该发票已申请,不可以重复申请！"),
    BILL_RECORD_CODE_1013("1013","申请发票账号流水主键不能为空！"),
    BILL_RECORD_CODE_1014("1014","发票列表获取异常,请联系后台！"),

    WALLET_CODE_1100("1100","根据钱包Id未能查到钱包信息！"),
    WALLET_CODE_1101("1101","钱包充值金额修改失败，请联系后台！"),
    WALLET_CODE_1102("1102","钱包充值记录新增失败，请联系后台！"),

    COURIER_CODE_2000("2000","快递单号不能为空"),
    COURIER_CODE_2001("2001","快递明细新增异常"),

    COMMENT_CODE_0001("0001","评论新增异常"),
    COMMENT_CODE_0002("0002","评论查询异常"),

    ENGINEER_CODE_7001("7001","工程师新增失败"),
    ENGINEER_CODE_7002("7002","工程师列表没有数据"),

    FACILITATOR_CODE_0001("0001","服务商新增失败"),

    FILE_UPLOAD_CODE_9000("9000","文件上传失败"),
    FILE_UPLOAD_CODE_9003("9003","文件上传超过限制！"),
    FILE_UPLOAD_CODE_9001("9001","创建文件夹失败"),
    FILE_UPLOAD_CODE_9002("9002","文件下载失败"),

    CASE_CODE_3000("3000","工单故障描述不能为空！"),
    CASE_CODE_3001("3001","工单创建失败！"),
    CASE_CODE_3002("3002","工单更新失败！"),
    CASE_CODE_3005("3005","工单列表没有数据！"),
    CASE_CODE_3006("3006","工单详情查询异常！"),
    CASE_CODE_3007("3007","报修工单公司Id不能为空！"),
    CASE_CODE_3008("3008","报修工单联系人不能为空！"),
    CASE_CODE_3009("3009","报修工单联系人电话不能为空！"),
    CASE_CODE_3010("3010","报修工单创建公司失败！"),
    CASE_CODE_3011("3011","该公司已创建成功,在给公司关联钱包发生异常！请联系后台!"),
    CASE_CODE_3012("3012","报修工单公司名称不能为空！"),
    CASE_CODE_3013("3013","根据主键未能找到该报修工单！"),
    CASE_CODE_3060("3060","工单状态同步异常！"),

    REVEICE_CODE_3001("3001","该故障单已接单,不可重复接单！"),
    REVEICE_CODE_3002("3002","接单数据创建失败！"),
    REMOTE_HELP_CODE_3004("3004","远程技术支持数据创建失败！"),
    REMOTE_HELP_CODE_3005("3005","该故障单已有远程技术支持，不能重复处理！"),

    CASE_COST_CODE_3010("3010","确认费用插入异常！"),
    CASE_COST_CODE_3011("3011","确认费用已存在,不可重复插入！"),

    VISIT_CODE_3019("3019","上门单已创建，不可重复创建上门单！"),
    VISIT_CODE_3020("3020","上门单插入异常！"),
    VISIT_CODE_3021("3021","工程师接上门单异常！"),
    VISIT_CODE_3022("3022","上门单预约工程师异常！"),
    VISIT_CODE_3023("3023","根据主键查询上门单为null！"),
    VISIT_CODE_3025("3025","该上门单已被工程师接单！"),

    SIGN_RECORD_CODE_3030("3030","工程师打卡记录异常！"),
    SIGN_RECORD_CODE_3031("3031","工程师打卡记录更新异常！"),
    SIGN_RECORD_CODE_3032("3032","工程师打卡记录重复！"),

    FAULT_CATEGORY_CODE_3040("3040","故障类目查询库没有数据"),
    SALVE_TYPE_CODE_3050("3050","解决方案查询库没有数据"),

    SERVICES_CODE_3060("3060","服务单创建异常！"),
    SERVICES_CODE_3061("3061","服务单更新异常！"),
    SERVICES_CODE_3062("3062","服务单删除失败！"),
    DEVICES_CODE_3063("3063","设备查询异常！"),
    DEVICES_CODE_3065("3065","设备新增异常！"),
    DEVICES_CODE_3066("3066","设备参数不能为空！"),
    DEVICES_CODE_3067("3067","服务单同一个工单设备号不允许重复！"),

    SEND_REVISED_CODE_3070("3070","寄修数据创建异常！"),
    SEND_REVISED_CODE_3071("3071","寄修完成状态更新异常！"),
    SEND_REVISED_CODE_3072("3072","该项寄修已完成创建数据，不可重复提交！"),

    DOWNLOAD_CODE_2000("2000","下载成功"),
    DOWNLOAD_CODE_2001("2000","下载失败"),

    ALIPAY_CODE_6100("6100","加签异常！请联系后台"),
    ALIPAY_CODE_6200("6200","交易并创建等待买家付款!"),
    ALIPAY_CODE_6110("6110","订单ID或订单编号不能为null!请检查！"),
    ALIPAY_CODE_6120("6120","订单ID不能为null!请检查！"),
    ALIPAY_CODE_6130("6130","订单产品名称不能为null!请检查！"),
    ALIPAY_CODE_6150("6150","订单价格不能为nul!小于或等于0！请检查！"),
    ALIPAY_CODE_6160("6160","订单相关公司ID不能为空！"),
    ALIPAY_CODE_6170("6170","订单对象不能为nul!请检查！"),
    ALIPAY_CODE_6180("6180","获取解码参数解析异常！请联系后台！"),
    ALIPAY_CODE_6300("6300","验签失败！请联系后台！"),
    ALIPAY_CODE_6310("6310","没有查到该笔订单！"),
    ALIPAY_CODE_6350("6350","支付成功！该订单支付状态异常，请联系后台！"),
    ALIPAY_CODE_6400("6400","支付成功！更新订单表异常!"),
    ALIPAY_CODE_6410("6410","支付成功！本次交易订单金额参数比对异常!"),
    ALIPAY_CODE_6500("6500","支付失败！请联系后台！"),
    //异常提示
    CASE_LIST_ERROR_CODE_500("500","报修工单列表查询异常,请联系后台！"),
    CASE_COUNT_ERROR_CODE_500("500","报修工单统计查询异常,请联系后台！"),
    CASE_REMOTEHELP_ERROR_CODE_500("500","开单并处理异常,请联系后台！"),
    CATALOGS_ERROR_CODE_500("500","服务目录查询异常,请联系后台！"),
    SALVERESULT_ERROR_CODE_500("500","处理结果查询异常,请联系后台！"),
    SALVETYPE_ERROR_CODE_500("500","解决方案查询异常,请联系后台！"),
    ENGINEER_ERROR_CODE_500("500","工程师查询列表异常,请联系后台！"),
    CUS_TAXNUMBER_ERROR_CODE_500("500","查询公司税号异常,请联系后台！"),
    CUSTOMER_ERROR_CODE_500("500","查询客户列表异常,请联系后台！"),
    CUS_ERROR_CODE_500("500","查询客户信息异常,请联系后台！"),
    ACCFLOW_LIST_ERROR_CODE_500("500","查询账号流水列表异常,请联系后台！"),
    APPLY_BILL_ERROR_CODE_500("500","去申请发票页获取开票信息异常,请联系后台！"),
    RECHARGE_LIST_ERROR_CODE_500("500","查询充值记录列表异常,请联系后台！"),
    CUS_ADDRESS_ERROR_CODE_500("500","查询客户地址列表异常,请联系后台！"),
    CUS_REMARK_ERROR_CODE_500("500","查询客户备忘录异常,请联系后台！"),
    SERVICE_ERROR_CODE_500("500","查询服务单异常,请联系后台！"),
    SERVICE_DEL_ERROR_CODE_500("500","批量删除服务单异常,请联系后台！"),
    COMPANY_CONTACT_ERR_COR_500("500","查询客户联系人列表异常,请联系后台！"),
    COMPANY_CONTACT_ADD_ERR_COR_500("500","新增客户联系人列表异常,请联系后台！"),
    ENGR_ONLIE_ERROR_CODE_500("500","工程师在线打卡异常,请联系后台！"),
    ENGR_ONLI_QRY_ERROR_CODE_500("500","查询工程师最新在线打卡异常,请联系后台！"),
    ;


    // 成员变量
    private String code;
    private String message;

    // 构造方法

    /**
     * @return a
     * @Description 添加描述
    * @Param
     **/
    HttpCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取名称
     * @return 名称
     */
    public String getCode() {

        return this.code;
    }

    /**
     * 获取索引
     * @return 索引
     */
    public String getMessage() {
        return this.message;
    }


}
