package org.tsinghuatj.web.controller.sys;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.Pagination;
import org.tsinghuatj.framework.domain.QueryDto;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.sys.domain.SysMessage;
import org.tsinghuatj.sys.service.ISysMessageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/message" })
public class MessageController extends BaseController {

	private @Autowired(required = false) ISysMessageService messageService;

	/**
	 * 查询未读消息数量
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "查询未读消息数量", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询未读消息数量", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/count")
	public AjaxReturn messageCount() throws BusinessException {
		log.debug("MessageController.messageCount<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		Integer count[] = messageService.selectMessageCount(userId, userId);
		log.debug("MessageController.messageCount>>>");
		return new AjaxReturn(200, null, count[0]);
	}

	/**
	 * 消息初始化
	 * 
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "消息初始化", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "消息初始化", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.GET, value = "/init")
	public AjaxReturn messageInt() throws BusinessException {
		log.debug("MessageController.messageCount<<<");

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(userId > 0);
		SysMessage where = new SysMessage();
		where.setReceiverId(userId);
		List<SysMessage> list = messageService.select(userId, where);
		Map<Integer, List<SysMessage>> map = list.stream().collect(Collectors.groupingBy(SysMessage::getReadFlag));
		log.debug("MessageController.messageCount>>>");
		return new AjaxReturn(200, null, map);
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "消息查询成功", response = SysMessage.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-page-list")
	public AjaxReturn getPageList(@ApiParam(name = "page", value = "page", required = true) @RequestParam(required = true) Integer page, //
			@ApiParam(name = "rows", value = "rows", required = false) @RequestParam(required = false, defaultValue = "10") Integer rows, @ApiParam(name = "readFlag", value = "0-未读1-已读", required = false) @RequestParam(required = false) Integer readFlag) throws BusinessException {

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验
		Validate.isTrue(page >= 0);
		Validate.isTrue(rows <= 100);
		// 封装查询条件
		QueryDto queryDto = new QueryDto();
		queryDto.setPage(page);
		queryDto.setRows(rows);
		SysMessage where = new SysMessage();
		where.setReceiverId(userId);
		where.setReadFlag(readFlag);
		Pagination<SysMessage> pagination = messageService.selectPageList(userId, where, queryDto);

		return pagination;
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "查询即时信息", response = SysMessage.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/get-immediate-list")
	public AjaxReturn getImmediateMessageList( //
	) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, messageService.selectImmediateMessageList(userId, 1000));
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "修改已读状态", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/update-read-flag")
	public AjaxReturn updateReadFlag(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId //
	) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		SysMessage message = new SysMessage();
		message.setReadFlag(1);
		message.setReadTime(new Date());
		messageService.updateActiveById(userId, message, pkId);
		return new AjaxReturn(200, null, 1);
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "修改删除状态", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/delete-message")
	public AjaxReturn deleteMessage(@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId //
	) throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		messageService.deleteById(userId, pkId);
		return new AjaxReturn(200, null, 1);
	}

	@ApiResponses({ @ApiResponse(code = 200, message = "待办事项", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/to-do-list")
	public AjaxReturn toDoList() throws BusinessException {
		// 获取当前用户
		Long userId = getAuthentication();
		return new AjaxReturn(200, null, messageService.selectToDoList(userId));
	}

}
