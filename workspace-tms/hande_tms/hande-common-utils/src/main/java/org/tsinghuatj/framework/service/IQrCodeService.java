package org.tsinghuatj.framework.service;

import java.io.File;

public interface IQrCodeService {
	 public void drawLogoQRCode(File logoFile, File codeFile, String qrUrl, String note) throws Exception;
}
