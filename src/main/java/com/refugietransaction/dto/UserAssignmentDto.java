package com.refugietransaction.dto;

import com.refugietransaction.model.UserAssignment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAssignmentDto {
	
	private Long id;
	private UtilisateurDto utilisateur;
	private CampDto camp;
	
	public static UserAssignmentDto fromEntity(UserAssignment userAssignment) {
		if(userAssignment == null) {
			return null;
		}
		
		return UserAssignmentDto.builder()
				.id(userAssignment.getId())
				.camp(CampDto.fromEntity(userAssignment.getCamp()))
				.build();
	}
	
	public static UserAssignment toEntity(UserAssignmentDto userAssignmentDto) {
		if(userAssignmentDto == null) {
			return null;
		}
		
		UserAssignment userAssignment = new UserAssignment();
		userAssignment.setId(userAssignmentDto.getId());
		userAssignment.setCamp(CampDto.toEntity(userAssignmentDto.getCamp()));
		
		return userAssignment;
	}
}
