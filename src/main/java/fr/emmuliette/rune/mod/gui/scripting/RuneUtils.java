package fr.emmuliette.rune.mod.gui.scripting;

import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.item.Item;

public class RuneUtils {

	public static final ModItems[] runeItems = { ModItems.PROJECTILE_RUNE, ModItems.TOUCH_RUNE, ModItems.SELF_RUNE,
			ModItems.LOAD_RUNE, ModItems.CHARGE_RUNE, ModItems.CHANNEL_RUNE, ModItems.TOGGLE_RUNE,
			ModItems.MAGIC_ENTITY_RUNE, ModItems.FIRE_RUNE, ModItems.DAMAGE_RUNE, ModItems.TELEPORT_RUNE,
			ModItems.SILENCE_RUNE, ModItems.MOVE_BLOCK_RUNE, ModItems.ILLUSION_BLOCK_RUNE, ModItems.PHASE_BLOCK_RUNE,
			ModItems.ANCHOR_BLOCK_RUNE, ModItems.BLINDNESS_RUNE, ModItems.NAUSEA_RUNE, ModItems.DAMAGEBOOST_RUNE,
			ModItems.DAMAGERESISTANCE_RUNE, ModItems.DIGSLOW_RUNE, ModItems.DIGSPEED_RUNE, ModItems.FIRERESISTANCE_RUNE,
			ModItems.GLOW_RUNE, ModItems.INVISIBILITY_RUNE, ModItems.JUMP_RUNE, ModItems.LEVITATION_RUNE,
			ModItems.MOVESLOW_RUNE, ModItems.MOVESPEED_RUNE, ModItems.NIGHTVISION_RUNE, ModItems.POISON_RUNE,
			ModItems.REGENERATION_RUNE, ModItems.SLOWFALL_RUNE, ModItems.WATERBREATHING_RUNE, ModItems.WEAKNESS_RUNE,
			ModItems.WITHER_RUNE };

	private static Map<Item, Double[][]> runeMap;

	public static Set<Item> getRunes() {
		return runeMap.keySet();
	}

	public static void register() {
		runeMap = new HashMap<Item, Double[][]>();
		Random runeRandom = new Random(69); // TODO change with world seed
		for (ModItems c : runeItems) {
			runeMap.put(c.get(), makePath(8, runeRandom));
		}
	}

	public static Double[][] getRune(int size, Item rune) {
		if (!runeMap.containsKey(rune)) {
			System.out.println("ERREUR NOT A RUNE " + rune);
			return null;
		}
		return pathToMatrix(size, size, runeMap.get(rune));
	}

	public static Double[][] pathToMatrix(int w, int h, Double[][] coord) {
		int p = coord.length;
		Path2D.Double path = new Path2D.Double();
		path.moveTo(coord[0][0] * w, coord[0][1] * h);
		for (int i = 1; i < p; i++) {
			path.lineTo(coord[i][0] * w, coord[i][1] * h);
		}

		for (int i = p - 1; i >= 0; i--) {
			path.lineTo(coord[i][0] * w, coord[i][1] * h);
		}

		Double[][] matrix = new Double[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				/*
				 * if (path.intersects(i, j, 1, 1)) matrix[i][j] = 1; else
				 */if (path.intersects(i, j, 1, 1))
					matrix[i][j] = 1.;
				/*
				 * else if (path.intersects(i - 2, j - 2, 5, 5)) matrix[i][j] = 0; else if
				 * (path.intersects(i - 3, j - 3, 7, 7)) matrix[i][j] = -0.5;
				 */
				else
					matrix[i][j] = 0.;
			}
		}

		return matrix;
	}

	public static Double[][] makePath(int p, Random rand) {
		Double[][] coord = new Double[p][2];

		double xMin = Double.MAX_VALUE, xMax = 0, yMin = Double.MAX_VALUE, yMax = 0;

		for (int i = 0; i < p; i++) {
			coord[i][0] = (rand.nextDouble());
			coord[i][1] = (rand.nextDouble());
			if (coord[i][0] < xMin)
				xMin = coord[i][0];
			if (coord[i][0] > xMax)
				xMax = coord[i][0];
			if (coord[i][1] < yMin)
				yMin = coord[i][1];
			if (coord[i][1] > yMax)
				yMax = coord[i][1];
		}
		double xMod = 1 / (xMax - xMin), yMod = 1 / (yMax - yMin);

		for (int i = 0; i < p; i++) {
			coord[i][0] = (coord[i][0] - xMin) * xMod;
			coord[i][1] = (coord[i][1] - yMin) * yMod;
		}

		return coord;
	}

	public static int[][] scaleUp(int[][] matrix, int row, int col) {
		int[][] newMatrix = new int[row][col];
		int oldRow = matrix.length;
		int oldCol = matrix[0].length;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				newMatrix[i][j] = matrix[(i * oldRow) / row][(j * oldCol) / col];
			}
		}

		return newMatrix;
	}

	public static double matrixDistance(Number[][] m1, Number[][] m2) {
		if (m1.length != m2.length) {
			return -Double.MAX_VALUE; // TODO throw error
		}
		int row = m1.length, col = m1[0].length;

		int max = 0, ok = 0, err = 0;

		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {
				double m1val = m1[x][y].doubleValue();
				double m2val = m2[x][y].doubleValue();
				if (m1val == 1) {
					if (m2val == 1) {
						ok++;
					} else if (m2val == 0) {
						err++;
					}
				}
				if (m2val == 1)
					max += 1;

				// System.out.println("score: " + oldscore + "->" + score + " (+ " +
				// m1[x][y].doubleValue() + " * " + m2[x][y].doubleValue() + ". Max: " + max);
			}
		}

		double score = ((double) ok / (double) max) * (1 - ((double) err * 0.01));
		// double perc = (double) ok / (double) max;
		// System.out.println("score = " + score + ", max = " + max + ", perc = " + perc
		// + " (" + ok + "/" + err + ")");
		return score;
	}

	public static Item testMatrix(Integer[][] matrix) {
		// printMatrix(matrix);
		Item maxRune = null;
		double max = 0.;
		for (Item c : runeMap.keySet()) {
			Double[][] runeMatrix = pathToMatrix(matrix.length, matrix[0].length, runeMap.get(c));
			double score = matrixDistance(matrix, runeMatrix);
			// System.out.println(c.get().getRegistryName() + ": " + score);
			if (score > max) {
				max = score;
				maxRune = c;
			}
		}
		return maxRune;
	}

	public static void printMatrix(Number[][] matrix) {
		int w = matrix.length, h = matrix[0].length;

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				System.out.print((matrix[i][j].intValue()) + " ");
			}
			System.out.print("\n");
		}
	}
}
