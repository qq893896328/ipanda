package cn.cntv.play.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

public class CBoxFileUtil {
	private static final String APP_FILE_Directory = "cntv.cn";
	private static final String XML_REMOTE_URL = "http://dispatch.3g.cntv.cn/approve/getchannel_area_shadow_all";
	private static final String NOW_PLAY_JSON_URL = "http://epg.app.cntv.cn/approve/livep";
	private static final String EPG_JSON_URL = "http://epg.app.cntv.cn/approve/epginfo";
	private static final String IMAGE_REMOTE_URL = "http://t.live.cntv.cn/newp2pb/images/ico/";
	private static final String XML_FILE_NAME = "channel.xml";
	private static final int XML = 0;
	private static final int IMAGE = 1;
	
	private static final int TRY_NUMBER = 5;
	
	/**
	 * ��ȡXMl 
	 */
	public static String getDataPath(){
		return Environment.getDataDirectory().getAbsolutePath() + "/data/";
	}

	public static InputStream getXmlStream(){
		String localFile = getXmlPath()+XML_FILE_NAME;
		String remoteFile = XML_REMOTE_URL;
		
		if(fileIsExist(localFile)){
			CBoxLog.w("LocalFileGet");
		}else{
			CBoxLog.w("RemoteFileGet");
			InputStream is = null;
			int i = 0 ;
			while(i < TRY_NUMBER){
			  is = getRemoteFile(remoteFile);
			  if(is != null)break;
			  i++;
			  SystemClock.sleep(2000);
			}
			if(is == null){
				CBoxLog.e("NET ERROR");
			}else{
			  saveFile(is,XML_FILE_NAME,XML);
			}
		}
		return getLocalFile(localFile);
	}
	/**
	 * ��ȡ���ڲ�����Ŀ��json��� 
	 */
	public static String getNowJson(){
		InputStream is = null;	
		int i = 0 ;
		while(i < TRY_NUMBER){
		  is = getRemoteFile(NOW_PLAY_JSON_URL);
		  if(is != null)break;
		  i++;
		  SystemClock.sleep(2000);
		}
		if(is != null){
			try {
				return InputStreamTOString(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * ��ȡEPG��json��� 
	 */
	
	public static String getEPGJson(String path){
		InputStream is = null;	
		int i = 0 ;
		while(i < TRY_NUMBER*3){
		  is = getRemoteFile(EPG_JSON_URL+"?"+path);
		  if(is != null)break;
		  i++;
		  SystemClock.sleep(2000);
		}
		if(is != null){
			try {
				return InputStreamTOString(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * ��ȡԶ���ļ� 
	 */
	@SuppressWarnings("unused")
	public static InputStream getRemoteFile(String u){
		InputStream inputStream = null;
		if(TextUtils.isEmpty(u))return null;
		try{
		 URL url = new URL(u);
         HttpURLConnection urlConnection = (HttpURLConnection) url
                 .openConnection();
         CBoxLog.e("Connection:"+urlConnection);
         urlConnection.setRequestProperty("user-agent","mozilla/4.0 (compatible; msie 6.0; windows 2000)"); 
         urlConnection.setRequestMethod("GET");
         urlConnection.setDoOutput(false);
         urlConnection.connect();
         inputStream = urlConnection.getInputStream();
		}catch (MalformedURLException e) {
            CBoxLog.e("MalformedURLException:"+e.toString());
            return null;
         } catch (IOException e) {
        	 CBoxLog.e("IOException"+e.toString());
        	 return null;
          }catch(Exception e){
        	  CBoxLog.e("Exception"+e.toString());
        	  return null;
        	  
          }
		return inputStream;
	}
	/**
	 * ��ȡ�����ļ� 
	 */
	@SuppressWarnings("unused")
	public static InputStream getLocalFile(String filePath){
		if (null == filePath) {
			CBoxLog.e("Invalid param. filePath: " + filePath);
			return null;
		}
		InputStream is = null;
		try {
			if (fileIsExist(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			CBoxLog.e("Exception, ex: " + ex.toString());
			return null;
		}
		return is;
	}
	/**
	 * �����ļ� 
	 * @throws Exception 
	 */
	
	public static boolean saveFile(InputStream inputStream,String fileName,int fileType){
	
		if(inputStream == null||TextUtils.isEmpty(fileName))return false;
		String path = getFilePath(fileType);
		File file = new File(path+fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try{
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			fileOutputStream.write(InputStreamToByte(inputStream));
			fileOutputStream.flush();
			fileOutputStream.close();		
			return true;
		}catch(Exception e){}
		return false;
	}
	
	/**
	 *  ����Ŀ¼
	 */
	public static boolean createDirectory(String filePath){
		if (null == filePath) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists()){
			return true;
		}	
		return file.mkdirs();
	}
	
	/**
	 * ɾ��Ŀ¼ 
	 */
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			CBoxLog.e("Invalid param. filePath: " + filePath);
			return false;
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}
		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				CBoxLog.d("delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}
		CBoxLog.d("delete filePath: " + file.getAbsolutePath());
		file.delete();
		return true;
	}	
	
	/**
	 * ����ļ��Ƿ���� 
	 */
	
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			CBoxLog.e("param invalid, filePath: " + filePath);
			return false;
		}
		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}
	
    /**
     * �ж�SD���Ƿ���� 
     */
    public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		return true;
	}
	
    /**
     *  ��ȡroot�ļ�·�� 
     */
	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
		}
	}
	/**
     *  ��ȡapp�ļ���·�� 
     */
	public static String getAppFilePath() {
			return getRootFilePath()+APP_FILE_Directory;
	}
	/**
	 * ��ȡapp�洢Ӧ���ļ�·�� 
	 */
	public static String getFilePath(int type) {
		String typeDirectory = "";
		switch(type){
			case XML:
				typeDirectory = "xml/";
				break;
			case IMAGE:
				typeDirectory = "image/";
				break;
			default:
				typeDirectory = "";
				break;
		}
		return getAppFilePath()+"/"+typeDirectory;
	}
	/**
	 * XML·�� 
	 */
	public static String getXmlPath(){
		return getFilePath(XML);
	}
	/**
	 * Image·�� 
	 */
	public static String getImagePath(){
		return getFilePath(IMAGE);
	}
	
	public static String getRemoteImageUrl(){
		return IMAGE_REMOTE_URL;
	}
/**
 * Stream ת�� Byte
 */	
	private static  byte[] InputStreamToByte(InputStream is) throws IOException {
		   ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		   int ch;
		   while ((ch = is.read()) != -1) {
		    bytestream.write(ch);
		   }
		   byte imgdata[] = bytestream.toByteArray();
		   bytestream.close();
		   return imgdata;
     }
	/**
	 * ��InputStreamת����String
	 * @param in InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String InputStreamTOString(InputStream in) throws Exception{
		int BUFFER_SIZE = 4096;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while((count = in.read(data,0,BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		
		data = null;
		return new String(outStream.toByteArray(),"UTF-8");
	}
	
}
