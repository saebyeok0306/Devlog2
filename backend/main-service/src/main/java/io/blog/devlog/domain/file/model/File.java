package io.blog.devlog.domain.file.model;

import io.blog.devlog.global.time.CreateTime;
import jakarta.persistence.*;
import lombok.*;

@Table(name="FILES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Builder
@ToString
public class File extends CreateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private long fileSize;
    @Column(unique = true)
    private String fileUrl; // year/month/day/fileName
    private String filePath; // res
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    private Long entityId;

    public File setEntity(EntityType entityType, Long entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
        return this;
    }
}

/* Post나 Comment이 삭제되는 경우, 종속되어 있던 File은 엔티티 삭제처리를 할 때,
*  따로 Select * From File Where entity.file.id = file.id 로 Repository를 통해 조회 후
*  파일삭제와 함께 처리됨. 고로 특정 Entity 타입을 기록할 필요없이, Type + id로 기록하면 됨. */