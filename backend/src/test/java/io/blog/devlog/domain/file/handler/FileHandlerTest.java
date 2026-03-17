package io.blog.devlog.domain.file.handler;

import io.blog.devlog.config.TestConfig;
import io.blog.devlog.domain.file.dto.FileDto;
import io.blog.devlog.domain.file.model.FileType;
import io.blog.devlog.domain.file.model.TempFile;
import io.blog.devlog.domain.file.repository.TempFileRepository;
import io.blog.devlog.domain.file.service.TempFileService;
import io.blog.devlog.global.handler.FolderHandler;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@DataJpaTest // Component Scan을 하지 않아 컨테이너에 @Component 빈들이 등록되지 않는다.
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FileHandlerTest {
    @Autowired
    TempFileRepository tempFileRepository;
    static FileHandler fileHandler;

    @BeforeEach
    public void beforeSetUp() {
        TempFileService tempFileService = new TempFileService(tempFileRepository);
        fileHandler = new TestConfig().createFileHandler(tempFileService);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        String OS = System.getProperty("os.name").toLowerCase();
        if(!OS.contains("win")) return;
        FolderHandler folderHandler = new FolderHandler();
        ReflectionTestUtils.setField(folderHandler, "uploadPath", "C:\\Programming\\Blog\\devlog\\backend\\src\\main\\resources\\upload");
        folderHandler.deleteFolder();
    }

    @Test
    @DisplayName("FileHandler TempFile 등록 확인")
    public void fileHandlerTest() throws FileUploadException {
        // given
        String originalFileName = "test.jpg";
        String contentType = "image/jpg";
        byte[] content = "Hello, World!".getBytes();

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                content
        );

        // when
        FileDto fileDto = fileHandler.uploadFile(multipartFile);

        // then
        TempFile tempFile = tempFileRepository.findByFileUrl(fileDto.getFileUrl()).orElse(null);
        Assertions.assertThat(tempFile).isNotNull();
    }

    @Test
    @DisplayName("FileHandler Image Type 업로드 확인")
    public void fileHandlerImageTypeTest() throws FileUploadException {
        // given
        String originalFileName = "test.jpg";
        String contentType = "image/jpg";
        byte[] content = "Hello, World!".getBytes();

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                content
        );

        // when
        FileDto fileDto = fileHandler.uploadFile(multipartFile);

        // then
        Assertions.assertThat(fileDto.getFileType()).isEqualTo(FileType.IMAGE);
    }

    @Test
    @DisplayName("FileHandler Video Type 업로드 확인")
    public void fileHandlerVideoTypeTest() throws FileUploadException {
        // given
        String originalFileName = "test.mp4";
        String contentType = "video/mp4";
        byte[] content = "Hello, World!".getBytes();

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                content
        );

        // when
        FileDto fileDto = fileHandler.uploadFile(multipartFile);

        // then
        Assertions.assertThat(fileDto.getFileType()).isEqualTo(FileType.VIDEO);
    }

    @Test
    @DisplayName("FileHandler Audio Type 업로드 확인")
    public void fileHandlerAudioTypeTest() throws FileUploadException {
        // given
        String originalFileName = "test.mp3";
        String contentType = "audio/mp3";
        byte[] content = "Hello, World!".getBytes();

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                content
        );

        // when
        FileDto fileDto = fileHandler.uploadFile(multipartFile);

        // then
        Assertions.assertThat(fileDto.getFileType()).isEqualTo(FileType.AUDIO);
    }

    @Test
    @DisplayName("FileHandler Files Type 업로드 확인")
    public void fileHandlerFilesTypeTest() throws FileUploadException {
        // given
        String originalFileName = "test.txt";
        String contentType = "text/plain";
        byte[] content = "Hello, World!".getBytes();

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                originalFileName,
                contentType,
                content
        );

        // when
        FileDto fileDto = fileHandler.uploadFile(multipartFile);

        // then
        Assertions.assertThat(fileDto.getFileType()).isEqualTo(FileType.FILES);
    }
}
