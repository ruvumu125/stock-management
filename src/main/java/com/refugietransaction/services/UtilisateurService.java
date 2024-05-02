package com.refugietransaction.services;

import com.refugietransaction.dto.UtilisateurDto;
import com.refugietransaction.dto.auth.AuthenticationRequest;
import com.refugietransaction.dto.auth.AuthenticationResponse;

public interface UtilisateurService {

    UtilisateurDto save(UtilisateurDto dto);

    UtilisateurDto findByEmail(String email);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
