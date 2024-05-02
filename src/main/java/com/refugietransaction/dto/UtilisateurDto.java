package com.refugietransaction.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.refugietransaction.model.RoleEnum;
import com.refugietransaction.model.Utilisateur;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UtilisateurDto {

    private Long id;
    private String nom;
    private String email;
    private String moteDePasse;
    private RoleEnum role;

    @JsonIgnore
    private List<MouvementStockDto> mouvementStocks;
    private UserAssignmentDto userAssignment;

    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .moteDePasse(utilisateur.getMoteDePasse())
                .role(utilisateur.getRole())
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDto dto) {
        if (dto == null) {
            return null;
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMoteDePasse(dto.getMoteDePasse());
        utilisateur.setRole(dto.getRole());
        return utilisateur;
    }
}
