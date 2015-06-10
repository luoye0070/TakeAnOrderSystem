package lj.taos.order

import grails.converters.JSON
import lj.enumCustom.MessageType
import lj.enumCustom.MsgSendType

class MessageController {
    def messageService;

    def index() {}

    def test() {
        //生成消息通知顾客
        def msgParams = [:];
        msgParams.orderId = 75;
        msgParams.type = MessageType.ORDER_HANDLE_TYPE.code;
        msgParams.receiveId = 0;
        msgParams.content = "司第六届发牢骚四大佛教历史了福建撒";
        msgParams.sendType = MsgSendType.STAFF_TO_STAFF.code;
        msgParams.restaurantId = 0;
        def reInfo = messageService.sendMsg(msgParams);
        println("reInfo->"+reInfo);
    }
    def sendMsgByCustomer(){
        if(!params.content){
            def reInfo =[recode:[code:-100,label:"消息内容为空!"]];
            render(reInfo as JSON);
             return;
        }
        def msgParams = [:];
        //msgParams.orderId = 75;
        msgParams.type = MessageType.OTHER_TYPE.code;
        msgParams.receiveId = 0;
        msgParams.content = params.content;
        msgParams.sendType = MsgSendType.CUSTOMER_TO_STAFF.code;
        msgParams.restaurantId = 0;
        def reInfo = messageService.sendMsg(msgParams);
        println("reInfo->"+reInfo);
        render(reInfo as JSON);
    }
}
