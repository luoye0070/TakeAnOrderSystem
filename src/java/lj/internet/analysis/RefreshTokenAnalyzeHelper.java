package lj.internet.analysis;

import com.google.gson.stream.JsonReader;
import lj.internet.model.RefreshToken;

import java.io.IOException;
import java.io.StringReader;

public class RefreshTokenAnalyzeHelper extends AnalyzeHelper {
	public RefreshToken rt=null;
	@Override
	public void analyze(String dataStr) {
		// TODO Auto-generated method stub
		JsonReader jrd=new JsonReader(new StringReader(dataStr));
		try {
			jrd.beginObject();
			rt=new RefreshToken();
			while(jrd.hasNext()){
				String tagName=jrd.nextName();
				if(tagName.equals("access_token")){
					try{
						String str=jrd.nextString();
						System.out.println(str);
						rt.access_token=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("expires_in")){
					try{
						long str=jrd.nextLong();
						System.out.println(str);
						rt.expires_in=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("refresh_token")){
					try{
						String str=jrd.nextString();
						System.out.println(str);
						rt.refresh_token=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("scope")){
					try{
						String str=jrd.nextString();
						System.out.println(str);
						rt.scope=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("session_key")){
					try{
						String str=jrd.nextString();
						System.out.println(str);
						rt.session_key=str;
					}
					catch(IllegalStateException e)
					{
						e.printStackTrace();
						jrd.nextNull();
					}
				}
				else if(tagName.equals("session_secret")){
					try{
						String str=jrd.nextString();
						System.out.println(str);
						rt.session_secret=str;
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
