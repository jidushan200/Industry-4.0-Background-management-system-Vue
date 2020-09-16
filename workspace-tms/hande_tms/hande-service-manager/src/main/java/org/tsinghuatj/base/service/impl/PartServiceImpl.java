package org.tsinghuatj.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.base.domain.Part;
import org.tsinghuatj.base.domain.PartName;
import org.tsinghuatj.base.repository.PartMapper;
import org.tsinghuatj.base.repository.PartNameMapper;
import org.tsinghuatj.base.service.IPartService;
import org.tsinghuatj.erp.domain.ErpMaterial;
import org.tsinghuatj.erp.repository.ErpMaterialMapper;
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
 * Part 表数据服务层接口实现类
 *
 */
@Service("partService")
public class PartServiceImpl extends BaseServiceImpl implements IPartService {

	private @Resource PartMapper partMapper;
	private @Resource PartNameMapper partNameMapper;
	private @Resource ErpMaterialMapper erpMaterialMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Value("${erp.base.code:/}") String erpbaseCode;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Part part) throws BusinessException {
		part.setPkId(getPkId());
		part.setCreateTime(new Date());
		part.setCreateUser(userId);
		part.setUpdateTime(new Date());
		part.setUpdateUser(userId);
		part.setDelMark(0);
		return partMapper.insert(part);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Part part, Long partId) throws BusinessException {
		part.setUpdateTime(new Date());
		part.setUpdateUser(userId);
		return partMapper.updateActiveById(part, partId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Part selectById(Long userId, Long partId) throws BusinessException {
		return partMapper.selectById(partId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long partId) throws BusinessException {
		return partMapper.removeById(partId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long partId) throws BusinessException {
		Part part = new Part();
		part.setPkId(partId);
		part.setUpdateTime(new Date());
		part.setUpdateUser(userId);
		return partMapper.deleteById(part);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Part> select(Long userId, Part part) throws BusinessException {
		return partMapper.select(part);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Part> selectPageList(Long userId, Part part, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Part> page = partMapper.selectPageList(part, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (Part item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (Part item : page.getResult()) {
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
		return new Pagination<Part>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer partImport(Long userId, List<Part> partList) throws BusinessException {
		Date date = new Date();
		List<String> nameList = new ArrayList<String>();
		for (Part part : partList) {
			nameList.add(part.getPartTypeName());
		}
		List<PartName> l = partNameMapper.selectByNameList(nameList);
		Map<String, PartName> accoutMap = l.stream().collect(Collectors.toMap(PartName::getPartTypeName, t -> t, (k1, k2) -> k1));

		partList.forEach(part -> {
			PartName p = accoutMap.get(part.getPartTypeName());
			if (null != p) {
				part.setPartType(p.getPkId());
			}
			Part old = partMapper.selectByPartCode(part.getPartCode(), null);
			part.setUpdateTime(date);
			part.setUpdateUser(userId);
			if (null != old) {				
				partMapper.updateActiveById(part, old.getPkId());
			} else {
				try {
					part.setPkId(getPkId());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				part.setCreateTime(date);
				part.setCreateUser(userId);
				part.setUpdateTime(date);
				part.setUpdateUser(userId);
				part.setDelMark(0);
				partMapper.insert(part);
			}

		});
		return 1;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer partSynchro(Long userId, String partCode) throws BusinessException {
		ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(partCode);
		if (null == material) {
			return 0;
		}

		String[] codeArray = erpbaseCode.split(",");
		boolean flag = true;
		for (String code : codeArray) {
			if (material.getItemCode().startsWith(code)) {
				flag = false;
				break;
			}
		}
		if (!flag) {
			return 0;
		}

		Part part = partMapper.selectByPartCode(partCode, null);
		Date date = new Date();
		if (null != part) {
			part.setPartName(material.getItemName());
			part.setPartTypeName(material.getItemType());
			part.setUpdateTime(date);
			part.setUpdateUser(userId);
			partMapper.updateActiveById(part, part.getPkId());
		} else {
			part = new Part();
			part.setPkId(getPkId());
			part.setCreateTime(new Date());
			part.setCreateUser(userId);
			part.setUpdateTime(new Date());
			part.setUpdateUser(userId);
			part.setDelMark(0);
			part.setPartCode(material.getItemCode());
			part.setPartName(material.getItemName());
			part.setPartTypeName(material.getItemType());
			partMapper.insert(part);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Part selectByCode(Long userId, String partCode) throws BusinessException {
		// TODO Auto-generated method stub
		return partMapper.selectByPartCode(partCode, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean checkPartCode(String partCode, Long pkId) throws BusinessException {
		Part part =partMapper.selectByPartCode(partCode, pkId);
		if(null!=part){
			throw new BusinessException("partcode.exists.error");
		}
		return false;
	}
}