package com.app.auth.dto;

import java.util.List;

// Respuesta al listar directores favoritos
public record FavoriteDirectorsResponse(
    Long userId,
    List<String> directors
) {}