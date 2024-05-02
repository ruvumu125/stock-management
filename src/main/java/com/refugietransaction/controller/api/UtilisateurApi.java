package com.refugietransaction.controller.api;
import com.refugietransaction.dto.UtilisateurDto;
import com.refugietransaction.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static com.refugietransaction.utils.Constants.UTILISATEUR_ENDPOINT;

@Api("utilisateurs")
public interface UtilisateurApi {

    @ApiOperation(value = "Créer un utilisateur", notes = "Cette methode permet d'enregistrer ou modifier un utilisateur", response = UtilisateurDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'objet utilisateur cree / modifie"),
            @ApiResponse(code = 400, message = "L'objet utilisateur n'est pas valide")
    })
    @PostMapping(value = UTILISATEUR_ENDPOINT + "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    UtilisateurDto save(@RequestBody UtilisateurDto dto);

    @GetMapping(UTILISATEUR_ENDPOINT + "/find/{email}")
    UtilisateurDto findByEmail(@PathVariable("email") String email);
}
