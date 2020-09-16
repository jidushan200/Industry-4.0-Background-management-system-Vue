package org.tsinghuatj.web.controller.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.DateFormatUtils;
import org.tsinghuatj.framework.web.controller.BaseController;
import org.tsinghuatj.framework.web.support.annotation.Secure;
import org.tsinghuatj.tool.domain.ToolAppendix;
import org.tsinghuatj.tool.service.IToolAppendixService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping({ "/tool" })
public class ToolAppendixController extends BaseController {

	private @Autowired(required = false) IToolAppendixService toolAppendixService;

	private @Value("${file.save.path:/}") String path;

	/**
	 * 附录删除
	 */
	@ApiOperation(value = "附录删除", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "附录删除成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-appendix-delete-by-id")
	public AjaxReturn toolAppendixDeleteById(
			@ApiParam(name = "pkId", value = "主键", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("ToolAppendixController.toolAppendixDeleteById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long userId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId > 0);

		log.debug("ToolAppendixController.toolAppendixDeleteById>>>");
		return new AjaxReturn(200, null, toolAppendixService.deleteById(userId, pkId));
	}

	/**
	 * 根据主键查找
	 */
	@ApiOperation(value = "刀具附录id查询列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "刀具附录Id查询列表查询成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-appendix-get-by-id")
	public AjaxReturn toolAppendixGetById(
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId)
			throws BusinessException {
		log.debug("ToolAppendixController.AppendixGetById<<<");
		if (log.isDebugEnabled()) {
			log.debug("pkId:" + pkId);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		Validate.isTrue(pkId >= 1);

		log.debug("ToolAppendixController.AppendixGetById>>>");
		return new AjaxReturn(200, null, toolAppendixService.selectById(curuserId, pkId));
	}

	/**
	 * 查询附录列表
	 */
	@ApiOperation(value = "查询附录列表", code = 200, produces = "application/json", notes = "")
	@ApiResponses({ @ApiResponse(code = 200, message = "附录列表查询成功", response = AjaxReturn.class) })
	@RequestMapping(method = RequestMethod.POST, value = "/tool-appendix-list")
	public AjaxReturn selectToolAppendixList(
			@ApiParam(name = "sourceName", value = "刀具名称", required = false) @RequestParam(required = false, defaultValue = "") String sourceName,
			@ApiParam(name = "appdenixType", value = "附件类型", required = false) @RequestParam(required = false, defaultValue = "") Integer appdenixType,
			@ApiParam(name = "toolType", value = "刀具类型", required = false) @RequestParam(required = false, defaultValue = "") Integer toolType)
			throws BusinessException {
		log.debug("ToolAppendixController.selectToolAppendixList<<<");
		if (log.isDebugEnabled()) {
			log.debug("sourceName:" + sourceName);
			log.debug("appdenixType:" + appdenixType);
			log.debug("toolType:" + toolType);
		}

		// 获取当前用户
		Long curuserId = getAuthentication();
		// 参数校验

		ToolAppendix toolAppendix = new ToolAppendix();
		toolAppendix.setSourceName(sourceName);
		toolAppendix.setAppdenixType(appdenixType);
		toolAppendix.setToolType(toolType);

		log.debug("ToolAppendixController.selectToolAppendixList>>>");
		return new AjaxReturn(200, null, toolAppendixService.select(curuserId, toolAppendix));
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param album
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	@Secure
	@PostMapping(path = { "/tool-appendix-upload" })
	public Map<String, Object> update(
			@ApiParam(name = "appdenixType", value = "appdenixType", required = false) @RequestParam(required = false) Integer appdenixType,
			@RequestParam(value = "file", required = true) MultipartFile[] files) throws BusinessException, Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		if (files != null) {
			List<String> datas = new ArrayList<String>();
			String dir = this.path + DateFormatUtils.format(date, "yy/MM/dd");
			File path = new File(dir);

			if (!path.exists()) {
				path.mkdirs();
			}
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					continue;
				}
				long userId = getAuthentication();
				byte[] data = file.getBytes();
				String name = file.getOriginalFilename();
				Long size = file.getSize();

				ToolAppendix appendix = new ToolAppendix();
				appendix.setAppendixSize(size);
				appendix.setSourceName(name);
				appendix.setAppdenixType(appdenixType);

				String extension = FilenameUtils.getExtension(name);
				appendix.setFileExt(extension);

				Long pkId = toolAppendixService.getNextPkId();
				appendix.setPkId(pkId);

				String newName = '/' + pkId + "." + extension;
				appendix.setNewName(DateFormatUtils.format(date, "yy/MM/dd/") + newName);
				appendix.setAppendixTime(date);
				File newfile = new File(path, newName);
				FileUtils.writeByteArrayToFile(newfile, data);
				//appendix.setToolId(toolId);
				//appendix.setAppdenixType(appdenixType);
				//appendix.setToolType(toolType);
				toolAppendixService.insert(userId, appendix);

				map.put("code", 200);
				map.put("error", 0);

				map.put("message", 200);
				map.put("pkId", pkId);
				map.put("sourceName", name);
				// datas.add(pkId);
			}
			map.put("datas", datas);
		}
		return map;
	}

	

	@ApiOperation(value = "文件下载", code = 200, produces = "application/json", notes = "")
	@GetMapping("/appendix-download")
	public ResponseEntity<byte[]> fileDownload(HttpServletRequest request,
			@ApiParam(name = "pkId", value = "pkId", required = true) @RequestParam(required = true) Long pkId) throws Exception {
		ToolAppendix toolAppendix = toolAppendixService.selectById(0l, pkId);
		if (toolAppendix == null) {
			return null;
		}
		try {
			byte[] bytes = FileUtils.readFileToByteArray(new File(path + toolAppendix.getNewName()));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentLength(toolAppendix.getAppendixSize());
			String fileName = toolAppendix.getSourceName();
			String userAgent = request.getHeader("user-agent").toLowerCase();
			if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
				// ie edge 浏览器
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				// chrome 火狐浏览器
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}
			headers.setContentDispositionFormData("attachment", fileName);
			// 解决文件名中文乱码问题
			return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			log.info("文件" + pkId + "不存在");
			return null;
		}

	}

}
