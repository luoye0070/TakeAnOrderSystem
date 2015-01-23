package lj

import org.springframework.validation.ObjectError

//获取错误国际化内容
class I118Error {
    public static String getMessage(def g,def errors){
        StringBuffer sb=new StringBuffer()
        for(def error in errors){
            if(error instanceof  String)
                sb.append(error+"\n")
            else if(error instanceof ObjectError)
            sb.append(g.message(error:error)+"\n")
        }
        return sb.toString()
    }
    public static String getMessage(def g,def errors, int idx){
        StringBuffer sb=new StringBuffer()
        if(errors instanceof String){
            sb.append(errors);
        }
        else if(errors instanceof String[]){
            sb.append(errors[((errors.length-1)>=idx)?idx:(errors.length-1)]);
        }
        else if(errors instanceof List){
            def error=errors.get(((errors.size()-1)>=idx)?idx:(errors.size()-1));
            if(error instanceof  String)
                sb.append(error+"\n")
            else if(error instanceof ObjectError)
                sb.append(g.message(error:error)+"\n")
        }
        return sb.toString()
    }
}
