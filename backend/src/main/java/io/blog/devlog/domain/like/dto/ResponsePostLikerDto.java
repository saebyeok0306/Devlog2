package io.blog.devlog.domain.like.dto;

import io.blog.devlog.domain.user.dto.ResponseUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostLikerDto {
    private String postUrl;
    private List<ResponseUserDto> users;
}
