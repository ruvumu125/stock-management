package com.refugietransaction.services.impl;

import com.refugietransaction.dto.CampDto;
import com.refugietransaction.dto.UtilisateurDto;
import com.refugietransaction.dto.auth.AuthenticationRequest;
import com.refugietransaction.dto.auth.AuthenticationResponse;
import com.refugietransaction.exceptions.EntityNotFoundException;
import com.refugietransaction.exceptions.ErrorCodes;
import com.refugietransaction.exceptions.InvalidEntityException;
import com.refugietransaction.model.Camp;
import com.refugietransaction.model.RoleEnum;
import com.refugietransaction.model.UserAssignment;
import com.refugietransaction.model.Utilisateur;
import com.refugietransaction.repository.UserAssignmentRepository;
import com.refugietransaction.repository.UtilisateurRepository;
import com.refugietransaction.services.JwtService;
import com.refugietransaction.services.MailSenderService;
import com.refugietransaction.services.UtilisateurService;
import com.refugietransaction.validator.UtilisateurValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final UserAssignmentRepository userAssignmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, UserAssignmentRepository userAssignmentRepository,PasswordEncoder passwordEncoder, MailSenderService mailService,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.userAssignmentRepository = userAssignmentRepository;
        this.passwordEncoder=passwordEncoder;
        this.mailService = mailService;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;

    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {

        List<String> errors = UtilisateurValidator.validate(dto);
        String noEncrypted_password="";
        if (!errors.isEmpty()) {
            //log.error("Utilisateur is not valid {}", dto);
            throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }

        if ((dto.getId() ==null || dto.getId().compareTo(0L) == 0)){

            if (userAlreadyExists(dto.getEmail())){

                throw new InvalidEntityException("Un autre utilisateurO avec le meme email existe deja", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                        Collections.singletonList("Un autre utilisateurO avec le meme email existe deja dans la BDD"));

            }

            noEncrypted_password=generateCommonLangPassword();
            dto.setMoteDePasse(passwordEncoder.encode(noEncrypted_password));


        }
        if (dto.getId() !=null){

            if (!dto.getEmail().equals(userEmail(dto.getId())) && userAlreadyExists(dto.getEmail())){

                throw new InvalidEntityException("Un autre utilisateurA avec le meme email existe deja", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
                        Collections.singletonList("Un autre utilisateurA avec le meme email existe deja dans la BDD"));

            }

            String password =utilisateurRepository.findUtilisateurById(dto.getId()).getMoteDePasse();
            dto.setMoteDePasse(passwordEncoder.encode(password));

        }

        if (dto.getId() !=null && !dto.getEmail().equals(userEmail(dto.getId())) ){

            noEncrypted_password=generateCommonLangPassword();
           dto.setMoteDePasse(passwordEncoder.encode(noEncrypted_password));
        }

        if (dto.getRole().equals(RoleEnum.AGENT)){

            if (dto.getId() ==null || dto.getId().compareTo(0L) == 0){

                Utilisateur utilisateur=new Utilisateur(dto.getNom(),dto.getEmail(),dto.getMoteDePasse(),dto.getRole());
                UserAssignment userAssignment=new UserAssignment(UtilisateurDto.toEntity(dto),CampDto.toEntity(dto.getUserAssignment().getCamp()));
                utilisateur.setUserAssignment(userAssignment);
                userAssignment.setUtilisateur(utilisateur);

                //send email
                mailService.sendNewMail(dto.getEmail().trim(), "Passord", "This is your password "+noEncrypted_password);

                return UtilisateurDto.fromEntity(
                        utilisateurRepository.save(utilisateur)
                );

            }
            else{

                UserAssignment userAssignment=userAssignmentRepository.findByUtilisateur(UtilisateurDto.toEntity(dto));

                Camp camp=new Camp();
                camp.setId(dto.getUserAssignment().getCamp().getId());
                userAssignment.setCamp(camp);
                userAssignmentRepository.updateCampById(camp,userAssignment.getId());
            }


        }

        //send email
        mailService.sendNewMail(dto.getEmail().trim(), "Passord", "This is your password "+noEncrypted_password);

        return UtilisateurDto.fromEntity(
                utilisateurRepository.save(UtilisateurDto.toEntity(dto))
        );

    }

    private boolean userAlreadyExists(String email) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findUtilisateurByEmail(email);
        return utilisateur.isPresent();
    }

    private String userEmail(Long id){
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);

        String user_email="";
        if(utilisateur.isPresent()) {
            Utilisateur existingUser = utilisateur.get();
            user_email = existingUser.getEmail();
            //operate on existingCustomer
        }

        return user_email;

    }
    public String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    @Override
    public UtilisateurDto findByEmail(String email) {

        return utilisateurRepository.findUtilisateurByEmail(email)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND)
                );
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final Utilisateur user = utilisateurRepository.findByEmail(request.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("fullName", user.getNom());
        claims.put("userRole", user.getRole());
        claims.put("userAssignment",user.getUserAssignment());
        final String token = jwtService.generateToken(user,claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
