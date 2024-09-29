package com.univalle.bubackend.services.menu;

import com.univalle.bubackend.DTOs.CreateMenuRequest;
import com.univalle.bubackend.exceptions.menu.MenuNotFound;
import com.univalle.bubackend.models.Menu;
import com.univalle.bubackend.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements IMenuService{
    private MenuRepository menuRepository;

    @Override
    public CreateMenuRequest createMenu(CreateMenuRequest createMenuRequest) {
        Menu menu = new Menu(createMenuRequest);
        menuRepository.save(menu);
        return new CreateMenuRequest(menu.getId(), menu.getMainDish(), menu.getDrink(), menu.getDessert(), menu.getPrice(), menu.getNote());

    }

    @Override
    public Optional<CreateMenuRequest> getMenu(Integer menuId) {
        return menuRepository.findMenuById(menuId).
                map(menu -> CreateMenuRequest.builder()
                        .id(menu.getId())
                        .mainDish(menu.getMainDish())
                        .drink(menu.getDrink())
                        .price(menu.getPrice())
                        .dessert(menu.getDessert())
                        .note(menu.getNote())
                        .build());

    }

    @Override
    public CreateMenuRequest editMenu(CreateMenuRequest createMenuRequest) {
        Optional<Menu> menuRequestOpt = menuRepository.findMenuById(createMenuRequest.id());
        if (menuRequestOpt.isPresent()) {
            Menu menuExist = menuRequestOpt.get();
            menuExist.setMainDish(createMenuRequest.mainDish());
            menuExist.setDrink(createMenuRequest.drink());
            menuExist.setDessert(createMenuRequest.dessert());
            menuExist.setPrice(createMenuRequest.price());
            menuExist.setNote(createMenuRequest.note());
            menuRepository.save(menuExist);
            return createMenuRequest;
        } else {
            throw new MenuNotFound("Menu no encontrado");
        }
    }


}
