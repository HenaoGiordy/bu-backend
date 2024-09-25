package com.univalle.bubackend.services.menu;

import com.univalle.bubackend.DTOs.auth.CreateMenuRequest;
import com.univalle.bubackend.models.Menu;
import com.univalle.bubackend.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements IMenuService{
    private MenuRepository menuRepository;

    @Override
    public CreateMenuRequest createMenu(CreateMenuRequest createMenuRequest) {
        Menu menu = new Menu(createMenuRequest);
        menuRepository.save(menu);
        return new CreateMenuRequest(menu.getId(), menu.getMainDish(), menu.getDrink(), menu.getDessert(), menu.getPrice());

    }

}
