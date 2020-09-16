package org.tsinghuatj.tool.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.tool.domain.CheckResult;
import org.tsinghuatj.tool.repository.CheckResultMapper;
import org.tsinghuatj.tool.service.ICheckResultService;


/**
 *
 * CheckResult 表数据服务层接口实现类
 *
 */
@Service("checkResultService")
public class CheckResultServiceImpl extends BaseServiceImpl implements ICheckResultService {

	private @Resource CheckResultMapper checkResultMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, CheckResult checkResult) throws BusinessException {	
		checkResult.setPkId(getPkId());
		checkResult.setCreateTime(new Date());
		checkResult.setCreateUser(userId);
		checkResult.setUpdateTime(new Date());
		checkResult.setUpdateUser(userId);
		checkResult.setDelMark(0);
		return checkResultMapper.insert(checkResult);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, CheckResult checkResult, Long checkResultId) throws BusinessException {
		checkResult.setUpdateTime(new Date());
		checkResult.setUpdateUser(userId);
		return checkResultMapper.updateActiveById(checkResult, checkResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public CheckResult selectById(Long userId, Long checkResultId) throws BusinessException {
		return checkResultMapper.selectById(checkResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long checkResultId) throws BusinessException {
		return checkResultMapper.removeById(checkResultId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long checkResultId) throws BusinessException {
		CheckResult checkResult = new CheckResult();
		checkResult.setPkId(checkResultId);
		checkResult.setUpdateTime(new Date());
		checkResult.setUpdateUser(userId);
		return checkResultMapper.deleteById(checkResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<CheckResult> select(Long userId, CheckResult checkResult) throws BusinessException {		
		return checkResultMapper.select(checkResult);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<CheckResult> selectPageList(Long userId, CheckResult checkResult,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<CheckResult> page = checkResultMapper.selectPageList(checkResult, queryDto);
		return new Pagination<CheckResult>(page.getTotal(), page.getResult());		
	}

	@Override
	public Integer checkResultImport(Long userId, List<CheckResult> CheckResultList) throws BusinessException {
		Date date = new Date();
		CheckResultList.forEach(CheckResult -> {	
			try {
				CheckResult.setPkId(getPkId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CheckResult.setCreateTime(date);
			CheckResult.setCreateUser(userId);
			CheckResult.setUpdateTime(date);
			CheckResult.setUpdateUser(userId);
			CheckResult.setDelMark(0);	
			checkResultMapper.insert(CheckResult);
		});
		return 1;
	}
}