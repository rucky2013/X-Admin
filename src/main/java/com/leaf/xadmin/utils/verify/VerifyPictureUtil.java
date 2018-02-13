package com.leaf.xadmin.utils.verify;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

import lombok.Data;
import org.apache.shiro.codec.Base64;
import org.springframework.stereotype.Component;

/**
 * 图片验证码生成工具
 * 
 * @author leaf
 */
@Component
@Data
public class VerifyPictureUtil {
	private int lineNum = 155;
	private int codeNum = 4;
	private int colorRange = 255;

	public final static String VERIFY_ENCODE_RESULT = "VERIFY_ENCODE_RESULT";
	public final static String VERIFY_CODE_VALUE = "VERIFY_CODE_VALUE";

	public Map<String, Object> draw() {
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
		for (int i = 0; i < lineNum; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(4位数字)
		StringBuffer sRand = new StringBuffer();
		for (int i = 0; i < codeNum; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand.append(rand);
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 13 * i + 14, 20);
		}
		// 返回封装结果
		Map<String, Object> map = new HashMap<>(2);
		map.put(VERIFY_ENCODE_RESULT, encodeImage(image));
		map.put(VERIFY_CODE_VALUE, sRand);
		return map;
	}

	/**
	 * 获取指定范围内颜色值
	 *
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > colorRange) {
			fc = 255;
		}
		if (bc > colorRange) {
			bc = 255;
		}
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
	private String encodeImage(RenderedImage image) {
		// 生成字节数组流，并将图片写入到字节数组流中
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  	
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 将字节数组流转换为字节数组，并调用编码方法返回结果值
		byte[] byteArray = baos.toByteArray();
		return Base64.encodeToString(byteArray);
	}
}
