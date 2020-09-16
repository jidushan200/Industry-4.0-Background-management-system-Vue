package org.tsinghuatj.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Department;
import org.tsinghuatj.base.domain.Equipment;
import org.tsinghuatj.base.repository.DepartmentMapper;
import org.tsinghuatj.base.repository.EquipmentMapper;
import org.tsinghuatj.base.service.IEquipmentService;
import org.tsinghuatj.erp.domain.ErpEquipment;
import org.tsinghuatj.erp.repository.ErpEquipmentMapper;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * Equipment 表数据服务层接口实现类
 *
 */
@Service("equipmentService")
public class EquipmentServiceImpl extends BaseServiceImpl implements IEquipmentService {

	private @Resource EquipmentMapper equipmentMapper;
	private @Resource ErpEquipmentMapper erpequipmentMapper;	
	private @Resource DepartmentMapper departmentMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Equipment equipment) throws BusinessException {
		equipment.setPkId(getPkId());
		equipment.setCreateTime(new Date());
		equipment.setCreateUser(userId);
		equipment.setUpdateTime(new Date());
		equipment.setUpdateUser(userId);
		equipment.setDelMark(0);
		return equipmentMapper.insert(equipment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Equipment equipment, Long equipmentId) throws BusinessException {
		equipment.setUpdateTime(new Date());
		equipment.setUpdateUser(userId);
		return equipmentMapper.updateActiveById(equipment, equipmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Equipment selectById(Long userId, Long equipmentId) throws BusinessException {
		return equipmentMapper.selectById(equipmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long equipmentId) throws BusinessException {
		return equipmentMapper.removeById(equipmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long equipmentId) throws BusinessException {
		Equipment equipment = new Equipment();
		equipment.setPkId(equipmentId);
		equipment.setUpdateTime(new Date());
		equipment.setUpdateUser(userId);
		return equipmentMapper.deleteById(equipment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Equipment> select(Long userId, Equipment equipment) throws BusinessException {
		return equipmentMapper.select(equipment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Equipment> selectPageList(Long userId, Equipment equipment, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Equipment> page = equipmentMapper.selectPageList(equipment, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (Equipment item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Equipment item : page.getResult()) {
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
		return new Pagination<Equipment>(page.getTotal(), page.getResult());
	}

	@Override
	public Integer equipmentImport(Long userId, List<Equipment> equipmentList) throws BusinessException {
		Date date = new Date();
		List<String> deartNamelist = new ArrayList<String>();
		for (Equipment item : equipmentList) {
			deartNamelist.add(item.getDepartmentName());
		}
		deartNamelist = deartNamelist.stream().distinct().collect(Collectors.toList());
		List<Department> dList = departmentMapper.selectByNameList(deartNamelist);
		Map<String, Department> accoutMap = dList.stream().collect(Collectors.toMap(Department::getDepartmentName, t -> t, (k1, k2) -> k1));
		
		equipmentList.forEach(equipment -> {
			try {
				equipment.setPkId(getPkId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Department depart = accoutMap.get(equipment.getDepartmentName());
			if (null != depart) {
				equipment.setDepartmentId(depart.getPkId());
			}
			equipment.setCreateTime(date);
			equipment.setCreateUser(userId);
			equipment.setUpdateTime(date);
			equipment.setUpdateUser(userId);
			equipment.setDelMark(0);
			Equipment oldequipment = equipmentMapper.selectByEquipmentCode(equipment.getEquipmentCode(),null);
			if(null!=oldequipment){
				equipmentMapper.updateActiveById(equipment, oldequipment.getPkId());
			}else{
				equipmentMapper.insert(equipment);
			}			
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer equipmentSynchro(Long userId, String equipmentCode) throws BusinessException {
		ErpEquipment erpEquipment = erpequipmentMapper.selectByEquipmentCode(equipmentCode);
		if (null == erpEquipment) {
			return 0;
		}
		Equipment equipment = equipmentMapper.selectByEquipmentCode(equipmentCode,null);
		if (null != equipment) {
			equipment.setEquipmentName(erpEquipment.getEquipmentName());
			equipment.setManufacturerName(erpEquipment.getManufacturerName());
			equipment.setModelNumber(erpEquipment.getModelNumber());
			equipment.setTagNumber(erpEquipment.getTagNumber());
			equipment.setDepartmentName(erpEquipment.getDistributionLocation());
			equipment.setUpdateTime(new Date());
			equipment.setUpdateUser(userId);
			equipmentMapper.updateActiveById(equipment, equipment.getPkId());
		} else {
			equipment = new Equipment();
			equipment.setPkId(getPkId());
			equipment.setCreateTime(new Date());
			equipment.setCreateUser(userId);
			equipment.setUpdateTime(new Date());
			equipment.setUpdateUser(userId);
			equipment.setDelMark(0);
			equipment.setEquipmentCode(erpEquipment.getEquipmentCode());
			equipment.setEquipmentName(erpEquipment.getEquipmentName());
			equipment.setDepartmentName(erpEquipment.getDistributionLocation());
			equipment.setManufacturerName(erpEquipment.getManufacturerName());
			equipment.setModelNumber(erpEquipment.getModelNumber());
			equipment.setTagNumber(erpEquipment.getTagNumber());
			equipmentMapper.insert(equipment);
		}
		return 1;
	}

	@Override
	public boolean checkEquipmentCode(String equipmentCode, Long pkId) throws BusinessException {
		Equipment equipment = equipmentMapper.selectByEquipmentCode(equipmentCode,pkId);
		if(null!=equipment){
			throw new BusinessException("equipmentcode.exists.error");
		}
		return true;
	}
}