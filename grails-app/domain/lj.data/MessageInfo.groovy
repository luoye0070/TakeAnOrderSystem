package lj.data

import lj.enumCustom.MessageType
import lj.enumCustom.MsgSendType

//消息
class MessageInfo {

    //订单ID
    long orderId=0
    //类型
    Integer type=0
    //状态
    Integer status=0
    //接受方ID
    long receiveId=0;
    //发送方ID
    long sendId=0;
    //内容
    String content
    //预定接收时间
    Date recTime
    //发送方向
    Integer sendType=0;
    //发送时间
    Date sendTime=new Date();

    /*************为了方便的冗余数据***********/

    static constraints = {
        orderId(nullable:false,min: 0l);
        type(nullable:false,inList:MessageType.getCodeList());
        status inList:[0,1]
        receiveId nullable:false,min:0l
        sendId nullable:false,min:0l
        content (nullable:true,blank:true,maxSize:256)
        recTime(nullable:false)
        sendType(nullable:false,inList:MsgSendType.getCodeList())
        sendTime(nullable: false);
    }


    @Override
    public java.lang.String toString() {
        return "MessageInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", type=" + type +
                ", status=" + status +
                ", receiveId=" + receiveId +
                ", sendId=" + sendId +
                ", content='" + content + '\'' +
                ", recTime=" + recTime +
                ", sendType=" + sendType +
                ", sendTime=" + sendTime +
                ", version=" + version +
                '}';
    }
}
