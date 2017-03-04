package com.legacybuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.legacybuy.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByIdAndUsername(Long id, String username);

	public User findByUsername(String username);

}
