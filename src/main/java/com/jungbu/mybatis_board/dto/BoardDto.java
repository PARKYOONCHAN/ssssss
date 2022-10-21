package com.jungbu.mybatis_board.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BoardDto {

	private int boardNo;
	private String title;
	private String contents;
	private Date postTime;
	private String userId;
	private int views;
	
	private int likes; // select count(*) prefer where prefer=1 AND board_no=no
	private int bads;  // select count(*) prefer where prefer=0 AND board_no=no
}
