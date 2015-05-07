package lj.data

import lj.common.StrCheckUtil
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderType
import lj.enumCustom.OrderValid
import lj.enumCustom.ReserveOrderStatus

class ReserveOrderInfo {

    //用户ID
    ClientInfo clientInfo;
    //桌位
    TableInfo tableInfo;
    //有效性
    int valid = OrderValid.EFFECTIVE_VALID.code;
    //状态,0初始态，1已到店
    int status=ReserveOrderStatus.ORIGINAL_STATUS.code;
    //饭店取消原因
    String cancelReason
    //跟进服务员ID
    StaffInfo waiter
    //备注
    String remark
    //店内编号
    int numInRestaurant = 0;
    //流水号
    long orderNum
    //用餐人数
    int personCount;
    //创建时间
    Date createTime = new Date();
    //用餐时间
    Date dinnerTime;
    //联系电话
    String phone;
    //联系人
    String customerName;
    //点菜列表
    List<ReserveDishesInfo> dishes;
    /*******************为了方便加的冗余数据*****************/
    //饭店Name
    String restaurantName;
    //用户ID
    String userName;
    //桌位
    String tableName;

    static hasMany = [dishes:ReserveDishesInfo];
    static constraints = {
        clientInfo(nullable: true)
        tableInfo(nullable: false)
        valid inList: OrderValid.getCodeList()
        status inList: ReserveOrderStatus.getCodeList()
        cancelReason nullable: true, maxSize: 128
        waiter nullable: true
        remark nullable: true, maxSizes: 256
        numInRestaurant(nullable: false, min: 0)
        orderNum(nullable: false, unique: true)
        personCount(nullable: true);
        createTime(nullable: false);
        dinnerTime(nullable: false);
        phone(nullable: false,blank: false,maxSize: 16 ,validator: {
            if (it) {
                if (!StrCheckUtil.chkStrFormat(it, "phone")) {
                    return ["formatError"]
                }
            }
        });
        customerName(nullable: true, blank: true, maxSize: 128);
        restaurantName(nullable: true, blank: true, maxSize: 256);
        userName(nullable: true, blank: true, maxSize: 32);
        tableName(nullable: true, blank: true, maxSize: 64);
    }

    @Override
    public java.lang.String toString() {
        return "ReserveOrderInfo{" +
                "id=" + id +
                ", clientInfo=" + clientInfo +
                ", tableInfo=" + tableInfo +
                ", valid=" + valid +
                ", cancelReason='" + cancelReason + '\'' +
                ", waiter=" + waiter +
                ", remark='" + remark + '\'' +
                ", numInRestaurant=" + numInRestaurant +
                ", orderNum=" + orderNum +
                ", personCount=" + personCount +
                ", createTime=" + createTime +
                ", dinnerTime=" + dinnerTime +
                ", phone='" + phone + '\'' +
                ", customerName='" + customerName + '\'' +
                ", dishes=" + dishes +
                ", restaurantName='" + restaurantName + '\'' +
                ", userName='" + userName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", version=" + version +
                '}';
    }
}
