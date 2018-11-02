package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class TableView extends Pane implements Observer {
  private GameFXMLLoader loader;
  private Node tableView;

  @FXML private GridPane setPane1;
  @FXML private GridPane setPane2;
  @FXML private GridPane runPane;

  public TableView(TableData tableData) {
    super();
    this.loader = new GameFXMLLoader("table");
    loader.setController(this);
    loadTableView(tableData);
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.registerObserver(this);
  }

  private void loadTableView(TableData tableData) {
    try {
      tableView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load table");
    }
    renderMelds(tableData);
    getChildren().setAll(tableView);
  }

  private void renderMelds(TableData tableData) {
    int[][] setGrid1 = tableData.setGrid1;
    int[][] setGrid2 = tableData.setGrid2;
    int[][] runGrid = tableData.runGrid;
    for (int row=0; row<13; row++) {
      for(int col=0; col<4; col++) {
        int meldId1 = setGrid1[row][col];
        int meldId2 = setGrid2[row][col];
        if (meldId1 != 0) {
          Meld meld1 = Meld.idsToMelds.get(meldId1);
          renderTile(meld1.getTile(col), TileSource.TABLE_SET, setPane1, row, col);
        }
        if (meldId2 != 0) {
          Meld meld2 = Meld.idsToMelds.get(meldId2);
          renderTile(meld2.getTile(col), TileSource.TABLE_SET, setPane2, row, col);
        }
      }
    }
    for (int row=0; row<8; row++) {
      for (int col=0; col<13; col++) {
        int runId = runGrid[row][col];
        if (runId != 0) {
          Meld run = Meld.idsToMelds.get(runId);
          int firstVal = run.getTile(0).value();
          renderTile(run.getTile(col +1 - firstVal), TileSource.TABLE_RUN, runPane, row, col);
        }
      }
    }
  }

  private void renderTile(Tile tile, TileSource source, GridPane pane, int row, int col) {
    pane.add(new TileView(tile, source), col, row);
  }

  @Override
  public void update(GameState status) {
    System.out.println("Table view updated");
    runPane.getChildren().clear();
    setPane1.getChildren().clear();
    setPane2.getChildren().clear();
    renderMelds(status.getTableData());
  }
}

