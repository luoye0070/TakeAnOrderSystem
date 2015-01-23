package lj.data

import lj.common.StrCheckUtil

//饭店信息
class RestaurantInfo {

    //店名
    String name
    //招牌图片
    String image
    //地址
    String address;
    //经度
    Double longitude=0.0
    //维度
    Double latitude=0.0
    //电话
    String phone
    //店主姓名
    String masterName
    //营业时间起
    Date shopHoursBeginTime
    //营业时间止
    Date shopHoursEndTime
    //开启关闭状态true开启，false关闭
    Boolean enabled=true
    //进店吃饭是否必须扫描二维码true是，false否
//    Boolean need2Code=true
    //菜系
    String cuisineName
    //图片空间大小，初始大小为1G，单位字节
    long imageSpaceSize=1024*1024l*1024
    //图片空间已用大小，初始大小为0，单位字节
    long imageSpaceUsedSize=0l;
    //人均消费水平，单位元
    Double averageConsume;
    //备注
    String remark;
    //简单描述
    String description;
    //店铺基础url地址
    String baseUrl;

    static constraints = {
        name (nullable: false,blank:false,maxSize:256)
        image (nullable:true,blank:true,maxSize:128)
        address (nullable: false,blank:false,maxSize:512)
        longitude (nullable:true,blank:true);
        latitude (nullable:true,blank:true);
        phone (bullable:false,blank:false,maxSize:16, validator: {
            if (it) {
                if (!StrCheckUtil.chkStrFormat(it, "phone")) {
                    return ["formatError"]
                }
            }
        });
        masterName (nullable: false,blank:false,maxSize:32)
        shopHoursBeginTime(nullable:false,blank:false)
        shopHoursEndTime(nullable:false,blank:false)
        enabled(nullable:false,blank:false);
//        need2Code()
        cuisineName(nullable: true,blank: true,maxSize: 16);
        imageSpaceSize(nullable: false,min: 0l);
        imageSpaceUsedSize(nullable: false,min: 0l);
        averageConsume(nullable: true,min: 0d);
        remark(nullable:true,blank:true,maxSize:128);
        description (nullable:true,blank: true,maxSize:1024*128);
        baseUrl(nullable:true,blank: true,maxSize:512);
    }
}
