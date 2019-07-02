

package com.hs.beijing_agent.entity.enums;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hs.beijing_agent.util.JsonUtil;
import java.util.HashMap;
import java.util.Map;

public enum ResponseMessageEnum {
	SUCCESS(200, "success"),
	FAIL(400, "fail"),
	VISIT_NOT_AUTHORIZED(401, "未授权"),
	ARGUMENT_ERROR(402,"参数错误"),
	ARGUMENT_EXCEPTION(407, "参数存在异常"),
	ARGUMENT_TOKEN_EMPTY(409, "Token为空"),
	ARGUMENT_TOKEN_INVALID(410, "Token无效"),
	SERVER_ERROR(501, "服务端异常"),
	SERVER_SQL_ERROR(503,"数据库操作出现异常"),
	SERVER_DATA_REPEAT(504, "服务器数据已存在"),
	SERVER_DATA_NOTEXIST(505,"数据不存在"),
	SERVER_DATA_STATUS_ERROR(506, "数据状态错误"),


	ERROR_LOGIN(601, "用户名或密码错误"),
	ERROR_MOBILE_EXIST(602, "该手机号已注册"),
	ERROR_AUTH_CODE(603, "验证码错误"),
    ERROR_USER_NOT_EXIT(604, "用户不存在"),
    ERROR_SHIRO_USER(605, "身份验证失败"),
    ERROR_MOBILE_CODE(606, "手机验证码错误"),
    ERROR_OLD_PASSWORD(607, "原密码错误"),
    ERROR_USER_FORBID(608, "该用户被禁用,请联系平台"),
    ERROR_PASSWORD_TOO_EASY(609, "密码太简单"),
    AUTH_CODE_NOT_EXIST(610, "请先发送验证码"),
    MEMBER_ID_AUDIT(611, "正在审核中，请勿重复提交"),
    FILE_UPLOAD_ERROR(612, "上传文件错误"),
    ERROR_NO_FILE(614, "文件不存在！"),
    ERROR_MOBILE_NOT_EXIST(615, "该手机号未注册"),

    //7XX是项目特殊情况返回,
    INFO_REFFERRER_NO_AUTH(701, "推荐人无推荐权限"),
    INFO_REFERRER_EXIST(702, "已有推荐人"),
    INFO_NO_WITHDRAWAL_NUM(703,"剩余提现次数为0，请下个月再提现"),
    INFO_WITHDRAWAL_LESS_THAN_MIN_MOMEY(704,"提现金额不能低于100元"),
    INFO_NO_BANK_ACCOUNT(705,"银行账户信息缺失，请先填写银行账户信息"),
    ERROR_NAME_REAL(706,"姓名与微信绑定的真实姓名不符"),
    ERROR_ID_CARD_NUM(707,"身份证号与微信绑定的身份证号不符"),
    NOTE_RECOMMEND_EXHIBITION(708,"请勿重复推荐会场"),
    NOTE_RECOMMEND_PRODUCT(708,"请勿重复推荐商品"),
    NOTE_REMIND_BEEN_SET(709,"您已设置提醒"),
    INVALID_CODE(710,"code无效"),
    NOTE_PRODUCT_SOLEOUT(711,"商品已下架"),
    INFO_BANK_ACCOUNT_VADITE_FAIL(712,"银行账户信息校验认证失败，请核对银行账户信息"),
    NOTE_GET_COUPON(713,"您已领取优惠卷"),
    ERROR_ORDER_OPERATION_NOT_ALLOWED_FOR_STATUS(714, "当前订单状态不支持当前操作"),
    INFO_ORDER_CART_AMOUNT_MIN(715,"该宝贝不能减少了呦"),
    INFO_ORDER_CART_AMOUNT_MAX(716,"应海关要求，单笔订单中同一商品不得超过30件"),
    ERROR_APPLET_LOGIN(717,"小程序登录异常"),
    ERROR_APPLET_VALIDATE_IDCARD(718,"实名验证失败，请如实填写身份信息"),
    INFO_COUPON_ACTIVITY_SOLDOUT(719,"优惠券活动已结束"),
    INFO_STOCK_UPDATE_FAIL(720,"库存更新失败"),
    INFO_ORDERITEM_BEEN_COMMNET(721,"订单已评价，请勿重复评价"),
    INFO_ORDER_REFUND_MONEY_ERROR(722,"订单退款金额异常"),
    INFO_ORDER_REFUND_EXIST(723,"退款订单已存在"),
    FAIL_ORDER_REFUND(724,"退款失败"),
    FAIL_ORDER_APPLY_REFUND(726,"当前订单不支持申请退款"),

    ;


    private int code;
    private String message;


	private ResponseMessageEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String toString() {
		Map rsMap = new HashMap();
		rsMap.put("code", Integer.valueOf(this.code));
		rsMap.put("msg", this.message);

		String str = JSON.toJSONString(rsMap);
        return str;

/*        JSONObject jsonNode = JSONObject.fromObject(rsMap);
		return jsonNode.toString();*/
	}

	public String appendObjectToString(Object obj) {
		if(obj == null){
			obj = new HashMap<String,Object>();
		}

		Map<String,Object> rsMap = new HashMap<String,Object>();
		rsMap.put("data", obj);
		rsMap.put("code", code);
		rsMap.put("msg", this.message);
		return JsonUtil.toJson(rsMap);
	}

	public String appendEmptyData() {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		rsMap.put("code", code);
		rsMap.put("msg", this.message);
		rsMap.put("data", new HashMap<String, String>());

        String str = JSON.toJSONString(rsMap);
        return str;
/*		JSONObject jsonNode = JSONObject.fromObject(rsMap);

		return jsonNode.toString();*/
	}

	public String paging(int total,Object obj){
		if(obj == null){
			obj = new HashMap<String,Object>();
		}

		Map<String,Object> rsMap = new HashMap<String,Object>();
		rsMap.put("code", code);
		rsMap.put("msg", this.message);
		rsMap.put("total", total);
		rsMap.put("rows", obj);

		return JsonUtil.toJson(rsMap);
	}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}