package com.weathermood.weathermood.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
//mysql users테이블 select해주는 이메일 조회 로그인할때 사용