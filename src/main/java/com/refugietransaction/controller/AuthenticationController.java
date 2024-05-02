package com.refugietransaction.controller;

import com.refugietransaction.controller.api.AuthenticationApi;
import com.refugietransaction.dto.auth.AuthenticationRequest;
import com.refugietransaction.dto.auth.AuthenticationResponse;
import com.refugietransaction.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {

    private final UtilisateurService utilisateurService;

    @Autowired
    public AuthenticationController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {

        return ResponseEntity.ok(utilisateurService.authenticate(request));
    }
}
