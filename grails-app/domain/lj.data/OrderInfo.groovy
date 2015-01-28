package lj.data

import lj.common.StrCheckUtil
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderType
import lj.enumCustom.OrderValid

//订单信息
class OrderInfo {
    //用户ID
    ClientInfo clientInfo;
    //桌位
    TableInfo tableInfo;
    //订单状态
    Integer status=0
    //有效性
    Integer valid=0
    //饭店取消原因
    String cancelReason
    //跟进服务员ID
    StaffInfo waiter
    //收银员ID
    StaffInfo cashier
    //备注
    String remark
    //店内编号
    Integer numInRestaurant
    //流水号
    long orderNum
    //订单参与验证码
    String partakeCode
    //总金额
    Double totalAccount;
    //实收金额
    Double realAccount;
    //用餐人数
    int personCount;
    //创建时间
    Date createTime=new Date();
    //联系人
    String customerName;
    //发票是否已开
    boolean isInvoiced=false;
    //是否参加活动标示
    boolean isJoinActivity=false;
    /*******************为了方便加的冗余数据*****************/
    //饭店Name
    String restaurantName;
    //用户ID
    String userName;
    //桌位
    String tableName;

    static constraints = {
        clientInfo(nullable:false)
        tableInfo(nullable:false)
        status inList: OrderStatus.getCodeList()
        valid  inList: OrderValid.getCodeList()
        cancelReason nullable:true,maxSize:128
        waiter nullable:true
        cashier nullable:true
        remark nullable:true,maxSizes:256
        numInRestaurant(nullable: false,min: 0)
        orderNum(nullable:false,unique: true)
        partakeCode(nullable:false,maxSize: 4)
        totalAccount(nullable:true)
        realAccount(nullable:true)
        personCount(nullable:true);
        createTime(nullable: true);
        customerName(nullable: true,blank: true,maxSize: 128);
        isInvoiced(nullable:false);
        isJoinActivity(nullable:false);
        restaurantName(nullable:true,blank: true,maxSize:256);
        userName(nullable:true,blank: true,maxSize: 32);
        tableName(nullable:true,blank: true,maxSize: 64);
    }

}
