package lj.data

import lj.enumCustom.ClientType

class ClientInfo {
    UserInfo userInfo;//用户,绑定的用户
    String nickname;//昵称
    String clientMark;//客户端标示
    String otherClientMark;//其他客户端标示
    int clientType;//客户类型
    static constraints = {
        userInfo(nullable: true);
        nickname(nullable: true,blank: true,maxSize:64);
        clientMark(nullable: true,blank: true,maxSize:64);
        otherClientMark(nullable: true,blank: true,maxSize:64);
        clientType(nullable:false,inList: ClientType.getCodeList());
    }

}
