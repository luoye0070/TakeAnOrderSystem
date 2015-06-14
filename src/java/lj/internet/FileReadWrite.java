package lj.internet;

import java.io.*;
import java.util.ArrayList;

public class FileReadWrite {
    String SDCardRoot=null;//sd卡路径
    public FileReadWrite()
    {
        getSDPath();
    }
    private void getSDPath()
    {
        SDCardRoot="";
    }

    public String getSDCardRoot() {
        return SDCardRoot;
    }
    /*************************************************************
     *检查文件是否存在
     *************************************************************/
    public boolean isFileExist(String qfileName)
    {
        File file=new File(SDCardRoot+qfileName);
        return file.exists();
    }

    private File createDir(String dirName)
    {
        File file=new File(SDCardRoot+dirName);
        file.mkdirs();
        return file;
    }

    private File createFile(String dirName,String fileName)
    {
        File file=new File(SDCardRoot+dirName+fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(SDCardRoot+dirName+fileName);
        return file;
    }

    /*****************************************************
     * 将一个输入流数据写入文件
     ******************************************************/
    public int writeFile(InputStream input,String fileName,String dirName)
    {
        int recode=0;
        File file=null;
        OutputStream output=null;
        System.out.println(dirName+fileName);
        if(!isFileExist(dirName))
        {
            createDir(dirName);
        }
        if(!isFileExist(dirName+fileName))
        {
            file=createFile(dirName, fileName);
        }
        else
        {
            return 1;//文件已经存在
        }
        if(file.canWrite())
        {
            try {
                output=new FileOutputStream(file);
                byte []buff=new byte[4*1024];
                int count=-1;
                while((count=input.read(buff))!=-1)
                {
                    //output.write(buff);
                    output.write(buff, 0, count);
                    output.flush();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                recode=-2;//IO异常
            }
            finally
            {
                try {
                    if(output!=null)
                        output.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else
        {
            recode=-1;//文件不能写
        }
        return recode;//写文件成功
    }

    /***********************************************
     * 获取文件读入流
     **********************************************/
    public InputStream getFileInputStream(String dirName,String fileName)
    {
        InputStream input=null;
        try{
            File file=new File(SDCardRoot+dirName+fileName);
            //file.setReadable(true);
            input=new FileInputStream(file);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        finally{

        }
        return input;
    }

    /***********************************************
     * 从文件读入全部字符串
     **********************************************/
    public String readFile(String dirName,String fileName)
    {
        StringBuilder sb=new StringBuilder("");
        BufferedReader fileReader=null;
        try{
            File file=new File(SDCardRoot+dirName+fileName);
            //file.setReadable(true);
            InputStream input=new FileInputStream(file);
            fileReader=new BufferedReader(new InputStreamReader(input));
            String line=null;
            while ((line=fileReader.readLine())!=null){
                sb.append(line);
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        finally{
             if(fileReader!=null){
                 try{
                    fileReader.close();
                 }catch (IOException ex){
                      ex.printStackTrace();
                 }
             }
        }
        return sb.toString();
    }

    public int writeFile(String data,String fileName,String dirName,boolean override){
        if(override){
            if(isFileExist(dirName+fileName))
            {
                File file=new File(SDCardRoot+dirName+fileName);
                file.delete();
            }
        }
        return writeFile(data, fileName, dirName);
    }
    /****************************************************
     *将一个字符串写入文件
     ****************************************************/
    public int writeFile(String data,String fileName,String dirName)
    {
        int reCode=0;
        File file=null;
        OutputStream output=null;
        BufferedWriter bw=null;
        System.out.println(dirName+fileName);
        if(!isFileExist(dirName))
        {
            createDir(dirName);
        }
        if(!isFileExist(dirName+fileName))
        {
            file=createFile(dirName, fileName);
        }
        else
        {
            file=new File(SDCardRoot+dirName+fileName);
        }
        if(file.canWrite())
        {
            try {
                output=new FileOutputStream(file,true);
                bw=new BufferedWriter(new OutputStreamWriter(output));
                bw.write(data+"\r\n");
                bw.flush();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                reCode=-1;
            }
            finally{
                if(bw!=null)
                {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        reCode=-1;
                    }
                }
            }
        }
        else
        {
            reCode=-1;
        }
        return reCode;
    }

    /***************************************************
     * 从文件中读取数据到字符列表中，以行分
     ***************************************************/
    public ArrayList<String> getStringArrayFromFile(String dirName,String fileName)
    {
        ArrayList<String> strList=null;
        BufferedReader br=null;
        try{
            File file=new File(SDCardRoot+dirName+fileName);
            if(file.exists())
            {
                //file.setReadable(true);
                br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                strList=new ArrayList<String>();
                String line=null;
                while((line=br.readLine())!=null)
                {
                    strList.add(line);
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        finally{
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
        return strList;
    }

    /***************************************************************
     * 检查字符串是否在文件中
     ***************************************************************/
    public boolean stringIsInFile(String data,String fileName,String dirName)
    {
        boolean isInFile=false;
        BufferedReader br=null;
        try{
            File file=new File(SDCardRoot+dirName+fileName);
            if(file.exists())
            {
                //file.setReadable(true);
                br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                String line=null;
                while((line=br.readLine())!=null)
                {
                    if(line.equals(data))
                    {
                        isInFile=true;
                        break;
                    }
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        finally{
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
        return isInFile;
    }

    /*************************************************************
     *获取文件大小，字节数
     *************************************************************/
    public long getFileSize(String qfileName)
    {
        File file=new File(SDCardRoot+qfileName);
        return file.length();
    }


    /*****************************************************
     * 将一个输入流数据写入文件
     ******************************************************/
    public int writeFile(InputStream input,String fileName,String dirName,boolean newwrite)
    {
        int recode=0;
        File file=null;
        OutputStream output=null;
        System.out.println(dirName+fileName);
        if(!isFileExist(dirName))
        {
            createDir(dirName);
        }
        if(!isFileExist(dirName+fileName))
        {
            file=createFile(dirName, fileName);
        }
        else
        {
            if(newwrite)
            {
                file=new File(SDCardRoot+dirName+fileName);//文件已经存在,重新写文件
            }
            else
            {
                return 1;//文件已经存在
            }
        }
        if(file.canWrite())
        {
            try {
                output=new FileOutputStream(file,false);
                byte []buff=new byte[4*1024];
                int count=-1;
                while((count=input.read(buff))!=-1)
                {
                    //output.write(buff);
                    output.write(buff, 0, count);
                    output.flush();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                recode=-2;//IO异常
            }
            finally
            {
                try {
                    if(output!=null)
                        output.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else
        {
            recode=-1;//文件不能写
            System.out.println("图片不能写");
        }
        return recode;//写文件成功
    }
    /*****************************************************
     * 将一个输入流数据写入输出流
     ******************************************************/
    public int writeFile(InputStream input,OutputStream os)
    {
        int recode=0;
        try {
            byte []buff=new byte[4*1024];
            int count=-1;
            while((count=input.read(buff))!=-1)
            {
                //output.write(buff);
                os.write(buff, 0, count);
                os.flush();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            recode=-2;//IO异常
        }
        finally
        {
        }
        return recode;//写文件成功
    }
}
