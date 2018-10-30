package project.rummy.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.entities.*;
import project.rummy.entities.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class TurnInteractionTest {
  private final Tile O3 = Tile.createTile(Color.ORANGE, 3);
  private final Tile O4 = Tile.createTile(Color.ORANGE, 4);
  private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
  private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
  private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
  private final Tile G8 = Tile.createTile(Color.GREEN, 8);
  private final Tile B8 = Tile.createTile(Color.BLACK, 8);
  private final Tile R8 = Tile.createTile(Color.RED, 8);

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private Controller controller;

  private Player player;

  @Before
  public void setUp() {
    player = new Player("Player", controller);
  }

  @Test
  //Add on -- Adding tiles from hand to from new meld on the table
 public void scenario_1_Test() throws IllegalAccessException{
      //Set up controllers and hand
      player.setStatus(PlayerStatus.ICE_BROKEN);
      Hand hand = player.hand();
      hand.addTiles(O3, O8);

      // Set up the table
      Table table = new Table();
      Meld setOfRun =  Meld.createMeld(O4, O5, O6);
      Meld setOfSet = Meld.createMeld(R8, G8, B8);
      table.addMeld(setOfRun);
      table.addMeld(setOfSet);

      //Create action handler
      ActionHandler handler = new ActionHandler(player, table);
      ManipulationTable tempTable = handler.getManipulationTable();

      //Test table before turn
      assertThat(tempTable.getMelds().isEmpty(), is (true));

      /* Start the turn  */
      //Adding {O4, O5, O6} and {R8, G8, B8}   to tempt table
      handler.takeTableMeld(0);
      handler.takeTableMeld(0);
      assertThat(tempTable.getMelds().get(0).tiles(),contains(O4, O5, O6) );
      assertThat(tempTable.getMelds().get(1).tiles(), contains(R8, G8, B8));

      //Forming tiles {O3} , {O8} to play from hand
      hand.formMeld(0);
      hand.formMeld(0);
      assertThat(hand.getMelds().get(0).tiles(), contains(O3));
      assertThat(hand.getMelds().get(1).tiles(), contains( O8));

      //Adding {O3} , {O8} to manipulation table
      handler.playFromHand(0);
      handler.playFromHand(0);
      assertThat(tempTable.getMelds().get(2).tiles(), contains(O3));
      assertThat(tempTable.getMelds().get(3).tiles(), contains(O8));

      //Combine {O3} to O4...O6 and {O8} to R8...B8
      tempTable.combineMelds(0,2);
      tempTable.combineMelds(0,1);
      assertThat(tempTable.getMelds().get(0).tiles(), contains(O3, O4, O5, O6));
      assertThat(tempTable.getMelds().get(1).tiles(), contains( R8, G8, B8, O8));

      //End turn
      handler.endTurn();

      //Checking the turn
      assertThat(table.getPlayingMelds().size(),is(2) );
      assertThat(table.getPlayingMelds().get(0).tiles(),contains(O3, O4, O5, O6));
      assertThat(table.getPlayingMelds().get(1).tiles(),contains(R8, G8, B8, O8));
      assertThat(hand.getMelds().size(), is( 0));
      assertThat(hand.getTiles().size(), is(0));
  }

  @Test
   //Forming new set
  public void scenario_2_Test() throws IllegalAccessException{
      //Set up controllers and hand
      player.setStatus(PlayerStatus.ICE_BROKEN);
      Hand hand = player.hand();
      hand.addTiles(O5, O6, O7);

      // Set up the table
      Table table = new Table();
      Meld setOf4 =  Meld.createMeld(O8, R8, G8, B8 );
      table.addMeld(setOf4);

      //Create action handler
      ActionHandler handler = new ActionHandler(player, table);
      ManipulationTable tempTable = handler.getManipulationTable();

      //Test table before turn
      assertThat(tempTable.getMelds().isEmpty(), is (true));

//      /* Start the turn  */
//      //Adding O4, O5, O6, R8, G8, B8 to tempt table
//      handler.takeTableMeld(0);
//      //Forming tiles to play from hand {O5, O6, O7}
//      hand.formMeld(0);
//      //Adding {O5, O6, O7} to manipulation table
//      handler.playFromHand(0);
//      //taking O8 out of set
//      tempTable.detach(0,0);
//      //Combine with {O5, O6, O7}
//      tempTable.combineMelds(0, 1);
//      //End Turn
//
//      //Checking the turn
//      assertThat(table.getPlayingMelds().size(),is(2) );
//      assertThat(table.getPlayingMelds().get(0).tiles(),contains(O5, O6, O7, O8));
//      assertThat(table.getPlayingMelds().get(1).tiles(),contains(R8, B8, G8));
//      assertThat(hand.getMelds().size(), is( 0));
//      assertThat(hand.getTiles().size(), is(0));
  }

    @Test
    //Add and split
    public void scenario_3_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(O5, B8, R8);

        // Set up the table
        Table table = new Table();
        Meld setOf3 =  Meld.createMeld(O6, O7, O8);
        table.addMeld(setOf3);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is (true));

        /* Start the turn  */
        //Adding O6, O7, O8 to tempt table
        handler.takeTableMeld(0);
        //Forming tiles to play from hand {O5} and {B8, R8}
        hand.formMeld(0);
        hand.formMeld(0, 1);
        //Adding {O5} and to {B8, R8} manipulation table
        handler.playFromHand(0);
        handler.playFromHand(0);

        tempTable.detach(0, 2);
        tempTable.combineMelds(0,2);
        tempTable.combineMelds(1,3);

        //Checking the turn
        assertThat(table.getPlayingMelds().size(),is(2) );
        assertThat(table.getPlayingMelds().get(0).tiles(),contains(O5, O6, O7));
        assertThat(table.getPlayingMelds().get(1).tiles(),contains(R8, B8, G8, O8));
        assertThat(hand.getMelds().size(), is( 0));
        assertThat(hand.getTiles().size(), is(0));
  }

    @Test
    //Add and split
    public void scenario_4_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(R8);

        // Set up the table
        Table table = new Table();
        Meld setOf7 = Meld.createMeld(O5,O6, O7, O8, B8, R8, G8, O8);
        table.addMeld(setOf7);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is(true));

        /* Start the turn  */
        //Adding O5,O6, O7, O8, B8, R8, G8, O8 to tempt table
        handler.takeTableMeld(0);
        //Forming tiles to play from hand {R8}
        hand.formMeld(0);
        //Adding {R8} manipulation table
        handler.playFromHand(0);

        tempTable.detach(0, 3);
        tempTable.detach(0,0);

        tempTable.combineMelds(0,2,4);

        //Checking the turn
        assertThat(table.getPlayingMelds().size(),is(3) );
        assertThat(table.getPlayingMelds().get(0).tiles(),contains(R8, O8, B8));
        assertThat(table.getPlayingMelds().get(1).tiles(),contains(O5, O6, O7));
        assertThat(table.getPlayingMelds().get(2).tiles(),contains(R8, G8, O8));
        assertThat(hand.getMelds().size(), is( 0));
        assertThat(hand.getTiles().size(), is(0));
    }
}

