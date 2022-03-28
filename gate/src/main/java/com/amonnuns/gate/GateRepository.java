package com.amonnuns.gate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GateRepository extends JpaRepository<UserHistoricalModel,UUID> {


    public UserHistoricalModel findFirstByUserNameOrderByDateOfInsertDesc(String username);

}
