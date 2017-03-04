package com.legacybuy.repository;

import com.legacybuy.model.Contract;
import com.legacybuy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
