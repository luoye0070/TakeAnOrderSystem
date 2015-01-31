package lj.taos.shop

import lj.enumCustom.ReCode

class ImageSpaceController {
    ImageSpaceService imageSpaceService;
    ShopService shopService;
    def index() {redirect(action: "imageList") }

    //上传图片
    def upload(){
        def errors=null;
        def msgs=null;
        def reInfo=null;

        if(request.method=="POST"){
            println("params-->"+params);
            reInfo=imageSpaceService.upload(params,request);
            if(reInfo.recode==ReCode.OK){
                msgs="图片上传成功";
                redirect(action:"imageList")
                return
            }
            else if(reInfo.recode==ReCode.SAVE_FAILED){
                errors=reInfo.errors;
            }
            else {
                errors=reInfo.recode.label;
            }
        }

        //获取类别列表
        def imgClassList=[];
        imgClassList.add([id:0,name:"请选择"]);
        def imgClassListTemp=imageSpaceService.searchClass().classList;
        if(imgClassListTemp){
            imgClassListTemp.each {
                imgClassList.add([id:it.id,name:it.name]);
            }
        }
        render(view: "upload",model: [imgClassList:imgClassList,msgs:msgs,errors:errors]);

    }

    //图片放入回收站
    def recycleToRecycled(){
        def errors=null;
        def msgs=null;
        params.isDel="true";
        def reInfo=imageSpaceService.recycleOrRecover(params);
        if(reInfo.recode==ReCode.OK){
            msgs="移入回收站成功";
        }
        else{
            errors=reInfo.recode.label;
        }
        redirect(controller: "imageSpace",action: "imageList",msgs:msgs,errors:errors);
    }
    //图片从回收站中恢复
    def recoverFromRecycled(){
        def errors=null;
        def msgs=null;
        params.isDel="false";
        def reInfo=imageSpaceService.recycleOrRecover(params);
        if(reInfo.recode==ReCode.OK){
            flash.message="恢复成功";
        }
        else{
            flash.errors=reInfo.recode.label;
        }
        redirect(controller: "imageSpace",action: "imageList");
    }
    //删除图片
    def delImage(){
        def reInfo=imageSpaceService.deleteImage(params);
        if(reInfo.recode==ReCode.OK){
            flash.message="删除成功";
        }
        else{
            flash.errors=reInfo.recode.label;
        }
        redirect(controller: "imageSpace",action: "imageList");
    }

    //图片列表
    def imageList(){
        def errors=null;
        def msgs=null;
        def reInfo=null;
        def imageList=null;
        def totalCount=0;
        def restaurantInfo=null;

        //获取图片列表
        reInfo=imageSpaceService.search(params);
        if(reInfo.recode==ReCode.OK){//成功
            imageList=reInfo.imageList;
            totalCount=reInfo.totalCount;
        }else {
            errors=reInfo.recode.label;
        }

        //获取店铺信息
        reInfo=shopService.getShopEnabled();
        if(reInfo.recode==ReCode.OK){
            restaurantInfo=reInfo.restaurantInfo;
        }
        else{
            errors=reInfo.recode.label;
        }

        //获取类别列表
        def imgClassList=[];
        imgClassList.add([id:0,name:"请选择"]);
        def imgClassListTemp=imageSpaceService.searchClass().classList;
        if(imgClassListTemp){
            imgClassListTemp.each {
                imgClassList.add([id:it.id,name:it.name]);
            }
        }

        render(view: "imageList",model: [errors:errors,msgs: msgs,imageList:imageList,totalCount:totalCount,restaurantInfo:restaurantInfo,imgClassList:imgClassList,params:params])

    }

    //图片分类列表
    def imageClassList(){
        def errors=null;
        def msgs=null;

        def reInfo=imageSpaceService.searchClass();

        render(view: "imageClassList",model: reInfo);
    }
    //图片分类保存
    def editImageClass(){
        def errors=null;
        def msgs=null;
        def reInfo=null;
        def imageClassInfo=null;

        if(request.method=="GET"){
            reInfo=imageSpaceService.getClass(params); //根据ID查询到一个图片类别
            if(reInfo.recode==ReCode.OK){
                imageClassInfo=reInfo.imageClassInfo;
            }
        }
        else if(request.method=="POST"){
            params.id=params.classId;
            reInfo=imageSpaceService.saveClass(params);
            if(reInfo.recode==ReCode.OK){
                msgs="图片分类保存成功";
            }
            else if(reInfo.recode==ReCode.SAVE_FAILED){
                errors=reInfo.errors;
            }
            else {
                errors=reInfo.recode.label;
            }
            imageClassInfo=reInfo.imageClassInfo;
        }

        render(view: "editImageClass",model: [imageClassInfo:imageClassInfo,msgs:msgs,errors:errors]);
    }
    //图片分类删除
    def delImageClass(){
        def reInfo=imageSpaceService.deleteClass(params);
        if(reInfo.recode==ReCode.OK){//删除成功
            flash.message=reInfo.recode.label;
        }
        else{
            flash.errors=reInfo.recode.label;
        }
        redirect(controller: "imageSpace",action: "imageClassList");
    }

    //选择图片
    def selectImage(){
        def errors=null;
        def msgs=null;
        def reInfo=null;
        def imageList=null;
        def totalCount=0;

        //获取图片列表
        reInfo=imageSpaceService.search(params);
        if(reInfo.recode==ReCode.OK){//成功
            imageList=reInfo.imageList;
            totalCount=reInfo.totalCount;
        }else {
            errors=reInfo.recode.label;
        }

        //获取类别列表
        def imgClassList=[];
        imgClassList.add([id:0,name:"请选择"]);
        def imgClassListTemp=imageSpaceService.searchClass().classList;
        if(imgClassListTemp){
            imgClassListTemp.each {
                imgClassList.add([id:it.id,name:it.name]);
            }
        }

        render(view: "selectImage",model: [errors:errors,msgs: msgs,imageList:imageList,totalCount:totalCount,imgClassList:imgClassList,params:params])

    }
}
