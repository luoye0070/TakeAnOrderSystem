package lj.data

//桌位
class TableInfo {
    //桌名
    String name
    //最少人数
    int minPeople;
    //最大人数
    int maxPeople;
    //是否多单共桌
    boolean canMultiOrder=false;
    //是否支持预订
    boolean canReserve=true;
    //描述
    String description
    //是否有效
    boolean enabled=true;

    static constraints = {
        name(nullable:false,blank:false,maxSize:64,unique: true);
        minPeople(nullable:false,min:1);
        maxPeople(nullable:false,min:1);
        canMultiOrder(nullable: false);
        canReserve(nullable: false);
        description(nullable:true,blank:true,maxSize:1024);
        enabled(nullable: false)
    }
}
