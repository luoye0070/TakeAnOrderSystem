package lj.data

import lj.enumCustom.AppraiseType

//评价
class AppraiseInfo {

    //订单ID
    Long orderId
    //类型
    Integer type=0
    //卫生质量评分(满分10)
    Integer hygienicQuality=0
    //服务态度评分(满分10)
    Integer serviceAttitude=0
    //送餐速度评分(满分10)
    Integer deliverySpeed=0
    //味道评分(满分10)
    Integer taste=0
    //总体评分(满分10)
    Integer whole=0
    //评价内容
    String content
    //评价时间
    Date appraiseTime
    //评价人用户ID
    Long clientId
    //是否匿名评价
    Boolean isAnonymity=false

    /***************以下是冗余数据，为了提高效率而添加的字段*************/
    //用户名
    String userName;

    static constraints = {
        orderId(nullable:false,min: 0l);
        type(nullable: false, inList:AppraiseType.getCodeList());
        hygienicQuality(nullable: false,min: 0);
        serviceAttitude(nullable: false,min: 0)
        deliverySpeed(nullable: false,min: 0)
        taste(nullable: false,min: 0)
        whole(nullable: false,min: 0)
        content(nullable:true,blank: true,maxSizes:256);
        appraiseTime(nullable: false) ;
        clientId(nullable:false,min: 0l);
        isAnonymity(nullable: false);
        userName(nullable: false,blank:false,maxSize:32);
    }
}
