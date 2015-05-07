package lj.taos.shop

import lj.enumCustom.ReCode

//菜单管理
class FoodManageController {
    FoodManageService foodManageService;
    def foodClassManageService;
    def index() {
        redirect(action: "foodList");
    }

    //菜单列表
    def foodList(){
        def errors=null;
        def msgs=null;

        def reInfo=foodManageService.foodList(params);

        render(view: "foodList",model: reInfo);
    }
    //菜单编辑
    def editFoodInfo(){
        def errors=null;
        def msgs=null;
        def foodInfoInstance=null;
        def foodClassList=null;

        def reInfo=foodClassManageService.list(params);
        if(reInfo.recode==ReCode.OK){
            foodClassList=reInfo.foodClassInfoInstanceList;
        }
        if(request.method=="GET"){
            if(!params.id){
                params.id=-1;
            }
            reInfo=foodManageService.getFoodInfo(params); //根据ID查询到一个菜单
            if(reInfo.recode==ReCode.OK){
                foodInfoInstance=reInfo.foodInfo;
            }

        }
        else if(request.method=="POST"){
            params.id=params.foodId;
            reInfo=foodManageService.save(params);
            if(reInfo.recode==ReCode.OK){
                msgs="保存成功";
            }
            else if(reInfo.recode==ReCode.SAVE_FAILED){
                errors=reInfo.errors;
            }
            else {
                errors=reInfo.recode.label;
            }
            foodInfoInstance=reInfo.foodInfo;
        }

        render(view: "editFoodInfo",model: [foodInfoInstance:foodInfoInstance,foodClassList:foodClassList,errors:errors,msgs:msgs]);
    }
    //菜单删除
    def delFoodInfo(){
        def reInfo=foodManageService.deleteFoodInfo(params);
        if(reInfo.recode==ReCode.OK){//删除成功
            flash.message=reInfo.recode.label;
        }
        else{
            flash.errors=reInfo.recode.label;
        }
        redirect(controller: "foodManage",action: "foodList");
    }
}
