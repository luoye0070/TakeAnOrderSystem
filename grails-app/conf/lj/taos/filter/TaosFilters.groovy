package lj.taos.filter

import lj.enumCustom.ReCode

class TaosFilters {
    def visitDeviceService;
    def clientService;
    def webUtilService;
    def staffManageService;
    def remoteShopService;
    def shopService;
    def filters = {
        //访问检查License是否有效
        requestCheckLicens(controller: '*', action: '*') {
            before = {
                if(
                        !(controllerName.equals('shop')&&(actionName in ["shopCreate"]))
                ){
                    println "requestCheckLicensFilter:"+controllerName+";"+actionName+";"+params
                    def reInfo=shopService.getShopInfo();
                    if(reInfo.recode== ReCode.OK){
                         reInfo=remoteShopService.getLicense(reInfo.restaurantInfo);
                        if(reInfo.recode==ReCode.OK){
                            return true;
                        }
                        else{
                            render(text: reInfo.recode.label);
                            return false;
                        }
                    }else{
                        render(text: reInfo.recode.label);
                        return false;
                    }
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        //访问设备判断过滤
        requestCheck(controller: '*', action: '*') {
            before = {
                println "requestCheckFilter:"+controllerName+";"+actionName+";"+params
                visitDeviceService.requestFilter(request,params);
                //顺带做一下过期标注
                //orderAndReserveService.markExpire();
                return true;
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        //客户访问
        clientRequest(controller: "*",action: "*"){
            before = {
                println "clientRequestFilter:"+controllerName+";"+actionName+";"+params
                clientService.setClient();
                return true;
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        //员工登录
        staffLogin(controller:'*', action:'*') {
            before = {
                if(
                        (controllerName.equals('staff') && (actionName in ["createOrder",'reserveTable',"orderList","orderShow","doDish","delOrder",
                                "completeDish","addDishes","cancelDish","dishList","delDish","settleAccounts","completeAffirmDish","affirmValid",
                                "affirmDish","beginCook","completeCook","index"])
                        )
                ){
                    println "staffLoginFilter:"+controllerName+";"+actionName+";"+params
                    if(!webUtilService.isStaffLoggedIn()){
                        webUtilService.session['urlBackParams']=params.clone();
                        redirect(controller:'staffManage',action:'staffLogin')
                        return false
                    }
                }
                return true;
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        //需要店主登录
        masterLogin(controller:'*', action:'*') {
            before = {
                if((controllerName.equals('shop')&&(actionName in ["editShopInfo"]))
                    ||controllerName.equals('tableManage')
                    ||controllerName.equals('foodClassInfo')
                    ||controllerName.equals('foodManage')
                    ||controllerName.equals('imageSpace')
                    ||(controllerName.equals('staffManage')&&(actionName in ["editStaffInfo",'staffList',"delStaff"]))
                    ||controllerName.equals('customerRelations')
                ){
                    println "masterLoginFilter:"+controllerName+";"+actionName+";"+params
                    if(!staffManageService.isMasterLogin()){
                        flash.errors="店主才能操作，如需操作请用店主账号登录!";
                        webUtilService.session['urlBackParams']=params.clone();
                        redirect(controller:'staffManage',action:'staffLogin')
                        return false
                    }
                }
                return true;
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
