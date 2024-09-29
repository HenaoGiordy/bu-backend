package com.univalle.bubackend.services.menu;

import com.univalle.bubackend.DTOs.CreateMenuRequest;

import java.util.Optional;

public interface IMenuService {
    CreateMenuRequest createMenu(CreateMenuRequest createMenuRequest);
    Optional<CreateMenuRequest> getMenu(Integer menuId);
    CreateMenuRequest editMenu(CreateMenuRequest createMenuRequest);
}
