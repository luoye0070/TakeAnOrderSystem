package lj.data

import lj.enumCustom.DishesCollectionStatus

class DishesCollection {
    OrderInfo orderInfo;
    List<DishesInfo> dishesInfos;
    int type=0;
    int status=0;
    static hasMany = [dishesInfos:DishesInfo];
    static constraints = {
        orderInfo(nullable: false);
        dishesInfos(nullable: true);
        type(nullable:false);
        status(nullable:false,inList: DishesCollectionStatus.codeList);
    }

    @Override
    public java.lang.String toString() {
        return "DishesCollection{" +
                "id=" + id +
                ", orderInfo=" + orderInfo +
                ", dishesInfos=" + dishesInfos +
                ", type=" + type +
                ", status=" + status +
                ", version=" + version +
                '}';
    }
}
