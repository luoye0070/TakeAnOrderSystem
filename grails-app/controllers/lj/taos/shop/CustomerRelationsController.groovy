package lj.taos.shop

import grails.converters.JSON
import lj.enumCustom.CustomerRelationsType
import lj.enumCustom.ReCode

class CustomerRelationsController {
    def customerRelationsService;
    def index() {
        redirect(action: "crList");
    }

    //客户关系列表
    def crList(){
        def errors=null;
        def msgs=null;

        def reInfo=customerRelationsService.search(params);

        render(view: "crList",model: reInfo);
    }

    //编辑客户关系
    def editCr(){
        def errors=null;
        def msgs=null;
        def customerRelationsInstance=null;
        if(request.method=="GET"){
            def reInfo=customerRelationsService.getCrInfo(params); //根据ID查询到一个客户关系
            if(reInfo.recode==ReCode.OK){
                customerRelationsInstance=reInfo.customerRelations;
            }
        }
        else if(request.method=="POST"){
            params.id=params.crId;
            def reInfo=customerRelationsService.save(params);
            if(reInfo.recode==ReCode.OK){
                msgs="保存成功";
            }
            else if(reInfo.recode==ReCode.SAVE_FAILED){
                errors=reInfo.errors;
            }
            else {
                errors=reInfo.recode.label;
            }
            customerRelationsInstance=reInfo.customerRelations;
        }

        //从订单中获取客户列表
        def userList=null;
        def reInfo=customerRelationsService.getCustomerFromOrder(params);
        if(reInfo.recode==ReCode.OK){
            userList=reInfo.userList;
        }
        println("userList--->"+userList);
        //获取客户类型列表
        def customerRelationsTypes=CustomerRelationsType.customerRelationsTypes;
        render(view: "editCr",model: [customerRelationsInstance:customerRelationsInstance,errors:errors,msgs:msgs,userList:userList,customerRelationsTypes:customerRelationsTypes]);
    }

    //删除客户关系
    def delCr(){
        def reInfo=customerRelationsService.delete(params);
        if(reInfo.recode==ReCode.OK){//删除成功
            flash.message=reInfo.recode.label;
        }
        else{
            flash.errors=reInfo.recode.label;
        }
        redirect(controller: "customerRelations",action: "crList");
    }

    //从订单中获取客户信息
    def cListFromOrderAsJson(){
        def errors=null;
        def msgs=null;

        def reInfo=customerRelationsService.getCustomerFromOrder(params);

        render reInfo as JSON;
    }


}
