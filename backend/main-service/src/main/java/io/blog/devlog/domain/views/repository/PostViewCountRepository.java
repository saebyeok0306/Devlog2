package io.blog.devlog.domain.views.repository;

import io.blog.devlog.domain.views.model.PostViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostViewCountRepository extends JpaRepository<PostViewCount, Long> {
    Optional<PostViewCount> findByPostIdAndViewDate(Long postId, LocalDate viewDate);

    // 일별 조회수 조회
    @Query("SELECT pvc.viewDate, SUM(pvc.viewCount) FROM PostViewCount pvc " +
            "WHERE pvc.postId = :postId AND pvc.viewDate BETWEEN :startDate AND :endDate " +
            "GROUP BY pvc.viewDate")
    List<Object[]> findDailyViews(@Param("postId") Long postId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 월별 조회수 조회
    @Query("SELECT YEAR(pvc.viewDate), MONTH(pvc.viewDate), SUM(pvc.viewCount) FROM PostViewCount pvc " +
            "WHERE pvc.postId = :postId AND pvc.viewDate BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(pvc.viewDate), MONTH(pvc.viewDate)")
    List<Object[]> findMonthlyViews(@Param("postId") Long postId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 연도별 조회수 조회
    @Query("SELECT YEAR(pvc.viewDate), SUM(pvc.viewCount) FROM PostViewCount pvc " +
            "WHERE pvc.postId = :postId AND pvc.viewDate BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(pvc.viewDate)")
    List<Object[]> findYearlyViews(@Param("postId") Long postId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
