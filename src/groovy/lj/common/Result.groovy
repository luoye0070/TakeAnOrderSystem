package lj.common

import grails.converters.JSON
import org.springframework.validation.ObjectError

//数据库操作返回结果对象
//success成功标记
//msg成功或错误信息
class Result {
    public boolean success=true
    public def msg
    public def obj

    public boolean getSuccess(){
        return success
    }

    public void setSuccess(boolean b){
        success=b
    }

    public String getMsg(def g){
        StringBuffer sb=new StringBuffer()
        if(msg instanceof String || msg instanceof GString){
            return msg.toString();
        }else if(msg instanceof ObjectError){
            sb.append(g.message(error:msg))
        }else if(msg instanceof List){
            boolean  first=true
            for(def error in msg){
                if(!first){
                    sb.append("\n")
                    first=false
                }

                if(error instanceof  String ||msg instanceof GString)
                    sb.append(error)
                else if(error instanceof ObjectError)
                    sb.append(g.message(error:error))
            }
        }
        return sb.toString()
    }

    public void setMsg(def m){
        msg=m
    }

    public JSON getJOSN(def g){
        def map=[success:success,msg:getMsg(g)]
        return map as JSON
    }

    public void setObj(def obj){
        this.obj=obj
    }


}
