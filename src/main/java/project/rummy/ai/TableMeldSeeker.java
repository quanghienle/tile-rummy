package project.rummy.ai;

import project.rummy.entities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableMeldSeeker {


    /**
     * find meld that can be detached to give a identical tile
     */
    public static int findDetachableIdenticalTile (int tileValue, Color tileColor, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();

        melds.stream().filter(meld -> meld.tiles().size() >= 4).forEach(meldsFound::add);

        for(Meld m: meldsFound){
            for(Tile t: m.tiles()){
                if(t.canFillToRun(tileColor, tileValue) ){
                    if(m.type() == MeldType.SET
                            || m.tiles().indexOf(t) == 0
                            || m.tiles().indexOf(t) == m.tiles().size()-1){
                        return m.getId();
                    }
                }
            }

        }
        return 0;
    }



    /**
     * find meld that can split and give:
     * on the right: tiles that can be added to the right of the given tile
     * on the left: a valid meld
     */
    public static int findRightDetachableTiles (int tileValue, Color tileColor, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();

        if(tileValue > 12 || tileValue < 3){ return 0;}

        for(Meld m: melds){
            List<Tile> temp = new ArrayList<>(m.tiles());
            for(Tile t: m.tiles()){
                temp.sort(Comparator.comparing(Tile::value));

                int index = m.tiles().indexOf(temp.get(0));
                int first = m.tiles().get(index).value() - index;

                Color actualColor = temp.get(0).color();
                int actualValue = first + m.tiles().indexOf(t);;
                if(actualColor==tileColor && actualValue== tileValue
                        && m.type() == MeldType.RUN
                        && m.tiles().indexOf(t) != m.tiles().size()-1
                        && m.tiles().indexOf(t) >= 2){
                    meldsFound.add(m);
                }
            }
        }
        return meldsFound.isEmpty() ? 0 : Collections.max(meldsFound, Comparator.comparing(meld -> meld.tiles().size())).getId();
    }


    /**
     * find meld that the tile can directly add to
     */
    public static int findDirectMeld(int tileValue, Color tileColor, List<Meld> melds){

        //the suitable meld will be the first one appears
        for(Meld m: melds){
            if(m.type() == MeldType.SET && m.tiles().size()==3 && m.getScore()/3==tileValue){
                boolean exist = false;
                for(Tile t: m.tiles()){
                    if(t.color() == tileColor && t.value()==tileValue){ exist=true; }
                }
                if(!exist){ return m.getId(); }
            }else if(m.type()==MeldType.RUN){
                List<Tile> meldTiles = new ArrayList<>(m.tiles());
                meldTiles.sort(Comparator.comparing(Tile::value));

                int index = m.tiles().indexOf(meldTiles.get(0));
                int first = m.tiles().get(index).value() - index;

                if ((meldTiles.get(0).color() == tileColor || tileColor == Color.ANY)
                        && (tileValue == first - 1 || tileValue == first + m.tiles().size())) {
                    return m.getId();
                }
            }
        }
        return 0;
    }
}
