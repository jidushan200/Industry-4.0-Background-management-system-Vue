package org.tsinghuatj.sys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.service.impl.BaseServiceImpl;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.sys.domain.MessageCount;
import org.tsinghuatj.sys.domain.SysMessage;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.repository.SysMessageMapper;
import org.tsinghuatj.sys.repository.SysUserAuthMapper;
import org.tsinghuatj.sys.repository.UserAccountMapper;
import org.tsinghuatj.sys.service.ISysMessageService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 *
 * SysMessage 表数据服务层接口实现类
 *
 */
@Service("sysMessageService")
public class SysMessageServiceImpl extends BaseServiceImpl implements ISysMessageService {

	private @Resource SysMessageMapper sysMessageMapper;
	private @Resource SysUserAuthMapper sysUserAuthMapper;
	private @Resource UserAccountMapper userAccountMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(String title, String message, Long userId, String realName, String authCode) throws BusinessException {
		List<Long> userList = sysUserAuthMapper.selectUserIdByAuthCode(authCode);
		Date date = new Date();
		SysMessage sysMessage = new SysMessage();
		sysMessage.setTitle(title);
		sysMessage.setMessage(message);
		sysMessage.setSenderId(userId);
		sysMessage.setSenderName(realName);
		sysMessage.setSendTime(date);
		sysMessage.setCreateTime(date);
		sysMessage.setCreateUser(userId);
		sysMessage.setUpdateTime(date);
		sysMessage.setUpdateUser(userId);
		sysMessage.setDelMark(0);
		for (Long receiverId : userList) {
			if (userId.equals(receiverId)) {
				continue;
			}
			sysMessage.setPkId(getPkId());
			sysMessage.setReceiverId(receiverId);
			sysMessageMapper.insert(sysMessage);
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, SysMessage sysMessage, Long sysMessageId) throws BusinessException {
		sysMessage.setUpdateTime(new Date());
		sysMessage.setUpdateUser(userId);
		return sysMessageMapper.updateActiveById(sysMessage, sysMessageId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public SysMessage selectById(Long userId, Long sysMessageId) throws BusinessException {
		return sysMessageMapper.selectById(sysMessageId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long sysMessageId) throws BusinessException {
		return sysMessageMapper.removeById(sysMessageId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long sysMessageId) throws BusinessException {
		SysMessage sysMessage = new SysMessage();
		sysMessage.setPkId(sysMessageId);
		sysMessage.setUpdateTime(new Date());
		sysMessage.setUpdateUser(userId);
		return sysMessageMapper.deleteById(sysMessage);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysMessage> select(Long userId, SysMessage sysMessage) throws BusinessException {
		List<SysMessage> list = sysMessageMapper.select(sysMessage);
		list.forEach(item -> {
			item.setStrTime(DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd HH:mm"));
		});
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<SysMessage> selectPageList(Long userId, SysMessage sysMessage, QueryDto queryDto) throws BusinessException {
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<SysMessage> page = sysMessageMapper.selectPageList(sysMessage, queryDto);
		return new Pagination<SysMessage>(page.getTotal(), page.getResult());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Integer[] selectMessageCount(Long userId, Long receiverId) throws BusinessException {
		Integer[] count = { 0, 0, 0 };
		List<MessageCount> list = sysMessageMapper.selectMessageGroupCount(receiverId);
		list.stream().forEach(item -> {
			count[item.getReadFlag()] = item.getCount();
		});
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<SysMessage> selectImmediateMessageList(Long userId, int second) throws BusinessException {
		Calendar c = new GregorianCalendar();
		Date date = new Date();
		c.setTime(date);// 设置参数时间
		c.add(Calendar.SECOND, -second);// 把日期往后增加SECOND 秒.整数往后推,负数往前移动
		List<SysMessage> list = sysMessageMapper.selectImmediateMessageList(userId, c.getTime());
		List<Long> pkIdList = new ArrayList<Long>();
		list.stream().forEach(item -> {
			pkIdList.add(item.getPkId());
		});
		if (pkIdList.size() > 0) {
			sysMessageMapper.updateSendFlag(pkIdList);
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Map<String, Integer> selectToDoList(Long userId) throws BusinessException {
		// 获取审核权限
		Map<String, Integer> map = new HashMap<String, Integer>();
		UserAccount userAccount = userAccountMapper.selectById(userId);
		if (1 != userAccount.getRoleId()) {
			String[] auditAuth = { "01030102", "01030103", "0103010401", "0103010402", 
					"01040102", "01040103", "01040104", "0104010501", "0104010502", 
					"01050102", "01050103", "01050104", "01050105", "0105010601", "0105010602" };
			List<String> authList = sysUserAuthMapper.selectAuditAuthCode(userId, auditAuth);
			if (null == authList || authList.size() < 1) {
				return null;
			}
			int toolPurchaseQty = 0,measurePurchaseQty = 0 ,fixturePurchaseQty = 0;
			//刀具
			if(authList.contains("01030102")){
				//本厂领导
				toolPurchaseQty = sysMessageMapper.countToAuditToolPruchase(userAccount.getDepartmentId(), 1, null);
			}else if(authList.contains("01030103")){
				//工艺部
				toolPurchaseQty = sysMessageMapper.countToAuditToolPruchase(null, 2, null);
			}else if(authList.contains("0103010401")){
				//新品开发
				toolPurchaseQty = sysMessageMapper.countToAuditToolPruchase(null, 3, 1);
			}else if(authList.contains("0103010402")){
				//常用刀具
				toolPurchaseQty = sysMessageMapper.countToAuditToolPruchase(null, 3, 2);
			}
			//量具
			if(authList.contains("01040102")){
				//本厂领导
				measurePurchaseQty = sysMessageMapper.countToAuditMeasurePruchase(userAccount.getDepartmentId(), 1, null);
			}else if(authList.contains("01030103")){
				//判定（装备车间）
				measurePurchaseQty = sysMessageMapper.countToAuditMeasurePruchase(null, 2, null);
			}else if(authList.contains("01040104")){
				//补充采购价格
				measurePurchaseQty = sysMessageMapper.countToAuditMeasurePruchase(null, 3, null);
			}else if(authList.contains("0104010501")){
				//主管领导审核-新品开发
				measurePurchaseQty = sysMessageMapper.countToAuditMeasurePruchase(null, 4, 1);
			}else if(authList.contains("0104010502")){
				//主管领导审核-常用量具
				measurePurchaseQty = sysMessageMapper.countToAuditMeasurePruchase(null, 4, 3);
			}
			//夹具
			if(authList.contains("01050102")){
				//本厂领导
				fixturePurchaseQty = sysMessageMapper.countToAuditFixturePruchase(userAccount.getDepartmentId(), 1, null);
			}else if(authList.contains("01050103")){
				//工艺部审核
				fixturePurchaseQty = sysMessageMapper.countToAuditFixturePruchase(null, 2, null);
			}else if(authList.contains("01050104")){
				//圆柱分厂审核
				fixturePurchaseQty = sysMessageMapper.countToAuditFixturePruchase(null, 3, null);
			}else if(authList.contains("01050105")){
				//待采购询价
				fixturePurchaseQty = sysMessageMapper.countToAuditFixturePruchase(null, 5, null);
			}else if(authList.contains("0105010601")){
				////主管领导审核-新品开发
				fixturePurchaseQty = sysMessageMapper.countToAuditMeasurePruchase(null, 6, 1);
			}
			else if(authList.contains("0105010602")){
				////主管领导审核-常用夹具
				fixturePurchaseQty = sysMessageMapper.countToAuditFixturePruchase(null, 6, 2);
			}
			map.put("toolPurchaseQty", toolPurchaseQty);
			map.put("measurePurchaseQty", measurePurchaseQty);
			map.put("fixturePurchaseQty", fixturePurchaseQty);
			return map;			
		} else {
			map.put("toolPurchaseQty", sysMessageMapper.countAllToAuditToolPruchase());
			map.put("measurePurchaseQty", sysMessageMapper.countAllToAuditMeasurePruchase());
			map.put("fixturePurchaseQty", sysMessageMapper.countAllToAuditFixturePruchase());
			return map;
		}
	}
}