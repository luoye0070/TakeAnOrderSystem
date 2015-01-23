package lj.enumCustom

public enum MessageStatus {


    ORIGINAL_STATUS(0,'初始状态'),
    READED_STATUS(1,'已读')

    public Integer code
    public String label
    MessageStatus(Integer code,String label){
        this.code=code
        this.label=label
    }
}
