package com.refugietransaction.controller;

import com.refugietransaction.controller.api.UtilisateurApi;
import com.refugietransaction.dto.UtilisateurDto;
import com.refugietransaction.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurController implements UtilisateurApi {

    private final UtilisateurService utilisateurService;
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }
    @Override
    public UtilisateurDto save(UtilisateurDto dto) {

        return utilisateurService.save(dto);
    }

    @Override
    public UtilisateurDto findByEmail(String email) {

        return utilisateurService.findByEmail(email);
    }
}
