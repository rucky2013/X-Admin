package com.leaf.xadmin.utils.verify;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import org.apache.shiro.codec.Base64;

/**
 * 图片验证码生成工具
 * 
 * @author zwl
 *
 */
public class CycleImageUtil {
	
	private static String resultString;

	public static String drawCycleImageToBase64() {
		// 在内存中创建图象
		int width = 75, height = 25;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 生成随机类
		Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		// 画边框
		g.setColor(getRandColor(160, 200));
		g.drawRect(0, 0, width - 1, height - 1);
		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(4位数字)
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 13 * i + 14, 20);
		}
		// 保存生成结果
		CycleImageUtil.setResultString(sRand);
		// 返回图片编码结果
		return encodeImage(image);
	}

	/**
	 * 获取随机颜色值
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	/**
	 * 将图片进行Base64编码，返回编码后的结果
	 * 
	 * @param image 
	 * @return
	 * */
	public static String encodeImage(RenderedImage image) {
		//生成字节数组流，并将图片写入到字节数组流中
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  	
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//将字节数组流转换为字节数组，并调用编码方法返回结果值
		byte[] byteArray = baos.toByteArray();
		return Base64.encodeToString(byteArray);
	}

	public static String getResultString() {
		return resultString;
	}

	public static void setResultString(String resultString) {
		CycleImageUtil.resultString = resultString;
	}
}
