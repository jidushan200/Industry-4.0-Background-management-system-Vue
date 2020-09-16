package org.tsinghuatj.base.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.PartName;
import org.tsinghuatj.base.repository.PartNameMapper;
import org.tsinghuatj.base.service.IPartNameService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * PartName 表数据服务层接口实现类
 *
 */
@Service("partNameService")
public class PartNameServiceImpl extends BaseServiceImpl implements IPartNameService {

	private @Resource PartNameMapper partNameMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, PartName partName) throws BusinessException {	
		
		return partNameMapper.insert(partName);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, PartName partName, Long partNameId) throws BusinessException {
	
		return partNameMapper.updateActiveById(partName, partNameId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public PartName selectById(Long userId, Long partNameId) throws BusinessException {
		return partNameMapper.selectById(partNameId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long partNameId) throws BusinessException {
		return partNameMapper.removeById(partNameId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long partNameId) throws BusinessException {
		PartName partName = new PartName();
		partName.setPkId(partNameId);
		
		return partNameMapper.deleteById(partName);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<PartName> select(Long userId, PartName partName) throws BusinessException {		
		return partNameMapper.select(partName);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<PartName> selectPageList(Long userId, PartName partName,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<PartName> page = partNameMapper.selectPageList(partName, queryDto);
		return new Pagination<PartName>(page.getTotal(), page.getResult());		
	}
}