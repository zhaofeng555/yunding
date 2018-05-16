package com.haojg.shouji.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haojg.shouji.bean.Notice;
import com.haojg.shouji.dao.NoticeDao;
import com.haojg.shouji.form.NoticeForm;
import com.haojg.shouji.service.NoticeService;
import com.haojg.shouji.util.PageBuilder;
import com.haojg.shouji.util.WebMiscMethod;

@Controller
@RequestMapping("notice")
public class NoticeController {

	static String savePath = "/var/www/html/serverImg/";// 文件保存目录路径
	static String saveUrl = "http://www.kaixinchaoshi.cn:8081/serverImg/";// 要返回给xhEditor的文件保存目录URL
	
	@Autowired
	NoticeDao noticeMapper;
	@Autowired
	NoticeService noticeService;
	
	@RequestMapping("listing.do")
	public String listing(Model m, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Pageable p = PageBuilder.generate(request);
		Page<NoticeForm> list = noticeService.listNotices(p);
		
		m.addAttribute("notices", list);

		return "notice/list";
	}

	
	@RequestMapping("save.do")
	public void save(Notice notice, HttpServletRequest request, HttpServletResponse response) throws IOException{
		notice.setCreateTime(new Date());
		Notice n = noticeMapper.save(notice);
		JSONObject rs =new JSONObject();
		if(n!=null && n.getId()!=0) {
			rs.put("code", 0);
		}else{
			rs.put("code", 1);
			rs.put("msg", "保存失败");
		}
		WebMiscMethod.writeJson(response, rs);
	}
	
	@RequestMapping("edit.do")
	public String edit(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Notice n = noticeMapper.findOne(id);
		NoticeForm nf = new NoticeForm();
		BeanUtils.copyProperties(n, nf);
		request.setAttribute("notice", nf);
		return "notice_info";
	}

	@RequestMapping("delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<Integer> idList = new ArrayList<Integer>();
		String idStr = WebMiscMethod.getStr(request, "id"); 
		String idArr[]=idStr.split(",");
		for (String id : idArr) {
			idList.add(Integer.valueOf(id));
			noticeMapper.delete(Long.valueOf(id));
		}
		JSONObject rs =new JSONObject();
		rs.put("code", 0);
		WebMiscMethod.writeJson(response, rs);
	}
	
	@RequestMapping("/uploadNoticeImage.do")
	public void upload(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		File uploadDir = new File(savePath);// 创建要上传文件到指定的目录
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		String contentDisposition = request.getHeader("Content-Disposition");// 如果是HTML5上传文件，那么这里有相应头的
		if (contentDisposition != null) {// HTML5拖拽上传文件
			String fileName = contentDisposition.substring(contentDisposition.lastIndexOf("filename=\""));// 文件名称
			fileName = fileName.substring(fileName.indexOf("\"") + 1);
			fileName = fileName.substring(0, fileName.indexOf("\""));

			ServletInputStream inputStream;
			try {
				inputStream = request.getInputStream();
			} catch (IOException e) {
				this.uploadError("上传文件出错！", response);
				return;
			}

			if (inputStream == null) {
				this.uploadError("您没有上传任何文件！", response);
				return;
			}

			// 检查文件扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

			String newFileName = System.currentTimeMillis() + "." + fileExt;// 新的文件名称
			File uploadedFile = new File(savePath, newFileName);

			try {
				FileCopyUtils.copy(inputStream, new FileOutputStream(uploadedFile));
			} catch (FileNotFoundException e) {
				this.uploadError("上传文件出错！", response);
				return;
			} catch (IOException e) {
				this.uploadError("上传文件出错！", response);
				return;
			}

			this.uploadSuccess(saveUrl + newFileName, fileName, 0, response);// 文件上传成功
			return;
			
		} else {// 不是HTML5拖拽上传,普通上传
			if (ServletFileUpload.isMultipartContent(request)) {// 判断表单是否存在enctype="multipart/form-data"
				DiskFileItemFactory dfif = new DiskFileItemFactory();
				dfif.setSizeThreshold(1 * 1024 * 1024);// 设定当上传文件超过1M时，将产生临时文件用于缓冲
				dfif.setRepository(new File(savePath));// 存放临时文件的目录
				ServletFileUpload sfu = new ServletFileUpload(dfif);
				try {
					List<FileItem> fis = sfu.parseRequest(request);
					for (FileItem fi : fis) {
						if (fi.isFormField()) {
							// 是表单项，不是上传项
						} else {
							String fileName = fi.getName();// 上传的文件名称，如果是欧鹏浏览器，这个会带路径

							try {
								// 检查文件扩展名
								String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
								String newFileName = System.currentTimeMillis() + "." + fileExt;// 新的文件名称
								fi.write(new File(savePath, newFileName));

								this.uploadSuccess(saveUrl + newFileName, fileName, 0, response);// 文件上传成功
								return;
							} catch (Exception e) {
								this.uploadError("上传文件出错！", response);
								return;
							}
						}
					}
				} catch (FileUploadException e) {
					this.uploadError("上传文件出错！", response);
					return;
				}
			} else {
				// 不是multipart/form-data表单
				this.uploadError("您没有上传任何文件！", response);
				return;
			}
			return;
		}

	}

	private void uploadError(String err, String msg, HttpServletResponse response) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("err", err);
		m.put("msg", msg);
		this.writeJson(m, response);
	}

	private void uploadError(String err, HttpServletResponse response) {
		this.uploadError(err, "", response);
	}

	private void uploadSuccess(String newFileName, String fileName, int id, HttpServletResponse response) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("err", "");
		Map<String, Object> nm = new HashMap<String, Object>();
		nm.put("url", newFileName);
		nm.put("localfile", fileName);
		nm.put("id", id);
		m.put("msg", nm);
		this.writeJson(m, response);
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @throws IOException
	 */
	private void writeJson(Object object, HttpServletResponse response) {
		try {
			String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
