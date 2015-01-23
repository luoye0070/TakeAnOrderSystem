package lj.internet;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class HttpsConnectionHelper {
    private String urlStr;
    private int resultCode;//访问网络结果代码,0成功,1网络错误，2其他错误
    private String resultStr;//服务器返回结果
    private ArrayList<HashMap<String, String>> paramList;
    private int outtime;
    private String bootPath="";

    public HttpsConnectionHelper(){resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OK;outtime=5000;}
    public HttpsConnectionHelper(String urlStr,
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
    public String getBootPath() {
        return bootPath;
    }
    public void setBootPath(String bootPath) {
        this.bootPath = bootPath;
    }
    public String getResponseStrPost()
    {
        System.out.println(this.urlStr);
        StringBuilder sb=new StringBuilder("");
        HttpsURLConnection httpConn=null;
        BufferedReader br=null;
        try {

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager(bootPath) };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url=new URL(urlStr);
            httpConn=(HttpsURLConnection) url.openConnection();
            httpConn.setSSLSocketFactory(ssf);
            //this.outtime=10000;
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpsURLConnection.setFollowRedirects(true);
            if(paramList!=null)
            {
                httpConn.setDoOutput(true);
                httpConn.setRequestMethod("POST");

                //发送数据
                String paramsStr="";
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE)+"&";
                }
                OutputStream out=httpConn.getOutputStream();
                out.write(paramsStr.getBytes());
                out.flush();
                out.close();
            }

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
    public String getResponseStrPost(String urlStr,
                                     ArrayList<HashMap<String, String>> paramList) {
        this.urlStr = urlStr;
        this.paramList = paramList;
        return getResponseStrPost();
    }

    public String getResponseStr()
    {
        System.out.println(this.urlStr);
        StringBuilder sb=new StringBuilder("");
        HttpsURLConnection httpConn=null;
        BufferedReader br=null;
        try {

            //发送数据
            String paramsStr="";
            if(paramList!=null)
            {
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE)+"&";
                }
            }
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager(bootPath) };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url=new URL(urlStr+"?"+paramsStr);
            httpConn=(HttpsURLConnection) url.openConnection();
            httpConn.setSSLSocketFactory(ssf);
            //this.outtime=10000;
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpsURLConnection.setFollowRedirects(true);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("GET");



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
        HttpsURLConnection httpConn=null;
        BufferedReader br=null;
        try {
            //发送参数数据
            String paramsStr="";
            if(paramList!=null)
            {
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    HashMap<String, String> paramHash = (HashMap<String, String>) iterator.next();
                    paramsStr+=paramHash.get(AppConstant.HttpParamRe.PARAM_NAME)+"="+URLEncoder.encode(paramHash.get(AppConstant.HttpParamRe.PARAM_VALUE),"UTF-8")+"&";
                }
            }

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager(bootPath) };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url=new URL(urlStr+"?"+paramsStr);
            System.out.println(urlStr+"?"+paramsStr);
            httpConn=(HttpsURLConnection) url.openConnection();
            httpConn.setSSLSocketFactory(ssf);
            //this.outtime=10000;
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpsURLConnection.setFollowRedirects(true);
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
        HttpsURLConnection httpConn=null;
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

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager(bootPath) };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpsURLConnection) url.openConnection();
            httpConn.setSSLSocketFactory(ssf);
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
        HttpsURLConnection httpConn=null;

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

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager(bootPath) };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpsURLConnection) url.openConnection();
            httpConn.setSSLSocketFactory(ssf);
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

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager(bootPath) };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url=new URL(urlStr+paramsStr);
            System.out.println(this.urlStr+paramsStr);
            httpConn=(HttpsURLConnection) url.openConnection();
            ((HttpsURLConnection)httpConn).setSSLSocketFactory(ssf);
            httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
            //HttpURLConnection.setFollowRedirects(true);

            //获取响应码
            int reCode=httpConn.getResponseCode();
            System.out.println("responseCode:"+reCode);
            while (reCode>=300&&reCode<310){
                String url_str=httpConn.getHeaderField("location");
                System.out.println("url_str:"+url_str);
                if(url_str.startsWith("http://")){
                    httpConn.disconnect();
                    url=new URL(url_str);
                    httpConn=(HttpURLConnection) url.openConnection();
                    //httpConn.setSSLSocketFactory(ssf);
                    httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
                    reCode=httpConn.getResponseCode();
                    System.out.println("responseCode:"+reCode);
                }
                else if(url_str.startsWith("https://")){
                    httpConn.disconnect();
                    url=new URL(url_str);
                    httpConn=(HttpsURLConnection) url.openConnection();
                    ((HttpsURLConnection)httpConn).setSSLSocketFactory(ssf);
                    httpConn.setReadTimeout(this.outtime);//设置超时时间为5秒
                    reCode=httpConn.getResponseCode();
                    System.out.println("responseCode:"+reCode);
                }
                else{
                    break;
                }
            }
            if(reCode!=200)
            {//任何错误都定义为其他错误
                this.resultCode=AppConstant.VisitServerResultCode.RESULT_CODE_OTHERERROR;
            }

            //接收返回数据
            inputStream=httpConn.getInputStream();
            //将输入流写入文件
            FileReadWrite frw=new FileReadWrite();
            //this.resultCode=frw.writeFile(inputStream, fileName, dirName);
            this.resultCode=frw.writeFile(inputStream, os);



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

class MyX509TrustManager implements X509TrustManager {
    /*
     * The default X509TrustManager returned by SunX509.  We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509TrustManager doesn't trust it.
     */
    X509TrustManager sunJSSEX509TrustManager;
    MyX509TrustManager(String bootPath) throws Exception {
        // create a "default" JSSE X509TrustManager.
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(bootPath+"my.truststore"),
                "liuzhaoguo".toCharArray());
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance("SunX509", "SunJSSE");
        tmf.init(ks);
        TrustManager tms [] = tmf.getTrustManagers();
        /*
         * Iterate over the returned trustmanagers, look
         * for an instance of X509TrustManager.  If found,
         * use that as our "default" trust manager.
         */
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                return;
            }
        }
        /*
         * Find some other way to initialize, or else we have to fail the
         * constructor.
         */
        throw new Exception("Couldn't initialize");
    }
    /*
     * Delegate to the default trust manager.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException excep) {
            // do any special handling here, or rethrow exception.
        }
    }
    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {
            /*
             * Possibly pop up a dialog box asking whether to trust the
             * cert chain.
             */
        }
    }
    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return sunJSSEX509TrustManager.getAcceptedIssuers();
    }
}
