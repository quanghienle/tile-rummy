package project.rummy.ai;

import project.rummy.entities.HandData;
import project.rummy.entities.Meld;
import project.rummy.entities.TableData;
import project.rummy.entities.Tile;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.HashSet;
import java.util.Set;

public class SmartStateAnalyzer {
  private GameState state;

  private int[] tableTileCounts;
  private int[] handTileCounts;

  public SmartStateAnalyzer() {
    this.state = null;
    tableTileCounts = new int[53];
    handTileCounts = new int[53];
  }

  public SmartStateAnalyzer(GameState state) {
    tableTileCounts = new int[53];
    handTileCounts = new int[53];
    this.state = state;
    analyzeCurrentHand();
    analyzeTableState();
  }

  private static int calculatePosition(Tile tile) {
    return !tile.isJoker() ? tile.value() + tile.color().value() * 13 - 1 : 52;
  }

  private void analyzeTableState() {
    tableTileCounts = new int[53];
    TableData tableData = state.getTableData();
    for (Meld meld : tableData.melds) {
      for (Tile tile : meld.tiles()) {
        tableTileCounts[calculatePosition(tile)]++;
      }
    }
  }

  private void analyzeCurrentHand() {
    HandData handData = state.getHandsData()[state.getCurrentPlayer()];
    handData.tiles.forEach(tile -> {
      handTileCounts[calculatePosition(tile)]++;
    });
  }

  public boolean shouldPlay(Tile tile) {
    if (state == null) {
      return true;
    }
    if(tile.isJoker()){
      return state.getHandsData()[state.getCurrentPlayer()].tiles.size()>1 ? false : true;
    }

    return !(isPartOfRun(tile)
        || isPartOfSet(tile)
        || shouldWaitForSet(tile)
        || shouldWaitForRun(tile));
  }

  public boolean isPartOfRun(Tile tile) {
    int pos = calculatePosition(tile);
    if(handTileCounts[52]==2){
      return true;
    }

    if(handTileCounts[52]==1) {
      return (pos < 50 && (handTileCounts[pos + 1] > 0 || handTileCounts[pos + 2] > 0))
              || (pos > 0 && pos < 51 && (handTileCounts[pos - 1] > 0 || handTileCounts[pos + 1] > 0))
              || (pos > 1 && (handTileCounts[pos - 1] > 0 || handTileCounts[pos - 2] > 0));
    }

    return (pos < 50 && handTileCounts[pos + 1] > 0 && handTileCounts[pos + 2] > 0)
            || (pos > 0 && pos < 51 && handTileCounts[pos - 1] > 0 && handTileCounts[pos + 1] > 0)
            || (pos > 1 && handTileCounts[pos - 1] > 0 && handTileCounts[pos - 2] > 0);

  }

  public boolean isPartOfSet(Tile tile) {
    int value = tile.value();
    int tileCount = 0;
    for (int i = 0; i < 4; i++) {
      if (handTileCounts[i * 13 + value - 1] > 0) {
        tileCount++;
      }
    }
    tileCount += handTileCounts[52];
    return tileCount >= 3;
  }

  public boolean shouldWaitForSet(Tile tile) {
    Set<Integer> set = new HashSet<>();

    int value = tile.value();
    for (int i = 0; i < 4; i++) {
      int pos = i * 13 + value - 1;
      if (handTileCounts[pos] > 0) {
        set.add(pos);
      }
    }
    if (set.size() + handTileCounts[52] == 1) {
      return false;
    }
    if (set.size() + handTileCounts[52] == 2) {
      int tablePoints = 0;
      for (int i = 0; i < 4; i++) {
        int pos = i * 13 + value - 1;
        if (handTileCounts[pos] == 0) {
          tablePoints += tableTileCounts[pos];
        }
      }

      return tablePoints +tableTileCounts[52]  <= 3;
    }
    return true;
  }

  public boolean shouldWaitForRun(Tile tile) {
    if (isPartOfRun(tile)) {
      return true;
    }
    int pos = tile.value() + tile.color().value() * 13 - 1;
    if(pos==52){
      return true;
    } else if (pos == 0) {
      if (handTileCounts[1] > 0) {
        return tableTileCounts[2] <= 1;
      } else {
        return tableTileCounts[1] <= 1;
      }
    } else if (pos == 1) {
      if (handTileCounts[0] > 0) {
        return tableTileCounts[2] <= 1;
      } else if (handTileCounts[2] > 0) {
        return tableTileCounts[0] + tableTileCounts[3] <= 2;
      } else {
        return tableTileCounts[1] <= 2;
      }
    }else if (pos == 50) {
      if (handTileCounts[51] > 0) {
        return tableTileCounts[49] <= 1;
      } else if (handTileCounts[49] > 0) {
        return tableTileCounts[51] + tableTileCounts[48] <= 2;
      } else {
        return tableTileCounts[49] <= 1;
      }
    }else if (pos == 51) {
      if (handTileCounts[50] > 0) {
        return tableTileCounts[49] <= 1;
      } else if (handTileCounts[49] > 0) {
        return tableTileCounts[50] <= 1;
      }
    }else {

      if (handTileCounts[pos + 1] > 0) {
        return tableTileCounts[pos + 2] + tableTileCounts[pos - 1] <= 2;
      }
      if (handTileCounts[pos - 1] > 0) {
        return tableTileCounts[pos + 1] + tableTileCounts[pos - 2] <= 2;
      }
      if (handTileCounts[pos + 2] > 0) {
        return tableTileCounts[pos + 1] <= 1;
      }
      if (handTileCounts[pos - 2] > 0) {
        return tableTileCounts[pos - 1] <= 1;
      }
    }
    return false;
  }

  public void setState(GameState state) {
    this.state = state;
    analyzeTableState();
    analyzeCurrentHand();
  }
}
