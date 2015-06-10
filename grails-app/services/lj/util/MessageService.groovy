package lj.util

import grails.converters.JSON
import lj.Number
import lj.data.MessageInfo
import lj.data.StaffPositionInfo
import lj.enumCustom.*
import lj.mina.server.MinaServer

import java.text.SimpleDateFormat

class MessageService {
    def clientService;

    def serviceMethod() {

    }

    /***************
     * 消息生成
     *
     * @param params是传入的参数map
     * params参数格式为：
     * orderId=12,订单ID，可选
     * type=1,消息类型，必须
     * receiveId=12,接收方ID，必须
     * sendId=12,发送方ID，可选
     * content,消息内容，可选
     * recTime,预定接收时间，可选
     * sendType,必须
     *
     * @return 返回值
     * [recode: ReCode.OK];成功
     * [recode: ReCode.SAVE_FAILED,errors:messageInfo.errors.allErrors];保存到数据库失败
     * [recode:ReCode.NOT_LOGIN];没登录
     * *****************/
    def sendMsg(def params) {
        long userId = clientService.getClient()?.id?:0;//顾客ID
        println("发送消息-》"+params.content);
//        if (!userId) {
//            userId = Number.toLong(session.staffId);//取工作人员ID,
//        }
//        if (userId) {
            if (!params.sendId) {
                params.sendId = userId;
            }
            if (!params.recTime) {
                params.recTime = new Date();
            }
            MessageInfo messageInfo = new MessageInfo(params);
            if (!messageInfo.save(flush: true)) {
                return [recode: ReCode.SAVE_FAILED, errors: messageInfo.errors.allErrors];
            }
            //发送消息
            int userType = 0;
            if (messageInfo.sendType == MsgSendType.CUSTOMER_TO_STAFF.code || messageInfo.sendType == MsgSendType.STAFF_TO_STAFF.code)
            {
                userType = 0; //饭店方
                if(messageInfo.type==MessageType.UPDATE_DISH_LIST.code){//要求更新点菜列表的消息，需发送给所有在线的厨师
                    def staffPositionInfos= StaffPositionInfo.findAllByPositionType(PositionType.COOK.code);
                    if(staffPositionInfos){
                        staffPositionInfos.each {//每个在线厨师都发送消息,并且不关心是否发送成功
                            if(MinaServer.isOnline(it.staffInfo?.id?:0, userType))
                                MinaServer.sendMsg(it.staffInfo?.id?:0, userType, ([recode: ReCode.OK, messageInfo: messageInfo] as JSON).toString());
                        }
                    }
                //}else if(messageInfo.type==MessageType.ORDER_HANDLE_TYPE.code){//订单处理消息，发送给在线服务员
                }else{
                    //检查接收服务员是否在线，不在线换一个在线的发过去
                    if(!MinaServer.isOnline(messageInfo.receiveId, userType)){ //不在线则查找到一个在线的服务员

                        def staffPositionInfos= StaffPositionInfo.findAllByPositionType(PositionType.WAITER.code);
                        StaffPositionInfo staffPositionInfoIsOnline=null;
                        if(staffPositionInfos){
                            int size=staffPositionInfos.size();
                            int rand=new Random().nextInt(size);
                            for(int i=0;i<size;i++) {
                                StaffPositionInfo staffPositionInfo=staffPositionInfos.get(i);
                                if(MinaServer.isOnline(staffPositionInfo.staffInfo?.id?:0, userType)){
                                    staffPositionInfoIsOnline=staffPositionInfo;
                                    if(i>=rand){
                                        break;
                                    }
                                }
                            }

                        }

                        if(staffPositionInfoIsOnline==null){//没有在线服务员主管,是否有在线店主
                            staffPositionInfos= StaffPositionInfo.findAllByPositionType(PositionType.SHOPKEEPER.code);
                            if(staffPositionInfos){
                                int size=staffPositionInfos.size();
                                int rand=new Random().nextInt(size);
                                for(int i=0;i<size;i++) {
                                    StaffPositionInfo staffPositionInfo=staffPositionInfos.get(i);
                                    if(MinaServer.isOnline(staffPositionInfo.staffInfo?.id?:0, userType)){
                                        staffPositionInfoIsOnline=staffPositionInfo;
                                        if(i>=rand){
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if(staffPositionInfoIsOnline){
                            messageInfo.receiveId=staffPositionInfoIsOnline.staffInfo?.id?:0;
                        }
                    }
                    if (MinaServer.sendMsg(messageInfo.receiveId, userType, ([recode: ReCode.OK, messageInfo: messageInfo] as JSON).toString())) {
                        messageInfo.status = MessageStatus.READED_STATUS.code;
                    }
                    //println("messageInfo.receiveId->"+messageInfo.receiveId);
                }
            } else {
                userType = 1; //顾客方
            }
            //println("messageInfo.receiveId->"+messageInfo.receiveId);
            //发送不成功则保存消息
            if (messageInfo.status!=MessageStatus.READED_STATUS.code){
                if (messageInfo.save(flush: true))
                    return [recode: ReCode.OK];
                else
                    return [recode: ReCode.SAVE_FAILED, errors: messageInfo.errors.allErrors];
            }

            return [recode: ReCode.OK];
//        } else {
//            return [recode: ReCode.NOT_LOGIN];
//        }
    }
}
