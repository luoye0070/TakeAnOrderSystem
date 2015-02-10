package lj.util

import lj.common.StringUtil
import lj.common.UniqueCode
import lj.data.ClientInfo
import lj.enumCustom.ClientType

class ClientService {
    def webUtilService;
    def getClient() {
        ClientInfo clientInfo=null;
        String clientIdentification= webUtilService.readCookie("clientIdentification");
        if(StringUtil.isEmpty(clientIdentification)){//新建
            clientInfo=new ClientInfo();
            clientInfo.clientType=ClientType.WEB_PAGE.code;//暂时设置只从网页来的客户请求
            if(clientInfo.save(flush: true)){
                clientIdentification=UniqueCode.getUniqueCode(clientInfo.id,32);//亿亿个客户后可能重复
                clientInfo.clientMark=clientIdentification;
                clientInfo.save(flush: true);
                //写入Cookie
                webUtilService.writeCookie("clientIdentification",clientIdentification,365);//将cookie保存1年
            }
        }else{
            clientInfo=ClientInfo.findByClientMark(clientIdentification);
        }
        if(clientInfo==null){ //新建一个
            clientInfo=new ClientInfo();
            clientInfo.clientMark=clientIdentification;
            clientInfo.clientType=ClientType.WEB_PAGE.code;//暂时设置只从网页来的客户请求
            if(!clientInfo.save(flush: true)){
                clientInfo=null;//如果保存不成功则返回一个null
            }
        }
        return clientInfo;
    }
    def setClient(){
       String clientIdentification=webUtilService.readCookie("clientIdentification");
       //println("clientIdentification-pre>"+clientIdentification);
       if(StringUtil.isEmpty(clientIdentification)){//新建
           ClientInfo clientInfo=new ClientInfo();
           clientInfo.clientType=ClientType.WEB_PAGE.code;//暂时设置只从网页来的客户请求
           if(clientInfo.save(flush: true)){
               clientIdentification=UniqueCode.getUniqueCode(clientInfo.id,32);//亿亿个客户后可能重复
               clientInfo.clientMark=clientIdentification;
               clientInfo.save(flush: true);
           }
       }
       //写入Cookie
        if(!StringUtil.isEmpty(clientIdentification)){
            webUtilService.writeCookie("clientIdentification",clientIdentification,365);//将cookie保存1年
        }
        //println("clientIdentification-final>"+clientIdentification);
    }
}
