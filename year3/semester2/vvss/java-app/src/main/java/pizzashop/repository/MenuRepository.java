package pizzashop.repository;

import pizzashop.model.MenuDataModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class MenuRepository implements IMenuRepository {
    private static String filename = "data/menu.txt";
    private List<MenuDataModel> listMenu;

    public MenuRepository() {
    }

    private void readMenu() {
        ClassLoader classLoader = MenuRepository.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        this.listMenu = new ArrayList<MenuDataModel>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                MenuDataModel menuItem = getMenuItem(line);
                if (menuItem != null) {
                    listMenu.add(menuItem);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private MenuDataModel getMenuItem(String line) {
        MenuDataModel item = null;
        if (line == null || line.equals("")) return null;
        StringTokenizer st = new StringTokenizer(line, ",");
        try {
            String name = st.nextToken();
            double price = Double.parseDouble(st.nextToken());
            item = new MenuDataModel(name, 0, price);
            return item;
        } catch (NumberFormatException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MenuDataModel> getMenu() {
        readMenu();//create a new menu for each table, on request
        return listMenu;
    }

}
