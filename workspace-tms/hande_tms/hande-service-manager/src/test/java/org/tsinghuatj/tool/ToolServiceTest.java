package org.tsinghuatj.tool;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolCheck;
import org.tsinghuatj.tool.domain.ToolOperLog;
import org.tsinghuatj.tool.service.IToolCheckService;
import org.tsinghuatj.tool.service.IToolOperLogService;
import org.tsinghuatj.tool.service.IToolOutboundService;
import org.tsinghuatj.tool.service.IToolProcessService;
import org.tsinghuatj.tool.service.IToolRepairService;
import org.tsinghuatj.tool.service.IToolService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToolServiceTest {
	private @Autowired(required = false) IToolService toolService;
	private @Autowired(required = false) IToolOutboundService toolOutboundService;
	private @Autowired(required = false) IToolOperLogService toolOperLogService;
	private @Autowired(required = false) IToolProcessService toolProcessService;
	private @Autowired(required = false) IToolRepairService toolRepairService;
	private @Autowired(required = false) IToolCheckService toolCheckService;

	@Test
	public void toolLogTest() throws BusinessException {
		/*
		 * List<Tool> list = toolService.select(2666l, new Tool()); for (Tool
		 * tool : list) { ToolOperLog toolOperLog = new ToolOperLog();
		 * toolOperLog.setCreateTime(tool.getCreateTime());
		 * toolOperLog.setCreateUser(tool.getCreateUser());
		 * toolOperLog.setOperType(1); toolOperLog.setOperateInfo("期初数据导入");
		 * toolOperLog.setFullNumber(tool.getFullNumber());
		 * toolOperLog.setToolNumber(tool.getToolNumber());
		 * toolOperLogService.init(toolOperLog); }
		 */
		// List<ToolOutbound> list = toolOutboundService.select(2666l, new
		// ToolOutbound());
		// for (ToolOutbound outbound : list) {
		// ToolOperLog toolOperLog = new ToolOperLog();
		// toolOperLog.setCreateTime(outbound.getCreateTime());
		// toolOperLog.setCreateUser(outbound.getCreateUser());
		// if (outbound.getOutType() == 1) {
		// toolOperLog.setOperType(2);
		// toolOperLog.setOperateInfo("领用人("+outbound.getStaffCode()+")");
		// } else if (outbound.getOutType() == 2) {
		// toolOperLog.setOperType(4);
		// toolOperLog.setOperateInfo("领用人("+outbound.getStaffCode()+")");
		// } else if (outbound.getOutType() == 3) {
		// toolOperLog.setOperType(7);
		// toolOperLog.setOperateInfo("涂层厂家("+outbound.getSupplierName()+")");
		// }
		// Tool tool = toolService.selectById(2666l, outbound.getToolId());
		// toolOperLog.setFullNumber(tool.getFullNumber());
		// toolOperLog.setToolNumber(outbound.getToolNumber());
		// toolOperLogService.init(toolOperLog);
		// }
		// List<ToolProcess> list = toolProcessService.select(2666l, new
		// ToolProcess());
		// for (ToolProcess process : list) {
		// if (process.getTypeId() == 3) {
		// continue;
		// }
		// ToolOperLog toolOperLog = new ToolOperLog();
		// toolOperLog.setCreateTime(process.getCreateTime());
		// toolOperLog.setCreateUser(process.getCreateUser());
		// toolOperLog.setOperType(3);
		// toolOperLog.setOperateInfo("返库人(" + process.getStaffCode() + "))");
		// toolOperLog.setOperateRemark("制件[" + process.getPartCode() + "]加工数量["
		// + process.getProcessQty() + "]");
		// toolOperLog.setFullNumber(process.getFullNumber());
		// toolOperLog.setToolNumber(process.getToolNumber());
		// toolOperLogService.init(toolOperLog);
		// }

		// List<ToolRepair> list = toolRepairService.select(2666l, new
		// ToolRepair());
		// for (ToolRepair item : list) {
		// ToolOperLog toolOperLog = new ToolOperLog();
		// toolOperLog.setCreateTime(item.getCreateTime());
		// toolOperLog.setCreateUser(item.getCreateUser());
		// toolOperLog.setOperType(5);
		// toolOperLog.setOperateInfo("");
		// toolOperLog.setOperateRemark("");
		// toolOperLog.setFullNumber(item.getFullNumber());
		// toolOperLog.setToolNumber(item.getToolNumber());
		// toolOperLogService.init(toolOperLog);
		// }

		List<ToolCheck> list = toolCheckService.select(2666l, new ToolCheck());
		for (ToolCheck item : list) {
			ToolOperLog toolOperLog = new ToolOperLog();
			toolOperLog.setCreateTime(item.getCreateTime());
			toolOperLog.setCreateUser(item.getCreateUser());
			if (item.getCheckType() == 2) {
				toolOperLog.setOperType(6);
			} else {
				toolOperLog.setOperType(8);
			}			
			if (item.getCheckResult() == 1) {
				toolOperLog.setOperateInfo("质检合格");
			} else {
				toolOperLog.setOperateInfo("质检不合格");
			}
			toolOperLog.setOperateRemark("");
			toolOperLog.setFullNumber(item.getFullNumber());
			toolOperLog.setToolNumber(item.getToolNumber());
			toolOperLogService.init(toolOperLog);
		}

	}

}
