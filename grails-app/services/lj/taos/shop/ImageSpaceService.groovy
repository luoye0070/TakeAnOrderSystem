package lj.taos.shop

import lj.I18nError
import lj.common.UploadFile
import lj.data.ImageClassInfo
import lj.data.ImageInfo
import lj.enumCustom.ReCode
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

class ImageSpaceService {
    def webUtilService;
    def shopService;
    def uploadFileService;
    //def config = org.codehaus.groovy.grails.commons.ConfigurationHolder.getConfig();
    def grailsApplication;
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    def serviceMethod() {

    }

    /***************
     * 上传图片
     *
     * params是传入的参数
     * 参数格式为：
     * classId 类别ID，非必须，默认为0无分类
     * imgfile 图片文件，必须
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode: ReCode.SAVE_FAILED,errors:staffInfo.errors.allErrors];保存保存图片信息失败
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];保存保存图片信息成功
     * [recode: ReCode.OTHER_ERROR];未知错误
     * [recode:  ReCode.NO_IMAGE_SPACE];没有足够的图片空间
     * **********************/
    def upload(def params, def request) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }
        //检查图片空间是否够用
        if (reInfo.restaurantInfo.imageSpaceUsedSize >= reInfo.restaurantInfo.imageSpaceSize) {//图片空间已经用完
            return [recode: ReCode.NO_IMAGE_SPACE];
        }
        //保存图片到文件或云盘
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            CommonsMultipartFile orginalFile = (CommonsMultipartFile) multiRequest.getFile("imgfile");
            //判断是否上传的是图片
            String type = orginalFile.contentType;
            if (type == null || type.indexOf("image/") < 0) {//上传的不是图片
                return [recode: ReCode.NOT_A_IMAGE];
            }
            // 判断是否上传文件
            if (orginalFile != null && !orginalFile.isEmpty()) {
                // 获取原文件名称
                String originalFilename = orginalFile.getOriginalFilename();
                //上传文件到文件系统
                //String fileFullName=UploadFile.uploadToFileSystem(orginalFile.getInputStream(),originalFilename);
                String fileFullName = uploadFileService.upload(orginalFile.getInputStream(), originalFilename);
                println("fileFullName-->" + fileFullName);
                if (fileFullName) {
                    //保存图片信息到数据表中
                    params.url = fileFullName;
                    params.width = 0;//这里先不保存宽高了
                    params.height = 0;
                    params.size = orginalFile.size;
                    params.fileName = originalFilename;
                    //params<<["imageClassInfo.id":lj.Number.toLong(params.classId)];
                    if (fileFullName.indexOf("/apps") == 0) {
                        params.savePlace = 1;//上传到百度云盘
                    } else {
                        params.savePlace = 0;//上传到本地文件系统
                    }
                    ImageInfo imageInfo = new ImageInfo(params);
                    imageInfo.imageClassInfo = ImageClassInfo.get(lj.Number.toLong(params.classId));
                    println("ImageClassInfo.get(lj.Number.toLong(params.classId))-->" + ImageClassInfo.get(lj.Number.toLong(params.classId)));
                    if (imageInfo.save(flush: true)) {
                        //增加饭店图片空间的使用量
                        reInfo.restaurantInfo.imageSpaceUsedSize += orginalFile.size;
                        reInfo.restaurantInfo.save(flush: true);
                        return [recode: ReCode.OK];
                    } else {
                        return [recode: ReCode.SAVE_FAILED, errors:  I18nError.getMessage(g,imageInfo.errors.allErrors)];
                    }
                } else {
                    return [recode: ReCode.SAVE_FILE_FAILED];
                }
            } else {
                return [recode: ReCode.ERROR_PARAMS];
            }
        } else {
            println "No multipart";
            return [recode: ReCode.SUBMIT_FORMAT_ERROR];
        }

    }

    //图片下载
    def download(def params, def request, def response) {
        //response.setHeader("Content-disposition", "attachment; filename=" + params.url);
        //防盗链处理
        String Referer = request.getHeader("Referer");
        String baseUrl = null;
        def reInfo = shopService.getShopInfo();
        if (ReCode.OK == reInfo.recode) {
            baseUrl = reInfo.restaurantInfo?.baseUrl;
        }
        if (baseUrl == null) {
            baseUrl = grailsApplication.config.grails.baseurls.baseUrl;
        }
        if (baseUrl==null||(Referer != null && Referer.indexOf(baseUrl) < 0)) {//盗链
            println("图片盗链");
            return;
        }
        //根据ID查询出图片url
        String url_str = params.imgUrl;
        long id = lj.Number.toLong(params.id);
        if (id > 0) {
            ImageInfo imageInfo = ImageInfo.get(id);
            if (imageInfo) {
                url_str = imageInfo.url;
            }
        }
        if (url_str) {
            response.contentType = "image/jpeg";
            def out = response.outputStream;
            uploadFileService.download(url_str, out);
            out.flush();
            out.close();
        } else {//显示一张默认图片
            response.contentType = "image/jpeg";
            def out = response.outputStream;
            UploadFile.downloadFromFileSystem("web-app/images/no_image.jpg", out);
            out.flush();
            out.close();
        }
    }
    //缩略图片下载
    def downloadThumbnail(def params, def request, def response) {
        //response.setHeader("Content-disposition", "attachment; filename=" + params.url);
        //防盗链处理
        String Referer = request.getHeader("Referer");
        String baseUrl = null;
        def reInfo = shopService.getShopInfo();
        if (ReCode.OK == reInfo.recode) {
            baseUrl = reInfo.restaurantInfo?.baseUrl;
        }
        if (baseUrl == null) {
            baseUrl = grailsApplication.config.grails.baseurls.baseUrl;
        }
        if (baseUrl==null||(Referer != null && Referer.indexOf(baseUrl) < 0)) {//盗链
            println("图片盗链");
            return;
        }
        //根据ID查询出图片url
        int width = 100;
        int height = 100;
        if (params.width)
            width = lj.Number.toInteger(params.width);
        if (params.height)
            height = lj.Number.toInteger(params.height);
        String url_str = params.imgUrl;
        long id = lj.Number.toLong(params.id);
        if (id > 0) {
            ImageInfo imageInfo = ImageInfo.get(id);
            if (imageInfo) {
                url_str = imageInfo.url;
            }
        }
        if (url_str) {
            response.contentType = "image/jpeg";
            def out = response.outputStream;
            uploadFileService.downloadThumbnail(url_str, out, width, height);
            //UploadFile.downloadThumbnailFromFileSystem("web-app/images/grails_logo.png",out,width,height);//测试
            out.flush();
            out.close();
        } else {//显示一张默认图片
            response.contentType = "image/jpeg";
            def out = response.outputStream;
            UploadFile.downloadFromFileSystem("web-app/images/no_image.jpg", out);
            out.flush();
            out.close();
        }
    }
    /***************
     * 图片查找
     *
     * params是传入的参数
     * 参数格式为：
     * id 图片ID，非必须
     * classId 类别ID，非必须
     * isDel，非必须，默认false
     * offset,非必须
     * max,非必须
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK,imageList:imageList,totalCount:totalCount];查询成功
     * **********************/
    def search(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //获取参数
        long id = lj.Number.toLong(params.id);//图片信息ID
        long classId = lj.Number.toLong(params.classId);//图片所在的类别ID
        boolean isDel = false;
        if (params.isDel == "true")
            isDel = true;
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        if (!params.max) {
            params.max = 10
        }
        if (!params.offset) {
            params.offset = 0;
        }
        //查询图片
        def condition = {
            eq("isDel", isDel);
            if (id) {
                eq("id", id);
            }
            if (classId) {
                eq("imageClassInfo.id", classId);
                //sqlRestriction("image_class_info_id = "+classId);
            }
        }

        def imageList = ImageInfo.createCriteria().list(params, condition);
        def totalCount = ImageInfo.createCriteria().count(condition);

        return [recode: ReCode.OK, imageList: imageList, totalCount: totalCount];
    }

    /***************
     * 图片删除状态改变,放回收站或恢复
     *
     * params是传入的参数
     * 参数格式为：
     * ids 图片ID数组，必须
     * isDel，非必须，默认false
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.SAVE_FAILED,errors:imageInfo.errors.allErrors];保存数据失败
     * [recode: ReCode.OTHER_ERROR];未知错误
     * [recode: ReCode.OK];成功
     * [recode: ReCode.ERROR_PARAMS];提交的参数错误
     * **********************/
    def recycleOrRecover(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //获取参数
        boolean isDel = false; //图片回收
        if (params.isDel == "true")
            isDel = true;
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        if (params.ids instanceof String) { //传入id
            //获取参数
            long id = lj.Number.toLong(params.ids);//图片信息ID
            //更新图片删除状态
            ImageInfo imageInfo = ImageInfo.findById(id);
            if (imageInfo) {
                imageInfo.isDel = isDel;
                if (imageInfo.save(flush: true)) {
                    return [recode: ReCode.OK];
                } else {
                    return [recode: ReCode.SAVE_FAILED, errors: I18nError.getMessage(g, imageInfo.errors.allErrors)];
                }
            }
        } else if (params.ids instanceof String[]) {//传入id数组
            String sql_s = "update ImageInfo set isDel=" + isDel + " where id in (0";
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                sql_s += "," + id;
            }
            sql_s += ")";
            if (ImageInfo.executeUpdate(sql_s))
                return [recode: ReCode.OK];
            else
                return [recode: ReCode.OTHER_ERROR];
        }
        return [recode: ReCode.ERROR_PARAMS];
    }

    /***************
     * 图片删除
     *
     * params是传入的参数
     * 参数格式为：
     * ids 图片ID数组，必须
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.NO_RESULT];图片不存在
     * [recode: ReCode.OK];成功
     * **********************/
    def deleteImage(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }
        def idList = [];
        if (params.ids instanceof String) { //传入id
            //获取参数
            long id = lj.Number.toLong(params.ids);//图片信息ID
            idList.add(id);
        } else if (params.ids instanceof String[]) {//传入id数组
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                idList.add(id);
            }
        }

        //查询出图片
        def imageList = ImageInfo.findAllByIdInList(idList);
        if (imageList) {
            imageList.each {
                //UploadFile.deleteFromFileSystem(it.url);
                if (uploadFileService.delete(it.url))
                    it.delete(flush: true);
                println("删除图片成功");
            }
            return [recode: ReCode.OK];
        } else {
            return [recode: ReCode.NO_RESULT];
        }
    }

    /***************
     * 图片类别保存
     *
     * params是传入的参数
     * 参数格式为：
     * id 图片类别ID，非必须，如果id传入了的话，下面的参数都是非必须的
     * name 类别名，必须
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode: ReCode.SAVE_FAILED,errors:staffInfo.errors.allErrors];保存保存图片分类失败
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];保存保存图片分类成功
     * **********************/
    def saveClass(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取参数
        Long id = lj.Number.toLong(params.id);

        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        ImageClassInfo imageClassInfo = null;
        if (id) {
            imageClassInfo = ImageClassInfo.get(id);
        }
        if (imageClassInfo) {
            imageClassInfo.setProperties(params);
        } else {
            imageClassInfo = new ImageClassInfo(params);
        }
        if (imageClassInfo.save(flush: true)) {
            return [recode: ReCode.OK, imageClassInfo: imageClassInfo];
        } else
            return [recode: ReCode.SAVE_FAILED, imageClassInfo: imageClassInfo, errors: I18nError.getMessage(g, imageClassInfo.errors.allErrors)];
    }

    /***************
     * 图片类别删除
     *
     * params是传入的参数
     * 参数格式为：
     * ids 图片类别ID数组，必须
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OTHER_ERROR];未知错误
     * [recode: ReCode.OK];成功
     * [recode: ReCode.ERROR_PARAMS];提交的参数错误
     * **********************/
    def deleteClass(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        if (params.ids instanceof String) { //传入id
            //获取参数
            long id = lj.Number.toLong(params.ids);//图片信息ID
            //删除图片类别
            String sql_s = "delete from ImageClassInfo where id=" + id;
            if (ImageClassInfo.executeUpdate(sql_s))
                return [recode: ReCode.OK];
            else
                return [recode: ReCode.OTHER_ERROR];
        } else if (params.ids instanceof String[]) {//传入id数组
            String sql_s = "delete from ImageClassInfo where id in (0";
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                sql_s += "," + id;
            }
            sql_s += ")";
            if (ImageClassInfo.executeUpdate(sql_s))
                return [recode: ReCode.OK];
            else
                return [recode: ReCode.OTHER_ERROR];
        }
        return [recode: ReCode.ERROR_PARAMS];
    }

    /***************
     * 图片类别查找
     *
     * params是传入的参数
     * 参数格式为：暂无
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode: ReCode.NO_RESULT];没有记录
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK,classList:classList];查询成功
     * **********************/
    def searchClass() {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        def classList = ImageClassInfo.findAll();
        if (classList) {
            return [recode: ReCode.OK, classList: classList];
        } else {
            return [recode: ReCode.NO_RESULT];
        }
    }

    def getClass(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        long id = lj.Number.toLong(params.id);//图片类别
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        long restaurantId = 0;
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        ImageClassInfo imageClassInfo = ImageClassInfo.findById(id);
        if (imageClassInfo) {
            return [recode: ReCode.OK, imageClassInfo: imageClassInfo];
        } else {
            return [recode: ReCode.NO_RESULT];
        }
    }
}
