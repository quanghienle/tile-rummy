package project.rummy.game.GameReader;

import com.google.gson.*;
import org.json.simple.JSONObject;
import project.rummy.entities.*;

import java.util.*;

public class PlayerLoad {
    private HandData data[];
    private PlayerStatus status[];
    private PlayerData playerData[];
    private TableData tableData;
    private int amountPlayers;

    public HandData[] configHandData(JSONObject object)
    {
        int size = 0;
        data = new HandData[4];

        if (object.get("HUMAN") != null) {
            data[size++] = getHandData(object, "HUMAN");
        }

        if (object.get("Player 1") != null) {
            data[size++] = getHandData(object, "Player 1");
        }

        if (object.get("Player 2") != null) {
            data[size++] = getHandData(object, "Player 2");
        }


        if (object.get("Player 3") != null) {
            data[size++] = getHandData(object, "Player 3");
        }
        return data;
    }


    private HandData getHandData(JSONObject object, String player) {
        JsonElement  parser = new JsonParser().parse(object.get(player).toString());
        JsonObject  jsonObject = parser.getAsJsonObject();
        List<Tile> tiles = new ArrayList<>();
        Color color;
        int tile_num;
        Tile tile;

        for (JsonElement element : jsonObject.get("Tiles").getAsJsonArray()) {
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = Tile.createTile(color, tile_num);
            tiles.add(tile);
        }
        return new HandData(new Hand(tiles, new ArrayList<>()));
    }

    private Color getColor(char c) {
        if (c == 'R') {
           return Color.RED;
        }
        if (c == 'B') {
            return Color.BLACK;
        }
        if (c == 'G') {
            return Color.GREEN;
        }
        if (c == 'O') {
            return Color.ORANGE;
        }
        if (c == 'A') {
            return Color.ANY;
        }
        // color is not detected
        return null;
    }

    public PlayerStatus[] getStatuses(JSONObject object) {
        int size = 0;
        PlayerStatus status[] = new PlayerStatus[4];

        if (object.get("HUMAN") != null) {
            status[size++] = getSingleStatus(object, "HUMAN");
        }


        if (object.get("Player 1") != null) {
            status[size++] = getSingleStatus(object, "Player 1");
        }

        if (object.get("Player 2") != null) {
            status[size++] = getSingleStatus(object, "Player 2");
        }

        if (object.get("Player 3") != null) {
            status[size++] = getSingleStatus(object, "Player 3");
        }
        return status;
    }

    private PlayerStatus getSingleStatus(JSONObject object, String player) {
        JsonElement parser = new JsonParser().parse(object.get(player).toString());
        JsonObject jsonObject = parser.getAsJsonObject();
        return check(jsonObject.get(FileLoadTypes.Status.name()).getAsString());

    }

    private PlayerStatus check(String str) {
        if (str.toUpperCase().equals("NOT PLAYED")) {
            return PlayerStatus.START;
        }
        else if ((str.toUpperCase().equals("ICE BROKEN"))) {
            return PlayerStatus.ICE_BROKEN;
        }
        return null;
    }

    public  PlayerData [] getPlayerDatas(JSONObject object) {
        int size;
        playerData = new PlayerData[4];
        if (object.get("HUMAN") != null) {
            playerData[amountPlayers++] = getSinglePlayer(object, "HUMAN");
        }


        if (object.get("Player 1") != null) {
            playerData[amountPlayers++] = getSinglePlayer(object, "Player 1");
        }


        if (object.get("Player 2") != null) {
            playerData[amountPlayers++] = getSinglePlayer(object, "Player 2");
        }

        if (object.get("Player 3") != null) {
            playerData[amountPlayers++] = getSinglePlayer(object, "Player 3");
        }
        return this.playerData;
    }

    private PlayerData getSinglePlayer(JSONObject object, String player) {
        JsonElement parser = new JsonParser().parse(object.get(player).toString());
        JsonObject jsonObject = parser.getAsJsonObject();
        return new PlayerData(jsonObject.get(FileLoadTypes.Name.name()).getAsString(), jsonObject.get(FileLoadTypes.Controller.name()).getAsString());
    }

    public int getAmountPlayers() {
        return amountPlayers;
    }

    public TableData getTableData(JSONObject object) {
        JsonElement parser = new JsonParser().parse(object.get(FileLoadTypes.FreeTiles.name()).toString());
        JsonArray jsonArray = parser.getAsJsonArray();
        TableData data = new TableData();
        ArrayList<Tile> tiles = new ArrayList<>();
        Color color;
        int tile_num;
        Tile tile;
        Meld meld;


        for (JsonElement element : jsonArray) {
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = Tile.createTile(color, tile_num);
            data.freeTiles.add(tile);
        }

        // add code to load from meld
        parser = new JsonParser().parse(object.get(FileLoadTypes.MeldsOnTable.name()).toString());
        jsonArray = parser.getAsJsonArray();

        for (JsonElement element : jsonArray) {
            tiles = new ArrayList<>();
            for (JsonElement element1 : element.getAsJsonObject().get(FileLoadTypes.Meld.name()).getAsJsonArray()) {
                color = getColor(element1.getAsString().charAt(0));
                tile_num = Integer.parseInt(element1.getAsString().substring(1));
                tile = Tile.createTile(color, tile_num);
                tiles.add(tile);


            }
            meld = Meld.createMeld(tiles);

            data.melds.add(meld);
        }


        return data;
    }

    public TurnStatus getTurnStatuses(JSONObject jsonObject) {
        JsonElement parser = new JsonParser().parse(jsonObject.toJSONString());
        TurnStatus status = new TurnStatus();

        status.canDraw = parser.getAsJsonObject().get(FileLoadTypes.canDraw.name()).getAsBoolean();
        status.canEnd = parser.getAsJsonObject().get(FileLoadTypes.canEnd.name()).getAsBoolean();
        status.canPlay = parser.getAsJsonObject().get(FileLoadTypes.canPlay.name()).getAsBoolean();
        status.isTurnEnd = parser.getAsJsonObject().get(FileLoadTypes.TurnEnd.name()).getAsBoolean();
        status.isIceBroken = parser.getAsJsonObject().get(FileLoadTypes.IceBroken.name()).getAsBoolean();

        return status;
    }



}