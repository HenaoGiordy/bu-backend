package com.univalle.bubackend.DTOs.odontology;

import org.springframework.data.domain.Page;

public record VisitOdontologyResponse(Page<VisitResponse> list, UserResponse user) {
}
