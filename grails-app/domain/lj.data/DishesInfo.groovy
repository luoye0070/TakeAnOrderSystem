package lj.data

import lj.enumCustom.DishesStatus
import lj.enumCustom.DishesValid
import lj.enumCustom.OrderType

//点菜
class DishesInfo {

    //订单ID
    OrderInfo order;
    //菜单Id
    FoodInfo food;
    //状态
    int status=0;
    //有效性
    int valid=0;
    //取消原因
    String cancelReason
    //备注
    String remark
    //店内编号
    int numInRestaurant=0
    //份数
    int num=1
    //厨师ID
    StaffInfo cook
    //单价
    double foodPrice=0d;
    //排序字段
    long sortId=0l;

    /*******为了方便添加的冗余数据******/
    //菜名
    String foodName;
    //用餐日期
    Date date;
    //到店时间/s送餐时间
    Date time;
    //菜谱图片
    String foodImg;
    //桌位
    String tableName;
    //订单类型
    int orderType=0;

    static belongsTo = [order:OrderInfo];
    static constraints = {
        order(nullable:false)
        food(nullable:false)
        status(nullable:false,inList:DishesStatus.codeList)
        valid(nullable:false,inList:DishesValid.codeList)
        cancelReason(nullable:true,blank: true,maxSize: 128);
        remark(nullable:true,blank: true,maxSize: 256);
        numInRestaurant(nullable:false,min: 0);
        num(nullable:false,min: 1);
        cook(nullable:true)
        foodPrice(nullable: false,min: 0d);
        sortId(nullable:false,min: 0l);
        foodName(nullable:true,blank: true);
        date(nullable: false);
        time(nullable: false);
        foodImg(nullable:true,blank: true, maxSize:128);
        tableName(nullable:true,blank: true,maxSize: 64);
        orderType(nullable:false,inList: OrderType.getCodeList());
    }


    @Override
    public java.lang.String toString() {
        return "DishesInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", foodId=" + foodId +
                ", status=" + status +
                ", valid=" + valid +
                ", cancelReason='" + cancelReason + '\'' +
                ", remark='" + remark + '\'' +
                ", numInRestaurant=" + numInRestaurant +
                ", num=" + num +
                ", cook=" + cook +
                ", foodPrice=" + foodPrice +
                ", sortId=" + sortId +
                ", foodName='" + foodName + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", foodImg='" + foodImg + '\'' +
                ", tableName='" + tableName + '\'' +
                ", orderType=" + orderType +
                ", version=" + version +
                '}';
    }
}
