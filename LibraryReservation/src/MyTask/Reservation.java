package MyTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Domain.User;

public class Reservation {
	String urlstart = "http://202.194.46.22/Default.aspx";
	String urllogin = "http://202.194.46.22/Default.aspx";
	String parse = "http://202.194.46.22/FunctionPages/SeatBespeak/SeatLayoutHandle.ashx";
	String urlpost = "http://202.194.46.22/FunctionPages/SeatBespeak/BespeakSubmitWindow.aspx?parameters=";
	
	User user = new User();	
//	业务改动已废弃
//	public void start(){
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet(urlstart);
//		try{
//			CloseableHttpResponse response = httpclient.execute(httpGet);
//			
//			try{
//				Header[] header = response.getHeaders("Set-Cookie");
//				String cookie = header[0].getValue();
//				System.out.println("已设置cookie");
//				user.setCookie(cookie);
//			
//			}finally{
//				response.close();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
	
	public int login() throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(urllogin);
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("__EVENTVALIDATION","/wEWBAK8pp7NCQKl1bKzCQK1qbSRCwLgiqiFDgNzV7kgaBcunMDDVL1G929jHgpAf23Adns+nsxJyvlN"));
		nvps.add(new BasicNameValuePair("__PREVIOUSPAGE","bSEXBMbxPzCxzu9xdSdqqA2"));
		nvps.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwUKMTA0NDcxMjYxN2QYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFBWNtZE9LoNIvXXTkFQy3Kpktd6Wr3Lp6nXSVJzvwZThs/GgNkqo="));
	
		nvps.add(new BasicNameValuePair("cmdOK.x","22"));
		nvps.add(new BasicNameValuePair("cmdOK.y","11"));
		nvps.add(new BasicNameValuePair("txtPassword",user.getPassword()));
		nvps.add(new BasicNameValuePair("txtUserName",user.getUsername()));
		nvps.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		nvps.add(new BasicNameValuePair("__EVENTTARGET",""));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		
		CloseableHttpResponse response = httpclient.execute(httpPost);
		Header[] header = response.getHeaders("Set-Cookie");
		String cookie = header[0].getValue();
		System.out.println("已设置cookie");
		user.setCookie(cookie);
		
		try{
			int StatusCode = response.getStatusLine().getStatusCode();		
			return StatusCode;
		}finally{
			response.close();
		}		
	}
	
	public String ParseHtml(int Day, int Month, int Year){		
		Document doc = null;
		String param = "";
		try {
			doc = Jsoup.connect(parse)
			.header("Accept", "*/*")
			.header("Accept-Encoding", "gzip, deflate")
			.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
			.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
			.cookie("Cookie", user.getCookie())
			.data("roomNum", "0001"+user.getFloor())
			.data("date", Year+"/"+Month+"/"+Day+" 0:00:00")
			.timeout(1000*120)
			.post();//2017/5/23
			
		} catch (IOException e) {
			System.out.println("页面解析出错");
		}
		
		try{
			param = doc.getElementById("0001"+user.getFloor()+user.getSeat()).attr("onclick");
			param = param.substring(param.indexOf("\"")+1, param.lastIndexOf("\""));
		}catch(Exception e){
			System.out.println("不存在该座位");
		}
		
		this.urlpost=this.urlpost+param;
		return param;
		
	}
	
	public StringBuffer reserv(){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(urlpost);
		httpPost.setHeader("Cookie", user.getCookie());
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		nvps.add(new BasicNameValuePair("__EVENTARGUMENT",""));
		nvps.add(new BasicNameValuePair("__EVENTTARGET","ContentPanel1$btnBespeak"));
		nvps.add(new BasicNameValuePair("__EVENTVALIDATION","/wEWAgK6hd6QAQL+mI+WBlc6oH4P83EpYVSaIbO8c1SjDGJlOxpfRO2aiiOWrL0Y"));
		nvps.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwULLTExNDEyODQ3MDVkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYHBQVGb3JtMgUURm9ybTIkY3RsMDMkcmJsTW9kZWwFIUZvcm0yJGN0bDA0JERyb3BEb3duTGlzdF9GcmVlVGltZQUdRm9ybTIkY3RsMDUkRHJvcERvd25MaXN0X1RpbWUFDUNvbnRlbnRQYW5lbDEFGENvbnRlbnRQYW5lbDEkYnRuQmVzcGVhawUWQ29udGVudFBhbmVsMSRidG5DbG9zZYd5Q9+bdHOVoXy1WD5oYX0jhHpJqfSXwSJAins5lYkX"));
		nvps.add(new BasicNameValuePair("ContentPanel1_Collapsed","false"));
		nvps.add(new BasicNameValuePair("Form2$ctl03$rblModel","0"));
		nvps.add(new BasicNameValuePair("Form2$ctl04$DropDownList_FreeTime","8:10"));
		nvps.add(new BasicNameValuePair("Form2$ctl05$DropDownList_Time","10:00"));
		nvps.add(new BasicNameValuePair("Form2_Collapsed","false"));
		nvps.add(new BasicNameValuePair("roomOpenTime","8:00"));//时间
		nvps.add(new BasicNameValuePair("X_AJAX","true"));
		nvps.add(new BasicNameValuePair("X_CHANGED","false"));
		nvps.add(new BasicNameValuePair("X_STATE","eyJGb3JtMl9jdGwwMF9sYmxSb29tTmFtZSI6eyJUZXh0Ijoi5LqU5qW8In0sIkZvcm0yX2N0bDAxX2xibFNlYXRObyI6eyJUZXh0IjoiMDI1In0sIkZvcm0yX2N0bDAyX2xibGJlZ2luRGF0ZSI6eyJUZXh0IjoiMjAxNy81LzIxIn0sIkZvcm0yX2N0bDAzX3JibE1vZGVsIjp7IkhpZGRlbiI6dHJ1ZX0sIkZvcm0yX2N0bDA0X0Ryb3BEb3duTGlzdF9GcmVlVGltZSI6eyJIaWRkZW4iOnRydWUsIlhfSXRlbXMiOltbIjg6MTAiLCI4OjEwIiwxXSxbIjg6MjAiLCI4OjIwIiwxXSxbIjg6MzAiLCI4OjMwIiwxXSxbIjg6NDAiLCI4OjQwIiwxXSxbIjg6NTAiLCI4OjUwIiwxXSxbIjk6MDAiLCI5OjAwIiwxXSxbIjk6MTAiLCI5OjEwIiwxXSxbIjk6MjAiLCI5OjIwIiwxXSxbIjk6MzAiLCI5OjMwIiwxXSxbIjk6NDAiLCI5OjQwIiwxXSxbIjk6NTAiLCI5OjUwIiwxXSxbIjEwOjAwIiwiMTA6MDAiLDFdLFsiMTA6MTAiLCIxMDoxMCIsMV0sWyIxMDoyMCIsIjEwOjIwIiwxXSxbIjEwOjMwIiwiMTA6MzAiLDFdLFsiMTA6NDAiLCIxMDo0MCIsMV0sWyIxMDo1MCIsIjEwOjUwIiwxXSxbIjExOjAwIiwiMTE6MDAiLDFdLFsiMTE6MTAiLCIxMToxMCIsMV0sWyIxMToyMCIsIjExOjIwIiwxXSxbIjExOjMwIiwiMTE6MzAiLDFdLFsiMTE6NDAiLCIxMTo0MCIsMV0sWyIxMTo1MCIsIjExOjUwIiwxXSxbIjEyOjAwIiwiMTI6MDAiLDFdLFsiMTI6MTAiLCIxMjoxMCIsMV0sWyIxMjoyMCIsIjEyOjIwIiwxXSxbIjEyOjMwIiwiMTI6MzAiLDFdLFsiMTI6NDAiLCIxMjo0MCIsMV0sWyIxMjo1MCIsIjEyOjUwIiwxXSxbIjEzOjAwIiwiMTM6MDAiLDFdLFsiMTM6MTAiLCIxMzoxMCIsMV0sWyIxMzoyMCIsIjEzOjIwIiwxXSxbIjEzOjMwIiwiMTM6MzAiLDFdLFsiMTM6NDAiLCIxMzo0MCIsMV0sWyIxMzo1MCIsIjEzOjUwIiwxXSxbIjE0OjAwIiwiMTQ6MDAiLDFdLFsiMTQ6MTAiLCIxNDoxMCIsMV0sWyIxNDoyMCIsIjE0OjIwIiwxXSxbIjE0OjMwIiwiMTQ6MzAiLDFdLFsiMTQ6NDAiLCIxNDo0MCIsMV0sWyIxNDo1MCIsIjE0OjUwIiwxXSxbIjE1OjAwIiwiMTU6MDAiLDFdLFsiMTU6MTAiLCIxNToxMCIsMV0sWyIxNToyMCIsIjE1OjIwIiwxXSxbIjE1OjMwIiwiMTU6MzAiLDFdLFsiMTU6NDAiLCIxNTo0MCIsMV0sWyIxNTo1MCIsIjE1OjUwIiwxXSxbIjE2OjAwIiwiMTY6MDAiLDFdLFsiMTY6MTAiLCIxNjoxMCIsMV0sWyIxNjoyMCIsIjE2OjIwIiwxXSxbIjE2OjMwIiwiMTY6MzAiLDFdLFsiMTY6NDAiLCIxNjo0MCIsMV0sWyIxNjo1MCIsIjE2OjUwIiwxXSxbIjE3OjAwIiwiMTc6MDAiLDFdLFsiMTc6MTAiLCIxNzoxMCIsMV0sWyIxNzoyMCIsIjE3OjIwIiwxXSxbIjE3OjMwIiwiMTc6MzAiLDFdLFsiMTc6NDAiLCIxNzo0MCIsMV0sWyIxNzo1MCIsIjE3OjUwIiwxXSxbIjE4OjAwIiwiMTg6MDAiLDFdLFsiMTg6MTAiLCIxODoxMCIsMV0sWyIxODoyMCIsIjE4OjIwIiwxXSxbIjE4OjMwIiwiMTg6MzAiLDFdLFsiMTg6NDAiLCIxODo0MCIsMV0sWyIxODo1MCIsIjE4OjUwIiwxXSxbIjE5OjAwIiwiMTk6MDAiLDFdLFsiMTk6MTAiLCIxOToxMCIsMV0sWyIxOToyMCIsIjE5OjIwIiwxXSxbIjE5OjMwIiwiMTk6MzAiLDFdLFsiMTk6NDAiLCIxOTo0MCIsMV0sWyIxOTo1MCIsIjE5OjUwIiwxXSxbIjIwOjAwIiwiMjA6MDAiLDFdLFsiMjA6MTAiLCIyMDoxMCIsMV0sWyIyMDoyMCIsIjIwOjIwIiwxXSxbIjIwOjMwIiwiMjA6MzAiLDFdLFsiMjA6NDAiLCIyMDo0MCIsMV0sWyIyMDo1MCIsIjIwOjUwIiwxXSxbIjIxOjAwIiwiMjE6MDAiLDFdLFsiMjE6MTAiLCIyMToxMCIsMV0sWyIyMToyMCIsIjIxOjIwIiwxXV0sIlNlbGVjdGVkVmFsdWUiOiI4OjEwIn0sIkZvcm0yX2N0bDA1X0Ryb3BEb3duTGlzdF9UaW1lIjp7IkhpZGRlbiI6dHJ1ZSwiWF9JdGVtcyI6W1siMTA6MDAiLCIxMDowMCIsMV0sWyIxMjowMCIsIjEyOjAwIiwxXV0sIlNlbGVjdGVkVmFsdWUiOiIxMDowMCJ9LCJGb3JtMl9jdGwwNl9sYmxFbmREYXRlIjp7IlRleHQiOiI3OjUw6IezODozNSJ9fQ=="));
		nvps.add(new BasicNameValuePair("X_TARGET","ContentPanel1_btnBespeak"));
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try{
				HttpEntity Entity = response.getEntity();
				InputStreamReader ir = new InputStreamReader(Entity.getContent(),"UTF-8");
				BufferedReader br = new BufferedReader(ir);
				String line = "";
				StringBuffer result = new StringBuffer();
				while((line=br.readLine())!=null)
					result = result.append(line);
				
				return result;
				
			}finally{
				response.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Reservation(User user) {
		super();
		this.user = user;
	}
	
}
