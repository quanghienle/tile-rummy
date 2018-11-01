package project.rummy.gui.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;
import project.rummy.events.TileChooseEvent;
import project.rummy.events.TileUnChooseEvent;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;

public class TileView extends Pane {
  private GameFXMLLoader loader;
  private Tile tile;
  private boolean isChosen;
  private TileSource tileSource;

  @FXML private Text value;
  @FXML private Rectangle border;

  public TileView(Tile tile, TileSource tileSource) {
    super();
    this.tile = tile;
    this.tileSource = tileSource;
    this.isChosen = false;
    this.loader = new GameFXMLLoader("tile");
    loader.setController(this);
    loadTileView(tile);
    setUpHandlers();
  }

  public Tile getTile() {
    return tile;
  }

  public boolean isChosen() {
    return isChosen;
  }

  private void setUpHandlers() {
    this.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onTileClicked);
  }

  private void loadTileView(Tile tile) {
    Node tileView;
    try {
      tileView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load tile");
    }
    value.setText(String.valueOf(tile.value()));
    value.setFill(getColor(tile.color()));
    if (tile.isHightlight()) {
      border.getStyleClass().add("highlight");
    }
    getChildren().setAll(tileView);
  }

  private static javafx.scene.paint.Color getColor(Color color) {
    switch (color) {
      case RED:
        return javafx.scene.paint.Color.RED;
      case BLACK:
        return javafx.scene.paint.Color.BLACK;
      case ORANGE:
        return javafx.scene.paint.Color.ORANGE;
      default:
        return javafx.scene.paint.Color.GREEN;
    }
  }

  private void onTileClicked(MouseEvent event) {
    isChosen = !isChosen;
    if (isChosen) {
      border.getStyleClass().add("chosen");
    } else {
      border.getStyleClass().clear();
    }
    this.fireEvent(new TileChooseEvent(tile, tileSource, isChosen));
    event.consume();
  }
}
