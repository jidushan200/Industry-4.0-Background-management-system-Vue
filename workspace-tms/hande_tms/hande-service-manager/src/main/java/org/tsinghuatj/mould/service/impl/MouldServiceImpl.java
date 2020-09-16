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
import org.springframework.util.CollectionUtils;
import org.tsinghuatj.base.domain.Staff;
import org.tsinghuatj.base.repository.StaffMapper;
import org.tsinghuatj.framework.constants.MouldCompromiseEnum;
import org.tsinghuatj.framework.constants.MouldStatusEnum;
import org.tsinghuatj.framework.constants.MouldTypeEnum;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.mould.domain.Mould;
import org.tsinghuatj.mould.domain.MouldCheck;
import org.tsinghuatj.mould.domain.MouldOutbound;
import org.tsinghuatj.mould.domain.MouldProcess;
import org.tsinghuatj.mould.domain.MouldReceipt;
import org.tsinghuatj.mould.domain.MouldWarehouse;
import org.tsinghuatj.mould.repository.MouldBaseMapper;
import org.tsinghuatj.mould.repository.MouldCheckMapper;
import org.tsinghuatj.mould.repository.MouldMapper;
import org.tsinghuatj.mould.repository.MouldOutboundMapper;
import org.tsinghuatj.mould.repository.MouldProcessMapper;
import org.tsinghuatj.mould.repository.MouldReceiptMapper;
import org.tsinghuatj.mould.repository.MouldWarehouseMapper;
import org.tsinghuatj.mould.service.IMouldService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * Mould 表数据服务层接口实现类
 *
 */
@Service("mouldService")
public class MouldServiceImpl extends BaseServiceImpl implements IMouldService {

	private @Resource StaffMapper staffMapper;
	private @Resource MouldMapper mouldMapper;
	private @Resource MouldBaseMapper mouldBaseMapper;
	private @Resource MouldWarehouseMapper mouldWarehouseMapper;
	private @Resource MouldOutboundMapper mouldOutboundMapper;
	private @Resource MouldCheckMapper mouldCheckMapper;
	private @Resource MouldReceiptMapper mouldReceiptMapper;
	private @Resource MouldProcessMapper mouldProcessMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Mould mould) throws BusinessException {
		Long pkId = getPkId();
		mould.setPkId(pkId);
		mould.setCreateTime(new Date());
		mould.setCreateUser(userId);
		mould.setUpdateTime(new Date());
		mould.setUpdateUser(userId);
		mould.setDelMark(0);

		return mouldMapper.insert(mould);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Mould mould, Long mouldId) throws BusinessException {
		mould.setUpdateTime(new Date());
		mould.setUpdateUser(userId);
		return mouldMapper.updateActiveById(mould, mouldId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Mould selectById(Long userId, Long mouldId) throws BusinessException {
		return mouldMapper.selectById(mouldId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long mouldId) throws BusinessException {
		return mouldMapper.removeById(mouldId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long mouldId) throws BusinessException {
		Mould mould = new Mould();
		mould.setPkId(mouldId);
		mould.setUpdateTime(new Date());
		mould.setUpdateUser(userId);
		return mouldMapper.deleteById(mould);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Mould> select(Long userId, Mould mould) throws BusinessException {
		return mouldMapper.select(mould);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Mould> selectPageList(Long userId, Mould mould, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Mould> page = mouldMapper.selectPageList(mould, queryDto);
		return new Pagination<Mould>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Mould selectByFullNumber(Long userId, String fullNumber) throws BusinessException {
		Mould mould =  mouldMapper.selectByFullNumber(fullNumber);
		Integer processQty = mouldProcessMapper.selectSumProcessAmount(fullNumber);
		mould.setProcessAmount(null!=processQty?processQty:0);
		return mould;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Mould> selectLifePageList(Long userId, Mould mould, QueryDto queryDto) throws BusinessException {
		mould.setIsList(1);
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Mould> page = mouldMapper.selectLifePageList(mould, queryDto);
		List<Long> mouldIdList = new ArrayList<Long>();
		page.forEach(item -> {
			mouldIdList.add(item.getPkId());
		});
		if (!CollectionUtils.isEmpty(mouldIdList)) {
			List<MouldWarehouse> warehouseList = mouldWarehouseMapper.selectByMouldIdList(mouldIdList);
			List<MouldOutbound> outboundList = mouldOutboundMapper.selectByMouldIdList(mouldIdList);
			List<MouldCheck> checkList = mouldCheckMapper.selectByMouldIdList(mouldIdList);
			List<MouldProcess> processList = mouldProcessMapper.selectByMouldIdList(mouldIdList);
			Map<Long, List<MouldWarehouse>> warehouseMap = warehouseList.stream().collect(Collectors.groupingBy(MouldWarehouse::getMouldId));
			Map<Long, List<MouldOutbound>> outbpundMap = outboundList.stream().collect(Collectors.groupingBy(MouldOutbound::getMouldId));
			Map<Long, List<MouldCheck>> checkMap = checkList.stream().collect(Collectors.groupingBy(MouldCheck::getMouldId));
			Map<Long, List<MouldProcess>> processMap = processList.stream().collect(Collectors.groupingBy(MouldProcess::getMouldId));
			page.forEach(item -> {
				item.setWarehouseList(warehouseMap.get(item.getPkId()));
				item.setOutboundList(outbpundMap.get(item.getPkId()));
				item.setCheckList(checkMap.get(item.getPkId()));
				item.setProcessList(processMap.get(item.getPkId()));
			});
		}
		return new Pagination<Mould>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Mould selectSeqByMouldNumber(Long userId, String mouldNumber) throws BusinessException {
		MouldReceipt mouldReceipt = mouldReceiptMapper.selectByMouldNumber(mouldNumber);
		Mould mould;
		if (null != mouldReceipt) {
			mould = new Mould();
			mould.setMouldNumber(mouldNumber);
			mould.setMouldName(mouldReceipt.getMouldName());
			mould.setMouldSeq(mouldReceipt.getMouldSeq());
			mould.setLifeMax(mouldReceipt.getLifeMax());
			mould.setHeatNumber(mouldReceipt.getHeatNumber());
			mould.setSurfaceNumber(mouldReceipt.getSurfaceNumber());
		} else {
			mould = mouldMapper.selectSeqByNumber(mouldNumber);
		}
		String mouldSeq;
		Integer seq;
		if (mould != null) {
			mouldSeq = mould.getMouldSeq();
			seq = Integer.parseInt(mouldSeq) + 1;
		} else {
			mould = new Mould();
			seq = 1;
		}

		mouldSeq = fillString(seq, 5);
		mould.setMouldSeq(mouldSeq);
		return mould;
	}

	private String fillString(int num, int digit) {
		/**
		 * 0：表示前面补0 digit：表示保留数字位数 d：表示参数为正数类型
		 */
		return String.format("%0" + digit + "d", num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer mouldImport(Long userId, List<Mould> mouldList) throws BusinessException {
		Date date = new Date();
		mouldList.forEach(mould -> {
			mould.setUpdateTime(date);
			mould.setUpdateUser(userId);
			mould.setWarehouseTime(date);
			Integer seq = Integer.parseInt(mould.getMouldBarcode());
			String mouldSeq = fillString(seq, 5);
			mould.setMouldSeq(mouldSeq);
			String warehouseNumber = mould.getMouldNumber() + '-' + mouldSeq;
			String fullNumber = warehouseNumber + '/' + mould.getMouldMap();
			mould.setWarehouseCode(warehouseNumber);
			mould.setMouldBarcode(fullNumber);
			Staff staff = staffMapper.selectByUserId(userId);
			mould.setKeeper(staff.getStaffName());
			mould.setMouldStatus(MouldStatusEnum.getCode(mould.getMouldStatusName()));
			mould.setIsCompromise(MouldCompromiseEnum.getCode(mould.getIsCompromiseName()));
			mould.setMouldType(MouldTypeEnum.getCode(mould.getMouldTypeName()));
			Mould old = mouldMapper.selectByFullNumber(fullNumber);
			if (null != old) {
				mouldMapper.updateActiveById(mould, old.getPkId());
			} else {
				try {
					mould.setPkId(getPkId());
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				mould.setCreateTime(date);
				mould.setCreateUser(userId);
				mould.setDelMark(0);
				mould.setProcessAmount(0);
				mould.setMouldAmount(1);
				mould.setProcessTimes(0);
				mould.setRepairTimes(0);
				mouldMapper.insert(mould);
			}
		});
		return 1;
	}
}