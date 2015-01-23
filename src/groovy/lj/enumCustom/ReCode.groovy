package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-3
 * Time: 上午11:25
 * To change this template use File | Settings | File Templates.
 */
public class ReCode {
    public final static def NO_RESULT=[code:1,label:"没有记录"];
    public final static def NO_ENOUGH_PARAMS=[code:2,label:"没有足够的参数"];
    public final static def ORDER_NOT_EXIST=[code:3,label:"订单不存在"];
    public final static def ORDER_APPRAISED=[code:4,label:"订单已经评价过"];
    public final static def DISH_HAVEERROR=[code:5,label:"点菜存在不成功"];
    public final static def CUSTOMER_RELATIONS_EXIST=[code:6,label:"客户关系已经存在"];
    public final static def HAVE_ERRORS=[code:7,label:"有错误存在"];
    public final static def OK=[code:0,label:"成功"];
    public final static def NOT_LOGIN=[code:-1,label:"您没有登录"];
    public final static def OUT_RANGE=[code:-2,label:"你的位置超出了该饭店的外卖配送范围，不能为您创建订单"];
    public final static def TABLE_HOLDED=[code:-3,label:"桌位已经被占，请选择别的桌位吧"];
    public final static def SAVE_FAILED=[code:-4,label:"保存数据失败"];
    public final static def OTHER_ERROR=[code:-5,label:"未知错误"];
    public final static def OUT_SHOPHOURS=[code:-6,label:"到店时间不能在营业时间之外哦"];
    public final static def ERROR_PARAMS=[code:-7,label:"提交的参数错误"];
    public final static def CANNOT_DISH=[code:-8,label:"订单当前状态或有效性下不能点菜"];
    public final static def NO_ORDER=[code:-9,label:"订单不存在"];
    public final static def ERROR_PARTAKECODE=[code:-10,label:"错误的点菜参与码"];
    public final static def ORDER_CANNOT_CANCEL=[code:-11,label:"订单当前状态或有效性下不能取消订单"];
    public final static def BLACKLIST_CUSTOMER=[code:-12,label:"已经被该饭店加入黑名单，不能做任何操作了"];
    public final static def ORDER_CANNOT_DELETE=[code:-13,label:"订单当前状态或有效性下不能删除订单"];
    public final static def USERNAME_NOT_EXIST=[code:-14,label:"用户名不存在"];
    public final static def PASSWORD_INCORRECT=[code:-15,label:"密码错误"];
    public final static def ORDER_CANNOT_UPDATE_VALID=[code:-16,label:"订单当前状态或有效性下不能更改其有效性"];
    public final static def ORDER_CANNOT_CAST_ACCOUNT=[code:-17,label:"订单当前状态或有效性下不能算账"];
    public final static def DENIED_USER=[code:-18,label:"非法用户"];
    public final static def NO_RESTAURANTINFO=[code:-19,label:"饭店不存在或不可用"];
    public final static def NOT_REGISTER_RESTAURANT=[code:-20,label:"您还没有注册饭店"];
    public final static def SHOP_NOT_ENABLED=[code:-21,label:"饭店关闭了"];
    public final static def SHOP_VERIFY_NOT_PASS=[code:-22,label:"饭店审核没有通过"];
    public final static def SHOP_WAIT_VERIFY=[code:-23,label:"饭店等待审核中"];
    public final static def CANNOT_REPEAT_REGISTER_SHOP=[code:-24,label:"不能重复注册店铺"];
    public final static def NO_POSITION=[code:-25,label:"必须设置工作人员的职位"];
    public final static def SAVE_FILE_FAILED=[code:-26,label:"保存文件失败"];
    public final static def SUBMIT_FORMAT_ERROR=[code:-27,label:"提交数据格式错误"];
    public final static def NO_IMAGE_SPACE=[code:-28,label:"没有足够的图片空间了"];
    public final static def NOT_A_IMAGE=[code:-29,label:"上传的不是图片"];
    public final static def CUSTOMER_NOT_EXIST=[code:-30,label:"客户不存在"];
    public final static def EXIST_THE_SAME_STAFF=[code:-31,label:"已经存在相同登录用户名的工作人员"];
    public final static def STAFF_LOGINNAME_NOT_EXIST=[code:-32,label:"登录用户名不存在"];
    public final static def NEED_CUSTOMER_ID_OR_NAME=[code:-33,label:"请提交客户的用户ID或用户名"];
    public final static def NEED_RESERVE_DATE=[code:-34,label:"预定桌位需要提交预定的日期"];
    public final static def NEED_RESERVE_TIME=[code:-35,label:"预定桌位需要提交预计到店时间"];
    public final static def ORDER_CANNOT_COMPLETE_DISH=[code:-36,label:"订单当前状态或有效性下不能做完成点菜操作"];
    public final static def OUT_RESERVETYPE_TIME=[code:-37,label:"到店时间不能在预定类型时间之外哦"];
    public final static def SYSTEM_ERROR=[code:-39,label:"系统错误"];
    public final static def TABLE_NOT_EXIST=[code:-40,label:"桌位不存在"];
    public final static def FOOD_IS_COLLECTED=[code:-41,label:"您已经收藏过该菜谱了哦"];
    public final static def RESTAURANT_IS_COLLECTED=[code:-42,label:"您已经收藏过该饭店了哦"];
    public final static def COLLECT_FAILED=[code:-43,label:"收藏失败"];
    public final static def NO_RESERVE_RESTAURANT=[code:-44,label:"请选择饭店"];
    public final static def NO_RESERVE_DATE=[code:-45,label:"请选择预定日期"];
    public final static def NO_RESERVE_TYPE=[code:-46,label:"请选择预定的类型"];
    public final static def NO_RIGHT_PHONE=[code:-47,label:"请填写正确的联系电话"];
    public final static def NO_RESERVE_TABLE=[code:-48,label:"请选择预定的桌位"];
    public final static def OVER_RESERVE_DATE=[code:-49,label:"预定日期已经过了"];
    public final static def NOT_GET_PHONE_NUM=[code:-50,label:"没有获取到手机号"];
    public final static def NO_CLIENT_OR_NOT_LOGIN=[code:-51,label:"没有客户端信息或没有登录"];
    public final static def NO_VALID_ADDRESS=[code:-52,label:"没有有效地址"];
    public final static def FOOD_NOT_EXIST=[code:-53,label:"食品不存在"];
    public final static def FOOD_CAN_NOT_TAKE_OUT=[code:-54,label:"食品不能外卖"];
    public final static def FOOD_NOT_ENOUGH=[code:-55,label:"食品已经卖完"];
    public final static def NO_RECORD=[code:-56,label:"没有记录"];
    public final static def NO_CARTS=[code:-57,label:"没有餐车记录"];
    public final static def SYSTEM_EXCEPTION = [code:-58, label:"系统异常"];
//    public Integer code
//    public String label
//
//    ReCode=[code:Integer code,String label){
//        this.code=code
//        this.label=label
//    }

}