package com.refugietransaction.repository;

import com.refugietransaction.model.Camp;
import com.refugietransaction.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import com.refugietransaction.model.UserAssignment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserAssignmentRepository extends JpaRepository<UserAssignment, Long> {

    UserAssignment findByUtilisateur(Utilisateur utilisateur);

    @Transactional
    @Modifying
    @Query("update UserAssignment u set u.camp = ?1 where u.id = ?2")
    int updateCampById(Camp camp, Long id);

}
