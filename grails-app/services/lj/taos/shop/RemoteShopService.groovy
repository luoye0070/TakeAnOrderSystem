package lj.taos.shop

import grails.converters.JSON
import groovy.json.JsonSlurper
import lj.TypeConversion
import lj.common.DesUtilGy
import lj.data.RestaurantInfo
import lj.enumCustom.ReCode
import lj.internet.AppConstant
import lj.internet.FileReadWrite
import lj.internet.HttpConnectionHelper
import lj.internet.model.LicenseResult

import java.lang.reflect.Field
import java.text.SimpleDateFormat

class RemoteShopService {
    def grailsApplication;
    def updateShopInfo(RestaurantInfo restaurantInfo){
        if(restaurantInfo==null){
            return [recode: ReCode.NO_RESTAURANTINFO];
        }

        String url = grailsApplication.config.grails.resaurant.update.url;//"http://localhost/TaosServer/restaurant/update";
        //String scope="";//：非必须参数。以空格分隔的权限列表，
        ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
        Class restaurantInfoClass=restaurantInfo.getClass();
        String className=restaurantInfoClass.getSimpleName();
        System.out.println("is DataModel:"+className);
        Field[] fields=restaurantInfoClass.getDeclaredFields();
        for(Field field:fields){
            try {
                String fieldType=field.getType().getSimpleName();
                System.out.println("fieldType Name-->"+field.getType().getSimpleName());
                String fieldName=field.getName();
                System.out.println("fieldName-->"+fieldName);
                if(fieldName=="constraints")
                {
                    break;
                }
                field.setAccessible(true);
                String valueStr="";
                if ("int".equals(fieldType)) {
                    int value=0;//Integer.MIN_VALUE;
                    value=field.getInt(restaurantInfo);
                    valueStr=""+value;
                } else if ("long".equals(fieldType)) {
                    long value=0;//Long.MIN_VALUE;
                    value=field.getLong(restaurantInfo);
                    valueStr=""+value;
                } else if ("float".equals(fieldType)) {
                    float value=0;//Float.MIN_VALUE;
                    value=field.getFloat(restaurantInfo);
                    valueStr=""+value;
                } else if ("double".equals(fieldType)) {
                    double value=0;//Double.MIN_VALUE;
                    value=field.getDouble(restaurantInfo);
                    valueStr=""+value;
                } else if ("char".equals(fieldType)) {
                    char value=0;//Character.MIN_VALUE;
                    value=field.getChar(restaurantInfo);
                    valueStr=""+value;
                } else if ("byte".equals(fieldType)) {
                    byte value=0;//Byte.MIN_VALUE;
                    value=field.getByte(restaurantInfo);
                    valueStr=""+value;
                } else if ("short".equals(fieldType)) {
                    short value=0;//Short.MIN_VALUE;
                    value=field.getShort(restaurantInfo);
                    valueStr=""+value;
                }else if ("boolean".equals(fieldType)) {
                    boolean value=field.getBoolean(restaurantInfo);
                    valueStr=""+value;
                }else if ("Date".equals(fieldType)) {
                    Date value=(Date)field.get(restaurantInfo);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    valueStr=""+sdf.format(value);
                }else if ("String".equals(fieldType)) {
                    String value=null;
                    value=(String) field.get(restaurantInfo);
                    if(value==null){
                        continue;
                    }
                    valueStr=""+value;
                }else{
                    continue;
                }
                if(fieldName.equals("id")){
                    continue;
                }
                if(fieldName.equals("remoteId")){
                    fieldName="id";
                }
                HashMap<String, String> param = new HashMap<String, String>();
                param.put(AppConstant.HttpParamRe.PARAM_NAME, fieldName);
                param.put(AppConstant.HttpParamRe.PARAM_VALUE, valueStr);
                paramList.add(param);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        HttpConnectionHelper hch = new HttpConnectionHelper();
        String responseStr = hch.getResponseStr(url, paramList);
        log.info("responseStr->"+responseStr);
        if(responseStr==null||responseStr.equals("")){
            return [recode: ReCode.NET_ERROR];
        }
        def slurper = new JsonSlurper();
        def responseObj=slurper.parseText(responseStr);
        //保存romteId
        restaurantInfo.remoteId=responseObj.remoteId;
        restaurantInfo.save(flush: true);

        //将License写入文件
        String licenseData="";
        String licenseSerial=responseObj.licenseSerial;//标示的序列化字符串
        boolean enable=responseObj.enable;//是否有效
        int expire=responseObj.expire;//有效期时间，单位天
        long createTime=responseObj.createTime;//创建时间
        long lastTime=responseObj.lastTime;//最后访问时间
        long id=responseObj.id;
        long remoteId=responseObj.remoteId;//饭店在服务器上的ID

        licenseData+="licenseSerial="+licenseSerial+"\n";
        licenseData+="enable="+enable+"\n";
        licenseData+="expire="+expire+"\n";
        licenseData+="createTime="+createTime+"\n";
        licenseData+="lastTime="+lastTime+"\n";
        licenseData+="id="+id+"\n";
        licenseData+="remoteId="+remoteId+"\n";

        String encryptedLicenseData=DesUtilGy.encryptDES(licenseData,"license");

        FileReadWrite fileReadWrite=new FileReadWrite();
        fileReadWrite.writeFile(encryptedLicenseData,"license","",true);

        return [recode: ReCode.OK];
    }

    private boolean isLicenseOk(LicenseResult licenseResult,long remoteId){
        if(licenseResult.getId()==0){
            return false;
        }
        if(!licenseResult.isEnable()){
            return false;
        }
        if(licenseResult.getLastTime()==null){
            return false;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(licenseResult.getLastTime());
        calendar.add(Calendar.DATE,licenseResult.getExpire());
        Date now=new Date();
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTime(now);
        log.info("now-->"+now);
        log.info("lastTime-->"+licenseResult.getLastTime());
        log.info("calendar-->"+calendar.getTime());
        log.info("calendar.before(now)->"+calendar.before(calendar1));
        if(calendar.before(calendar1)){
            return false;
        }
        if(remoteId!=licenseResult.getRemoteId()){
            return false;
        }
        return true;
    }
    def getLicense(RestaurantInfo restaurantInfo){

        LicenseResult licenseResult=LicenseResult.getInstance();
        if(isLicenseOk(licenseResult,restaurantInfo.remoteId)){
            return [recode: ReCode.OK];
        }else{//从文件中读取
            FileReadWrite fileReadWrite=new FileReadWrite();
            String encryptedLicenseData=fileReadWrite.readFile("","license");
            log.info("encryptedLicenseData-->"+encryptedLicenseData);
            if(encryptedLicenseData!=null&&!"".equals(encryptedLicenseData)){ //从文件中获取到
                String licenseData=DesUtilGy.decryptDES(encryptedLicenseData,"license");
                log.info("licenseData-->"+licenseData);
                Properties licenseProperties=new Properties();
                licenseProperties.load(new StringReader(licenseData));
                Date createTime=new Date();
                createTime.setTime(TypeConversion.toLong(licenseProperties.getProperty("createTime")));
                licenseResult.setCreateTime(createTime);
                licenseResult.setEnable(TypeConversion.toBoolean(licenseProperties.getProperty("enable")));
                licenseResult.setExpire(TypeConversion.toInteger(licenseProperties.getProperty("expire")));
                licenseResult.setId(TypeConversion.toLong(licenseProperties.getProperty("id")));
                Date lastTime=new Date();
                lastTime.setTime(TypeConversion.toLong(licenseProperties.getProperty("lastTime")));
                licenseResult.setLastTime(lastTime) ;
                licenseResult.setLicenseSerial(licenseProperties.getProperty("licenseSerial"));
                licenseResult.setRemoteId(TypeConversion.toLong(licenseProperties.getProperty("remoteId")));
            }
        }

        if(isLicenseOk(licenseResult,restaurantInfo.remoteId)){
            return [recode: ReCode.OK];
        }else{//从网络获取，并判断时候有效License
            try {
                String url = grailsApplication.config.grails.license.url+"?rId="+restaurantInfo.remoteId;//"http://localhost/TaosServer/restaurant/getLicense?rId="+restaurantInfo.remoteId;
                def result=url.toURL().getText("utf8");
                if(result){
                    def responseObj=JSON.parse(result);
                    //将License写入文件
                    String licenseData="";
                    String licenseSerial=responseObj.licenseSerial;//标示的序列化字符串
                    boolean enable=responseObj.enable;//是否有效
                    int expire=responseObj.expire;//有效期时间，单位天
                    long createTime=responseObj.createTime;//创建时间
                    long lastTime=responseObj.lastTime;//最后访问时间
                    long id=responseObj.id;
                    long remoteId=responseObj.remoteId;//饭店在服务器上的ID

                    licenseData+="licenseSerial="+licenseSerial+"\n";
                    licenseData+="enable="+enable+"\n";
                    licenseData+="expire="+expire+"\n";
                    licenseData+="createTime="+createTime+"\n";
                    licenseData+="lastTime="+lastTime+"\n";
                    licenseData+="id="+id+"\n";
                    licenseData+="remoteId="+remoteId+"\n";

                    String encryptedLicenseData=DesUtilGy.encryptDES(licenseData,"license");

                    FileReadWrite fileReadWrite=new FileReadWrite();
                    fileReadWrite.writeFile(encryptedLicenseData,"license","",true);

                    //将License写入内存中
                    Properties licenseProperties=new Properties();
                    licenseProperties.load(new StringReader(licenseData));
                    Date createTimeD=new Date();
                    createTimeD.setTime(TypeConversion.toLong(licenseProperties.getProperty("createTime")));
                    licenseResult.setCreateTime(createTimeD);
                    licenseResult.setEnable(TypeConversion.toBoolean(licenseProperties.getProperty("enable")));
                    licenseResult.setExpire(TypeConversion.toInteger(licenseProperties.getProperty("expire")));
                    licenseResult.setId(TypeConversion.toLong(licenseProperties.getProperty("id")));
                    Date lastTimeD=new Date();
                    lastTimeD.setTime(TypeConversion.toLong(licenseProperties.getProperty("lastTime")));
                    licenseResult.setLastTime(lastTimeD) ;
                    licenseResult.setLicenseSerial(licenseProperties.getProperty("licenseSerial"));
                    licenseResult.setRemoteId(TypeConversion.toLong(licenseProperties.getProperty("remoteId")));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        if(isLicenseOk(licenseResult,restaurantInfo.remoteId)){
            return [recode: ReCode.OK];
        }
        return [recode: ReCode.LICENSE_DISABLE];
    }


}
