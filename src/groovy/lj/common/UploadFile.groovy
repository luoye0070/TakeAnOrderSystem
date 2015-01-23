package lj.common

import lj.internet.AppConstant
import lj.internet.FileContentType
import lj.internet.HttpsConnectionHelper
import lj.internet.analysis.RefreshTokenAnalyzeHelper
import lj.internet.analysis.UploadResultAnalyzeHelper

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-11
 * Time: 上午1:13
 * To change this template use File | Settings | File Templates.
 */
class UploadFile {
    public static String bootPath="";
    //上传文件到文件系统
    public static String uploadToFileSystem(InputStream is, String fileName) {
        String fileFullName = null;
        // 获取系统默认文件路径分隔符
        def separator = System.getProperty("file.separator");
        // 获取上传文件扩展名
        //def extension = fileName.substring(fileName.indexOf("."));
        //文件名加上时间
        fileName = new Date().getTime() + fileName;
        //文件保存路径
        //def filePath = "web-app" + separator + "uploadFile" + separator;
        def filePath = bootPath+"uploadFile" + separator;
        FileOutputStream fos = null;
        //InputStream is=null;
        try {
            fos = new FileOutputStream(filePath + fileName);
            //is=orginalFile.getInputStream();
            byte[] temp = new byte[1024];
            while (is.read(temp) > 0) {
                fos.write(temp);
                fos.flush();
            }
            //is.close();
            fos.close();
            //is = null;
            fos = null;
            fileFullName = filePath + fileName;
        } catch (IOException exception) {
            exception.printStackTrace()
        } finally {
            if (is != null) {
                //is.close()
            }
            if (fos != null) {
                fos.close()
            }
        }
//        //获取图片宽高
//        int width=0;
//        int height=0;
//        BufferedImage src = null;
//        try {
//            src = javax.imageio.ImageIO.read(is);
//            width = src.getWidth(null); // 得到源图宽
//            height=src.getHeight();
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return fileFullName;
    }
    //从文件系统删除文件
    public static boolean deleteFromFileSystem(String fileFullName) {
        try {
            File file = new File(fileFullName);
            return file.delete();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    //从文件系统下载文件
    public static int downloadFromFileSystem(String fileFullName, OutputStream os) {
        FileInputStream fis = null;
        int recode = 0;
        try {
            fis = new FileInputStream(fileFullName);
            byte[] buff = new byte[4 * 1024];
            int count = -1;
            while ((count = fis.read(buff)) != -1) {
                //output.write(buff);
                os.write(buff, 0, count);
                os.flush();
            }
        }
        catch (Exception ex) {
            recode = -1;
            ex.printStackTrace();
        }
        finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return recode;
    }
    //从文件系统下载缩略图片
    public static int downloadThumbnailFromFileSystem(String fileFullName, OutputStream os, int width, int height) {
        FileInputStream fis = null;
        int recode = 0;
        BufferedImage originalImage = null;
        try {
            println("fileFullName-->"+fileFullName);
            fis = new FileInputStream(fileFullName);
            originalImage = ImageIO.read(fis);
            int oldWidth = originalImage.width;
            int oldHeight = originalImage.height;

            //缩放
            if (oldWidth - width > oldHeight - height) {//按宽缩放
                height = oldHeight * width / oldWidth;
            } else {//按高缩放
                width = oldWidth * height / oldHeight;
            }
            //println("originalImage.getType()-->"+originalImage.getType());
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = newImage.getGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();
            //写入到输出流
            ImageIO.write(newImage, "PNG", os);
//            byte []buff=new byte[4*1024];
//            int count=-1;
//            while((count=fis.read(buff))!=-1)
//            {
//                //output.write(buff);
//                os.write(buff, 0, count);
//                os.flush();
//            }
        }
        catch (Exception ex) {
            recode = -1;
            ex.printStackTrace();
        }
        finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return recode;
    }




    private static String access_token = null;
    private static String refresh_token = null;
    private static Date refresh_time = null;
    private static String client_id = null;
    private static String client_secret = null;
    private static long expires_in = 0;
    private static String path = "";
    //获取access_token
    private static void getAT() {
        try {
            if (access_token == null || refresh_token == null || refresh_time == null || client_id == null || client_secret == null || expires_in == 0) {
                //从配置文件中获取token等信息
                System.out.println("当前路径：" + System.getProperty("user.dir"));
                Properties pt = new Properties();
                pt.load(new FileInputStream(bootPath+"token.properties"));
                access_token = pt.getProperty("access_token");
                refresh_token = pt.getProperty("refresh_token");//：必须参数，用于刷新Access Token用的Refresh Token；
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                refresh_time = sdf.parse(pt.getProperty("refresh_time"));
                client_id = pt.getProperty("client_id");
                client_secret = pt.getProperty("client_secret");
                expires_in = lj.Number.toLong(pt.getProperty("expires_in"));
                path = pt.getProperty("path");
                System.out.println(access_token + "," + refresh_token + "," + refresh_time);
            }
            //如果刷新日期到了，则刷新access_token
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(refresh_time);
            //System.out.println("shijiancha-->"+(calendar.getTimeInMillis()-calendar1.getTimeInMillis()));
            //System.out.println("25天-->"+25*24 * 3600 * 1000l);
            //if ((calendar.getTimeInMillis()-calendar1.getTimeInMillis()) >= (expires_in - 24 * 3600) * 1000) {
            if ((calendar.getTimeInMillis()-calendar1.getTimeInMillis()) >= 25*24 * 3600 * 1000l) {
                //刷新access_token
                System.out.println("刷新access_token");
                String url = "https://openapi.baidu.com/oauth/2.0/token";
                String grant_type = "refresh_token"; //：必须参数，固定为“refresh_token”；
                //String scope="";//：非必须参数。以空格分隔的权限列表，
                ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> param1 = new HashMap<String, String>();
                param1.put(AppConstant.HttpParamRe.PARAM_NAME, "grant_type");
                param1.put(AppConstant.HttpParamRe.PARAM_VALUE, grant_type);
                paramList.add(param1);
                HashMap<String, String> param2 = new HashMap<String, String>();
                param2.put(AppConstant.HttpParamRe.PARAM_NAME, "refresh_token");
                param2.put(AppConstant.HttpParamRe.PARAM_VALUE, refresh_token);
                paramList.add(param2);
                HashMap<String, String> param3 = new HashMap<String, String>();
                param3.put(AppConstant.HttpParamRe.PARAM_NAME, "client_id");
                param3.put(AppConstant.HttpParamRe.PARAM_VALUE, client_id);
                paramList.add(param3);
                HashMap<String, String> param4 = new HashMap<String, String>();
                param4.put(AppConstant.HttpParamRe.PARAM_NAME, "client_secret");
                param4.put(AppConstant.HttpParamRe.PARAM_VALUE, client_secret);
                paramList.add(param4);

                HttpsConnectionHelper hch = new HttpsConnectionHelper();
                hch.setBootPath(bootPath);
                String responseStr = hch.getResponseStr(url, paramList);
                RefreshTokenAnalyzeHelper rtah = new RefreshTokenAnalyzeHelper();
                rtah.analyze(responseStr);
                System.out.println("recode-->" + rtah.rt);
                if (rtah.rt != null) {
                    Date refresh_time_new = new Date();
                    //设置新的token等信息
                    access_token = rtah.rt.access_token;
                    refresh_token = rtah.rt.refresh_token;
                    refresh_time = refresh_time_new;
                    expires_in = rtah.rt.expires_in;

                    //将token等信息重新写入配置文件

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Properties pt = new Properties();
                    pt.setProperty("access_token", rtah.rt.access_token);
                    pt.setProperty("refresh_token", rtah.rt.refresh_token);
                    pt.setProperty("expires_in", rtah.rt.expires_in + "");
                    pt.setProperty("scope", rtah.rt.scope);
                    pt.setProperty("session_key", rtah.rt.session_key);
                    pt.setProperty("session_secret", rtah.rt.session_secret);
                    if (client_id == null) {
                        client_id = "";
                    }
                    pt.setProperty("client_id", client_id);
                    if (client_secret == null) {
                        client_secret = "";
                    }
                    pt.setProperty("client_secret", client_secret);
                    pt.setProperty("refresh_time", sdf.format(refresh_time));
                    if (path == null) {
                        path = "";
                    }
                    pt.setProperty("path", path);
                    //pt.save(new FileOutputStream("token.properties"),"token配置");
                    pt.store(new FileOutputStream("token.properties"), "token配置");
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //上传文件到云盘
    public static String uploadToYuPan(InputStream is, String fileName) {
        String fileFullName = null;
        try {
            getAT();//先获取token
            String url = "https://c.pcs.baidu.com/rest/2.0/pcs/file";
            String method = "upload";
            //String path="/apps/canmeng1/lzg/";
            //文件名加上时间
            fileName = new Date().getTime() + fileName;

            ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> param1 = new HashMap<String, String>();
            param1.put(AppConstant.HttpParamRe.PARAM_NAME, "method");
            param1.put(AppConstant.HttpParamRe.PARAM_VALUE, method);
            paramList.add(param1);
            HashMap<String, String> param2 = new HashMap<String, String>();
            param2.put(AppConstant.HttpParamRe.PARAM_NAME, "access_token");
            param2.put(AppConstant.HttpParamRe.PARAM_VALUE, access_token);
            paramList.add(param2);
            HashMap<String, String> param3 = new HashMap<String, String>();
            param3.put(AppConstant.HttpParamRe.PARAM_NAME, "path");
            param3.put(AppConstant.HttpParamRe.PARAM_VALUE, path + fileName);
            paramList.add(param3);
            HashMap<String, String> param4 = new HashMap<String, String>();
            param4.put(AppConstant.HttpParamRe.PARAM_NAME, "ondup");
            param4.put(AppConstant.HttpParamRe.PARAM_VALUE, "newcopy");
            paramList.add(param4);

            HttpsConnectionHelper hch = new HttpsConnectionHelper();
            hch.setBootPath(bootPath);
            hch.setOuttime(30000);
            int recode = hch.uploadFile(url, paramList, fileName, is, "file", FileContentType.IMAGE_JPEG);
            //is.close();
            //is = null;
            if (recode != AppConstant.VisitServerResultCode.RESULT_CODE_OK) {
                return null;
            }
            UploadResultAnalyzeHelper urah = new UploadResultAnalyzeHelper();
            urah.analyze(hch.getResultStr());
            if (urah.ur == null) {
                return null;
            }
            System.out.println("recode-->" + urah.ur);
            fileFullName = urah.ur.path;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileFullName;
    }

    //从云盘删除文件
    public static boolean deleteFromYuPan(String fileFullName) {
        getAT();//先获取token
        //下载一个文件
        String url = "https://pcs.baidu.com/rest/2.0/pcs/file";
        String method = "delete";

        ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> param1 = new HashMap<String, String>();
        param1.put(AppConstant.HttpParamRe.PARAM_NAME, "method");
        param1.put(AppConstant.HttpParamRe.PARAM_VALUE, method);
        paramList.add(param1);
        HashMap<String, String> param2 = new HashMap<String, String>();
        param2.put(AppConstant.HttpParamRe.PARAM_NAME, "access_token");
        param2.put(AppConstant.HttpParamRe.PARAM_VALUE, access_token);
        paramList.add(param2);
        HashMap<String, String> param3 = new HashMap<String, String>();
        param3.put(AppConstant.HttpParamRe.PARAM_NAME, "path");
        param3.put(AppConstant.HttpParamRe.PARAM_VALUE, fileFullName);
        paramList.add(param3);

        HttpsConnectionHelper hch = new HttpsConnectionHelper();
        hch.setBootPath(bootPath);
        String responseStr = hch.getResponseStr(url, paramList);
        System.out.println("responseStr-->" + responseStr + ";recode-->" + hch.getResultCode());
        int recode = hch.getResultCode();
        if (recode == AppConstant.VisitServerResultCode.RESULT_CODE_OK) {
            return true;
        } else {
            return false;
        }
    }
//    //获取云盘文件下载路径
//    public static String getUrlFromYuPan(String fileFullName) {
//
//    }
    //从云盘下载文件
    public static int downloadFromYunPan(String fileFullName, OutputStream os) {
        getAT();//先获取token
        //下载一个文件
        String url = "https://d.pcs.baidu.com/rest/2.0/pcs/file";
        String method = "download";

        ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> param1 = new HashMap<String, String>();
        param1.put(AppConstant.HttpParamRe.PARAM_NAME, "method");
        param1.put(AppConstant.HttpParamRe.PARAM_VALUE, method);
        paramList.add(param1);
        HashMap<String, String> param2 = new HashMap<String, String>();
        param2.put(AppConstant.HttpParamRe.PARAM_NAME, "access_token");
        param2.put(AppConstant.HttpParamRe.PARAM_VALUE, access_token);
        paramList.add(param2);
        HashMap<String, String> param3 = new HashMap<String, String>();
        param3.put(AppConstant.HttpParamRe.PARAM_NAME, "path");
        param3.put(AppConstant.HttpParamRe.PARAM_VALUE, fileFullName);
        paramList.add(param3);

        HttpsConnectionHelper hch = new HttpsConnectionHelper();
        hch.setBootPath(bootPath);
        int recode = hch.downloadFile(url, paramList, os);
        System.out.println("recode-->" + recode);
        return recode;
    }
    //从云盘下载缩略图
    public static int downloadThumbnailFromYunPan(String fileFullName, OutputStream os, int width, int height) {
        getAT();//先获取token
        //下载一个文件
        String url = "https://pcs.baidu.com/rest/2.0/pcs/thumbnail";
        String method = "generate";

        ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> param1 = new HashMap<String, String>();
        param1.put(AppConstant.HttpParamRe.PARAM_NAME, "method");
        param1.put(AppConstant.HttpParamRe.PARAM_VALUE, method);
        paramList.add(param1);
        HashMap<String, String> param2 = new HashMap<String, String>();
        param2.put(AppConstant.HttpParamRe.PARAM_NAME, "access_token");
        param2.put(AppConstant.HttpParamRe.PARAM_VALUE, access_token);
        paramList.add(param2);
        HashMap<String, String> param3 = new HashMap<String, String>();
        param3.put(AppConstant.HttpParamRe.PARAM_NAME, "path");
        param3.put(AppConstant.HttpParamRe.PARAM_VALUE, fileFullName);
        paramList.add(param3);
        HashMap<String, String> param4 = new HashMap<String, String>();
        param4.put(AppConstant.HttpParamRe.PARAM_NAME, "quality");
        param4.put(AppConstant.HttpParamRe.PARAM_VALUE, "100");
        paramList.add(param4);
        HashMap<String, String> param5 = new HashMap<String, String>();
        param5.put(AppConstant.HttpParamRe.PARAM_NAME, "width");
        param5.put(AppConstant.HttpParamRe.PARAM_VALUE, width + "");
        paramList.add(param5);
        HashMap<String, String> param6 = new HashMap<String, String>();
        param6.put(AppConstant.HttpParamRe.PARAM_NAME, "height");
        param6.put(AppConstant.HttpParamRe.PARAM_VALUE, height + "");
        paramList.add(param6);

        HttpsConnectionHelper hch = new HttpsConnectionHelper();
        hch.setBootPath(bootPath);
        int recode = hch.downloadFile(url, paramList, os);
        System.out.println("recode-->" + recode);
        return recode;
    }
//    //上传图片
//    public static String upload(InputStream is,String fileName){
//
//    }
//    //删除图片
//    public static boolean delete(String fileFullName){
//
//    }
//    //获取图片URL
//    public static String getUrl(String fileFullName){
//
//    }
}
