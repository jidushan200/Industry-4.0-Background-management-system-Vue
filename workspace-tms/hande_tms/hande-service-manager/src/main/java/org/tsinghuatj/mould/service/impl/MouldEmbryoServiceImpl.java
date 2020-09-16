package org.tsinghuatj.mould.service.impl;

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
import org.tsinghuatj.mould.domain.MouldEmbryo;
import org.tsinghuatj.mould.repository.MouldEmbryoMapper;
import org.tsinghuatj.mould.service.IMouldEmbryoService;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 *
 * MouldEmbryo 表数据服务层接口实现类
 *
 */
@Service("mouldEmbryoService")
public class MouldEmbryoServiceImpl extends BaseServiceImpl implements IMouldEmbryoService {

	private @Resource MouldEmbryoMapper mouldEmbryoMapper;
	private @Resource ErpMaterialMapper erpMaterialMapper;
	private @Resource UserAccountMapper userAccountMapper;
	private @Resource SysParamMapper paramMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, MouldEmbryo mouldEmbryo) throws BusinessException {	
		mouldEmbryo.setPkId(getPkId());
		mouldEmbryo.setCreateTime(new Date());
		mouldEmbryo.setCreateUser(userId);
		mouldEmbryo.setUpdateTime(new Date());
		mouldEmbryo.setUpdateUser(userId);
		mouldEmbryo.setDelMark(0);
		return mouldEmbryoMapper.insert(mouldEmbryo);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, MouldEmbryo mouldEmbryo, Long mouldEmbryoId) throws BusinessException {
		mouldEmbryo.setUpdateTime(new Date());
		mouldEmbryo.setUpdateUser(userId);
		return mouldEmbryoMapper.updateActiveById(mouldEmbryo, mouldEmbryoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public MouldEmbryo selectById(Long userId, Long mouldEmbryoId) throws BusinessException {
		return mouldEmbryoMapper.selectById(mouldEmbryoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldEmbryoId) throws BusinessException {
		return mouldEmbryoMapper.removeById(mouldEmbryoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldEmbryoId) throws BusinessException {
		MouldEmbryo mouldEmbryo = new MouldEmbryo();
		mouldEmbryo.setPkId(mouldEmbryoId);
		mouldEmbryo.setUpdateTime(new Date());
		mouldEmbryo.setUpdateUser(userId);
		return mouldEmbryoMapper.deleteById(mouldEmbryo);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<MouldEmbryo> select(Long userId, MouldEmbryo mouldEmbryo) throws BusinessException {		
		return mouldEmbryoMapper.select(mouldEmbryo);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<MouldEmbryo> selectPageList(Long userId, MouldEmbryo mouldEmbryo,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<MouldEmbryo> page = mouldEmbryoMapper.selectPageList(mouldEmbryo, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (MouldEmbryo item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}

		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (MouldEmbryo item : page.getResult()) {
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
		return new Pagination<MouldEmbryo>(page.getTotal(), page.getResult());		
	}
	
	@Override
	public Integer embryoImport(Long userId, List<MouldEmbryo> embryoList) throws BusinessException {
		Date date = new Date();
		embryoList.forEach(mouldEmbryo -> {
			mouldEmbryo.setUpdateTime(date);
			mouldEmbryo.setUpdateUser(userId);
			MouldEmbryo old = mouldEmbryoMapper.selectByCode(mouldEmbryo.getEmbryoCode());
			if (null != old) {
				mouldEmbryoMapper.updateActiveById(mouldEmbryo, old.getPkId());
			} else {
				try {
					mouldEmbryo.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				mouldEmbryo.setCreateTime(date);
				mouldEmbryo.setCreateUser(userId);
				mouldEmbryo.setDelMark(0);
				mouldEmbryoMapper.insert(mouldEmbryo);
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer embryoSynchro(Long userId, String embryoCode) throws BusinessException {
		ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(embryoCode);
		if (null == material) {
			return 0;
		}
//		SysParam param = paramMapper.selectByParamKey("prefixMould");	
//		if(param == null){
//			return 0;
//		}else if (!material.getItemCode().startsWith(param.getParamValue())) {
//			return 0;
//		}
		
		MouldEmbryo mouldEmbryo = mouldEmbryoMapper.selectByCode(embryoCode);
		Date date = new Date();
		if (null != mouldEmbryo) {
			mouldEmbryo.setEmbryoCode(material.getItemCode());
			mouldEmbryo.setEmbryoName(material.getItemName());
			mouldEmbryo.setUpdateTime(date);
			mouldEmbryo.setUpdateUser(userId);
			mouldEmbryoMapper.updateActiveById(mouldEmbryo, mouldEmbryo.getPkId());
		} else {
			mouldEmbryo = new MouldEmbryo();
			mouldEmbryo.setPkId(getPkId());
			mouldEmbryo.setCreateTime(new Date());
			mouldEmbryo.setCreateUser(userId);
			mouldEmbryo.setUpdateTime(new Date());
			mouldEmbryo.setUpdateUser(userId);
			mouldEmbryo.setDelMark(0);
			mouldEmbryo.setEmbryoCode(material.getItemCode());
			mouldEmbryo.setEmbryoName(material.getItemName());
			mouldEmbryoMapper.insert(mouldEmbryo);
		}

		return 1;
	}

	@Override
	public boolean checkEmbryoCode(String embryoCode, Long pkId) throws BusinessException {
		MouldEmbryo mouldEmbryo = mouldEmbryoMapper.selectByCode(embryoCode);
		if(null!=mouldEmbryo){
			throw new BusinessException("embryoCode.exists.error");
		}
		return true;
	}
}