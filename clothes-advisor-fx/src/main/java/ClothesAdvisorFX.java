import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
public class ClothesAdvisorFX extends Application {
    enum Season { WINTER, SPRING, SUMMER, AUTUMN }
    enum Weather { SUNNY, RAIN, SNOW, WINDY, THUNDERSTORM, FROST }
    static class Advice {
        List<String> clothes;
        List<String> drinks;
        Advice(List<String> clothes, List<String> drinks) {
            this.clothes = clothes;
            this.drinks = drinks;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Clothes:\n");
            for (String c : clothes) sb.append("  - ").append(c).append("\n");
            sb.append("Drinks:\n");
            for (String d : drinks) sb.append("  - ").append(d).append("\n");
            return sb.toString();
        }
    }
    Map<Season, Map<Weather, Advice>> adviceMap = new HashMap<>();
    @Override
    public void start(Stage stage) {
        setupAdvice();
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-font-size: 14;");
        // Сезон
        ComboBox<Season> seasonCombo = new ComboBox<>();
        seasonCombo.getItems().addAll(Season.values());
        seasonCombo.setValue(Season.SUMMER);
        // Погода
        List<CheckBox> weatherChecks = new ArrayList<>();
        for (Weather w : Weather.values()) {
            CheckBox cb = new CheckBox(w.name());
            weatherChecks.add(cb);
        }
        // Кнопка и вывод
        Button showButton = new Button("Показать рекомендации");
        TextArea output = new TextArea();
        output.setEditable(false);
        showButton.setOnAction(e -> {
            Season selectedSeason = seasonCombo.getValue();
            EnumSet<Weather> selectedWeather = EnumSet.noneOf(Weather.class);
            for (CheckBox cb : weatherChecks) {
                if (cb.isSelected()) selectedWeather.add(Weather.valueOf(cb.getText()));
            }
            if (selectedWeather.isEmpty()) {
                output.setText("❗ Пожалуйста, выберите хотя бы одну погоду.");
                return;
            }
            StringBuilder result = new StringBuilder();
            result.append("Season: ").append(selectedSeason).append("\nWeather: ").append(selectedWeather).append("\n\n");
            Map<Weather, Advice> seasonAdvice = adviceMap.get(selectedSeason);
            for (Weather w : selectedWeather) {
                Advice adv = seasonAdvice.get(w);
                if (adv != null) {
                    result.append(adv).append("\n");
                } else {
                    result.append("Нет рекомендаций для ").append(w).append("\n");
                }
            }
            output.setText(result.toString());
        });
        // Добавляем элементы на экран
        root.getChildren().addAll(
                new Label("Выберите сезон:"), seasonCombo,
                new Label("Выберите погоду:")
        );
        root.getChildren().addAll(weatherChecks);
        root.getChildren().addAll(showButton, output);
        // Настройка сцены
        Scene scene = new Scene(root, 450, 550);
        stage.setTitle("Советчик одежды");
        stage.setScene(scene);
        stage.show();
    }

    private void setupAdvice() {
        // Зима
        Map<Weather, Advice> winter = new HashMap<>();
        winter.put(Weather.SNOW, new Advice(Arrays.asList("Coat", "Boots", "Scarf"), Arrays.asList("Hot chocolate", "Tea")));
        winter.put(Weather.WINDY, new Advice(Arrays.asList("Warm coat", "Hat", "Gloves"), Arrays.asList("Tea")));
        adviceMap.put(Season.WINTER, winter);
// Весна
        Map<Weather, Advice> spring = new HashMap<>();
        spring.put(Weather.RAIN, new Advice(Arrays.asList("Raincoat", "Umbrella"), Arrays.asList("Tea")));
        spring.put(Weather.SUNNY, new Advice(Arrays.asList("Light jacket"), Arrays.asList("Water")));
        adviceMap.put(Season.SPRING, spring);

        // Лето
        Map<Weather, Advice> summer = new HashMap<>();
        summer.put(Weather.SUNNY, new Advice(Arrays.asList("Cap", "Light clothes"), Arrays.asList("Water")));
        summer.put(Weather.THUNDERSTORM, new Advice(Arrays.asList("Umbrella"), Arrays.asList("Water")));
        adviceMap.put(Season.SUMMER, summer);

        // Осень
        Map<Weather, Advice> autumn = new HashMap<>();
        autumn.put(Weather.RAIN, new Advice(Arrays.asList("Umbrella", "Warm coat"), Arrays.asList("Tea")));
        autumn.put(Weather.WINDY, new Advice(Arrays.asList("Windbreaker"), Arrays.asList("Coffee")));
        adviceMap.put(Season.AUTUMN, autumn);
    }

    public static void main(String[] args) {
        launch();
    }
}