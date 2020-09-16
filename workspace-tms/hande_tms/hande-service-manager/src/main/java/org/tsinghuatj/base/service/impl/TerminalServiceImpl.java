package org.tsinghuatj.base.service.impl;

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
import org.tsinghuatj.base.domain.Terminal;
import org.tsinghuatj.base.repository.TerminalMapper;
import org.tsinghuatj.base.service.ITerminalService;


/**
 *
 * Terminal 表数据服务层接口实现类
 *
 */
@Service("terminalService")
public class TerminalServiceImpl extends BaseServiceImpl implements ITerminalService {

	private @Resource TerminalMapper terminalMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer insert(Long userId, Terminal terminal) throws BusinessException {	
		terminal.setPkId(getPkId());
		terminal.setCreateTime(new Date());
		terminal.setCreateUser(userId);
		terminal.setUpdateTime(new Date());
		terminal.setUpdateUser(userId);
		terminal.setDelMark(0);
		return terminalMapper.insert(terminal);
	}
				
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer updateActiveById(Long userId, Terminal terminal, Long terminalId) throws BusinessException {
		terminal.setUpdateTime(new Date());
		terminal.setUpdateUser(userId);
		return terminalMapper.updateActiveById(terminal, terminalId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Terminal selectById(Long userId, Long terminalId) throws BusinessException {
		return terminalMapper.selectById(terminalId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer removeById(Long userId, Long terminalId) throws BusinessException {
		return terminalMapper.removeById(terminalId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Integer deleteById(Long userId, Long terminalId) throws BusinessException {
		Terminal terminal = new Terminal();
		terminal.setPkId(terminalId);
		terminal.setUpdateTime(new Date());
		terminal.setUpdateUser(userId);
		return terminalMapper.deleteById(terminal);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public List<Terminal> select(Long userId, Terminal terminal) throws BusinessException {		
		return terminalMapper.select(terminal);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
	public Pagination<Terminal> selectPageList(Long userId, Terminal terminal,QueryDto queryDto) throws BusinessException {		
		PageHelper.startPage(queryDto.getPage(), queryDto.getRows(), true);
		Page<Terminal> page = terminalMapper.selectPageList(terminal, queryDto);
		return new Pagination<Terminal>(page.getTotal(), page.getResult());		
	}

	@Override
	public Integer terminalImport(Long userId, List<Terminal> terminalList) throws BusinessException {
		Date date = new Date();
		terminalList.forEach(terminal -> {	
			try {
				terminal.setPkId(getPkId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			terminal.setCreateTime(date);
			terminal.setCreateUser(userId);
			terminal.setUpdateTime(date);
			terminal.setUpdateUser(userId);
			terminal.setDelMark(0);	
			terminalMapper.insert(terminal);
		});
		return 1;
	}
}