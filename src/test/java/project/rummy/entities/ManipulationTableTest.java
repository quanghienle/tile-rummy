package project.rummy.entities;

import org.junit.Test;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;


public class ManipulationTableTest {

  private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
  private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
  private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
  private static final Tile O9 = Tile.createTile(Color.ORANGE, 9);
  private static final Tile R3 = Tile.createTile(Color.RED, 3);
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);
  private static final Tile O3 = Tile.createTile(Color.ORANGE, 3);

  @Test(expected = IllegalArgumentException.class)
  public void split_invalidBreakpoint_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void split_duplicateBreakpoint_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(0, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void split_invalidMeldIndex_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(2, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void split_invalidMeldSize0_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.split(0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void split_invalidMeldSize1_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5));
    table.split(0, 1);
  }

  @Test
  public void split_Run_WithSingleBreakPoint() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(0, 3);

    assertEquals(3, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(R3, G3, B3, O3));
    assertThat(table.getMelds().get(1).tiles(), contains(O5, O6, O7));
    assertThat(table.getMelds().get(2).tiles(), contains(O8, O9));
  }

  @Test
  public void split_Set_WithSingleBreakPoint() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(1, 3);

    assertEquals(3, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5, O6, O7, O8, O9));
    assertThat(table.getMelds().get(1).tiles(), contains(R3, G3, B3));
    assertThat(table.getMelds().get(2).tiles(), contains(O3));
  }

  @Test
  public void split_Run_With2BreakPoints() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(0, 2, 4);

    assertEquals(4, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(R3, G3, B3, O3));
    assertThat(table.getMelds().get(1).tiles(), contains(O5, O6));
    assertThat(table.getMelds().get(2).tiles(), contains(O7, O8));
    assertThat(table.getMelds().get(3).tiles(), contains(O9));
  }

  @Test
  public void split_Set_With2BreakPoints() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(1, 1, 2);

    assertEquals(4, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5, O6, O7, O8, O9));
    assertThat(table.getMelds().get(1).tiles(), contains(R3));
    assertThat(table.getMelds().get(2).tiles(), contains(G3));
    assertThat(table.getMelds().get(3).tiles(), contains(B3, O3));
  }

  @Test
  public void split_RunAndSet() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.split(0, 4, 1);
    table.split(0, 2);

    assertEquals(5, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5));
    assertThat(table.getMelds().get(1).tiles(), contains(O6, O7, O8));
    assertThat(table.getMelds().get(2).tiles(), contains(O9));
    assertThat(table.getMelds().get(3).tiles(), contains(R3, G3));
    assertThat(table.getMelds().get(4).tiles(), contains(B3, O3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void detach_invalidMeldIndex_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(R3, G3, B3, O3));
    table.detach(-1, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void detach_duplicateTileIndexes_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.detach(1, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void detach_invalidTileIndex_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.detach(1, 2, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void detach_tryToDetachRun_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.detach(0, 2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void detach_canNotDetachSingleMeld_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5), Meld.createMeld(R3, G3, B3, O3));
    table.detach(0, 0);
  }

  @Test
  public void detach_shouldSucceed() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.detach(1, 3);

    assertEquals(3, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5, O6, O7, O8, O9));
    assertThat(table.getMelds().get(1).tiles(), contains(O3));
    assertThat(table.getMelds().get(2).tiles(), contains(R3, G3, B3));
  }

  @Test
  public void detach_case2_shouldSucceed() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.detach(1, 0, 1, 3);

    assertEquals(3, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5, O6, O7, O8, O9));
    assertThat(table.getMelds().get(1).tiles(), contains(R3, G3, O3));
    assertThat(table.getMelds().get(2).tiles(), contains(B3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void combine_invalidIndexes_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6), Meld.createMeld(O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.combineMelds(0, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void combine_duplicateIndexes_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6), Meld.createMeld(O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.combineMelds(1, 1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void combine_invalidMeld_shouldThrow() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O6), Meld.createMeld(O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.combineMelds(0, 1);
  }


  @Test
  public void combine_TwoRuns() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6), Meld.createMeld(O7, O8, O9), Meld.createMeld(R3, G3, B3, O3));
    table.combineMelds(0, 1);

    assertEquals(2, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(R3, G3, B3, O3));
    assertThat(table.getMelds().get(1).tiles(), contains(O5, O6, O7, O8, O9));
  }

  @Test
  public void combine_TwoSets() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5, O6, O7, O8, O9), Meld.createMeld(B3, O3), Meld.createMeld(R3, G3));
    table.combineMelds(2, 1);

    assertEquals(2, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5, O6, O7, O8, O9));
    assertThat(table.getMelds().get(1).tiles(), contains(B3, O3, R3, G3));
  }

  @Test
  public void combine_SetAndRun() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5), Meld.createMeld(B3, O3), Meld.createMeld(R3, G3), Meld.createMeld(O6, O7, O8, O9));
    table.combineMelds(3, 0);
    table.combineMelds(0, 1);

    assertEquals(2, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(O5, O6, O7, O8, O9));
    assertThat(table.getMelds().get(1).tiles(), contains(B3, O3, R3, G3));
  }

  @Test
  public void combine_runWith2Tiles() {
    ManipulationTable table = new ManipulationTable();

    table.add(Meld.createMeld(O5), Meld.createMeld(B3, O3), Meld.createMeld(R3, G3), Meld.createMeld(O6));
    table.combineMelds(3, 0);

    assertEquals(3, table.getMelds().size());

    assertThat(table.getMelds().get(0).tiles(), contains(B3, O3));
    assertThat(table.getMelds().get(1).tiles(), contains(R3, G3));
    assertThat(table.getMelds().get(2).tiles(), contains(O5, O6));
  }

  @Test
  public void submit_withInvalidRun() {
    ManipulationTable t = new ManipulationTable();
    Table realTable = new Table();

    t.add(Meld.createMeld(B3, O3, G3), Meld.createMeld(O6, O7));

    assertFalse(t.submit(realTable));
    assertEquals(0, realTable.getPlayingMelds().size());
  }

  @Test
  public void submit_withInvalidSet() {
    ManipulationTable t = new ManipulationTable();
    Table realTable = new Table();

    t.add(Meld.createMeld(B3, O3), Meld.createMeld(R3, G3), Meld.createMeld(O6, O7, O8));

    assertFalse(t.submit(realTable));
    assertEquals(0, realTable.getPlayingMelds().size());
  }

  @Test
  public void submit_withValidMelds() {
    ManipulationTable t = new ManipulationTable();
    Table realTable = new Table();

    t.add(Meld.createMeld(B3, R3, G3, O3), Meld.createMeld(O6, O7, O8, O9));
    assertTrue(t.submit(realTable));
    assertEquals(2, realTable.getPlayingMelds().size());
  }
}
