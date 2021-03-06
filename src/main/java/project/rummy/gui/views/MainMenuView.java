package project.rummy.gui.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import project.rummy.entities.PlayerData;
import project.rummy.main.GameFXMLLoader;
import project.rummy.main.TileRummyApplication;
import project.rummy.rigging.ResourceReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class MainMenuView extends Pane {
  private GameFXMLLoader loader;

  @FXML
  private Button start;
  @FXML
  private Button load;
  @FXML
  private Button credits;
  @FXML
  private Button exit;
  @FXML
  private Pane startMenu;
  @FXML
  private Pane gameSetupMenu;
  @FXML
  private Pane offlineButton;
  @FXML
  private Pane onlineButton;
  @FXML
  private ToggleGroup players;
  @FXML
  private ToggleGroup players1;
  @FXML
  private ToggleGroup players2;
  @FXML
  private ToggleGroup players3;
  @FXML
  private Button playButton;
  @FXML
  private TextField name1;
  @FXML
  private TextField name2;
  @FXML
  private TextField name3;
  @FXML
  private TextField name4;
  @FXML
  private Pane loadMenu;
  @FXML
  private ScrollPane scroll;
  @FXML
  private Pane files;

  private Map<String, Pane> menus;
  private List<ToggleGroup> groups;
  private List<TextField> names;

  MainMenuView() {
    super();
    this.loader = new GameFXMLLoader("MainMenu");
    loader.setController(this);
    loadMainMenuView();

    this.start.setOnMouseClicked(mouseEvent1 -> onStartClicked());
    this.load.setOnMouseClicked(mouseEvent1 -> onLoadClicked());
    this.credits.setOnMouseClicked(mouseEvent1 -> onCreditsClicked());
    this.exit.setOnMouseClicked(mouseEvent1 -> onExitClicked());
    this.offlineButton.setOnMouseClicked(event -> this.openMenu("setup"));
    this.onlineButton.setOnMouseClicked(event -> this.startNetworkGame());
    this.playButton.setOnMouseClicked(mouseEvent -> onPlayButtonClicked());
    menus = new HashMap<>();
    groups = Arrays.asList(players, players1, players2, players3);
    names = Arrays.asList(name1, name2, name3, name4);
    setup();
  }

  private void setup() {
    menus.put("start", startMenu);
    menus.put("setup", gameSetupMenu);
    menus.put("load", loadMenu);
    scroll.setContent(files);
    loadLoadGameMenu();
    groups.forEach(group -> group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      onGameOptionChanged();
    }));
  }

  private void loadLoadGameMenu() {
    ResourceReader reader = new ResourceReader();
    try {
      List<String> fileNames = reader.getResourceFiles("load").stream()
          .filter(name -> name.matches(".+\\.json"))
          .collect(Collectors.toList());
      for (String fileName : fileNames) {
        Button button = FXMLLoader.load(getClass().getResource("/fxml/fileButton.fxml"));
        button.setText(fileName);
        button.setOnMouseClicked(event -> startLoadGame(fileName));
        files.getChildren().add(button);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startLoadGame(String fileName) {
    TileRummyApplication.getInstance().startLoadedGame(fileName);
  }

  private void startNetworkGame() {
    TileRummyApplication.getInstance().startNetworkClient();
  }

  private void onPlayButtonClicked() {
    List<PlayerData> playerDataList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      int index = groups.get(i).getToggles().indexOf(groups.get(i).getSelectedToggle());
      switch (index) {
        case 0:
          playerDataList.add(new PlayerData(names.get(i).getText(), "human"));
          break;
        case 1:
          playerDataList.add(new PlayerData(names.get(i).getText(), "strategy1"));
          break;
        case 2:
          playerDataList.add(new PlayerData(names.get(i).getText(), "strategy2"));
          break;
        case 3:
          playerDataList.add(new PlayerData(names.get(i).getText(), "strategy3"));
          break;
      }
    }
    TileRummyApplication.getInstance().startSinglePlayerGame(playerDataList);
  }

  private void loadMainMenuView() {
    Node mainMenuView;
    try {
      mainMenuView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load main menu");
    }
    getChildren().setAll(mainMenuView);
  }

  private void onStartClicked() {
    openMenu("start");
  }

  private void openMenu(String type) {
    menus.values().forEach(pane -> pane.setVisible(false));
    if (menus.containsKey(type)) {
      menus.get(type).setVisible(true);
    }
  }

  private void onLoadClicked() {
    openMenu("load");
  }

  private void onCreditsClicked() {
    openMenu("credits");
    System.out.println("Credits");
  }

  private void onExitClicked() {
    System.exit(0);
  }

  private void onGameOptionChanged() {
    long disableCounts =
        groups.stream().filter(group -> group.getToggles().indexOf(group.getSelectedToggle()) == 4).count();
    playButton.setDisable(disableCounts > 2);
  }
}
