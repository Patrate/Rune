package fr.emmuliette.rune.mod.gui.scripting;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fr.emmuliette.rune.mod.items.ModItems;
import net.minecraft.item.Item;
import net.minecraft.util.Direction8;

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

	private static Map<Item, Rune> runeMap;

	public static class Rune {
		Path2D.Double path;

		public Rune(Path2D.Double path) {
			this.path = path;
		}

		public Double[][] toMatrix(int w, int h) {
			Double[][] matrix = new Double[w][h];
			double stepX = 1. / w, stepY = 1. / h;
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					boolean inters = path.intersects(stepX * i, stepY * j, stepX, stepY);
					/*
					 * System.out.println("Path intersect " + stepX * i + "/" + stepY * j + ", " +
					 * stepX + "/" + stepY + ": " + inters);
					 */
					if (inters)
						matrix[i][j] = 1.;
					else
						matrix[i][j] = 0.;
				}
			}
			return matrix;
		}
	}

	public static Set<Item> getRunes() {
		return runeMap.keySet();
	}

	public static void register() {
		runeMap = new HashMap<Item, Rune>();
		Random runeRandom = new Random(69); // TODO change with world seed
		for (ModItems c : runeItems) {
			runeMap.put(c.get(), new Rune(makePathV3(0, runeRandom)));
		}
	}

	public static Double[][] getRune(int size, Item rune) {
		if (!runeMap.containsKey(rune)) {
			System.out.println("ERREUR NOT A RUNE " + rune);
			return null;
		}
		return runeMap.get(rune).toMatrix(size, size);
	}

	public static class PathElement {
		Set<Direction8> outs;

		public PathElement() {
			this.outs = new HashSet<Direction8>();
		}

		public void addOut(Direction8 out) {
			outs.add(out);
		}

		public boolean isEmpty() {
			return outs.isEmpty();
		}
	}

	/*
	public static Path2D.Double makePathGrid(int complexity, Random rand) {
		int size = 3 + complexity;
		PathElement[][] grid = new PathElement[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				PathElement newElement = new PathElement();
				boolean horizontal = false, vertical = false;

				int k = i - 1;
				if (k > 0) {
					if (grid[k][j] != null) {
						if (k == i - 1 && grid[k][j].outs.contains(Direction8.SOUTH)) {
							newElement.addOut(Direction8.NORTH);
							vertical = true;
						}
					}
				}

				k = j - 1;
				if (k > 0) {
					if (grid[k][j] != null) {
						if (k == i - 1 && grid[k][j].outs.contains(Direction8.EAST)) {
							newElement.addOut(Direction8.WEST);
							horizontal = true;
						}
					}
				}

				double rVal;
				if (i + 1 < size) {
					rVal = rand.nextDouble();
					if (horizontal)
						rVal += 0.5;

					if (rVal > 0.7)
						newElement.addOut(Direction8.EAST);
				}
				if (j + 1 < size) {
					rVal = rand.nextDouble();
					if (vertical)
						rVal += 0.5;

					if (rVal > 0.7)
						newElement.addOut(Direction8.SOUTH);
				}

				if (newElement.isEmpty()) {
					newElement.addOut(rand.nextBoolean() ? Direction8.EAST : Direction8.SOUTH);
				}

				grid[i][j] = newElement;
			}
		}

		Path2D.Double path = new Path2D.Double();

		double cellSize = 1. / size;
		double step = cellSize / 2;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				double leftX = cellSize * i, topY = cellSize * j;
				double x = leftX + step, y = topY + step;

				for (Direction8 dir : grid[i][j].outs) {
					Line2D.Double shape;
					if (dir.equals(Direction8.WEST)) {
						shape = new Line2D.Double(x, y, x - step, y);
					} else if (dir.equals(Direction8.EAST)) {
						shape = new Line2D.Double(x, y, x + step, y);
					} else if (dir.equals(Direction8.NORTH)) {
						shape = new Line2D.Double(x, y, x, y - step);
					} else if (dir.equals(Direction8.SOUTH)) {
						shape = new Line2D.Double(x, y, x, y + step);
					} else {
						System.out.println("ERREUR DIR INCONNUE" + dir);
						continue;
					}

					path.append(shape, false);
				}
			}
		}

		return path;
	}*/

	public static Path2D.Double makePath2D(int p, Random rand) {
		Path2D.Double path = new Path2D.Double();

		Point2D.Double current = new Point2D.Double(rand.nextDouble(), rand.nextDouble());
//		double xMin = Double.MAX_VALUE, xMax = 0, yMin = Double.MAX_VALUE, yMax = 0;

		List<Shape> shapes = new ArrayList<Shape>();

		for (int i = 0; i < p; i++) {
			Point2D.Double next = new Point2D.Double(rand.nextDouble(), rand.nextDouble());
			Shape shape;

			double randShape = rand.nextDouble();

			if (randShape > 2) {
				System.out.println("ALERTE");
				double extent = rand.nextDouble() * 180;
//				double x = current.x + (next.x - current.x) / 2;
//				double y = current.y + (next.y - current.y) / 2;

				double rectX = Math.min(next.x, current.x);
				double rectY = Math.min(next.y, current.y);
				double w = Math.abs(next.x - current.x), h = Math.abs(next.y - current.y);
				Rectangle2D.Double bound = new Rectangle2D.Double(rectX, rectY, w, h);

				Arc2D.Double arc = new Arc2D.Double(bound, 0, extent, Arc2D.CHORD);
				Arc2D.Double reverse = new Arc2D.Double(bound, 0, extent, Arc2D.CHORD);

				shape = new Path2D.Double();
				((Path2D.Double) shape).append(arc, false);
				((Path2D.Double) shape).append(reverse, false);

			} else if (randShape > 3.3) {
				double centreX = (current.x + next.x) / 2, centreY = (current.y + next.y) / 2,
						rayon = Math.abs(current.x - centreX) + Math.abs(current.y - centreY);

				double ctrlX1, ctrlY1, dist1;
				double ctrlX2, ctrlY2, dist2;
				do {
					ctrlX1 = rand.nextDouble();
					ctrlY1 = rand.nextDouble();
					dist1 = Math.abs(ctrlX1 - centreX) + Math.abs(ctrlY1 - centreY);
				} while (dist1 > rayon);
				do {
					ctrlX2 = rand.nextDouble();
					ctrlY2 = rand.nextDouble();
					dist2 = Math.abs(ctrlX2 - centreX) + Math.abs(ctrlY2 - centreY);
				} while (dist2 > rayon);

				CubicCurve2D.Double subShape = new CubicCurve2D.Double(current.x, current.y, ctrlX1, ctrlY1, ctrlX2,
						ctrlY2, next.x, next.y);
				CubicCurve2D.Double reverse = new CubicCurve2D.Double(current.x, current.y, ctrlX1, ctrlY1, ctrlX2,
						ctrlY2, next.x, next.y);

				shape = new Path2D.Double();
				((Path2D.Double) shape).append(subShape, false);
				((Path2D.Double) shape).append(reverse, false);

			} else if (randShape > 2.2) {
				double centreX = (current.x + next.x) / 2, centreY = (current.y + next.y) / 2,
						rayon = Math.abs(current.x - centreX) + Math.abs(current.y - centreY);

				double ctrlX1, ctrlY1, dist1;
				do {
					ctrlX1 = rand.nextDouble();
					ctrlY1 = rand.nextDouble();
					dist1 = Math.abs(ctrlX1 - centreX) + Math.abs(ctrlY1 - centreY);
				} while (dist1 > rayon);

//				Polygon pp = new Polygon();
//				pp.

				QuadCurve2D.Double subShape = new QuadCurve2D.Double(current.x, current.y, ctrlX1, ctrlY1, next.x,
						next.y);
				QuadCurve2D.Double reverse = new QuadCurve2D.Double(next.x, next.y, ctrlX1, ctrlY1, current.x,
						current.y);

				shape = new Path2D.Double();
				((Path2D.Double) shape).append(subShape, false);
				((Path2D.Double) shape).append(reverse, false);
			} else {
				shape = new Line2D.Double(current, next);
			}

//			java.awt.geom.Ellipse2D;
//			java.awt.geom.Arc2D;
//			java.awt.geom.CubicCurve2D;
//			java.awt.geom.Rectangle2D;
//			java.awt.geom.RoundRectangle2D;
//			java.awt.geom.RectangularShape;

			shapes.add(shape);
			current = next;
			path.append(shape, false);
		}

		return path;
	}
	
	public static Path2D.Double makePathV3(int p, Random rand) {
		Path2D.Double path = new Path2D.Double();
		boolean valid = false;
		
		
		while(!valid) {
			
		}

		Point2D.Double current = new Point2D.Double(rand.nextDouble(), rand.nextDouble());
//		double xMin = Double.MAX_VALUE, xMax = 0, yMin = Double.MAX_VALUE, yMax = 0;

		List<Shape> shapes = new ArrayList<Shape>();

		for (int i = 0; i < p; i++) {
			Point2D.Double next = new Point2D.Double(rand.nextDouble(), rand.nextDouble());
			Shape shape;

			double randShape = rand.nextDouble();

			if (randShape > 2) {
				System.out.println("ALERTE");
				double extent = rand.nextDouble() * 180;
//				double x = current.x + (next.x - current.x) / 2;
//				double y = current.y + (next.y - current.y) / 2;

				double rectX = Math.min(next.x, current.x);
				double rectY = Math.min(next.y, current.y);
				double w = Math.abs(next.x - current.x), h = Math.abs(next.y - current.y);
				Rectangle2D.Double bound = new Rectangle2D.Double(rectX, rectY, w, h);

				Arc2D.Double arc = new Arc2D.Double(bound, 0, extent, Arc2D.CHORD);
				Arc2D.Double reverse = new Arc2D.Double(bound, 0, extent, Arc2D.CHORD);

				shape = new Path2D.Double();
				((Path2D.Double) shape).append(arc, false);
				((Path2D.Double) shape).append(reverse, false);

			} else if (randShape > 3.3) {
				double centreX = (current.x + next.x) / 2, centreY = (current.y + next.y) / 2,
						rayon = Math.abs(current.x - centreX) + Math.abs(current.y - centreY);

				double ctrlX1, ctrlY1, dist1;
				double ctrlX2, ctrlY2, dist2;
				do {
					ctrlX1 = rand.nextDouble();
					ctrlY1 = rand.nextDouble();
					dist1 = Math.abs(ctrlX1 - centreX) + Math.abs(ctrlY1 - centreY);
				} while (dist1 > rayon);
				do {
					ctrlX2 = rand.nextDouble();
					ctrlY2 = rand.nextDouble();
					dist2 = Math.abs(ctrlX2 - centreX) + Math.abs(ctrlY2 - centreY);
				} while (dist2 > rayon);

				CubicCurve2D.Double subShape = new CubicCurve2D.Double(current.x, current.y, ctrlX1, ctrlY1, ctrlX2,
						ctrlY2, next.x, next.y);
				CubicCurve2D.Double reverse = new CubicCurve2D.Double(current.x, current.y, ctrlX1, ctrlY1, ctrlX2,
						ctrlY2, next.x, next.y);

				shape = new Path2D.Double();
				((Path2D.Double) shape).append(subShape, false);
				((Path2D.Double) shape).append(reverse, false);

			} else if (randShape > 2.2) {
				double centreX = (current.x + next.x) / 2, centreY = (current.y + next.y) / 2,
						rayon = Math.abs(current.x - centreX) + Math.abs(current.y - centreY);

				double ctrlX1, ctrlY1, dist1;
				do {
					ctrlX1 = rand.nextDouble();
					ctrlY1 = rand.nextDouble();
					dist1 = Math.abs(ctrlX1 - centreX) + Math.abs(ctrlY1 - centreY);
				} while (dist1 > rayon);

//				Polygon pp = new Polygon();
//				pp.

				QuadCurve2D.Double subShape = new QuadCurve2D.Double(current.x, current.y, ctrlX1, ctrlY1, next.x,
						next.y);
				QuadCurve2D.Double reverse = new QuadCurve2D.Double(next.x, next.y, ctrlX1, ctrlY1, current.x,
						current.y);

				shape = new Path2D.Double();
				((Path2D.Double) shape).append(subShape, false);
				((Path2D.Double) shape).append(reverse, false);
			} else {
				shape = new Line2D.Double(current, next);
			}

//			java.awt.geom.Ellipse2D;
//			java.awt.geom.Arc2D;
//			java.awt.geom.CubicCurve2D;
//			java.awt.geom.Rectangle2D;
//			java.awt.geom.RoundRectangle2D;
//			java.awt.geom.RectangularShape;

			shapes.add(shape);
			current = next;
			path.append(shape, false);
		}

		return path;
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
			Double[][] runeMatrix = runeMap.get(c).toMatrix(matrix.length, matrix[0].length);
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
