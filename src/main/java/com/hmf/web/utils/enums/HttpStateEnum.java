package com.hmf.web.utils.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 添加描述
 * @Param
 * @return
 **/
public enum HttpStateEnum {

    OK("服务器成功返回", "200"),
    INVALID_REQUEST("参数请求错误", "400"),
    UNAUTHORIZED("用户名密码错误", "401"),
    FORBIDDEN("访问被禁止", "403"),
    NOT_FOUND("服务器无法正常提供信息", "404"),
    INTERNAL_SERVER_ERROR("亲，请求失败呦！", "500"),
    TIME_OUT("网络请求超时", "504"),
    PARAM_ERROR("必填参数不能为空", "PARAM_400"),
    PARAM_NULL("请填写参数", "PARAM_401"),
    SELECT_ERROR("查询失败", "SELECT_501"),
    SELECT_NULL("未找到您所查询的信息", "SELECT_502"),
    SELECT_EXCEP("查询异常", "SELECT_500"),
    INSERET_ERROR("创建失败", "INSERET_501"),
    INSERET_EXCEP("创建异常", "INSERET_500"),
    UPDATE_ERROR("更新失败", "UPDATE_501"),
    UPDATE_EXCEP("更新异常", "UPDATE_500"),
    DELETE_ERROR("删除失败", "DELETE_501"),
    DELETE_EXCEP("删除异常", "DELETE_500"),
    TOKEN_ERROR_1("token已过期","TOKEN_500"),
    TOKEN_ERROR_2("token sign解析失败","TOKEN_500"),
    TOKEN_ERROR_3("token的head解析失败","TOKEN_500"),
    TOKEN_STATE_4("token的有效期小于5天,已返回新token有效期五天","TOKEN_0"),
    TOKEN_ERROR_5("token解析失败","TOKEN_500"),
    TOKEN_ERROR_6("token解析成功，但是userId无效","TOKEN_500"),
    CRM_LOGIN_ERROR_3("必须销售和工程师才可登录该系统","CRM_LOGIN_3"),
    DATA_NULL("DATA为空，无需返回参数","DATA_NULL"),
    INSERT_COMPANIES_1("该公司已经存在","COMPANIES_1"),
    /**
     * @Description 管理后端enum
     * @Param
     * @return 
     **/
    ROLE_NULL("未找到该角色信息","ROLE_100"),
    ROLE_EXIST("已有同名角色","ROLE_101"),
    CRM_LOGIN_ERROR_1("验证码错误","CRM_LOGIN_1"),
    CRM_LOGIN_ERROR_2("该账号无crm管理后台登录权限","CRM_LOGIN_2"),
    PERMISSION_ERROR_1("权限列表没有主键ID","PERMISSION_ERROR_1"),
    PERMISSION_ERROR_2("该账号无查看订单权限","PERMISSION_ERROR_2"),
    PERMISSION_ERROR_3("该账号无管理用户权限","PERMISSION_ERROR_3"),
    PERMISSION_ERROR_4("该账号无管理角色权限","PERMISSION_ERROR_4"),
    /**
     * @Description 账户明细
     * @Param
     * @return
     **/
    MONTH_AMOUNT("MONTH_AMOUNT", "MONTH_AMOUNT_100"),
    /**
     * @Description 报修单的数量状态
     * @Param             //100,"待接单" 105,"处理中" 106,"待评价" 107,"已完成" 108,"待支付"
     * @return 
     **/
    WAIT_CASE_100("WAIT","WAIT_100"),
    HANDLE_CASE_105("HANDL","HANDLE_105"),
    NOASSESS_CASE_106("NOASSESS","NASSESS_106"),
    FINISH_CASE_107("FINISH","FINISH__107"),
    NOPAY_CASE_108("NOPAY","NOPAY__108"),
    /**
     * @Description 客户企业
     * @Param
     * @return 
     **/
    MEMBERPOOL_NULL("未找到可用的企业会员号","MEMBERPOOL_201"),
    /**
     * @Description 微信和HT5端状态描述描述
     * @Param
     * @return
     **/
    LOGINTYPE_PWD("密码登录","PWD_LOGIN"),
    LOGINTYPE_IDENTITY("短信验证码登录","IDT_LOGIN"),
    LOGIN_STATE_1("登录成功","LGN_201"),
    LOGIN_ERROR_2("手机号码或者密码错误，请重试","LGN_202"),
    LOGIN_ERROR_3("未查找到该用户","LGN_203"),
    LOGIN_ERROR_4("验证码错误","LGN_204"),
    LOGIN_ERROR_5("必填参数不能为空","LGN_205"),
    LOGIN_ERROR_6("请至少选择一种方式登录","LGN_206"),
    LOGIN_ERROR_7("用户登陆异常","LGN_207"),
    LOGIN_ERROR_8("登录失败超过三次","LGN_208"),
    LOGIN_ERROR_9("密码不对","LGN_209"),

    WECHAT_ERROR_1("未获得code","WX_201"),
    WECHAT_ERROR_2("获取openid失败","WX_202"),

    //微信支付
    WEPAY_STATE_1("支付成功","SUCCESS"),
    WEPAY_ERROR_1("转入退款","REFUND"),
    WEPAY_ERROR_2("未支付","NOTPAY"),
    WEPAY_ERROR_3("已关闭","CLOSED"),
    WEPAY_ERROR_4("已撤销（付款码支付）","REVOKED"),
    WEPAY_ERROR_5("用户支付中（付款码支付）","USERPAYING"),
    WEPAY_ERROR_6("支付失败(其他原因，如银行返回失败)","PAYERROR"),

    //微信下单
    WEPAY_ERROR_7("下单失败","CRTPAYERROR"),



    //注册用户错误码
    REGIST_STATE_1("用户新增成功","RGT_201"),
    REGIST_ERROR_2("用户插入为空","RGT_202"),
    REGIST_ERROR_3("验证码错误","RGT_203"),
    REGIST_ERROR_4("必填参数不能为空","RGT_204"),
    REGIST_ERROR_5("新增用户异常","RGT_205"),
    REGIST_ERROR_6("该手机号已经注册过","RGT_206"),
    REGIST_ERROR_7("该公司已经注册过","RGT_207"),
    REGIST_ERROR_8("该用户名已经注册过","RGT_208"),
    REGIST_ERROR_9("该会员号已经使用","RGT_209"),
    REGIST_ERROR_10("该公司已有注册用户","RGT_2109"),

    //发送验证码
    SMS_STATE_1("验证码发送成功","SMS_201"),
    SMS_ERROR_2("验证码发送异常","SMS_202"),
    SMS_ERROR_3("该手机或者该IP当天发送验证码超过三次","SMS_203"),
    SMS_ERROR_4("必填参数不能为空","SMS_204"),
    SMS_ERROR_5("验证码发送失败","SMS_205"),
    SMS_ERROR_6("验证码已经失效","SMS_206"),
    SMS_ERROR_7("该手机号未发送验证码","SMS_207"),
    SMS_ERROR_9("验证码错误","SMS_209"),
    SMS_EXCEP_8("验证码校验异常","SMS_208"),


    //校验图片验证码
    IMGCODE_STATE_1("验证成功","IMGCODE_201"),
    IMGCODE_ERROR_2("验证失败","IMGCODE_202"),
    IMGCODE_ERROR_3("必填参数不能为空","IMGCODE_203"),

    //地域信息
    AREA_STATE_1("地址信息查询成功","AREA_201"),
    AREA_ERROR_2("地址信息查询异常","AREA_202"),
    AREA_ERROR_3("必填参数不能为空","AREA_203"),
    AREA_ERROR_4("地址信息查询失败","AREA_204"),

    //用户下单
    ORDER_STATE_1("订单创建成功","ORDER_201"),
    ORDER_ERROR_2("订单创建失败","ORDER_202"),
    ORDER_ERROR_3("必填参数不能为空","ORDER_203"),
    ORDER_ERROR_4("订单创建异常","ORDER_204"),
    ORDER_ERROR_5("订单金额不一致","ORDER_205"),
    ORDER_ERROR_7("订单已支付","ORDER_207"),
    ORDER_ERROR_8("流水订单不存在","ORDER_208"),
    ORDER_ERROR_6("未查询到该订单","ORDER_206");

    
    // 成员变量
    private String name;
    private String index;

    // 构造方法

    /**
     * @return a
     * @Description 添加描述
    * @Param
     **/
    HttpStateEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }

    /**
     * 获取名称
     * @return 名称
     */
    public String getName() {

        return this.name;
    }

    /**
     * 获取索引
     * @return 索引
     */
    public String getIndex() {
        return this.index;
    }

    /**
     * 根据索引获取名称
     * @param index 索引
     * @return 名称
     */
    public static String getName(String index) {
        String name = "";
        for (HttpStateEnum statusCodeEnum : HttpStateEnum.values()) {
            if (index.equals(statusCodeEnum.getIndex())) {
                name = statusCodeEnum.getName();
                break;
            }
        }
        return name;
    }

    /**
     * 获取List列表
     */
    public static List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HttpStateEnum statusCodeEnum : HttpStateEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("index", statusCodeEnum.getIndex());
            map.put("name", statusCodeEnum.getName());
            list.add(map);
        }
        return list;
    }
}
