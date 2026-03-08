package io.blog.devlog.domain.file.model;

import io.blog.devlog.global.time.CreateTime;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
@Table(name = "temp_file")
public class TempFile extends CreateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileUrl;
}
