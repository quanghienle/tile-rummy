package project.rummy.behaviors;

import project.rummy.commands.Command;
import project.rummy.game.GameState;

import java.util.List;

public class DumbTableUsedMoveMaker implements ComputerMoveMaker {
  @Override
  public List<Command> calculateMove(GameState state) {
    return null;
  }
}
