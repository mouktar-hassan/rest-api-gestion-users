package com.example.utils;

/**
 * Classe utilitaire pour centraliser les chemins d'API
 */
public final class ApiPaths {
    private ApiPaths() {
        // Empêche l'instanciation
    }

    public static final String BASE_API = "/api";

    // Utilisateur endpoints
    public static final String UTILISATEURS = BASE_API + "/utilisateurs";
    public static final String UTILISATEUR = BASE_API + "/utilisateur";
    public static final String UTILISATEUR_BY_ID = UTILISATEUR + "/{id}";
    public static final String UTILISATEUR_ADRESSES = UTILISATEUR_BY_ID + "/adresses";
    public static final String UTILISATEUR_ADRESSE = UTILISATEUR_ADRESSES + "/{adresseId}";
}
