package lj.internet.analysis;

import com.google.gson.stream.JsonReader;
import lj.internet.model.UploadResult;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;

public class UploadResultAnalyzeHelper extends AnalyzeHelper {
	public UploadResult ur=null;
	@Override
	public void analyze(String dataStr) {
		// TODO Auto-generated method stub
		JsonReader jrd=new JsonReader(new StringReader(dataStr));
		try {
			jrd.beginObject();
			ur=new UploadResult();
			while(jrd.hasNext()){
				String tagName=jrd.nextName();
				if(tagName.equals("path")){
					try{
						String str=jrd.nextString();
						if(str!=null){
							str=URLDecoder.decode(str, "UTF-8");
						}
						System.out.println(str);
						ur.path=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("size")){
					try{
						long str=jrd.nextLong();
						System.out.println(str);
						ur.size=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("ctime")){
					try{
						long str=jrd.nextLong();
						System.out.println(str);
						ur.ctime=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("mtime")){
					try{
						long str=jrd.nextLong();
						System.out.println(str);
						ur.mtime=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("md5")){
					try{
						String str=jrd.nextString();
						System.out.println(str);
						ur.md5=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("fs_id")){
					try{
						long str=jrd.nextLong();
						System.out.println(str);
						ur.fs_id=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else{
					jrd.skipValue();
				}
			}
			jrd.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
