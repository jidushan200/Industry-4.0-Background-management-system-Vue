package org.tsinghuatj.measure.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.framework.constants.MeasureStatusEnum;
import org.tsinghuatj.framework.constants.SealTypeEnum;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.measure.domain.Measure;
import org.tsinghuatj.measure.domain.MeasureCheck;
import org.tsinghuatj.measure.domain.MeasureOutbound;
import org.tsinghuatj.measure.domain.MeasureWarehouse;
import org.tsinghuatj.measure.repository.MeasureCheckMapper;
import org.tsinghuatj.measure.repository.MeasureMapper;
import org.tsinghuatj.measure.repository.MeasureOutboundMapper;
import org.tsinghuatj.measure.repository.MeasureWarehouseMapper;
import org.tsinghuatj.measure.service.IMeasureService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;

/**
 *
 * Measure 表数据服务层接口实现类
 *
 */
@Service("measureService")
public class MeasureServiceImpl extends BaseServiceImpl implements IMeasureService {

	private @Resource MeasureMapper measureMapper;
	private @Resource StaffMapper staffMapper;
	private @Resource MeasureWarehouseMapper measureWarehouseMapper;
	private @Resource MeasureOutboundMapper measureOutboundMapper;
	private @Resource MeasureCheckMapper measureCheckMapper;
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Measure measure) throws BusinessException {
		measure.setPkId(getPkId());
		measure.setCreateTime(new Date());
		measure.setCreateUser(userId);
		measure.setUpdateTime(new Date());
		measure.setUpdateUser(userId);
		measure.setDelMark(0);
		return measureMapper.insert(measure);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Measure measure, Long measureId) throws BusinessException {
		measure.setUpdateTime(new Date());
		measure.setUpdateUser(userId);
		return measureMapper.updateActiveById(measure, measureId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Measure selectById(Long userId, Long measureId) throws BusinessException {
		return measureMapper.selectById(measureId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long measureId) throws BusinessException {
		return measureMapper.removeById(measureId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long measureId) throws BusinessException {
		Measure measure = new Measure();
		measure.setPkId(measureId);
		measure.setUpdateTime(new Date());
		measure.setUpdateUser(userId);
		return measureMapper.deleteById(measure);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Measure> select(Long userId, Measure measure) throws BusinessException {
		return measureMapper.select(measure);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Measure> selectPageList(Long userId, Measure measure, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Measure> page = measureMapper.selectPageList(measure, queryDto);
		return new Pagination<Measure>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Measure selectByFullNumber(Long userId, String fullNumber) throws BusinessException {
		return measureMapper.selectByFullNumber(fullNumber);
	}

	@Override

	public Pagination<Measure> selectLifePageList(Long userId, Measure measure, QueryDto queryDto) throws BusinessException {
		measure.setIsList(1);
		measure.setIsLife(1);
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Measure> page = measureMapper.selectPageList(measure, queryDto);
		List<Long> measureIdList = new ArrayList<Long>();
		page.forEach(item -> {
			measureIdList.add(item.getPkId());
		});
		if (!CollectionUtils.isEmpty(measureIdList)) {
			List<MeasureWarehouse> warehouseList = measureWarehouseMapper.selectByMeasureIdList(measureIdList);
			List<MeasureOutbound> outboundList = measureOutboundMapper.selectByMeasureIdList(measureIdList);
			List<MeasureCheck> checkList = measureCheckMapper.selectByMeasureIdList(measureIdList);
			Map<Long, List<MeasureWarehouse>> warehouseMap = warehouseList.stream().collect(Collectors.groupingBy(MeasureWarehouse::getMeasureId));
			Map<Long, List<MeasureOutbound>> outbpundMap = outboundList.stream().collect(Collectors.groupingBy(MeasureOutbound::getMeasureId));
			Map<Long, List<MeasureCheck>> checkMap = checkList.stream().collect(Collectors.groupingBy(MeasureCheck::getMeasureId));
			page.forEach(item -> {
				item.setWarehouseList(warehouseMap.get(item.getPkId()));
				item.setOutboundList(outbpundMap.get(item.getPkId()));
				item.setCheckList(checkMap.get(item.getPkId()));
			});
		}
		return new Pagination<Measure>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Measure> selectScripPageList(Long userId, Measure measure, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Measure> page = measureMapper.selectScripPageList(measure, queryDto);
		return new Pagination<Measure>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer measureImport(Long userId, List<Measure> measureList) throws BusinessException {
		Date date = new Date();
		int i = 1;
		for (Measure measure : measureList) {
			measure.setStorageTime(date);
			measure.setUpdateTime(date);
			measure.setUpdateUser(userId);
			measure.setMeasureSeq(Integer.parseInt(measure.getMeasureBarcode()));
			String barCode = measure.getMeasureNumber() + '-' + measure.getMeasureBarcode() + '/' + measure.getModel();
			measure.setManufacturer(measure.getSupplierName());
			measure.setMeasureBarcode(barCode);
			measure.setMeasureStatus(MeasureStatusEnum.getCode(measure.getMeasureStatusName()));
			measure.setSealMark(SealTypeEnum.getCode(measure.getSealMarkName()));
			String staffCode = measure.getUserCode();
			i++;
			if (StringUtil.isEmpty(staffCode)) {
				Object arry[] = { i,staffCode };
				throw new BusinessException("staff.not.exists.error", arry);
			}
			Staff staff = staffMapper.selectByStaffCode(staffCode, null);
			if (null == staff) {
				Object arry[] = { i,staffCode };
				throw new BusinessException("staff.not.exists.error", arry);
			}
			measure.setUseDepartmentName(staff.getDepartmentName());
			measure.setUseDepartmentId(staff.getDepartmentId());
			measure.setUseTeamName(staff.getTeamName());
			measure.setUseTeamId(staff.getTeamId());
			measure.setUserName(staff.getStaffName());
			Date checkDate;
			if(StringUtil.isNotEmpty(measure.getCheckTimeString())){
				try {
					checkDate = new SimpleDateFormat("yyyy-MM-dd").parse(measure.getCheckTimeString());
					Date nextCheckDate = new SimpleDateFormat("yyyy-MM-dd").parse(measure.getNextCheckTimeString());
					measure.setCheckTime(checkDate);
					measure.setNextCheckTime(nextCheckDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			Measure old = measureMapper.selectByFullNumber(barCode);
			if (null != old) {
				measureMapper.updateActiveById(measure, old.getPkId());
			} else {
				measure.setPkId(getPkId());
				measure.setCreateTime(date);
				measure.setCreateUser(userId);
				measure.setDelMark(0);
				measureMapper.insert(measure);
			}
		}
		return 1;
	}
}