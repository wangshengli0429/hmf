package com.hmf.web.utils.enums;

/**
 * @Author
 * @Description 添加描述
 * @Param
 * @return
 **/
public enum HttpCodeEnum {

    CODE_0000("0", "成功"),

    PARAM_CODE_500("valid_500","缺少必填参数"),

    DEL_CODE_200("delete_200","删除成功"),
    DEL_FAIL_CODE_200("delete_fail_200","删除失败"),
    DEL_CODE_500("delete_500","删除异常"),
    ADD_CODE_200("add_200","新增成功"),
    ADD_FAIL_CODE_200("add_fail_200","新增成功"),
    ADD_CODE_500("add_500","新增失败"),
    EDIT_CODE_200("edit_200","修改成功"),
    EDIT_FAIL_CODE_200("edit_fail_200","修改成功"),
    EDIT_CODE_500("edit_500","修改失败"),
    QUERY_CODE_200("query_200","查询成功"),
    QUERY_FAIL_CODE_200("query_fail_200","查询成功"),
    QUERY_CODE_500("query_500","查询失败"),



    FILE_UPLOAD_CODE_9000("9000","文件上传失败"),
    FILE_UPLOAD_CODE_9003("9003","文件上传超过限制！"),
    FILE_UPLOAD_CODE_9001("9001","创建文件夹失败"),
    FILE_UPLOAD_CODE_9002("9002","文件下载失败"),
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
