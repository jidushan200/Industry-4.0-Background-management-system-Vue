package org.tsinghuatj.measure.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.erp.domain.ErpMaterial;
import org.tsinghuatj.erp.repository.ErpMaterialMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.MeasureBase;
import org.tsinghuatj.measure.repository.MeasureBaseMapper;
import org.tsinghuatj.measure.service.IMeasureBaseService;
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * MeasureBase 表数据服务层接口实现类
 *
 */
@Service("measureBaseService")
public class MeasureBaseServiceImpl extends BaseServiceImpl implements IMeasureBaseService {

	private @Resource MeasureBaseMapper measureBaseMapper;	
	private @Resource ErpMaterialMapper erpMaterialMapper;	
	private @Resource SysParamMapper paramMapper;
	private @Resource UserAccountMapper userAccountMapper;

	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MeasureBase measureBase) throws BusinessException {	
		measureBase.setPkId(getPkId());
		measureBase.setCreateTime(new Date());
		measureBase.setCreateUser(userId);
		measureBase.setUpdateTime(new Date());
		measureBase.setUpdateUser(userId);
		measureBase.setDelMark(0);
		return measureBaseMapper.insert(measureBase);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MeasureBase measureBase, Long measureBaseId) throws BusinessException {
		measureBase.setUpdateTime(new Date());
		measureBase.setUpdateUser(userId);
		return measureBaseMapper.updateActiveById(measureBase, measureBaseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MeasureBase selectById(Long userId, Long measureBaseId) throws BusinessException {
		return measureBaseMapper.selectById(measureBaseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measureBaseId) throws BusinessException {
		return measureBaseMapper.removeById(measureBaseId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measureBaseId) throws BusinessException {
		MeasureBase measureBase = new MeasureBase();
		measureBase.setPkId(measureBaseId);
		measureBase.setUpdateTime(new Date());
		measureBase.setUpdateUser(userId);
		return measureBaseMapper.deleteById(measureBase);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MeasureBase> select(Long userId, MeasureBase measureBase) throws BusinessException {		
		return measureBaseMapper.select(measureBase);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MeasureBase> selectPageList(Long userId, MeasureBase measureBase,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MeasureBase> page = measureBaseMapper.selectPageList(measureBase, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (MeasureBase item : page) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (MeasureBase item : page.getResult()) {
				UserAccount create = accoutMap.get(item.getCreateUser());
				if (null != create) {
					item.setCreateUserName(create.getRealName());
				}
				UserAccount account = accoutMap.get(item.getUpdateUser());
				if (null != account) {
					item.setUpdateUserName(account.getRealName());
				}
			}
		}
		return new Pagination<MeasureBase>(page.getTotal(), page.getResult());		
	}

	@Override
	public MeasureBase selectByNumber(Long userId, String measureNumber) throws BusinessException {
		return measureBaseMapper.selectByNumber(measureNumber);

	}

	@Override
	public Integer measureBaseSynchro(Long userId, String measureNumber) throws BusinessException {
		ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(measureNumber);
		if (null == material) {
			return 0;
		}
		SysParam param = paramMapper.selectByParamKey("prefixMeasure");		
		if(!material.getItemCode().startsWith(param.getParamValue())){
			return 0;
		}
		MeasureBase measureBase = measureBaseMapper.selectByNumber(measureNumber);
		Date date = new Date();
		if (null != measureBase) {
			measureBase.setMeasureName(material.getItemName());
			//measureBase.setModel(material.getItemType());
			measureBase.setUpdateTime(date);
			measureBase.setUpdateUser(userId);
			measureBaseMapper.updateActiveById(measureBase, measureBase.getPkId());
		} else {
			measureBase = new MeasureBase();
			measureBase.setPkId(getPkId());
			measureBase.setCreateTime(new Date());
			measureBase.setCreateUser(userId);
			measureBase.setUpdateTime(new Date());
			measureBase.setUpdateUser(userId);
			measureBase.setDelMark(0);
			measureBase.setMeasureNumber(material.getItemCode());
			measureBase.setMeasureName(material.getItemName());
			//measureBase.setModel(material.getItemType());
			measureBaseMapper.insert(measureBase);
		}

		return 1;
	}

	@Override
	public Integer measureBaseImport(Long userId, List<MeasureBase> measureBaseList) throws BusinessException {
		Date date = new Date();
		measureBaseList.forEach(measureBase -> {
			measureBase.setUpdateTime(date);
			measureBase.setUpdateUser(userId);
			MeasureBase old = measureBaseMapper.selectByNumber(measureBase.getMeasureNumber());
			if (null != old) {
				measureBaseMapper.updateActiveById(measureBase, old.getPkId());
			} else {
				try {
					measureBase.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				measureBase.setCreateTime(date);
				measureBase.setCreateUser(userId);
				measureBase.setDelMark(0);
				measureBaseMapper.insert(measureBase);
			}
		});
		return 1;
	}
}