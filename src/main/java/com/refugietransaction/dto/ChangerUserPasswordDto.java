package com.refugietransaction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangerUserPasswordDto {
	
	private Long id;
	
	private String motDePasse;
	
	private String confirmMotDePasse;
}
