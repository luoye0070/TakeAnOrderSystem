package lj.data

class FoodClassInfo {
    //名称
    String name
    //描述
    String description
    List foodInfos;
    static hasMany = [foodInfos:FoodInfo];
    static constraints = {
        name(nullable:false,blank:false,maxSize:16);
        description(nullable:true,blank:true,maxSize:256);
    }
}
