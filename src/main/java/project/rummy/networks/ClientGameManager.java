package project.rummy.networks;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import project.rummy.game.*;
import project.rummy.main.TileRummyApplication;
import project.rummy.messages.GameStateMessage;
import project.rummy.messages.PlayerInfo;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class ClientGameManager implements Observable, Observer {
  private TileRummyApplication gameApplication;
  private int playerId;
  private String playerName;
  private ChannelId channelId;
  private PlayerInfo[] playerInfos;
  private boolean isGameStarted;
  private List<Observer> observers;
  private GameState gameState;

  public ClientGameManager(TileRummyApplication application) {
    this.gameApplication = application;
    this.observers = new ArrayList<>();
  }

  public void onLobbyJoined(ChannelId channelId, int playerId, String playerName) {
    this.channelId = channelId;
    this.playerId = playerId;
    this.playerName = playerName;
  }

  public void onLobbyUpdated(PlayerInfo[] playerInfos) {
    this.playerInfos = playerInfos;
    for (PlayerInfo info : playerInfos) {
      System.out.println("Connected " + info.getName());
      if (info.getChannelId() == channelId) {
        playerId = info.getPlayerId();
        playerName = info.getName();
      }
    }
  }

  public void initializeGame(GameState initialState) {
    Game game =
        new GameStore(new NetworkGameInitializer(initialState, playerId, gameApplication.getChannel(), this))
            .initializeGame();
    gameApplication.setUpGame(game);
    game.registerObserver(this);
    isGameStarted = true;
  }

  public void onGameStateUpdated(GameState state) {
    this.gameState = state;
    notifyObservers();
  }

  public boolean isGameStarted() {
    return isGameStarted;
  }

  @Override
  public void registerObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    observers.forEach(observer -> observer.update(gameState));
  }

  @Override
  public void update(GameState status) {
    if (isGameStarted && (playerId == status.getCurrentPlayer())) {
      gameApplication.getChannel().writeAndFlush(new GameStateMessage(status));
    }
  }
}