package project.rummy.networks;

import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.Game;
import project.rummy.messages.PlayerInfo;

public class ServerGameInitializer extends DefaultGameInitializer {
  private PlayerInfo[] playerInfos;

  ServerGameInitializer(PlayerInfo[] playerInfos) {
    this.playerInfos = playerInfos;
  }

  @Override
  public void initPlayers(Game game) {
    Controller[] controllers = new Controller[]{
        new ManualController(),
        new ManualController(),
        new ManualController(),
        new ManualController()};
    Player[] players = new Player[4];
    for (int i = 0; i<4; i++) {
      if (playerInfos[i] == null) {
        players[i] = new Player("Computer", controllers[i], i);
      } else {
        players[i] = new Player(playerInfos[i].getName(), controllers[i], i);
      }
    }
    game.setUpPlayers(players);
    game.setControlledPlayer(0);
  }
}
