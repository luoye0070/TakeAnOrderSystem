package lj.taos.filter

class TaosFilters {
    def visitDeviceService;
    def clientService;
    def webUtilService;
    def filters = {
        //访问设备判断过滤
        requestCheck(controller: '*', action: '*') {
            before = {
                println "requestCheckFilter:"+controllerName+";"+actionName+";"+params
                visitDeviceService.requestFilter(request,params);
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
        staffLogin(controller:'*', action:'*') {
            before = {
                if(
                        (controllerName.equals('staff') && (actionName in ["createOrder",'reserveTable',"orderList","orderShow","doDish","delOrder",
                                "completeDish","addDishes","cancelDish","dishList","delDish","settleAccounts","completeAffirmDish","affirmValid",
                                "affirmDish","beginCook","completeCook","index"])
                        )
                ){
                    if(!webUtilService.isStaffLoggedIn()){
                        webUtilService.session['urlBackParams']=params.clone();
                        redirect(controller:'staffManage',action:'staffLogin')
                        return false
                    }
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
