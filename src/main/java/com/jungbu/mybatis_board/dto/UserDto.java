package com.jungbu.mybatis_board.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Getter
//@Setter
//@ToString
@Data
public class UserDto {
	private String userId;
	private String pw;
	private String name;
	private String email;
	private String phone;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birth;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date signup;
}
