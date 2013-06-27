import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.fest.assertions.api.Assertions;
import org.junit.Test;


public class MinesTest {

	
	public class Mines {

		private static final char MINE = '*';
		private static final char NO_MINE = '.';
		private char[] cells;

		public Mines(String baseChart) {
			this.cells = convertFrom(baseChart);
		}

		private char[] convertFrom(String baseChart) {
			return baseChart.toCharArray();
		}

		public String getChart() {
			char[] nextCells = new char[cells.length];
			for (int column = 0; column < cells.length; column++) {
				nextCells[column] = getResultCell(column);
			}
			return new String(nextCells);
		}

		private char getResultCell(int i) {
			if (cells[i] == NO_MINE)
				return getNumberOfMines(i);
			else
				return MINE;
		}

		private char getNumberOfMines(int i) {
			boolean[] howManyMines = new boolean[] {previousCellIsMine(i), nextCellIsMine(i)};
			int howMany = countTrues(howManyMines);
			return (""+howMany).charAt(0);
		}

		private int countTrues(boolean[] howManyMines) {
			int howMany = 0;
			for (boolean b : howManyMines) {
				if(b) howMany++;
			}
			return howMany;
		}

		private boolean nextCellIsMine(int i) {
			return i+1 < cells.length && cells[i+1] == MINE;
		}

		private boolean previousCellIsMine(int i) {
			return i > 0 && cells[i-1] == MINE;
		}

	}

	@Test
	public void shouldBe0WHenNoMines_1x1() throws Exception {
		Assertions.assertThat(new Mines(".").getChart()).isEqualTo("0");
	}
	
	@Test
	public void shouldBe0WHenNoMines_1x2() throws Exception {
		Assertions.assertThat(new Mines("..").getChart()).isEqualTo("00");
	}
	
	@Test
	public void shouldBeStarForMines() throws Exception {
		Assertions.assertThat(new Mines("*").getChart()).isEqualTo("*");
	}
	
	@Test
	public void shouldBeOneAfterMine() throws Exception {
		Assertions.assertThat(new Mines("*.").getChart()).isEqualTo("*1");
	}
	
	@Test
	public void shouldBeOneBeforeMine() throws Exception {
		Assertions.assertThat(new Mines(".*").getChart()).isEqualTo("1*");
	}
	
	@Test
	public void shouldBeTwoBetweenMines() throws Exception {
		Assertions.assertThat(new Mines("*.*").getChart()).isEqualTo("*2*");
	}
}
