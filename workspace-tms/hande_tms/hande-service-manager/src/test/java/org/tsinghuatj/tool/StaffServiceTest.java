package org.tsinghuatj.tool;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.service.IStaffService;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureOutbound;
import org.tsinghuatj.measure.service.IMeasureOutboundService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StaffServiceTest {

	private @Autowired(required = false) IStaffService staffService;
	private @Autowired(required = false) IMeasureOutboundService measureOutBoundService;

	// @Test
	public void importTest() throws BusinessException {
		List<Staff> list = staffService.select(1l, new Staff());
		for (Staff staff : list) {
			staffService.staffSynchro(1l, staff.getStaffCode());
		}
	}

	@Test
	public void measureOutboundTest() throws BusinessException {
		MeasureOutbound where = new MeasureOutbound();
		where.setOutType(2);
		List<MeasureOutbound> list = measureOutBoundService.select(1l, where);
		for (MeasureOutbound out : list) {
			Staff staff = staffService.selectById(2666l, out.getKeeperId());
			out.setDepartmentId(staff.getDepartmentId());
			out.setDepartmentName(staff.getDepartmentName());
			out.setReceiveClass(staff.getTeamName());
			out.setReceiver(staff.getStaffName());
			measureOutBoundService.updateActiveById(2666l, out, out.getPkId());
		}
	}

}
