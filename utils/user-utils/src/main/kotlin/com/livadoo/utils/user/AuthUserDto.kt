package com.livadoo.utils.user

data class AuthUserDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto,
)
