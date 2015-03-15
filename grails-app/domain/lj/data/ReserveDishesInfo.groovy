package lj.data

import lj.enumCustom.DishesStatus
import lj.enumCustom.DishesValid
import lj.enumCustom.OrderType

//点菜
class ReserveDishesInfo {

    //订单ID
    ReserveOrderInfo order;
    //菜单Id
    FoodInfo food;
    //备注
    String remark
    //份数
    int num = 1
    //单价
    double foodPrice = 0d;

    /*******为了方便添加的冗余数据******/
    //菜名
    String foodName;
    //订单时间
    Date orderTime;
    //菜谱图片
    String foodImg;
    //桌位
    String tableName;

    static constraints = {
        order(nullable: false)
        food(nullable: false)
        remark(nullable: true, blank: true, maxSize: 256);
        num(nullable: false, min: 1);
        foodPrice(nullable: false, min: 0d);
        foodName(nullable: true, blank: true);
        orderTime(nullable: true);
        foodImg(nullable: true, blank: true, maxSize: 128);
        tableName(nullable: true, blank: true, maxSize: 64);
    }


    @Override
    public java.lang.String toString() {
        return "ReserveDishesInfo{" +
                "id=" + id +
                ", order=" + order +
                ", food=" + food +
                ", remark='" + remark + '\'' +
                ", num=" + num +
                ", foodPrice=" + foodPrice +
                ", foodName='" + foodName + '\'' +
                ", orderTime=" + orderTime +
                ", foodImg='" + foodImg + '\'' +
                ", tableName='" + tableName + '\'' +
                ", version=" + version +
                '}';
    }
}
