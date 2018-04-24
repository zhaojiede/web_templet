package com.joy.common.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * 图片工具类， 图片水印，文字水印，缩放，补白等
 * 
 * @author Carl He
 */
public final class ImageUtils {
	/** 图片格式：JPG */
	private static final String PICTRUE_FORMATE_JPG = "jpg";

	private ImageUtils() {
	}

	private static BufferedImage makeThumbnail(Image img, int width, int height) {
		BufferedImage tag = new BufferedImage(width, height, 1);
		Graphics g = tag.getGraphics();
		g.drawImage(img.getScaledInstance(width, height, 4), 0, 0, null);
		g.dispose();
		return tag;
	}

	private static void saveSubImage(BufferedImage image, Rectangle subImageBounds, File subImageFile) throws IOException {
		String fileName = subImageFile.getName();
		String formatName = fileName.substring(fileName.lastIndexOf(46) + 1);
		BufferedImage subImage = new BufferedImage(subImageBounds.width, subImageBounds.height, 1);
		Graphics g = subImage.getGraphics();
		if ((subImageBounds.width > image.getWidth()) || (subImageBounds.height > image.getHeight())) {
			int left = subImageBounds.x;
			int top = subImageBounds.y;
			if (image.getWidth() < subImageBounds.width) {
				left = (subImageBounds.width - image.getWidth()) / 2;
			}
			if (image.getHeight() < subImageBounds.height) {
				top = (subImageBounds.height - image.getHeight()) / 2;
			}
			g.setColor(Color.white);
			g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
			g.drawImage(image, left, top, null);
			ImageIO.write(image, formatName, subImageFile);
			//			System.out.println("if is running left:" + left + " top: " + top);
		} else {
			g.drawImage(
				image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height), 0,
				0, null);
			//			System.out.println("else is running");
		}
		g.dispose();
		ImageIO.write(subImage, formatName, subImageFile);
	}

	public static void cut(String srcImageFile, String descDir, int width, int height, Rectangle rect) throws IOException {
		Image image = ImageIO.read(new File(srcImageFile));
		BufferedImage bImage = makeThumbnail(image, width, height);

		saveSubImage(bImage, rect, new File(descDir));
	}

	public static void cut(File srcImageFile, File descDir, int width, int height, Rectangle rect) throws IOException {
		Image image = ImageIO.read(srcImageFile);
		BufferedImage bImage = makeThumbnail(image, width, height);

		saveSubImage(bImage, rect, descDir);
	}

	private static void saveSubImage(File srcImageFile, File descDir, int width, int height, Rectangle rect) throws IOException {
		cut(srcImageFile, descDir, width, height, rect);
	}

	public static void makeThumnail(InputStream srcImgInputStream, String dstImageFileName, int nw) throws IOException{
		File fo = new File(dstImageFileName); //将要转换出的小图文件
		/*
		AffineTransform 类表示 2D 仿射变换，它执行从 2D 坐标到其他 2D
		坐项目线性映射，保留了线的“直线性”和“平行性”。可以使用一系
		列平移、缩放、翻转、旋转和剪切来构造仿射变换。
		*/
		AffineTransform transform = new AffineTransform();
		BufferedImage bis = ImageIO.read(srcImgInputStream); //读取图片
		int w = bis.getWidth();
		int h = bis.getHeight();
		//double scale = (double)w/h;
		
		int nh = (nw * h) / w;
		double sx = 1;
		double sy = 1;
		
		if(w > nw){
			nh = (nw * h) / w;
			sx = (double)nw / w;
			sy = (double)nh / h;
		}
		
		transform.setToScale(sx, sy); //setToScale(double sx, double sy) 将此变换设置为缩放变换。
//		System.out.println(w + " " + h);
		/*
		 * AffineTransformOp类使用仿射转换来执行从源图像或 Raster 中 2D 坐标到目标图像或
		 *  Raster 中 2D 坐项目线性映射。所使用的插值类型由构造方法通过
		 *  一个 RenderingHints 对象或通过此类中定义的整数插值类型之一来指定。
		如果在构造方法中指定了 RenderingHints 对象，则使用插值提示和呈现
		的质量提示为此操作设置插值类型。要求进行颜色转换时，可以使用颜色
		呈现提示和抖动提示。 注意，务必要满足以下约束：源图像与目标图像
		必须不同。 对于 Raster 对象，源图像中的 band 数必须等于目标图像中
		的 band 数。
		*/
		AffineTransformOp ato = new AffineTransformOp(transform, null);
		BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
		/*
		 * TYPE_3BYTE_BGR 表示一个具有 8 位 RGB 颜色分量的图像，
		 * 对应于 Windows 风格的 BGR 颜色模型，具有用 3 字节存
		 * 储的 Blue、Green 和 Red 三种颜色。
		*/
		ato.filter(bis, bid);
		ImageIO.write(bid, "jpeg", fo);
	}
	
	public static HashMap<String, Integer> makeThumnail(InputStream srcImgInputStream, String dstImageFileName, int tw, int th) throws IOException{
		HashMap<String, Integer> rsltMap = new HashMap<String, Integer>();
		File fo = new File(dstImageFileName); //将要转换出的小图文件
		/*
		AffineTransform 类表示 2D 仿射变换，它执行从 2D 坐标到其他 2D
		坐项目线性映射，保留了线的“直线性”和“平行性”。可以使用一系
		列平移、缩放、翻转、旋转和剪切来构造仿射变换。
		*/
		AffineTransform transform = new AffineTransform();
		BufferedImage bis = ImageIO.read(srcImgInputStream); //读取图片
		int w = bis.getWidth();
		int h = bis.getHeight();
		//double scale = (double)w/h;
		
		double wRt = (double)tw/w;
		double hRt = (double)th/w;
		
		double sx = 1;
		double sy = 1;
		
		int nw = 0;
		int nh = 0;
		if(wRt <= hRt){
			nw = tw;
			nh = (tw * h) / w;
			
			if(w > tw){
				sx = (double)nw / w;
				sy = (double)nh / h;
			}
		} else {
			nw = (th*w) / h;
			nh = th;
			
			if(h > th){
				sx = (double)nw / w;
				sy = (double)nh / h;
			}
		}
		
		transform.setToScale(sx, sy); //setToScale(double sx, double sy) 将此变换设置为缩放变换。
//		System.out.println(w + " " + h);
		/*
		 * AffineTransformOp类使用仿射转换来执行从源图像或 Raster 中 2D 坐标到目标图像或
		 *  Raster 中 2D 坐项目线性映射。所使用的插值类型由构造方法通过
		 *  一个 RenderingHints 对象或通过此类中定义的整数插值类型之一来指定。
		如果在构造方法中指定了 RenderingHints 对象，则使用插值提示和呈现
		的质量提示为此操作设置插值类型。要求进行颜色转换时，可以使用颜色
		呈现提示和抖动提示。 注意，务必要满足以下约束：源图像与目标图像
		必须不同。 对于 Raster 对象，源图像中的 band 数必须等于目标图像中
		的 band 数。
		*/
		AffineTransformOp ato = new AffineTransformOp(transform, null);
		BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
		rsltMap.put("width", nw);
		rsltMap.put("height", nh);
		/*
		 * TYPE_3BYTE_BGR 表示一个具有 8 位 RGB 颜色分量的图像，
		 * 对应于 Windows 风格的 BGR 颜色模型，具有用 3 字节存
		 * 储的 Blue、Green 和 Red 三种颜色。
		*/
		ato.filter(bis, bid);
		ImageIO.write(bid, "jpeg", fo);
		return rsltMap;
	}
	
	public static void main(String[] args) throws IOException {
		//		Image image = ImageIO.read(new File("/Users/shenghui555/Documents/flowers.jpeg"));
		//		// 图片实际宽度：
		//		int imageWidth = 500;
		//		// 图片实际高度：
		//		int imageHeight = 370;
		//		// 距离顶部：
		//		int cutTop = 92;
		//		// 距离左边
		//		int cutLeft = 86;
		//		// 截取框的宽
		//		int dropWidth = 125;
		//		// 截取框的高
		//		int dropHeight = 125;
		//		
		////		float imageZoom = 10;
		//
		//		Rectangle rec = new Rectangle(cutLeft, cutTop, dropWidth, dropHeight);
		//		File srcImageFile = new File("/Users/shenghui555/Documents/flowers.jpg");
		//		File descDir = new File("/Users/shenghui555/Documents/flowers_1.jpg");
		//
		//		saveSubImage(srcImageFile, descDir, imageWidth, imageHeight, rec);
		File file = new File("/Users/shenghui555/Documents/flowers.jpeg");
		FileInputStream  fis = new FileInputStream(file);
		makeThumnail(fis,"/Users/shenghui555/Documents/flowers_thumb3.jpeg",200);
	}
}
