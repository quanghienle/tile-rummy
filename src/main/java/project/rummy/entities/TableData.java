package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.util.List;

public class TableData extends Component {
  public List<Tile> freeTiles;
  public List<Meld> melds;
  public int[][] setGrid1;
  public int[][] setGrid2;
  public int[][] runGrid;

  public TableData(Table table) {
    freeTiles = table.getFreeTiles();
    melds = table.getPlayingMelds();
    setGrid1 = table.getSetGrid1();
    setGrid2 = table.getSetGrid2();
    runGrid = table.getRunGrid();
  }
}
