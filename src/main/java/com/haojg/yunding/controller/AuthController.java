package com.haojg.yunding.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.haojg.component.IncreaseInfoSysScheduler;
import com.haojg.util.GenICodeUtils;
import com.haojg.util.WebMiscMethod;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("auth")
@Slf4j
public class AuthController {

	@Autowired
	IncreaseInfoSysScheduler sysScheduler;

	@RequestMapping({ "super" })
	@ResponseBody
	public String superManCtrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String param = request.getParameter("haojg");
		if (StringUtils.equals(param, "woshisuperman123")) {
			this.sysScheduler.scheduler();
		}
		return "OK";
	}

	@RequestMapping(value = "validateIdentifycode")
	public void validateIdentifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String remoteHost = request.getRemoteHost();
		// if(StringUtils.equalsIgnoreCase("127.0.0.1", remoteHost)){
		// WebMiscMethod.writeJson(response, OutpubResult.getSuccess("OK"));
		// return;
		// }
		log.info("access remote host {}", remoteHost);
		String identifycode = WebMiscMethod.getStr(request, "identifycode");
		String code = (String) session.getAttribute("code");
		JSONObject rs = new JSONObject();
		if (StringUtils.equals(identifycode, code)) {
			rs.put("code", 0);
		} else {
			rs.put("code", 1);
			rs.put("msg", "验证码错误");
		}
		WebMiscMethod.writeJson(response, rs);
	}

	@RequestMapping(value = "identifycode")
	public void identifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置响应头 Content-type类型
		response.setContentType("image/jpeg");
		// 以下三句是用于设置页面不缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);

		HttpSession session = request.getSession();
		String s = GenICodeUtils.getIdentifyCode(4);
		session.setAttribute("code", s);
		if (StringUtils.isBlank(s)) {
			s = "1234";
		}

		// 创建图片对象
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();
		// 随机生成颜色 Color color = new Color(255,255,255) ;
		// random.nextInt(256)的值范围是0~255
		Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

		// 把随机数写到图片上
		String a = null;
		Font font = new Font(a, Font.ITALIC, 18);
		g.setColor(color);
		g.setFont(font);
		g.drawString(s, 10, height - 5);
		g.dispose(); // 关闭画笔

		// 把图片送到客户端
		ServletOutputStream output = response.getOutputStream();
		ImageIO.write(image, "JPEG", output);
		output.flush(); // 关闭输出流
	}

}
