package com.joy.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

	/**
	 * Method:append(File file,String content) Description: 在文件尾追加内容
	 * 
	 * @param file
	 * @param content
	 *            return boolean
	 */
	public static final boolean append(File file, String content) {
		return append(file.getAbsolutePath(), content);
	}

	/**
	 * Method:append(String file,String content) Description: 在文件尾追加内容
	 * 
	 * @param file
	 * @param content
	 *            return boolean
	 */
	public static final boolean append(String file, String content) {
		FileOutputStream fos = null;
		FileChannel fc = null;

		try {
			fos = new FileOutputStream(new File(file), true);
			fc = fos.getChannel();
			fc.position(fc.size());
			fc.write(ByteBuffer.wrap(content.getBytes()));
			
			fc.close();
			fos.close();
			return true;
		} catch (Exception e) {
			try {
				fc.close();
				fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method:exist(String file) Description: 查文/目录是否存在
	 * 
	 * @param file
	 *            文件/目录路径 return boolean
	 */
	public static final boolean exist(String file) {
		boolean bRet = false;
		if (file != null && !file.equals("")) {
			File newFile = new File(file);
			bRet = newFile.exists(); // 查父目录是否存在
		}
		return bRet;
	}

	/**
	 * Method:append(String file,String content) Description: 删除文件
	 * 
	 * @param file
	 *            文件路径
	 * @param content
	 *            return boolean
	 */
	public static void delete(String file) throws Exception {
		if (!exist(file))
			return;
		File pFile = new File(file);
		pFile.delete();
	}

	/**
	 * delete all files and directorys.
	 * 
	 * @param dir
	 *            the directory that will be deleted.
	 */
	public static void deleteDir(String dir) throws Exception {
		File directory = new File(dir);
		deleteDir(directory);
	}

	/**
	 * delete all files and directorys.
	 * 
	 * @param dir
	 *            the directory that will be deleted.
	 */
	public static void deleteDir(File dir) throws Exception {
		if (!dir.exists()) {
			return;
		}

		String[] filelist = dir.list();
		File tmpFile = null;
		for (int i = 0; i < filelist.length; i++) {
			tmpFile = new File(dir.getAbsolutePath(), filelist[i]);
			if (tmpFile.isDirectory()) {
				deleteDir(tmpFile);
			} else if (tmpFile.isFile()) {
				try {
					tmpFile.delete();
				} catch (Exception ex) {
					throw new Exception(tmpFile.toString()
							+ " can not be deleted " + ex.getMessage());
				}
			}
		}
		try {
			dir.delete();
		} catch (Exception ex) {
			throw new Exception(dir.toString() + " can not be deleted "
					+ ex.getMessage());
		} finally {
			filelist = null;
		}
	}
	
	/**
	 * Method:makedir(String dirPath)
	 * Description: 创建多级目录
	 *
	 * @param cstrPath 目录路径
	 *                 return boolean
	 */
	public static final boolean makedir(String dirPath) {
		boolean bRet = true;
		String lpcstrParent;

		String cstrParent;
		int iPos = 0;
//		int iLen;

		if (dirPath == null || dirPath.equals("")) return false;

//		iLen = dirPath.length();
		if (dirPath.lastIndexOf('\\') != -1) iPos = dirPath.lastIndexOf('\\');
		else iPos = dirPath.lastIndexOf('/');

		cstrParent = dirPath.substring(0, iPos);

		if (cstrParent == null || cstrParent.equals("")) return false; // 目录名称错误
		lpcstrParent = cstrParent.substring(0, cstrParent.length());
		if (cstrParent.length() > 3) { // 如果长度小于3，表示为磁盘根目
			bRet = exist(lpcstrParent); // 查父目录是否存在
		}

		if (!bRet) bRet = makedir(lpcstrParent); // 父目录不存在,递归调用创建父目

		if (bRet) { // 父目录存,直接创建目录
			File newDir = new File(dirPath);
			if (!newDir.exists()) {
				newDir.mkdirs(); //创建路径
				bRet = true;
			}
		}
		return bRet;
	}

	/**
	 * Method:getFileSuffix(String strFile)
	 * Description: 获得文件扩展
	 *
	 * @param strFile 文件名称
	 *                return String 扩展
	 */
	public static final String getFileSuffix(String strFile) {
		String strExtName = "";
		int pos = -1;
		pos = strFile.lastIndexOf(".");
		if (pos == strFile.length()) strExtName = "dat";
		else strExtName = strFile.substring(pos + 1);

		return strExtName;
	}
	
	/**
	 * Method:getExtName(String strFile)
	 * Description: 获得文件扩展
	 *
	 * @param strFile 文件名称
	 *                return String 扩展
	 */
	public static final String getFileName(String strFile) {
		String strFileName = "";
		int pos = -1;
		pos = strFile.lastIndexOf(".");
		if (pos == strFile.length()) strFileName = strFile;
		else strFileName = strFile.substring(0, pos);

		return strFileName;
	}
	
	/**
	 * 获取当前路径
	 *
	 * @return 当前路径.
	 */
	public static String getCurrentDirectory() {
		try {
			return (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Rename file name
	 *
	 * @param filename1 old name of file
	 * @param filename2 new name of file
	 */
	public static void rename(String filename1, String filename2) throws Exception {
		File oldFile = new File(filename1);
		File newFile = new File(filename2);
		try {
			oldFile.renameTo(newFile);
		} catch (Exception ex) {
			throw new Exception("Can not rename" + filename1 + " to " +
					filename2 +
					ex.getMessage());
		}
	}
	
	/**
	 * copy file
	 *
	 * @param sourcefile source file to copy from
	 * @param destfile   destination file
	 */
	@SuppressWarnings("resource")
	public static void copy_recover(String sourcefile, String destfile) {
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		FileChannel in = null;
		FileChannel out = null;
				
		try {
			fis = new FileInputStream(sourcefile);
			fos = new FileOutputStream(destfile);
			
			in = fis.getChannel();
			out = fos.getChannel();
			ByteBuffer  buffer = ByteBuffer.allocate(1024*8);
			
			while(in.read(buffer) != -1){
				buffer.flip();
				out.write(buffer);
				buffer.clear();
			}
			
			out.close();
			in.close();
		} catch (Exception e) {
			try {
				out.close();
				in.close();
				
				fos.close();
				fis.close();
			} catch (Exception e2) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos); // 获得后缀
	}

	public static void main(String[] args) {
		// test for method append
		append("/Users/shenghui555/Downloads/test.txt", "\n测试文字");
		
		// test for method copy
		try {
			copy_recover("/Users/shenghui555/Downloads/test.txt","/Users/shenghui555/Downloads/test_bak.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
