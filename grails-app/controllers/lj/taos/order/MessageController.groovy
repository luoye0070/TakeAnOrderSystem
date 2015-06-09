package lj.taos.order

import lj.enumCustom.MessageType
import lj.enumCustom.MsgSendType

class MessageController {
    def messageService;

    def index() {}

    def test() {
        //生成消息通知顾客
        def msgParams = [:];
        msgParams.orderId = 0;
        msgParams.type = MessageType.UPDATE_DISH_LIST.code;
        msgParams.receiveId = 0;
        msgParams.content = "需要更新点菜列表";
        msgParams.sendType = MsgSendType.STAFF_TO_STAFF.code;
        msgParams.restaurantId = 0;
        def reInfo = messageService.sendMsg(msgParams);
    }
}
