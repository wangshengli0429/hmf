//package com.hmf.web.config;
//
///**
// * 权限常量
// */
//public class PermissionConstants {
//    /**
//     * 登录后台管理系统
//     * 权限说明： 该权限用于控制用户是否可以登录ITSM后台系统。和账号的禁用状态不一样的是，如果用户没有登录权限但是账号启用状态，是可以登录第三方的系统比如App，Crm等。账号禁用时无法使用任何系统。
//     */
//    public static final String SYS_LOGIN = "syslogin";
//    /**
//     * 编辑客户信息
//     * 权限说明： 该权限用于控制是否编辑客户信息和公司信息，该权限通常需要依赖查看客户信息权限才能正常使用，但是无需做绑定处理。
//     */
//    public static final String CUST_EDIT = "custedit";
//    /**
//     * 查看订单信息
//     * 权限说明： 该权限用于控制是否查看客户的订单信息，包含查看列表和详情，以及其他会显示订单或者订单详情信息的地方。
//     */
//    public static final String ORDER_QUERY = "orderquery";
//    /**
//     * 编辑订单信息
//     * 权限说明： 该权限用于控制是否修改客户的订单信息，包括订单金额等信息。该权限通常需要依赖查看客户信息权限才能正常使用，但是无需做绑定处理。
//     */
//    public static final String ORDER_EDIT = "orderedit";
//    /**
//     * 查看报修信息
//     * 权限说明： 该权限用于控制是否查看客户的报修信息，包含查看列表和详情，以及其他会显示报修或者报修详情信息的地方。
//     */
//    public static final String CASE_QUERY = "casequery";
//    /**
//     * 发起报修信息
//     * 权限说明： 该权限用于控制是否可以发起新的报修信息。
//     */
//    public static final String CASE_CREATE = "casecreat";
//    /**
//     * 处理报修信息
//     * 权限说明： 该权限用于控制是否可以接单并处理报修信息。
//     */
//    public static final String CASE_EDIT = "caseedit";
//    /**
//     * 查看发票信息
//     * 权限说明： 该权限用于控制是否查看客户的发票信息，包含查看列表和详情。
//     */
//    public static final String BILL_QUERY = "billquery";
//    /**
//     * 处理发票信息
//     * 权限说明： 该权限用于控制是否处理客户的发票申请，包括审核和开票。
//     */
//    public static final String BILL_EDIT = "billedit";
//    /**
//     * 账户充值
//     * 权限说明： 该权限用于控制是否可以对账户进行充值。
//     */
//    public static final String ACC_RECHARGE = "accrecharge";
//    /**
//     * 管理用户
//     * 权限说明： 该权限用于控制是否可以查看和添加ITSM后台系统的账号信息。
//     */
//    public static final String USER_MANAGE = "usermanage";
//
//    /**
//     * 管理角色
//     * 权限说明： 该权限用于控制是否可以查看和添加ITSM后台系统的角色组信息。
//     */
//    public static final String ROLE_MANAGE = "rolemanage";
//    /**
//     * 查看客户信息
//     * 权限说明： 该权限用于控制是否查看客户信息和公司信息，包含查看列表和详情。
//     */
//    public static final String CUST_QUERY = "custquery";
//    /**
//     * 查看财务信息
//     * 权限说明： 该权限用于控制是否查看客户的财务信息比如账户流水、充值信息，包含查看列表和详情，以及其他会显示财务或者财务详情信息的地方。
//     */
//    public static final String FINANCE_QUERY = "financequery";
//
//}
