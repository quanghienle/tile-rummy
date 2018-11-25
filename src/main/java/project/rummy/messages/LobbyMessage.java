package project.rummy.messages;

import io.netty.channel.Channel;

public class LobbyMessage extends Message {
  private PlayerInfo[] playerInfos;

  public LobbyMessage(PlayerInfo[] playerInfos) {
    super(playerInfos);
    this.playerInfos = playerInfos;
  }

  @Override
  public void handleBy(Channel channel, MessageProcessor processor) {
    if (payload instanceof PlayerInfo[]) {
      PlayerInfo[] playerInfos = (PlayerInfo[]) payload;
      processor.processLobbyInfo(channel, playerInfos);
    } else {
      throw new IllegalStateException("Payload is not of type GameState");
    }
  }
}
