package com.jungbu.mybatis_board.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jungbu.mybatis_board.dto.UserDto;
import com.jungbu.mybatis_board.mapper.UserMapper;

import lombok.Getter;
import lombok.Setter;
@RequestMapping("/user")
@Controller // Spring MVC 컨테이너에서 관리하는 servlet
public class UserController {
	//datasource factory(데이터 컨테이너)에서 db접속 후 생성한 객체를 주입
	@Autowired //객체를 주입받겠다
	DataSource dataSource;
	//사용하는 시점에 객체를 생성 (객체지향 프로그래밍)
	//사용하는 시점에 객체를 주입 (관점지향 프로그래밍 제어의 역전)
	
	@Autowired
	UserMapper userMapper;
	
	@GetMapping("/list.do")
	public String list(
			Model model,
			@RequestParam(defaultValue = "1") int page
			) { //view에 객체를 전달할때 사용하는 객체
		System.out.println("/user/list.do");
		final int LOWS = 10;
		int startRow = (page-1)*LOWS;
		
		List<UserDto> userList = null;
		try(Connection conn = dataSource.getConnection();) {
			 //null오류 발생
			PreparedStatement pstmt = conn.prepareStatement("Select * from user LIMIT ?,?");
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, LOWS);
			ResultSet rs = pstmt.executeQuery();
			userList = new ArrayList<UserDto>();
			while(rs.next()) {
				UserDto user = new UserDto();
				user.setUserId(rs.getString("user_id"));
				user.setPw(rs.getString("pw"));
				user.setName(rs.getString("name"));
				user.setPhone(rs.getString("phone"));
				user.setEmail(rs.getString("email"));
				
				user.setBirth(rs.getDate("birth"));
				user.setSignup(rs.getDate("signup"));
				userList.add(user);
			}
			//System.out.println(userList);
			model.addAttribute("userList", userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/user/list"; //req.getRequestDispatcher("WEB-INF/templates/user/list.html").forward(req,resp);
	}
	@GetMapping("/list2.do")
	public String List2(
			Model model,
			@RequestParam(defaultValue = "1") int page
			) {
		final int ROWS = 10;
		int startRow = (page-1)*ROWS;
		List<UserDto> userList = userMapper.list(startRow,ROWS);
		model.addAttribute("userList", userList);
		return "/user/list";
	}
	@GetMapping("/detail.do")
	public UserDto detail(String userId) {
		UserDto userDto = userMapper.detail(userId); 
		return userDto;
		//model.addAttribure("userDto", userDto);
		//return "/user/detail";
	}
	
	@PostMapping("/update.do")
	public String update(UserDto user) {
		int update = 0;
		try {
			update = userMapper.update(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(update>0) {
			return "redirect:/user/list.do";
			
		}else {
			return "redirect:/user/detail.dp?userId=" + user.getUserId();
			
		}
	}
	
	@GetMapping("/delete.do")
	public String delete(String userId) {
		int delete=0;
		try {
			delete = userMapper.delete(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(delete>0) {
			return "redirect:/user/list.do";
			
		}else {
			return "redirect:/user/detail.do?userId="+userId;
		}
	}
	
	@GetMapping("/insert.do")
	public void insert() {
		
	}
	
	@PostMapping("insert.do")
	public String insert(UserDto user) {
		System.out.println(user);
		int insert = 0;
		try {
			insert=userMapper.insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(insert>0) {
			return "redirect:/user/list.do";
		}else {
			return "redirect:/user/insert.do";
		}
	}
	
	@Getter@Setter
	class CheckUser{
		private int check; //0:없음, 1:있음, -1:오류
		private UserDto user;
	}
	
	//@ResponseBodys DTO 객체를 JSON으로 바꿔서 반환 (캡슐화된 필드만 ) 
	@GetMapping("/checkUserId.do")
	public @ResponseBody CheckUser checkUserId(
			@RequestParam(required = true) String userId
			) {
		CheckUser checkUser = new CheckUser();
		UserDto user = null;
		try {
			user = userMapper.detail(userId);
			if(user!=null) {
				checkUser.setCheck(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			checkUser.setCheck(-1);
		}
		return checkUser;
	}
	
	@GetMapping("/login.do")
	public void login() {
		
	}
	@PostMapping("/login.do")
	public String login(
			@RequestParam(required = true) String userId, 
			@RequestParam(required = true) String pw,
			HttpSession session
			) {
		UserDto loginUser = null;
		try {
			loginUser = userMapper.login(userId, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(loginUser);
		if(loginUser != null) {
			session.setAttribute("loginUser", loginUser);
			return "redirect:/";
		}else {
			return "redirect:/user/login.do";
		}
	}
	@GetMapping("/logout.do")
	public String logout(HttpSession session) {
		//session.invalidate();
		session.removeAttribute("loginUser");
		return "redirect:/";
	}
}
