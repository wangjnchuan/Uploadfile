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
	 * �ϴ������ļ���������
	 */
	public static void keyi() {
		String targetURL = null; //�C ָ��URL 
		File targetFile = null; //�C ָ���ϴ��ļ� 
		targetFile = new File("E:\\RSplatform\\userdata\\undefined\\�Ϻ���Ŀ��¡.txt"); 
		targetURL = "http://192.168.1.111:8080/servletTest/UploadServlet"; // servleturl 
		PostMethod filePost = new PostMethod(targetURL);       
		try { 
		//ͨ�����·�������ģ��ҳ������ύ 
		//filePost.setParameter(��name��, �����ġ�); 
		//filePost.setParameter(��pass��, ��1234��); 
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
		System.out.println("�ϴ��ɹ�");// �ϴ��ɹ� 
		} else { 
		System.out.println("�ϴ�ʧ��");// �ϴ�ʧ�� 
		} 
		} catch (Exception ex) { 
		ex.printStackTrace(); 
		} finally { 
		filePost.releaseConnection(); 
		} 
	}	
	/**
	 * @param targetFile
	 * ���ֵ����ļ�
	 * @throws FileNotFoundException
	 * @throws IOException
	 * �����ֵ����ļ��ϴ���������
	 */
	public static void uploadFile(String targetFile) throws FileNotFoundException, IOException {
		//String targetURL = null; //�C ָ��URL 
		//File targetFile = null; //�C ָ���ϴ��ļ� 
		//targetFile = new File("E:\\RSplatform\\userdata\\undefined\\afer.jpeg"); 
		String targetURL = "http://192.168.1.111:8080/servletTest/UploadServlet"; // servleturl 
		PostMethod filePost = new PostMethod(targetURL) {
			public String getRequestCharSet() {
				return "UTF-8";
    }};
		try { 
		//ͨ�����·�������ģ��ҳ������ύ 
		//filePost.setParameter(��name��, �����ġ�); 
		//filePost.setParameter(��pass��, ��1234��);
		File file = new File(targetFile);
		Part[] parts = { new CustomFilePart(file.getName(), file) }; 
		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams())); 
		HttpClient client = new HttpClient(); 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000); 
		int status = client.executeMethod(filePost); 
		if (status == HttpStatus.SC_OK) { 
		System.out.println("�ϴ��ɹ�");// �ϴ��ɹ� 
		} else { 
		System.out.println("�ϴ�ʧ��");// �ϴ�ʧ�� 
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
	 * ����ĳ���ļ��У��������ļ����֣��������Ӻ����������ļ�
	 * Ȼ�����uploadFile�ϴ���������
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
					     	  File file = new File(path);//pathΪ�����ļ��� 
					     	  File[] files = file.listFiles();
							  System.out.println("�ȴ����ͼƬ��");
					     	  key = watchService.take();//û���ļ�����ʱ������������
							  for (WatchEvent<?> event : key.pollEvents()) {
								  String fileName = path+"\\"+event.context();
								  System.out.println("������ͼƬ���ļ�·�� "+fileName);
								  File file1 = new File(fileName);
								  //File file1 = files[files.length-1];//��ȡ�����ļ�
								  System.out.println("�����ļ����� "+file1.getName());//���ݺ�׺�ж�
								  uploadFile(fileName);//�ϴ�������
							  }if (!key.reset()) {
								  break; //�ж�ѭ��
							  }
						}
				  }catch (Exception e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
		       }
			}, 2000 , 10000);//��һ������2000��ʾ��2000ms�Ժ�����ʱ��,�ڶ�������3000����ʾ3000ms������һ��run  
		}
	
}


