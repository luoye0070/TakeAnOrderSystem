package lj.data

class ImageClassInfo {
    //名称
    String name
//    List imageInfos;
//    static hasMany = [imageInfos:ImageInfo]

    static constraints = {
        name(nullable: false, blank:false,maxSize:32);
    }
}
