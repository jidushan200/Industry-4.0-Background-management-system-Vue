package ${basepackage}.${namespace}.service.impl;

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
import ${basepackage}.${namespace}.domain.${table.className};
import ${basepackage}.${namespace}.repository.${table.className}Mapper;
import ${basepackage}.${namespace}.service.I${table.className}Service;


/**
 *
 * ${table.className} 表数据服务层接口实现类
 *
 */
@Service("${table.classNameFirstLower}Service")
public class ${table.className}ServiceImpl extends BaseServiceImpl implements I${table.className}Service {

	private @Resource ${table.className}Mapper ${table.classNameFirstLower}Mapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ${table.className} ${table.classNameFirstLower}) throws BusinessException {	
		${table.classNameFirstLower}.setPkId(getPkId());
		${table.classNameFirstLower}.setCreateTime(new Date());
		${table.classNameFirstLower}.setCreateUser(userId);
		${table.classNameFirstLower}.setUpdateTime(new Date());
		${table.classNameFirstLower}.setUpdateUser(userId);
		${table.classNameFirstLower}.setDelMark(0);
		return ${table.classNameFirstLower}Mapper.insert(${table.classNameFirstLower});
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ${table.className} ${table.classNameFirstLower}, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException {
		${table.classNameFirstLower}.setUpdateTime(new Date());
		${table.classNameFirstLower}.setUpdateUser(userId);
		return ${table.classNameFirstLower}Mapper.updateActiveById(${table.classNameFirstLower}, ${table.classNameFirstLower}Id);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ${table.className} selectById(Long userId, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException {
		return ${table.classNameFirstLower}Mapper.selectById(${table.classNameFirstLower}Id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException {
		return ${table.classNameFirstLower}Mapper.removeById(${table.classNameFirstLower}Id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, ${table.pkColumn.javaType} ${table.classNameFirstLower}Id) throws BusinessException {
		${table.className} ${table.classNameFirstLower} = new ${table.className}();
		${table.classNameFirstLower}.setPkId(${table.classNameFirstLower}Id);
		${table.classNameFirstLower}.setUpdateTime(new Date());
		${table.classNameFirstLower}.setUpdateUser(userId);
		return ${table.classNameFirstLower}Mapper.deleteById(${table.classNameFirstLower});
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<${table.className}> select(Long userId, ${table.className} ${table.classNameFirstLower}) throws BusinessException {		
		return ${table.classNameFirstLower}Mapper.select(${table.classNameFirstLower});
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<${table.className}> selectPageList(Long userId, ${table.className} ${table.classNameFirstLower},QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<${table.className}> page = ${table.classNameFirstLower}Mapper.selectPageList(${table.classNameFirstLower}, queryDto);
		return new Pagination<${table.className}>(page.getTotal(), page.getResult());		
	}
}