package org.tsinghuatj.fixture.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.fixture.domain.Fixture;
import org.tsinghuatj.fixture.domain.FixtureCheck;
import org.tsinghuatj.fixture.domain.FixtureScripApply;
import org.tsinghuatj.fixture.repository.FixtureCheckMapper;
import org.tsinghuatj.fixture.repository.FixtureMapper;
import org.tsinghuatj.fixture.repository.FixtureScripApplyMapper;
import org.tsinghuatj.fixture.service.IFixtureScripApplyService;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.SysMessage;
import org.tsinghuatj.sys.repository.SysMessageMapper;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.tool.domain.ToolApplyAudit;
import org.tsinghuatj.tool.repository.ToolApplyAuditMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * FixtureScripApply 表数据服务层接口实现类
 *
 */
@Service("fixtureScripApplyService")
public class FixtureScripApplyServiceImpl extends BaseServiceImpl implements IFixtureScripApplyService {

	private @Resource FixtureScripApplyMapper fixtureScripApplyMapper;
	private @Resource ToolApplyAuditMapper toolApplyAuditMapper;
	private @Resource SysMessageMapper sysMessageMapper;
	private @Resource SysUserAuthMapper sysUserAuthMapper;
	private @Resource FixtureCheckMapper fixtureCheckMapper;
	private @Resource FixtureMapper fixtureMapper;


	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, FixtureScripApply fixtureScripApply) throws BusinessException {
		Date date = new Date();
		fixtureScripApply.setPkId(getPkId());
		fixtureScripApply.setCreateTime(date);
		fixtureScripApply.setCreateUser(userId);
		fixtureScripApply.setUpdateTime(date);
		fixtureScripApply.setUpdateUser(userId);
		fixtureScripApply.setDelMark(0);
		if (null != fixtureScripApply.getCheckId()) {
			FixtureCheck check = new FixtureCheck();
			check.setPkId(fixtureScripApply.getCheckId());
			check.setUpdateTime(date);
			check.setUpdateUser(userId);
			check.setHandleResult(3);
			fixtureCheckMapper.updateActiveById(check, check.getPkId());
		}
		if(fixtureScripApply.getApplyStatus()==1){
			String message = fixtureScripApply.getDepartmentName()+fixtureScripApply.getApplierName()+"提交了物料条码为" + fixtureScripApply.getFixtureBarcode() + "的报废申请,请您及时审核";
			String authCode = "0105060401";	
			setMessage(message, userId, authCode);
		}
		return fixtureScripApplyMapper.insert(fixtureScripApply);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, FixtureScripApply fixtureScripApply, Long fixtureScripApplyId) throws BusinessException {
		fixtureScripApply.setUpdateTime(new Date());
		fixtureScripApply.setUpdateUser(userId);
		if(fixtureScripApply.getApplyStatus()==1){
			String message = fixtureScripApply.getDepartmentName()+fixtureScripApply.getApplierName()+"提交了物料条码为" + fixtureScripApply.getFixtureBarcode() + "的报废申请,请您及时审核";
			String authCode = "0105060401";	
			setMessage(message, userId, authCode);
		}
		return fixtureScripApplyMapper.updateActiveById(fixtureScripApply, fixtureScripApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureScripApply selectById(Long userId, Long fixtureScripApplyId) throws BusinessException {
		return fixtureScripApplyMapper.selectById(fixtureScripApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long fixtureScripApplyId) throws BusinessException {
		return fixtureScripApplyMapper.removeById(fixtureScripApplyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long fixtureScripApplyId) throws BusinessException {
		FixtureScripApply fixtureScripApply = new FixtureScripApply();
		fixtureScripApply.setPkId(fixtureScripApplyId);
		fixtureScripApply.setUpdateTime(new Date());
		fixtureScripApply.setUpdateUser(userId);
		return fixtureScripApplyMapper.deleteById(fixtureScripApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<FixtureScripApply> select(Long userId, FixtureScripApply fixtureScripApply) throws BusinessException {
		return fixtureScripApplyMapper.select(fixtureScripApply);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<FixtureScripApply> selectPageList(Long userId, FixtureScripApply fixtureScripApply, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<FixtureScripApply> page = fixtureScripApplyMapper.selectPageList(fixtureScripApply, queryDto);
		return new Pagination<FixtureScripApply>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public FixtureScripApply applyGetByfullNumber(Long userId, String fixtureBarcode) throws BusinessException {
		return fixtureScripApplyMapper.applyGetByfullNumber(fixtureBarcode);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer reportAudit(Long userId, String realName, ToolApplyAudit applyAudit, FixtureScripApply fixtureScripReport) throws BusinessException {
		Date date = new Date();
		fixtureScripReport.setUpdateTime(new Date());
		fixtureScripReport.setUpdateUser(userId);

		String auditResult = "";
		String message = "";
		String authCode = "";

		int auditStatus = fixtureScripReport.getApplyStatus();
		String fullNumber = fixtureScripReport.getFixtureBarcode();
		if (auditStatus == -1) {
			auditResult = "工艺部审核，未通过";
			message = "工艺部驳回了物料条码为" + fullNumber + "的报废申请,请您及时处理";
			authCode = "01050604";
		} else if (auditStatus == 2) {
			auditResult = "工艺部审核通过";
			message = "物料条码为" + fullNumber + "的报废申请已通过工艺部审核,请您及时安排报废";
			authCode = "01050604";	
			if (null != fixtureScripReport.getFixtureId()) {
				Long fixtureId = fixtureScripReport.getFixtureId();
				Fixture fixture = new Fixture();
				fixture.setPkId(fixtureId);
				fixture.setUpdateTime(date);
				fixture.setUpdateUser(userId);
				fixture.setFixtureStatus(6);
				fixtureMapper.updateActiveById(fixture, fixtureId);
			}
		}

		// 审核日志
		saveAudit(userId, realName, fixtureScripReport.getPkId(), auditResult, applyAudit);
		// 系统消息
		setMessage(message, userId, authCode);

		return fixtureScripApplyMapper.updateActiveById(fixtureScripReport, fixtureScripReport.getPkId());
	}

	private void saveAudit(Long userId, String realName, Long reportId, String auditResult, ToolApplyAudit applyAudit) throws BusinessException {
		ToolApplyAudit applyAuditSeq = toolApplyAuditMapper.selectSeqByApplyId(reportId);
		Integer auditSeq;
		if (applyAuditSeq != null) {
			Integer seqMax = applyAuditSeq.getAuditSeq();
			auditSeq = seqMax + 1;
		} else {
			auditSeq = 1;
		}
		Date date = new Date();
		applyAudit.setPkId(getPkId());
		applyAudit.setCreateTime(date);
		applyAudit.setCreateUser(userId);
		applyAudit.setUpdateTime(date);
		applyAudit.setUpdateUser(userId);
		applyAudit.setDelMark(0);
		applyAudit.setApplyId(reportId);
		applyAudit.setApplyType(1);
		applyAudit.setAuditResult(auditResult);
		applyAudit.setAuditorId(userId);
		applyAudit.setAuditorName(realName);
		applyAudit.setAuditSeq(auditSeq);
		toolApplyAuditMapper.insert(applyAudit);

	}

	private void setMessage(String message, Long userId, String authCode) throws BusinessException {
		// 获取拥有此项权限的用户ID列表
		List<Long> userList = sysUserAuthMapper.selectUserIdByAuthCode(authCode);
		Date date = new Date();
		SysMessage sysMessage = new SysMessage();
		sysMessage.setMessage(message);
		sysMessage.setSenderId(userId);
		sysMessage.setSendTime(date);
		sysMessage.setCreateTime(date);
		sysMessage.setCreateUser(userId);
		sysMessage.setUpdateTime(date);
		sysMessage.setUpdateUser(userId);
		sysMessage.setTitle("夹具报废审核");
		sysMessage.setDelMark(0);
		// 遍历拥有权限的人，并设置为该条推送信息的接收人。循环封装并执行添加
		for (Long receiverId : userList) {
			sysMessage.setPkId(getPkId());
			sysMessage.setReceiverId(receiverId);
			sysMessageMapper.insert(sysMessage);
		}
	}
}