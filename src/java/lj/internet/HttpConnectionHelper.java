package lj.internet;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HttpConnectionHelper {
    private String urlStr;
    private int resultCode;//访问网络结果代码,0成功,1网络错误，2其他错误
    private String resultStr;//服务器返回结果
    private ArrayList<HashMap<String, String>> paramList;
    private int outtime;

    public HttpConnectionHelper(){resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OK;outtime=5000;}
    public HttpConnectionHelper(String urlStr,
                                ArrayList<HashMap<String, String>> paramList) {
        super();
        this.urlStr = urlStr;
        this.paramList = paramList;
        this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OK;
        this.outtime=5000;
    }

    public int getOuttime() {
        return outtime;
    }
    public void setOuttime(int outtime) {
        this.outtime = outtime;
    }
    public String getUrlStr() {
        return urlStr;
    }
    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }
    public int getResultCode() {
        return resultCode;
    }
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    public String getResultStr() {
        return resultStr;
    }
    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }
    public ArrayList<HashMap<String, String>> getParamList() {
        return paramList;
    }
    public void setParamList(ArrayList<HashMap<String, String>> paramList) {
        this.paramList = paramList;
    }

    public String getResponseStr()
    {
        System.out.println(this.urlStr);
        StringBuilder sb=new StringBuilder("");
        HttpURLConnection httpConn=null;
        BufferedReader br=null;
        try {
            String paramsStr="";
            if(paramList!=null)
            {
                //发送数据
                paramsStr="?";
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+URLEncoder.encode(paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE),"UTF-8")+"&";
                }
            }

            URL url=new URL(urlStr+paramsStr);
            System.out.println(urlStr+paramsStr);
            httpConn=(HttpURLConnection) url.openConnection();
            //this.outtime=10000;
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpURLConnection.setFollowRedirects(true);

            //接收返回数据
            br=new BufferedReader(new InputStreamReader(httpConn.getInputStream()),1024*512);

            //获取响应码
            int reCode=httpConn.getResponseCode();
            System.out.println("responseCode:"+reCode);
            if(reCode!=200)
            {//任何错误都定义为其他错误
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }

            //读取数据
            String line=null;
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
                sb.append(line);
            }

        }
        catch(IOException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_NETWORKERROR;
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        finally
        {
            if(httpConn!=null)
            {
                httpConn.disconnect();
            }
            if(br!=null)
            {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
    public String getResponseStr(String urlStr,
                                 ArrayList<HashMap<String, String>> paramList) {
        this.urlStr = urlStr;
        this.paramList = paramList;
        return getResponseStr();
    }

    /*********
     * 上传文件
     * *************/
    public int uploadFile(String fileName,String dirName,String fileFieldName,String fileType){
        System.out.println(this.urlStr);

        String BOUNDARY ="----7d4a6d158c9"; //数据分隔线
        String MULTIPART_FORM_DATA = "multipart/form-lj.data";
        String endline = "--" + BOUNDARY + "--\r\n";

        StringBuilder sb=new StringBuilder("");
        HttpURLConnection httpConn=null;
        BufferedReader br=null;
        try {
            String paramsStr="";
            if(paramList!=null)
            {
                paramsStr="?";
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+URLEncoder.encode(paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE),"UTF-8")+"&";
                }
            }
            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpURLConnection) url.openConnection();
            //this.outtime=10000;
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpURLConnection.setFollowRedirects(true);
            //if(paramList!=null)
            //{
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);//允许输入
            httpConn.setDoOutput(true);//允许输出
            httpConn.setUseCaches(false);//不使用Cache
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset","UTF-8");
            httpConn.setRequestProperty("Content-Type",MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
            OutputStream out=httpConn.getOutputStream();
            //发送文件分割
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-lj.data;name=\""+ fileFieldName+"\";filename=\""+ fileName + "\"\r\n");
            fileEntity.append("Content-Type: "+ fileType+"\r\n\r\n");
            out.write(fileEntity.toString().getBytes());
            out.flush();

            //发送文件
            //边读边写
            //String SDCardRoot=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
            String SDCardRoot="";
            InputStream input=null;
            try{
                File file=new File(SDCardRoot+dirName+fileName);
                //file.setReadable(true);
                input=new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = input.read(buffer, 0, 1024))!=-1)
                {
                    out.write(buffer, 0, len);
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }
            finally{
                if(input!=null){
                    input.close();
                }
            }

            out.write("\r\n".getBytes());
            //下面发送数据结束标志，表示数据已经结束
            out.write(endline.getBytes());

            out.close();
            //}

            //接收返回数据
            br=new BufferedReader(new InputStreamReader(httpConn.getInputStream()),1024*512);

            //获取响应码
            int reCode=httpConn.getResponseCode();
            System.out.println("responseCode:"+reCode);
            if(reCode!=200)
            {//任何错误都定义为其他错误
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }

            //读取数据
            String line=null;
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
                sb.append(line);
            }
            this.resultStr=sb.toString();
        }
        catch(IOException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_NETWORKERROR;
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        finally
        {
            if(httpConn!=null)
            {
                httpConn.disconnect();
            }
            if(br!=null)
            {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return this.resultCode;
    }
    /*********
     * 上传文件
     * *************/
    public int uploadFile(String urlStr,
                          ArrayList<HashMap<String, String>> paramList,String fileName,String dirName,String fileFieldName,String fileType){
        this.urlStr = urlStr;
        this.paramList = paramList;
        return uploadFile(fileName,dirName,fileFieldName,fileType);
    }
    /*********
     * 上传文件
     * *************/
    public int uploadFile(String fileName,InputStream is,String fileFieldName,String fileType){
        System.out.println(this.urlStr);

        String BOUNDARY ="----7d4a6d158c9"; //数据分隔线
        String MULTIPART_FORM_DATA = "multipart/form-lj.data";
        String endline = "--" + BOUNDARY + "--\r\n";

        StringBuilder sb=new StringBuilder("");
        HttpURLConnection httpConn=null;
        BufferedReader br=null;
        try {
            String paramsStr="";
            if(paramList!=null)
            {
                paramsStr="?";
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+URLEncoder.encode(paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE),"UTF-8")+"&";
                }
            }
            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpURLConnection) url.openConnection();
            //this.outtime=10000;
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpURLConnection.setFollowRedirects(true);
            //if(paramList!=null)
            //{
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);//允许输入
            httpConn.setDoOutput(true);//允许输出
            httpConn.setUseCaches(false);//不使用Cache
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("Charset","UTF-8");
            httpConn.setRequestProperty("Content-Type",MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
            OutputStream out=httpConn.getOutputStream();
            //发送文件分割
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-lj.data;name=\""+ fileFieldName+"\";filename=\""+ fileName + "\"\r\n");
            fileEntity.append("Content-Type: "+ fileType+"\r\n\r\n");
            out.write(fileEntity.toString().getBytes());
            out.flush();

            //发送文件
            //边读边写
            //InputStream input=null;
            try{
                //input=is;
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = is.read(buffer, 0, 1024))!=-1)
                {
                    out.write(buffer, 0, len);
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }
            finally{
                //if(input!=null){
                //    input.close();
                //}
            }

            out.write("\r\n".getBytes());
            //下面发送数据结束标志，表示数据已经结束
            out.write(endline.getBytes());

            out.close();
            //}

            //接收返回数据
            br=new BufferedReader(new InputStreamReader(httpConn.getInputStream()),1024*512);

            //获取响应码
            int reCode=httpConn.getResponseCode();
            System.out.println("responseCode:"+reCode);
            if(reCode!=200)
            {//任何错误都定义为其他错误
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }

            //读取数据
            String line=null;
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
                sb.append(line);
            }
            this.resultStr=sb.toString();
        }
        catch(IOException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_NETWORKERROR;
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        finally
        {
            if(httpConn!=null)
            {
                httpConn.disconnect();
            }
            if(br!=null)
            {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return this.resultCode;
    }
    /*********
     * 上传文件
     * *************/
    public int uploadFile(String urlStr,
                          ArrayList<HashMap<String, String>> paramList,String fileName,InputStream is,String fileFieldName,String fileType){
        this.urlStr = urlStr;
        this.paramList = paramList;
        return uploadFile(fileName,is,fileFieldName,fileType);
    }
    /*************************************************************
     * 下载文件
     * @param fileName,文件名
     * @param dirName,目录名
     * @param newWrite,文件存在是否重新下载
     * @return 返回错误代码
     **************************************************************/
    public int downloadFile(String fileName,String dirName,boolean newWrite)
    {
        System.out.println(this.urlStr);
        //StringBuilder sb=new StringBuilder("");
        InputStream inputStream=null;
        HttpURLConnection httpConn=null;

        try {
            String paramsStr="";
            if(paramList!=null&&paramList.size()>0)
            {
                //发送数据
                paramsStr="?";
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+URLEncoder.encode(paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE),"UTF-8")+"&";
                }
            }
            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpURLConnection) url.openConnection();
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpURLConnection.setFollowRedirects(true);


            //接收返回数据
            inputStream=httpConn.getInputStream();
            //将输入流写入文件
            FileReadWrite frw=new FileReadWrite();
            //this.resultCode=frw.writeFile(inputStream, fileName, dirName);
            this.resultCode=frw.writeFile(inputStream, fileName, dirName,newWrite);

            //获取响应码
            int reCode=httpConn.getResponseCode();
            System.out.println("responseCode:"+reCode);
            if(reCode!=200)
            {//任何错误都定义为其他错误
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }

        }
        catch(FileNotFoundException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_FILENOTFOUND;
            //this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        catch(IOException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_NETWORKERROR;
            //this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        finally
        {
            try {
                if(inputStream!=null)
                {
                    inputStream.close();
                }
                if(httpConn!=null)
                {
                    httpConn.disconnect();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return this.resultCode;
    }

    /*************************************************************
     * 下载文件
     * @param urlStr,请求的地址
     * @param paramList,参数列表
     * @param fileName,文件名
     * @param dirName,目录名
     * @param newWrite,文件存在是否重新下载
     * @return 返回错误代码
     **************************************************************/
    public int downloadFile(String urlStr,
                            ArrayList<HashMap<String, String>> paramList,String fileName,String dirName,boolean newWrite)
    {
        this.urlStr = urlStr;
        this.paramList = paramList;
        return downloadFile(fileName,dirName,newWrite);
    }

    /*************************************************************
     * 下载文件
     * @param os,输出流
     * @return 返回错误代码
     **************************************************************/
    public int downloadFile(OutputStream os)
    {
        System.out.println(this.urlStr);
        //StringBuilder sb=new StringBuilder("");
        InputStream inputStream=null;
        HttpURLConnection httpConn=null;

        try {
            String paramsStr="";
            if(paramList!=null&&paramList.size()>0)
            {
                //发送数据
                paramsStr="?";
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+URLEncoder.encode(paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE),"UTF-8")+"&";
                }
            }
            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpURLConnection) url.openConnection();
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpURLConnection.setFollowRedirects(true);


            //接收返回数据
            inputStream=httpConn.getInputStream();
            //将输入流写入文件
            FileReadWrite frw=new FileReadWrite();
            //this.resultCode=frw.writeFile(inputStream, fileName, dirName);
            this.resultCode=frw.writeFile(inputStream, os);

            //获取响应码
            int reCode=httpConn.getResponseCode();
            System.out.println("responseCode:"+reCode);
            if(reCode!=200)
            {//任何错误都定义为其他错误
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }

        }
        catch(FileNotFoundException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_FILENOTFOUND;
            //this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        catch(IOException e)
        {
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_NETWORKERROR;
            //this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            e.printStackTrace();
        }
        finally
        {
            try {
                if(inputStream!=null)
                {
                    inputStream.close();
                }
                if(httpConn!=null)
                {
                    httpConn.disconnect();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return this.resultCode;
    }

    /*************************************************************
     * 下载文件
     * @param urlStr,请求的地址
     * @param paramList,参数列表
     * @param os,输出流
     * @return 返回错误代码
     **************************************************************/
    public int downloadFile(String urlStr,
                            ArrayList<HashMap<String, String>> paramList,OutputStream os)
    {
        this.urlStr = urlStr;
        this.paramList = paramList;
        return downloadFile(os);
    }
}
