package org.tsinghuatj.tool.service.impl;

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
import org.tsinghuatj.sys.domain.SysParam;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysParamMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.tool.domain.ToolBase;
import org.tsinghuatj.tool.domain.ToolType;
import org.tsinghuatj.tool.repository.ToolBaseMapper;
import org.tsinghuatj.tool.repository.ToolMapper;
import org.tsinghuatj.tool.repository.ToolTypeMapper;
import org.tsinghuatj.tool.service.IToolBaseService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolBase 表数据服务层接口实现类
 *
 */
@Service("toolBaseService")
public class ToolBaseServiceImpl extends BaseServiceImpl implements IToolBaseService {

	private @Resource ToolBaseMapper toolBaseMapper;
	private @Resource ToolMapper toolMapper;
	private @Resource ErpMaterialMapper erpMaterialMapper;
	private @Resource ToolTypeMapper toolTypeMapper;
	private @Resource SysParamMapper paramMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, ToolBase toolBase) throws BusinessException {
		toolBase.setPkId(getPkId());
		toolBase.setCreateTime(new Date());
		toolBase.setCreateUser(userId);
		toolBase.setUpdateTime(new Date());
		toolBase.setUpdateUser(userId);
		toolBase.setDelMark(0);
		return toolBaseMapper.insert(toolBase);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBase toolBase, Long toolBaseId) throws BusinessException {
		toolBase.setUpdateTime(new Date());
		toolBase.setUpdateUser(userId);
		return toolBaseMapper.updateActiveById(toolBase, toolBaseId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBase selectById(Long userId, Long toolBaseId) throws BusinessException {
		ToolBase toolBase = toolBaseMapper.selectById(toolBaseId);
		if (null != toolBase) {
			String toolNumber = toolBase.getToolNumber();
			toolBase.setToolAmount(toolMapper.countByToolNumber(toolNumber));
			ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(toolNumber);
			if (null != material) {
				toolBase.setErpAmount(material.getSumOnhandQuantity());
				toolBase.setNoCheckQty(material.getSumNocheckQuantity());
			}
		} else {
			throw new BusinessException("material.not.exists.error");
		}
		return toolBase;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBaseId) throws BusinessException {
		return toolBaseMapper.removeById(toolBaseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBaseId) throws BusinessException {
		ToolBase toolBase = new ToolBase();
		toolBase.setPkId(toolBaseId);
		toolBase.setUpdateTime(new Date());
		toolBase.setUpdateUser(userId);
		return toolBaseMapper.deleteById(toolBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBase> select(Long userId, ToolBase toolBase) throws BusinessException {
		return toolBaseMapper.select(toolBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBase> selectMapList(Long userId, ToolBase toolBase) throws BusinessException {
		return toolBaseMapper.selectMapList(toolBase);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBase> selectPageList(Long userId, ToolBase toolBase, QueryDto queryDto)
			throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBase> page = toolBaseMapper.selectPageList(toolBase, queryDto);
		List<Long> idList = new ArrayList<Long>();
		for (ToolBase item : page.getResult()) {
			idList.add(item.getUpdateUser());
			idList.add(item.getCreateUser());
		}
		if (idList.size() > 0) {
			idList = idList.stream().distinct().collect(Collectors.toList());
			List<UserAccount> userList = userAccountMapper.selectByIdList(idList);
			Map<Long, UserAccount> accoutMap = userList.stream().collect(Collectors.toMap(UserAccount::getPkId, t -> t, (k1, k2) -> k1));
			for (ToolBase item : page.getResult()) {
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
		return new Pagination<ToolBase>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolBaseImport(Long userId, List<ToolBase> toolBaseList) throws BusinessException {
		Date date = new Date();
		List<ToolType> typeList = toolTypeMapper.select(new ToolType());
		toolBaseList.forEach(toolBase -> {
			toolBase.setUpdateTime(date);
			toolBase.setUpdateUser(userId);
			if (null != toolBase.getProcessEach() && null != toolBase.getAvailableTimes()) {
				toolBase.setProcessTotal(toolBase.getProcessEach() * toolBase.getAvailableTimes());
			}
			for (ToolType toolType : typeList) {
				if (toolType.getTypeName().equals(toolBase.getTypeName())) {
					toolBase.setTypeId(toolType.getPkId().intValue());
				}
			}
			ToolBase old = toolBaseMapper.selectByToolNumber(toolBase.getToolNumber(), null);
			if (null != old) {
				toolBaseMapper.updateActiveById(toolBase, old.getPkId());
			} else {
				try {
					toolBase.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				toolBase.setCreateTime(date);
				toolBase.setCreateUser(userId);
				toolBase.setDelMark(0);
				toolBaseMapper.insert(toolBase);
			}
		});
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer toolBaseSynchro(Long userId, String toolNumber) throws BusinessException {
		ErpMaterial material = erpMaterialMapper.selectByMaterialNumber(toolNumber);
		if (null == material) {
			return 0;
		}
		SysParam param = paramMapper.selectByParamKey("prefixTool");
		String[] codeArray = param.getParamValue().split(",");
		boolean flag = false;
		for (String code : codeArray) {
			if (material.getItemCode().startsWith(code)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			return 0;
		}

		ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolNumber, null);
		Date date = new Date();
		if (null != toolBase) {
			toolBase.setToolName(material.getItemName());
			toolBase.setPrice(material.getItemPrice());
			toolBase.setUpdateTime(date);
			toolBase.setUpdateUser(userId);
			toolBaseMapper.updateActiveById(toolBase, toolBase.getPkId());
		} else {
			toolBase = new ToolBase();
			toolBase.setPkId(getPkId());
			toolBase.setCreateTime(new Date());
			toolBase.setCreateUser(userId);
			toolBase.setUpdateTime(new Date());
			toolBase.setUpdateUser(userId);
			toolBase.setDelMark(0);
			toolBase.setToolNumber(material.getItemCode());
			toolBase.setToolName(material.getItemName());
			toolBase.setPrice(material.getItemPrice());
			toolBaseMapper.insert(toolBase);
		}

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBase selectByNumber(Long userId, String toolNumber) throws BusinessException {
		// TODO Auto-generated method stub
		return toolBaseMapper.selectByToolNumber(toolNumber, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public boolean toolNumberCheck(String toolNumber, Long pkId) throws BusinessException {
		ToolBase toolBase = toolBaseMapper.selectByToolNumber(toolNumber, pkId);
		if (null != toolBase) {
			throw new BusinessException("toolnumber.exists.error");
		}
		return true;
	}

	@Override
	public int toolMapValidate(String toolMap) {
		return toolBaseMapper.toolMapValidate(toolMap);
	}
}