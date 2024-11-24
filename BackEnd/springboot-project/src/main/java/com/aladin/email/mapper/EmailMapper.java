package com.aladin.email.mapper;

import org.apache.ibatis.annotations.Param;

public interface EmailMapper {
	boolean existsByEmailAndUsername(@Param("email") String email, @Param("username") String username);
}
