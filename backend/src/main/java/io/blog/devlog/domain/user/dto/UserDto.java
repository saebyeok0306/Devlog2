package io.blog.devlog.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 직렬화(객체 > Json)할 때 제외됨.
    @NotNull
    private String password;
    @NotNull
    private String email;
}
