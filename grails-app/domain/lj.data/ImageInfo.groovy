package lj.data

//图片
class ImageInfo {
    //图片访问地址
    String url
    //宽
    Integer width=0;
    //高
    Integer height=0;
    //是否回收了
    Boolean isDel=false;
    //类别号
    ImageClassInfo imageClassInfo;
    //上传时间
    Date uploadTime=new Date();
    //图片大小
    long size=0l;
    //存储位置 ,0本地，1百度云盘
    Integer savePlace=0;
    //文件名,记录文件名，用于显示给用户，方便用户选择
    String fileName;

    static constraints = {
        url(nullable: false, blank:false,maxSize:128);
        width(nullable:false,min: 0);
        height(nullable:false,min: 0);
        isDel(nullable:false);
        imageClassInfo(nullable:true);
        uploadTime(nullable:false);
        size(nullable: false,min:0l);
        savePlace(nullable: false,inList: [0,1]);
        fileName(nullable: true,blank: true,maxSize: 64);
    }
}
