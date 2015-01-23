package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-2
 * Time: 下午8:56
 * To change this template use File | Settings | File Templates.
 */
public enum ImageSavePlace {
    LOCAL_FILE_SYSTEM(0,'本地文件系统'),
    BAIDU_YUNFILE(1,'百度云盘')

    public Integer code
    public String label
    ImageSavePlace(Integer code,String label){
        this.code=code
        this.label=label
    }
}