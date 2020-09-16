package org.tsinghuatj.tool;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.excel.ExcelUtils;
import org.tsinghuatj.sys.domain.SysAuthInfo;
import org.tsinghuatj.sys.service.ISysAuthInfoService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysAuthImportTest {

	private @Autowired(required = false) ISysAuthInfoService sysAuthInfoService;

	private List<SysAuthInfo> read() {
		// 基于注解,将Excel内容读至List<SysAuthInfo>对象内
		List<SysAuthInfo> sysAuthList = null;
		try (InputStream inputStream = new ClassPathResource("auth.xlsx").getInputStream()) {
			sysAuthList = ExcelUtils.getInstance().readExcel2Objects(inputStream, SysAuthInfo.class, 1, 200, 0);
			return sysAuthList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 导入权限
	@Test
	public void importTest() throws BusinessException {
		List<SysAuthInfo> sysAuthList = read();
		sysAuthInfoService.batchInsert(1l, sysAuthList);
	}

	/*// 导出权限
	// @Test
	public void exportTest() throws BusinessException {
		List<SysAuthInfo> sysAuthList = sysAuthInfoService.select(1l, new SysAuthInfo());
		for (SysAuthInfo sysAuthInfo : sysAuthList) {
			for (SysAuthInfo auth : sysAuthList) {
				if (sysAuthInfo.getParentId().equals(auth.getPkId())) {
					sysAuthInfo.setParentAuthCode(auth.getAuthorCode());
					break;
				}
			}
		}
		try {
			ExcelUtils.getInstance().exportObjects2Excel(sysAuthList, SysAuthInfo.class, true, null, true, "D:/auth.xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
}
