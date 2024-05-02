package com.refugietransaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "utilisateur")
public class Utilisateur extends AbstractEntity implements UserDetails {

    @Getter
    @Column(name = "nom")
    private String nom;

    @Getter
    @Column(name = "email")
    private String email;

    @Getter
    @Column(name = "motdepasse")
    private String moteDePasse;

    @Getter
    @Column(name = "active")
    private boolean active;

    @Getter
    @Column(name = "userrole")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "utilisateur")
    @JsonIgnore
    private List<MouvementStock> mouvementStocks;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "utilisateur")
    private UserAssignment userAssignment;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String email, String moteDePasse, RoleEnum role) {
        this.nom = nom;
        this.email = email;
        this.moteDePasse = moteDePasse;
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoteDePasse() {
        return moteDePasse;
    }

    public void setMoteDePasse(String moteDePasse) {
        this.moteDePasse = moteDePasse;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public List<MouvementStock> getMouvementStocks() {
        return mouvementStocks;
    }

    public void setMouvementStocks(List<MouvementStock> mouvementStocks) {
        this.mouvementStocks = mouvementStocks;
    }

    public UserAssignment getUserAssignment() {
        return userAssignment;
    }

    public void setUserAssignment(UserAssignment userAssignment) {
        this.userAssignment = userAssignment;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return moteDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
