package com.univalle.bubackend.services.menu;

import com.univalle.bubackend.DTOs.auth.CreateMenuRequest;

public interface IMenuService {
    CreateMenuRequest createMenu(CreateMenuRequest createMenuRequest);
}
