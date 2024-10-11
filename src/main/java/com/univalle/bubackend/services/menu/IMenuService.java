package com.univalle.bubackend.services.menu;

import com.univalle.bubackend.DTOs.CreateMenuRequest;

import java.util.List;
import java.util.Optional;

public interface IMenuService {
    CreateMenuRequest createMenu(CreateMenuRequest createMenuRequest);
    List<CreateMenuRequest> getMenu();
    CreateMenuRequest editMenu(CreateMenuRequest createMenuRequest);
}
