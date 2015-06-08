import grails.converters.JSON
import lj.data.MessageInfo
import lj.data.StaffInfo
import lj.data.StaffPositionInfo
import lj.enumCustom.MessageStatus
import lj.enumCustom.MessageType
import lj.enumCustom.MsgSendType
import lj.enumCustom.PositionType
import lj.enumCustom.ReCode
import lj.mina.server.MinaServer
import lj.mina.server.OnLineListener

class BootStrap {
    def orderAndReserveService;
    def init = { servletContext ->
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy-MM-dd HH:mm:ss")
        }
        orderAndReserveService.markExpire();

        MinaServer.start(5000, "UTF-8", "\n", "\n", new OnLineListener() {
            @Override
            void on(long userId, String sn, String phoneNum, int userType) {
                println("上线--》userId:"+userId+"-sn:"+sn+"-phoneNum:"+phoneNum+"-userType:"+userType);
                MessageInfo.withTransaction {
                    //上线后查询出未发送的消息发送
                    if (userType == 0) {//饭店端
                        //根据userId查询出职位和饭店ID
                        StaffPositionInfo staffPositionInfo = StaffPositionInfo.findByStaffInfo(StaffInfo.get(userId));
                        if (staffPositionInfo) {
                            if (staffPositionInfo.positionType == PositionType.WAITER.code ||
                                    staffPositionInfo.positionType == PositionType.SHOPKEEPER.code) {//服务员上线，发送订单处理类消息
                                def msgList = MessageInfo.findAllByTypeAndSendTypeInList(MessageType.ORDER_HANDLE_TYPE.code,
                                        [MsgSendType.STAFF_TO_STAFF.code, MsgSendType.CUSTOMER_TO_STAFF.code]);
                                if (msgList) {
                                    msgList.each {
                                        if (MinaServer.sendMsg(userId, userType, ([recode: ReCode.OK, messageInfo: it] as JSON).toString())) {
                                            it.status = MessageStatus.READED_STATUS.code;
                                            it.receiveId = userId;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (userType == 1) {//顾客端

                    }
                }
            }

            @Override
            void down(long userId, String sn, String phoneNum, int userType) {
                //To change body of implemented methods use File | Settings | File Templates.
                println("下线--》userId:"+userId+"-sn:"+sn+"-phoneNum:"+phoneNum+"-userType:"+userType);
            }
        });
    }
    def destroy = {
    }
}
