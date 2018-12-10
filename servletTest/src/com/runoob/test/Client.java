package com.runoob.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class Client {

	public static void main(String args[]) { 
		try {
			getFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//keyi();
		} 
	/////
	/**
	 * 上传单个文件至服务器
	 */
	public static void keyi() {
		String targetURL = null; //– 指定URL 
		File targetFile = null; //– 指定上传文件 
		targetFile = new File("E:\\RSplatform\\userdata\\undefined\\南海项目克隆.txt"); 
		targetURL = "http://192.168.1.111:8080/servletTest/UploadServlet"; // servleturl 
		PostMethod filePost = new PostMethod(targetURL);       
		try { 
		//通过以下方法可以模拟页面参数提交 
		//filePost.setParameter(“name”, “中文”); 
		//filePost.setParameter(“pass”, “1234”); 
		///String targetFilename= new String(targetFile.getName().getBytes("GBK"),"utf-8");
		FilePart part = new CustomFilePart(targetFile.getName(), targetFile);
		
		//part.setCharSet("utf-8");
		System.out.println(part.getCharSet()); 
		Part[] parts = { part };
		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams())); 
		HttpClient client = new HttpClient(); 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000); 
		int status = client.executeMethod(filePost); 
		if (status == HttpStatus.SC_OK) { 
		System.out.println("上传成功");// 上传成功 
		} else { 
		System.out.println("上传失败");// 上传失败 
		} 
		} catch (Exception ex) { 
		ex.printStackTrace(); 
		} finally { 
		filePost.releaseConnection(); 
		} 
	}	
	/**
	 * @param targetFile
	 * 发现的新文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 将发现的新文件上传至服务器
	 */
	public static void uploadFile(String targetFile) throws FileNotFoundException, IOException {
		//String targetURL = null; //– 指定URL 
		//File targetFile = null; //– 指定上传文件 
		//targetFile = new File("E:\\RSplatform\\userdata\\undefined\\afer.jpeg"); 
		String targetURL = "http://192.168.1.111:8080/servletTest/UploadServlet"; // servleturl 
		PostMethod filePost = new PostMethod(targetURL) {
			public String getRequestCharSet() {
				return "UTF-8";
    }};
		try { 
		//通过以下方法可以模拟页面参数提交 
		//filePost.setParameter(“name”, “中文”); 
		//filePost.setParameter(“pass”, “1234”);
		File file = new File(targetFile);
		Part[] parts = { new CustomFilePart(file.getName(), file) }; 
		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams())); 
		HttpClient client = new HttpClient(); 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000); 
		int status = client.executeMethod(filePost); 
		if (status == HttpStatus.SC_OK) { 
		System.out.println("上传成功");// 上传成功 
		} else { 
		System.out.println("上传失败");// 上传失败 
		} 
		} catch (Exception ex) { 
		ex.printStackTrace(); 
		} finally { 
		filePost.releaseConnection(); 
		} 
	  }
	//System.getProperty("catalina.home")+"
	//final static String uploadAddres = "http://localhost:8080/servletTest/UploadServlet";
	private static String path = "E:\\Kankan";
	/**
	 * 监听某个文件夹，发现新文件出现，包括增加和重命名的文件
	 * 然后调用uploadFile上传至服务器
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void getFile() throws FileNotFoundException, IOException{
	  final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {	 
				 WatchKey key;
				 try {
					WatchService watchService = FileSystems.getDefault().newWatchService();
					Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
					     while (true) {
					     	  File file = new File(path);//path为监听文件夹 
					     	  File[] files = file.listFiles();
							  System.out.println("等待添加图片！");
					     	  key = watchService.take();//没有文件增加时，阻塞在这里
							  for (WatchEvent<?> event : key.pollEvents()) {
								  String fileName = path+"\\"+event.context();
								  System.out.println("新增加图片的文件路径 "+fileName);
								  File file1 = new File(fileName);
								  //File file1 = files[files.length-1];//获取最新文件
								  System.out.println("最新文件名： "+file1.getName());//根据后缀判断
								  uploadFile(fileName);//上传服务器
							  }if (!key.reset()) {
								  break; //中断循环
							  }
						}
				  }catch (Exception e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
		       }
			}, 2000 , 10000);//第一个数字2000表示，2000ms以后开启定时器,第二个数字3000，表示3000ms后运行一次run  
		}
	
}


