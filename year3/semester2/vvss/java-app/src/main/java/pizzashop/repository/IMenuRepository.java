package pizzashop.repository;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;

import java.util.List;

public interface IMenuRepository {
    List<MenuDataModel> getMenu();
}
