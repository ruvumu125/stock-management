package com.refugietransaction.validator;

import java.util.ArrayList;
import java.util.List;

import com.refugietransaction.dto.UserAssignmentDto;

public class UserAssignmentValidator {
	
	public static List<String> validate(UserAssignmentDto userAssignmentDto){
		List<String> errors = new ArrayList<>();
		
		if(userAssignmentDto == null) {
			errors.add("Veillez selectionner un camp");

			return errors;
		}

        if(userAssignmentDto.getCamp() == null || userAssignmentDto.getCamp().getId() == null) {
			errors.add("Veillez selectionner un camp");
		}
		
		
		return errors;
	}
}
