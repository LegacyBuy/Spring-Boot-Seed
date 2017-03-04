package com.legacybuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.legacybuy.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	public Authority findByAuthority(String authority);

	public List<Authority> findAllByAuthority(String authority);

	public List<Authority> findAllByAuthorityIn(List<String> authorities);

}
