package pers.lurker.webconfig.response;

/**
 * 接口返回码和返回值
 * 结合返回数据封装类ResponseWrapperUtil，统一接口的数据返回格式
 *
 * @author Administrator
 * @date 2019-5-22
 */
public enum ReturnCodeEnum {

    SUCCESS("0000", "操作成功"),
    FAILED("0001", "操作失败"),
    NODATA("0002", "操作成功，但无记录"),
    CUSTOM("0003", "自定义消息提示"),
    TOKEN_EXPIRATION("0004", "您的令牌已过期"),
    REMOTE_LOGIN("0005", "您的账号在其他地方登录"),
    NOT_AUTHORITY("0007", "您没有权限访问"),
    INVALID_TOKEN("0008", "TOKEN无效"),
    UNKNOWN_EXCEPTION("0009", "服务异常"),
    ACCOUNT_ERROR("1000", "账户不存在或被禁用"),
    API_NOT_EXISTS("1001", "请求的接口不存在"),
    API_NOT_PER("1002", "没有该接口的访问权限"),
    PARAMS_ERROR("1004", "参数为空或格式错误"),
    SIGN_ERROR("1005", "数据签名错误"),
    AMOUNT_NOT_QUERY("1010", "余额不够，无法进行查询"),
    API_DISABLE("1011", "查询权限已被限制"),
    UNKNOWN_IP("1099", "非法IP请求"),
    SYSTEM_ERROR("9999", "请求出现异常");


    private final String code;
    private final String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
