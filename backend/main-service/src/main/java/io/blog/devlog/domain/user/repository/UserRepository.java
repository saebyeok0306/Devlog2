package io.blog.devlog.domain.user.repository;

import io.blog.devlog.domain.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import io.blog.devlog.domain.user.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository를 하지 않아도, JpaRepository에 있고 그걸 상속했기 때문에 괜찮음.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // findBy 규칙 -> select * from user where username = 1?
    public Optional<User> findByUsername(String username); // JPA Query Methods
    public Optional<User> findByEmail(String email);
//    public Optional<User> findByRefreshToken(String refreshToken);
    public List<User> findAllByRole(Role role);
    @Modifying
    @Query("update User u set u.profileUrl = :profileUrl where u.email = :email")
    public void updateProfileUrl(String email, String profileUrl);
}
