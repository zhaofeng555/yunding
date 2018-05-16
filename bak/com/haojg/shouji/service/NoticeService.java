package com.haojg.shouji.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.haojg.shouji.bean.Notice;
import com.haojg.shouji.dao.NoticeDao;
import com.haojg.shouji.form.NoticeForm;
import com.haojg.shouji.util.PageBuilder;
import com.haojg.shouji.util.SortBuilder;

@Service
public class NoticeService {

	@Autowired
	NoticeDao noticeMapper;

	public Page<NoticeForm> listNotices(Pageable p) {

		List<NoticeForm> list = new ArrayList<NoticeForm>();
		Page<Notice> data = noticeMapper.findAll(p);
		for (Notice n : data) {
			String con = n.getContent();
			con = Html2Text(con);
			if (con.length() > 40) {
				con = con.substring(0, 40);
			}
			n.setContent(con);
			NoticeForm nf = new NoticeForm();
			BeanUtils.copyProperties(n, nf);
			list.add(nf);
		}
		PageImpl<NoticeForm> rs = new PageImpl<>(list, p, data.getTotalElements());
		return rs;
	}

	private static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		java.util.regex.Matcher m_script;
		Pattern p_style;
		java.util.regex.Matcher m_style;
		Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr.replaceAll("&nbsp;", "");
		} catch (Exception e) {
		}
		return textStr;// 返回文本字符串
	}
	
	public static void main(String[] args) {
		String s = "<span style=\"font-family: Simsun;font-size:16px; text-align: center;\">&nbsp; &nbsp;开鑫购物超市免费领取手机活动，全体员工竭尽全力服务好龙江大地的每一位客户，公司感谢在这寒冷的冬季进店的每一位顾客，特此在这元旦将近的日子里，我公司特此推出以下活动：凡是在2017年12月15日----2017年12月25日之间申请免费领取手机活动的顾客，公司将给予以下政策，如办理完成的会员分享不到下面的市场，公司将会为会员进行分享人数3人（公司给分享人员不参与任何奖金），如自己能分享出去的也可以不用公司帮助分享，这样一来将会解决大部分因为分享不出去不敢办理的家人。 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;空前绝后的一次活动 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 每一位感觉分享不出去的会员请在活动日期内办理</span><br />";
		System.out.println(Html2Text(s));
	}

}
