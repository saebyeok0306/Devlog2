package io.blog.devlog.domain.views.service;

import io.blog.devlog.domain.views.dto.PostViewCountDto;
import io.blog.devlog.domain.views.model.PostViewCount;
import io.blog.devlog.domain.views.repository.PostViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostViewCountServiceImpl implements PostViewCountService {
    private final PostViewCountRepository postViewCountRepository;

    @Override
    public void increaseViewCount(Long postId) {
        LocalDate now = LocalDate.now();
        Optional<PostViewCount> postViewCountOptional = postViewCountRepository.findByPostIdAndViewDate(postId, now);
        PostViewCount postviewCount = null;
        if (postViewCountOptional.isEmpty()) {
            postviewCount = new PostViewCount(postId, now, 1);
        } else {
            postviewCount = postViewCountOptional.get();
            postviewCount.increaseViewCount();
        }
        postViewCountRepository.save(postviewCount);
    }

    @Override
    public LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }

    @Override
    public LocalDate parseStartMonth(String month) {
        // month format: yyyy-MM
        List<Integer> splits = Arrays.stream(month.split("-")).map(Integer::parseInt).toList();
        if (splits.size() != 2) {
            throw new IllegalArgumentException("Invalid month format");
        }
        return LocalDate.of(splits.get(0), splits.get(1), 1);
    }

    @Override
    public LocalDate parseEndMonth(String month) {
        // month format: yyyy-MM
        List<Integer> splits = Arrays.stream(month.split("-")).map(Integer::parseInt).toList();
        if (splits.size() != 2) {
            throw new IllegalArgumentException("Invalid month format");
        }
        LocalDate end = LocalDate.of(splits.get(0), splits.get(1), 1);
        return LocalDate.of(splits.get(0), splits.get(1), end.lengthOfMonth());
    }

    @Override
    public LocalDate parseStartYear(String year) {
        // year format: yyyy
        try {
            return LocalDate.of(Integer.parseInt(year), 1, 1);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year format");
        }
    }

    @Override
    public LocalDate parseEndYear(String year) {
        try {
            return LocalDate.of(Integer.parseInt(year), 12, 31);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year format");
        }
    }

    @Override
    public List<PostViewCountDto> getDailyPostViewCount(Long postId, String start, String end) {
        LocalDate startDate = this.parseDate(start);
        LocalDate endDate = this.parseDate(end);

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (startDate.plusMonths(12).isBefore(endDate)) {
            throw new IllegalArgumentException("The difference between the two dates must be less than 12 months");
        }
        List<Object[]> dailyViews = postViewCountRepository.findDailyViews(postId, startDate, endDate);
        return dailyViews.stream().map(PostViewCountDto::ofDailyView).toList();
    }

    @Override
    public List<PostViewCountDto> getMonthlyPostViewCount(Long postId, String start, String end) {
        LocalDate startDate = this.parseStartMonth(start);
        LocalDate endDate = this.parseEndMonth(end);

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (startDate.plusMonths(12).isBefore(endDate)) {
            throw new IllegalArgumentException("The difference between the two dates must be less than 12 months");
        }
        List<Object[]> monthlyViews = postViewCountRepository.findMonthlyViews(postId, startDate, endDate);
        return monthlyViews.stream().map(PostViewCountDto::ofMonthlyView).toList();
    }

    @Override
    public List<PostViewCountDto> getYearlyPostViewCount(Long postId, String start, String end) {
        LocalDate startDate = this.parseStartYear(start);
        LocalDate endDate = this.parseEndYear(end);

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (startDate.plusYears(3).isBefore(endDate)) {
            throw new IllegalArgumentException("The difference between the two dates must be less than 3 years");
        }
        List<Object[]> yearlyViews = postViewCountRepository.findYearlyViews(postId, startDate, endDate);
        return yearlyViews.stream().map(PostViewCountDto::ofYearlyView).toList();
    }
}
