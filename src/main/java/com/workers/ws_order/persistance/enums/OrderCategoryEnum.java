package com.workers.ws_order.persistance.enums;

public enum OrderCategoryEnum {
    ELECTRICITY(1, "Электрика"),
    PLUMBING(2, "Сантехника"),
    PAINTING(3, "Покраска"),
    CARPENTRY(4, "Плотницкие работы"),
    ROOFING(5, "Кровельные работы"),
    FLOORING(6, "Укладка полов"),
    WINDOWS_INSTALLATION(7, "Установка окон"),
    DOORS_INSTALLATION(8, "Установка дверей"),
    TILING(9, "Укладка плитки"),
    PLASTERING(10, "Штукатурные работы"),
    INSULATION(11, "Утепление"),
    DEMOLITION(12, "Демонтаж"),
    FENCING(13, "Установка заборов"),
    LANDSCAPING(14, "Благоустройство территории"),
    HVAC(15, "Монтаж вентиляции и кондиционирования"),
    MASONRY(16, "Кладка кирпича"),
    CONCRETE_WORK(17, "Бетонные работы"),
    POOL_CONSTRUCTION(18, "Строительство бассейнов"),
    GUTTER_INSTALLATION(19, "Монтаж водостоков"),
    SOLAR_PANEL_INSTALLATION(20, "Установка солнечных панелей"),
    WALLPAPERING(21, "Поклейка обоев"),
    PAVING(22, "Укладка тротуарной плитки"),
    CABINET_INSTALLATION(23, "Установка кухонных гарнитуров"),
    GYPSUM_BOARD_WORK(24, "Работы с гипсокартоном"),
    ELECTRIC_HEATING_INSTALLATION(25, "Установка электрического отопления"),
    WATERPROOFING(26, "Гидроизоляция"),
    FOUNDATION_REPAIR(27, "Ремонт фундамента"),
    EXTERIOR_CLADDING(28, "Монтаж наружного сайдинга"),
    GAZEBO_CONSTRUCTION(29, "Строительство беседок"),
    FACADE_WORK(30, "Фасадные работы"),
    DRIVEWAY_CONSTRUCTION(31, "Строительство подъездных путей"),
    BATHROOM_RENOVATION(32, "Ремонт ванных комнат"),
    KITCHEN_RENOVATION(33, "Ремонт кухонь"),
    BASEMENT_FINISHING(34, "Отделка подвалов"),
    STONE_WORK(35, "Каменные работы"),
    DECK_CONSTRUCTION(36, "Строительство террас"),
    WINDOW_REPLACEMENT(37, "Замена окон"),
    CEILING_WORK(38, "Работы с потолками"),
    GARAGE_DOOR_INSTALLATION(39, "Установка гаражных ворот"),
    FURNITURE_ASSEMBLY(40, "Сборка мебели"),
    SMART_HOME_INSTALLATION(41, "Установка умных систем дома"),
    POOL_MAINTENANCE(42, "Обслуживание бассейнов");

    private final int code;
    private final String description;

    OrderCategoryEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByCode(int code) {
        for (OrderCategoryEnum type : OrderCategoryEnum.values()) {
            if (type.getCode() == code) {
                return type.getDescription();
            }
        }
        throw new IllegalArgumentException("Unknown order category code: " + code);
    }
}
