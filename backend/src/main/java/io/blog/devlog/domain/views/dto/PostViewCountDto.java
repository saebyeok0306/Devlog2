package io.blog.devlog.domain.views.dto;

import io.blog.devlog.domain.post.dto.ResponsePostNonContentDto;
import io.blog.devlog.domain.post.model.Post;
import io.blog.devlog.domain.views.model.PostViewCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostViewCountDto {
    private String viewDate;
    private int viewCount;

    public static PostViewCountDto of(PostViewCount postViewCount) {
        return PostViewCountDto.builder()
                .viewDate(postViewCount.getViewDate().toString())
                .viewCount(postViewCount.getViewCount())
                .build();
    }

    public static PostViewCountDto ofDailyView(Object[] dailyView) {
        LocalDate localDate = (LocalDate) dailyView[0];
        Long viewCount = (Long) dailyView[1];
        return PostViewCountDto.builder()
                .viewDate(localDate.toString())
                .viewCount(viewCount.intValue())
                .build();
    }

    public static PostViewCountDto ofMonthlyView(Object[] monthlyView) {
        Long viewCount = (Long) monthlyView[2];
        return PostViewCountDto.builder()
                .viewDate(String.format("%d-%02d", (int) monthlyView[0], (int) monthlyView[1]))
                .viewCount(viewCount.intValue())
                .build();
    }

    public static PostViewCountDto ofYearlyView(Object[] yearlyView) {
        Long viewCount = (Long) yearlyView[1];
        return PostViewCountDto.builder()
                .viewDate(String.format("%d", (int) yearlyView[0]))
                .viewCount(viewCount.intValue())
                .build();
    }
}
