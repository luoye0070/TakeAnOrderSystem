package lj.util

import lj.common.UploadFile


class UploadFileService {
    WebUtilService webUtilService;
    def serviceMethod() {

    }

    //上传图片
    public String upload(InputStream is,String fileName){
        if(UploadFile.bootPath==""){
            UploadFile.bootPath=webUtilService.getServletContext().getRealPath("/");
        }
        //return UploadFile.uploadToFileSystem(is,fileName);
        //return UploadFile.uploadToYuPan(is,fileName);
        //将文件名限定在64个字符内
        if(fileName.length()>64){
            fileName=fileName.substring(fileName.length()-64);
        }
        BufferedInputStream bis=new BufferedInputStream(is);
        bis.mark(bis.available()+1);
        String fileFullName= UploadFile.uploadToYuPan(bis,fileName);
        if(fileFullName==null){//上传到云盘失败,则上传到服务器文件系统
            bis.reset();
            fileFullName=UploadFile.uploadToFileSystem(bis,fileName);
        }
        bis.close();
        bis=null;
        return fileFullName;
    }
    //删除图片
    public boolean delete(String fileFullName){
        if(UploadFile.bootPath==""){
            UploadFile.bootPath=webUtilService.getServletContext().getRealPath("/");
        }
        if(fileFullName.indexOf("/apps")==0){ //云盘
            return UploadFile.deleteFromYuPan(fileFullName);
        }
        else {//本地
            return UploadFile.deleteFromFileSystem(fileFullName);
        }
        //return UploadFile.deleteFromFileSystem(fileFullName);
        //return UploadFile.deleteFromYuPan(fileFullName);
    }
    //获取图片URL
    public String getUrl(String fileFullName){

    }
    //图片下载
    public int download(String fileFullName,OutputStream os){
        if(UploadFile.bootPath==""){
            UploadFile.bootPath=webUtilService.getServletContext().getRealPath("/");
        }
        if(fileFullName.indexOf("/apps")==0){ //云盘
            return UploadFile.downloadFromYunPan(fileFullName,os);
        }
        else {//本地
            return UploadFile.downloadFromFileSystem(fileFullName,os);
        }
        //return UploadFile.downloadFromFileSystem(fileFullName,os);
        //return UploadFile.downloadFromYunPan(fileFullName,os);
    }
    //缩略图片下载
    public int downloadThumbnail(String fileFullName,OutputStream os,int width,int height){
        if(UploadFile.bootPath==""){
            UploadFile.bootPath=webUtilService.getServletContext().getRealPath("/");
        }
        if(fileFullName.indexOf("/apps")==0){ //云盘
            return UploadFile.downloadThumbnailFromYunPan(fileFullName,os,width,height);
        }
        else {//本地
            return UploadFile.downloadThumbnailFromFileSystem(fileFullName,os,width,height);
        }
        //return UploadFile.downloadThumbnailFromFileSystem(fileFullName,os,width,height);
        //return UploadFile.downloadThumbnailFromYunPan(fileFullName,os,width,height);
    }
}
