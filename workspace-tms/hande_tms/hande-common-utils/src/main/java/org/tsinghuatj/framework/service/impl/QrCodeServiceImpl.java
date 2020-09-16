package org.tsinghuatj.framework.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.tsinghuatj.framework.service.IQrCodeService;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Service
public class QrCodeServiceImpl implements IQrCodeService {
	private static final int QRCOLOR = 0xFF000000; // 默认是黑色
	private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

	private static final int WIDTH = 400; // 二维码宽
	private static final int HEIGHT = 400; // 二维码高

	// 用于设置QR二维码参数
	private Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
			put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
			put(EncodeHintType.MARGIN, 0);
		}
	};

	@Override
	public void drawLogoQRCode(File logoFile, File codeFile, String qrUrl, String note) throws Exception {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

		BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
			}
		}
		int width = image.getWidth();
		int height = image.getHeight();
		if (Objects.nonNull(logoFile) && logoFile.exists()) {

			Graphics2D g = image.createGraphics();

			BufferedImage logo = ImageIO.read(logoFile);

			g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
			g.dispose();
			logo.flush();
		}

		if (org.tsinghuatj.framework.utils.StringUtils.isNotBlank(note)) {

			BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D outg = outImage.createGraphics();

			outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

			outg.setColor(Color.BLACK);
			outg.setFont(new Font("楷体", Font.BOLD, 30)); // 字体、字型、字号
			int strWidth = outg.getFontMetrics().stringWidth(note);
			if (strWidth > 399) {

				String note1 = note.substring(0, note.length() / 2);
				String note2 = note.substring(note.length() / 2, note.length());
				int strWidth1 = outg.getFontMetrics().stringWidth(note1);
				int strWidth2 = outg.getFontMetrics().stringWidth(note2);
				outg.drawString(note1, 200 - strWidth1 / 2, height + (outImage.getHeight() - height) / 2 + 12);
				BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D outg2 = outImage2.createGraphics();
				outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
				outg2.setColor(Color.BLACK);
				outg2.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
				outg2.drawString(note2, 200 - strWidth2 / 2, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
				outg2.dispose();
				outImage2.flush();
				outImage = outImage2;
			} else {
				outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12); // 画文字
			}
			outg.dispose();
			outImage.flush();
			image = outImage;
		}

		image.flush();

		ImageIO.write(image, "png", codeFile); // TODO

	}

}
