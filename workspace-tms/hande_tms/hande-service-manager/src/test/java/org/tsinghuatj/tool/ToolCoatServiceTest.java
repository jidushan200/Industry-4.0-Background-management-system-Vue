package org.tsinghuatj.tool;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolProcess;
import org.tsinghuatj.tool.service.IToolCoatService;
import org.tsinghuatj.tool.service.IToolProcessService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToolCoatServiceTest {

	private @Autowired(required = false) IToolProcessService toolProcessService;
	private @Autowired(required = false) IToolCoatService toolCoatService;

	@Test
	public void toolLogTest() throws BusinessException {

		List<ToolCoat> list = toolCoatService.select(2666l, new ToolCoat());
		for (ToolCoat item : list) {
			ToolProcess where = new ToolProcess();
			where.setFullNumber(item.getFullNumber());
			where.setCreateTime(item.getCoatTime());
			ToolProcess toolProcess = toolProcessService.selectLatestProcess(2666l, where);
			if (null != toolProcess) {
				System.out.println(toolProcess.getFullNumber() + " " + toolProcess.getProcessQty());
				item.setProcessQty(toolProcess.getProcessQty());
				toolCoatService.updateActiveById(2666l, item, item.getPkId());
			}
		}

	}

}
