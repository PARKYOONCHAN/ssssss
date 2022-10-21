package com.jungbu.mybatis_board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jungbu.mybatis_board.dto.UserDto;

@Mapper //xml로 작성한 sql을 맵핑하는 type 지정
public interface UserMapper {
	//select id = list : return List
	List<UserDto> list(@Param(value = "start") int startRow, int rows);
	UserDto detail(String userId);
	//select가 1개만 반환할때 resultType으로 반환값 작성 가능
	UserDto login(String userId, String pw);
	int update(UserDto user);
	int insert(UserDto user);
	int delete(String userId);
}
