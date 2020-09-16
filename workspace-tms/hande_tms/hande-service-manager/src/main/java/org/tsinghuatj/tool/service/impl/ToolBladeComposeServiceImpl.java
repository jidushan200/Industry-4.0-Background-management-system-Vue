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
import org.springframework.util.CollectionUtils;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.tool.domain.ToolBladeCompose;
import org.tsinghuatj.tool.domain.ToolBladeComposeDetail;
import org.tsinghuatj.tool.domain.ToolBladeComposePart;
import org.tsinghuatj.tool.domain.ToolBladeProcess;
import org.tsinghuatj.tool.domain.ToolCoat;
import org.tsinghuatj.tool.domain.ToolPlate;
import org.tsinghuatj.tool.domain.ToolRepair;
import org.tsinghuatj.tool.domain.ToolWaitHandle;
import org.tsinghuatj.tool.repository.ToolBladeComposeDetailMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposeMapper;
import org.tsinghuatj.tool.repository.ToolBladeComposePartMapper;
import org.tsinghuatj.tool.repository.ToolBladeProcessMapper;
import org.tsinghuatj.tool.repository.ToolCoatMapper;
import org.tsinghuatj.tool.repository.ToolPlateMapper;
import org.tsinghuatj.tool.repository.ToolRepairMapper;
import org.tsinghuatj.tool.repository.ToolWaitHandleMapper;
import org.tsinghuatj.tool.service.IToolBladeComposeService;
import org.tsinghuatj.tool.service.IToolOperLogService;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * ToolBladeCompose 表数据服务层接口实现类
 *
 */
@Service("toolBladeComposeService")
public class ToolBladeComposeServiceImpl extends BaseServiceImpl implements IToolBladeComposeService {

	private @Resource ToolBladeComposeMapper toolBladeComposeMapper;
	private @Resource ToolBladeComposeDetailMapper toolBladeComposeDetailMapper;
	private @Resource ToolCoatMapper toolCoatMapper;
	private @Resource ToolRepairMapper toolRepairMapper;
	private @Resource ToolBladeProcessMapper toolBladeProcessMapper;
	private @Resource ToolWaitHandleMapper toolWaitHandleMapper;
	private @Resource ToolBladeComposePartMapper composePartMapper;
	private @Resource ToolPlateMapper plateMapper;
	private @Resource IToolOperLogService operLogService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, String realName, ToolBladeCompose toolBladeCompose) throws BusinessException {
		toolBladeCompose.setPkId(getPkId());
		toolBladeCompose.setCreateTime(new Date());
		toolBladeCompose.setCreateUser(userId);
		toolBladeCompose.setUpdateTime(new Date());
		toolBladeCompose.setUpdateUser(userId);
		toolBladeCompose.setDelMark(0);
		return toolBladeComposeMapper.insert(toolBladeCompose);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, ToolBladeCompose toolBladeCompose, Long toolBladeComposeId) throws BusinessException {
		toolBladeCompose.setUpdateTime(new Date());
		toolBladeCompose.setUpdateUser(userId);
		return toolBladeComposeMapper.updateActiveById(toolBladeCompose, toolBladeComposeId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeCompose selectById(Long userId, Long toolBladeComposeId) throws BusinessException {
		ToolBladeCompose compose = toolBladeComposeMapper.selectById(toolBladeComposeId);
		if (null != compose) {
			compose.setPartList(composePartMapper.selectByComposeNumber(compose.getComposeNumber()));
		}
		return compose;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long toolBladeComposeId) throws BusinessException {
		return toolBladeComposeMapper.removeById(toolBladeComposeId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long toolBladeComposeId) throws BusinessException {
		ToolBladeCompose toolBladeCompose = new ToolBladeCompose();
		toolBladeCompose.setPkId(toolBladeComposeId);
		toolBladeCompose.setUpdateTime(new Date());
		toolBladeCompose.setUpdateUser(userId);
		return toolBladeComposeMapper.deleteById(toolBladeCompose);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<ToolBladeCompose> select(Long userId, ToolBladeCompose toolBladeCompose) throws BusinessException {
		return toolBladeComposeMapper.select(toolBladeCompose);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeCompose> selectPageList(Long userId, ToolBladeCompose toolBladeCompose, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeCompose> page = toolBladeComposeMapper.selectPageList(toolBladeCompose, queryDto);
		List<String> composeNumberList = new ArrayList<String>();
		for (ToolBladeCompose item : page) {
			composeNumberList.add(item.getComposeNumber());
		}
		if (composeNumberList.size() > 0) {
			List<ToolBladeComposeDetail> detailList = toolBladeComposeDetailMapper.selectByComposeList(composeNumberList);
			List<ToolBladeComposePart> partList = composePartMapper.selectByComposeList(composeNumberList);

			Map<String, List<ToolBladeComposeDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));
			Map<String, List<ToolBladeComposePart>> partMap = partList.stream().collect(Collectors.groupingBy(ToolBladeComposePart::getComposeNumber));

			page.forEach(item -> {
				item.setDetailList(detailMap.get(item.getComposeNumber()));
				item.setPartList(partMap.get(item.getComposeNumber()));
			});
		}
		return new Pagination<ToolBladeCompose>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public ToolBladeCompose selectByComposeNumber(Long userId, String composeNumber) throws BusinessException {
		ToolBladeCompose compose = toolBladeComposeMapper.selectByComposeNumber(composeNumber);
		if (null != compose) {
			// 查刀条组合的制件
			// compose.setPartList(composePartMapper.selectByComposeNumber(compose.getComposeNumber()));
			List<ToolBladeComposePart> list = composePartMapper.selectByHeadNumber(compose.getHeadNumber());
			list.forEach(item -> {
				item.setComposeNumber(composeNumber);
			});
			compose.setPartList(list);
		}
		return compose;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<ToolBladeCompose> selectLifePageList(Long userId, ToolBladeCompose toolBladeCompose, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<ToolBladeCompose> page = toolBladeComposeMapper.selectLifePageList(toolBladeCompose, queryDto);

		// 循环遍历
		List<String> composeNumberList = new ArrayList<String>();
		page.forEach(item -> {
			composeNumberList.add(item.getComposeNumber());
		});

		if (!CollectionUtils.isEmpty(composeNumberList)) {
			List<ToolCoat> coatlist = toolCoatMapper.selectByFullNumberList(composeNumberList);
			List<ToolRepair> repairList = toolRepairMapper.selectByFullNumberList(composeNumberList);
			List<ToolBladeProcess> processList = toolBladeProcessMapper.selectByComposeNumberList(composeNumberList);
			List<ToolBladeComposeDetail> detailList = toolBladeComposeDetailMapper.selectByComposeList(composeNumberList);
			List<ToolBladeComposePart> partList = composePartMapper.selectByComposeList(composeNumberList);
			Map<String, List<ToolCoat>> coatMap = coatlist.stream().collect(Collectors.groupingBy(ToolCoat::getFullNumber));
			Map<String, List<ToolRepair>> repairMap = repairList.stream().collect(Collectors.groupingBy(ToolRepair::getFullNumber));
			Map<String, List<ToolBladeProcess>> processMap = processList.stream().collect(Collectors.groupingBy(ToolBladeProcess::getComposeNumber));
			Map<String, List<ToolBladeComposeDetail>> detailMap = detailList.stream().collect(Collectors.groupingBy(ToolBladeComposeDetail::getComposeNumber));
			Map<String, List<ToolBladeComposePart>> partMap = partList.stream().collect(Collectors.groupingBy(ToolBladeComposePart::getComposeNumber));
			page.forEach(item -> {
				String composeNumber = item.getComposeNumber();
				item.setCoatList(coatMap.get(composeNumber));
				item.setRepairList(repairMap.get(composeNumber));
				item.setProcessList(processMap.get(composeNumber));
				item.setDetailList(detailMap.get(composeNumber));
				item.setPartList(partMap.get(composeNumber));
			});
		}
		return new Pagination<ToolBladeCompose>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer setRepair(Long userId, String userName, ToolBladeCompose toolBladeCompose, String remark) throws BusinessException {
		Date date = new Date();
		toolBladeCompose.setUpdateTime(new Date());
		toolBladeCompose.setUpdateUser(userId);

		// 修改刀盘组合状态
		ToolBladeCompose old = toolBladeComposeMapper.selectByComposeNumber(toolBladeCompose.getComposeNumber());
		toolBladeCompose.setLastPlateNumber(old.getPlateNumber());
		ToolPlate toolPlate = new ToolPlate();
		toolPlate.setUseStatus(2);
		toolPlate.setUpdateTime(date);
		toolPlate.setUpdateUser(userId);
		plateMapper.updateActiveByNumber(toolPlate, old.getPlateNumber());
		toolBladeComposeMapper.updateActiveByComposeNumber(toolBladeCompose, toolBladeCompose.getComposeNumber());
		ToolWaitHandle waitHandle = new ToolWaitHandle();
		waitHandle.setComposeNumber(toolBladeCompose.getComposeNumber());

		waitHandle.setHandleType(2);
		waitHandle.setIsNew(0);
		waitHandle.setHandleResult(0);

		waitHandle.setCreateTime(date);
		waitHandle.setCreateUser(userId);
		waitHandle.setPkId(getPkId());
		waitHandle.setUpdateTime(date);
		waitHandle.setUpdateUser(userId);
		waitHandle.setDelMark(0);
		toolWaitHandleMapper.insert(waitHandle);

		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public String createComposeNumber(Long userId, String headNumber) throws BusinessException {
		String composeNumber = toolBladeComposeMapper.selectByHeaderNumber(headNumber);
		if (StringUtils.isEmpty(composeNumber)) {
			composeNumber = headNumber + "-00001";
		} else {
			Integer length = composeNumber.length();
			int startIndex = Integer.parseInt(composeNumber.substring(length - 5, length)) + 1;
			composeNumber = headNumber + "-" + fillString(startIndex, 5);
		}
		ToolBladeCompose old = toolBladeComposeMapper.selectByComposeNumber(composeNumber);
		if (null != old) {
			Object[] param = { composeNumber };
			throw new BusinessException("composenumber.exists.error", param);
		}
		return composeNumber;
	}

	private String fillString(int num, int digit) {
		/**
		 * 0：表示前面补0 digit：表示保留数字位数 d：表示参数为正数类型
		 */
		return String.format("%0" + digit + "d", num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer bladeComposeInstall(Long userId, String userName, ToolBladeCompose toolBladeCompose) throws BusinessException {
		toolBladeCompose.setUpdateTime(new Date());
		toolBladeCompose.setUpdateUser(userId);
		toolBladeComposeMapper.updateActiveById(toolBladeCompose, toolBladeCompose.getPkId());
		toolBladeCompose = toolBladeComposeMapper.selectById(toolBladeCompose.getPkId());
		ToolPlate toolPlate = new ToolPlate();
		toolPlate.setUseStatus(1);
		toolPlate.setUpdateTime(new Date());
		toolPlate.setUpdateUser(userId);
		plateMapper.updateActiveByNumber(toolPlate, toolBladeCompose.getPlateNumber());
		// operLogService.insert(userId, 9, toolBladeCompose.getToolNumber(),
		// toolBladeCompose.getComposeNumber(),"安装人:"+userName, "");

		return 1;
	}
}