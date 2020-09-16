package org.tsinghuatj.base.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.erp.domain.ErpDepartment;
import org.tsinghuatj.erp.repository.ErpDepartmentMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.base.domain.Department;
import org.tsinghuatj.base.repository.DepartmentMapper;
import org.tsinghuatj.base.service.IDepartmentService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * Department 表数据服务层接口实现类
 *
 */
@Service("departmentService")
public class DepartmentServiceImpl extends BaseServiceImpl implements IDepartmentService {

	private @Resource DepartmentMapper departmentMapper;
	private @Resource ErpDepartmentMapper erpdepartmentMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Department department) throws BusinessException {
		department.setPkId(getPkId());
		department.setCreateTime(new Date());
		department.setCreateUser(userId);
		department.setUpdateTime(new Date());
		department.setUpdateUser(userId);
		department.setDelMark(0);
		return departmentMapper.insert(department);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Department department, Long departmentId) throws BusinessException {
		department.setUpdateTime(new Date());
		department.setUpdateUser(userId);
		return departmentMapper.updateActiveById(department, departmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Department selectById(Long userId, Long departmentId) throws BusinessException {
		return departmentMapper.selectById(departmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long departmentId) throws BusinessException {
		return departmentMapper.removeById(departmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long departmentId) throws BusinessException {
		Department department = new Department();
		department.setPkId(departmentId);
		department.setUpdateTime(new Date());
		department.setUpdateUser(userId);
		return departmentMapper.deleteById(department);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Department> select(Long userId, Department department) throws BusinessException {
		return departmentMapper.select(department);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Department> selectPageList(Long userId, Department department, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Department> page = departmentMapper.selectPageList(department, queryDto);
		return new Pagination<Department>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer departmentImport(Long userId, List<Department> departmentList) throws BusinessException {
		Date date = new Date();
		departmentList.forEach(department -> {
			Department item = departmentMapper.selectByCode(department.getDepartmentCode());
			if (null != item) {
				item.setUpdateTime(date);
				item.setUpdateUser(userId);
				item.setDepartmentName(department.getDepartmentName());
				item.setDepartmentDesc("");
				departmentMapper.updateActiveById(item, item.getPkId());
			} else {
				try {
					department.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				department.setCreateTime(date);
				department.setCreateUser(userId);
				department.setUpdateTime(date);
				department.setUpdateUser(userId);
				department.setDelMark(0);
				departmentMapper.insert(department);
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer departmentSynchro(Long userId, String departmentCode) throws BusinessException {
		ErpDepartment erpdepartment = erpdepartmentMapper.selectByDepartmentNumber(departmentCode);
		if (erpdepartment == null) {
			return 0;
		}
		Date date = new Date();
		Department department = departmentMapper.selectByCode(departmentCode);
		if (null != department) {
			department.setUpdateTime(date);
			department.setUpdateUser(userId);
			department.setDepartmentName(erpdepartment.getDepartmentName());
			department.setDepartmentDesc("");
			departmentMapper.updateActiveById(department, department.getPkId());
			return 1;
		}
		department = new Department();
		department.setPkId(getPkId());
		department.setCreateTime(date);
		department.setCreateUser(userId);
		department.setUpdateTime(date);
		department.setUpdateUser(userId);
		department.setDelMark(0);
		department.setDepartmentCode(departmentCode);
		department.setDepartmentName(erpdepartment.getDepartmentName());
		department.setDepartmentDesc("");
		departmentMapper.insert(department);
		return 1;
	}
}