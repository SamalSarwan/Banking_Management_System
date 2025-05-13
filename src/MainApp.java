import service.MenuService;

public class MainApp {
    public static void main(String[] args) {
        MenuService menuService = new MenuService();
        menuService.run();
    }
}