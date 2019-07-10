package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import bullet.Bullet;
import effect.Effect;
import item.ItemData;
import physics.Dynam;
import physics.HasBoundingBox;
import physics.HasDynam;
import physics.HasPoint;
import physics.HasStandpoint;
import physics.HitInteractable;
import physics.Point;
import physics.Standpoint;
import structure.Structure;
import unit.Unit;
import vegetation.DropItem;
import vegetation.Vegetation;

public class GHQStage implements HasBoundingBox, Serializable{
	private static final long serialVersionUID = 6086633760555467724L;
	
	public final int WIDTH, HEIGHT;
	public final GHQObjectList<Unit> units = new GHQObjectList<Unit>();
	public final GHQObjectList<Bullet> bullets = new GHQObjectList<Bullet>();
	public final GHQObjectList<Effect> effects = new GHQObjectList<Effect>();
	public final GHQObjectList<Structure> structures = new GHQObjectList<Structure>();
	public final GHQObjectList<Vegetation> vegetations = new GHQObjectList<Vegetation>();
	
	public GHQStage(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
	}
	
	//main role
	public void idle() {
		units.defaultTraverse();
		structures.defaultTraverse();
		vegetations.defaultTraverse();
		bullets.defaultTraverse();
		effects.defaultTraverse();
	}
	public void specifiedGroupIdle(GHQObjectType type) {
		switch(type) {
		case UNIT: units.defaultTraverse(); break;
		case BULLET: bullets.defaultTraverse(); break;
		case EFFECT: effects.defaultTraverse(); break;
		case STRUCTURE: structures.defaultTraverse(); break;
		case VEGETATION: vegetations.defaultTraverse(); break;
		}
	}
	public void fill(Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(color);
		G2.fillRect(0, 0, WIDTH, HEIGHT);
	}
	
	//update

	/**
	 * Add an {@link Bullet} to current stage.
	 * Parameters of the bullet refers to {@link BulletInfo} settings.
	 * @param bullet
	 * @return created Bullet
	 * @since alpha1.0
	 */
	public final <T extends Bullet>T addBullet(T bullet){
		if(!bullets.contains(bullet))
			bullets.add(bullet);
		return bullet;
	}
	/**
	 * Add an {@link Effect} to current stage.
	 * Parameters of the effect refers to {@link EffectInfo} settings.
	 * @param effect
	 * @return created Effect
	 * @since alpha1.0
	 */
	public final <T extends Effect>T addEffect(T effect){
		if(!effects.contains(effect))
			effects.add(effect);
		return effect;
	}
	/**
	 * Add an {@link Unit} to current stage.
	 * @param unit
	 * @return added unit
	 */
	public final <T extends Unit>T addUnit(T unit) {
		if(!units.contains(unit))
			units.add(unit);
		return unit;
	}
	/**
	 * Add a {@link Structure} to current stage.
	 * @param structure
	 * @return added structure
	 */
	public final <T extends Structure>T addStructure(T structure) {
		if(!structures.contains(structure))
			structures.add(structure);
		return structure;
	}
	/**
	 * Add a {@link Vegetation} to current stage.
	 * @param vegetation
	 * @return added vegetation
	 */
	public final <T extends Vegetation>T addVegetation(T vegetation){
		if(!vegetations.contains(vegetation))
			vegetations.add(vegetation);
		return vegetation;
	}
	
	//tool
	/////////////////
	//Unit
	/////////////////
	public final ArrayList<Unit> getUnits_standpoint(Standpoint standPoint, boolean white) {
		final ArrayList<Unit> unitArray = new ArrayList<Unit>();
		for(Unit unit : units) {
			if(white == standPoint.isFriend(unit.standpoint()))
				unitArray.add(unit);
		}
		return unitArray;
	}
	public final ArrayList<Unit> getUnits_standpoint(HasStandpoint source, boolean white) {
		return getUnits_standpoint(source.standpoint(), white);
	}
	public final ArrayList<Unit> getUnits_standPoint(HasStandpoint source) {
		return getUnits_standpoint(source, true);
	}
	public final Unit getNearstEnemy(Standpoint standpoint, int x, int y) {
		double nearstDistanceSq = GHQ.MAX;
		Unit nearstUnit = null;
		for(Unit enemy : getUnits_standpoint(standpoint, false)) {
			if(!enemy.isAlive() || enemy.isFriend(standpoint))
				continue;
			final double DISTANCE_SQ = enemy.dynam().distanceSq(x, y);
			if(nearstUnit == null || DISTANCE_SQ < nearstDistanceSq) {
				nearstDistanceSq = DISTANCE_SQ;
				nearstUnit = enemy;
			}
		}
		return nearstUnit;
	}
	public final Unit getNearstEnemy(Standpoint source, Point point) {
		return getNearstEnemy(source, point.intX(), point.intY());
	}
	public final Unit getNearstEnemy(Unit unit) {
		return getNearstEnemy(unit.standpoint(), unit.point());
	}
	public final ArrayList<Unit> getVisibleEnemies(Unit unit) {
		final ArrayList<Unit> visibleEnemies = new ArrayList<Unit>();
		for(Unit enemy : getUnits_standpoint(unit, false)) {
			if(!enemy.isAlive())
				continue;
			if(enemy.dynam().isVisible(unit))
				visibleEnemies.add(enemy);
		}
		return visibleEnemies;
	}
	public final Unit getNearstVisibleEnemy(Unit unit) {
		final Dynam DYNAM = unit.dynam();
		Unit nearstVisibleEnemy = null;
		double nearstDistanceSq = GHQ.MAX;
		for(Unit enemy : getUnits_standpoint(unit, false)) {
			if(!enemy.isAlive() || !DYNAM.isVisible(enemy))
				continue;
			if(nearstDistanceSq == GHQ.MAX) {
				nearstVisibleEnemy = enemy;
				nearstDistanceSq = DYNAM.distanceSq(enemy);
			}else {
				final double DISTANCE = DYNAM.distanceSq(enemy);
				if(nearstDistanceSq > DISTANCE) {
					nearstVisibleEnemy = enemy;
					nearstDistanceSq = DISTANCE;
				}
			}
		}
		return nearstVisibleEnemy;
	}
	public final Unit getNearstVisibleEnemy(Bullet bullet) {
		final Dynam DYNAM = bullet.dynam();
		Unit nearstVisibleEnemy = null;
		double nearstDistanceSq = GHQ.MAX;
		for(Unit enemy : getUnits_standpoint(bullet, false)) {
			if(!enemy.isAlive() || !DYNAM.isVisible(enemy))
				continue;
			if(nearstDistanceSq == GHQ.MAX) {
				nearstVisibleEnemy = enemy;
				nearstDistanceSq = DYNAM.distanceSq(enemy);
			}else {
				final double DISTANCE = DYNAM.distanceSq(enemy);
				if(nearstDistanceSq > DISTANCE) {
					nearstVisibleEnemy = enemy;
					nearstDistanceSq = DISTANCE;
				}
			}
		}
		return nearstVisibleEnemy;
	}
	/**
	 * A class helps {@link Bullet} or other objects to detect touching enemies.
	 * @param bullet
	 * @return
	 */
	public final ArrayList<Unit> getHitUnits(ArrayList<Unit> units,HitInteractable object) {
		final ArrayList<Unit> result = new ArrayList<Unit>();
		for(Unit unit : units) {
			if(unit.intersects(object))
				result.add(unit);
		}
		return result;
	}
	/**
	 * A class helps {@link Bullet} or other objects to detect touching enemies.
	 * @param bullet
	 * @return
	 */
	public final ArrayList<Unit> getHitUnits(HitInteractable object) {
		return getHitUnits(getUnits_standpoint(object, false), object);
	}
	public void clear() {
		units.clear();
		bullets.clear();
		effects.clear();
		structures.clear();
		vegetations.clear();
	}
	
	//tool
	//vegetation
	public final DropItem getCoveredDropItem(HasDynam di, int distance) {
		for(Vegetation vegetation : vegetations) {
			if(vegetation instanceof DropItem && ((DropItem)vegetation).isCovered(di, distance)) {
				return (DropItem)vegetation;
			}
		}
		return null;
	}
	public final ItemData getCoveredDropItem_pickup(HasDynam di, int distance) {
		for(Vegetation vegetation : vegetations) {
			if(vegetation instanceof DropItem && ((DropItem)vegetation).isCovered(di, distance)) {
				return ((DropItem)vegetation).pickup();
			}
		}
		return null;
	}
	
	//tool
	//structure
	public final boolean checkLoS(int x1, int y1, int x2, int y2) {
		for(Structure structure : structures) {
			if(structure.intersectsLine(x1, y1, x2, y2))
				return false;
		}
		return true;
	}
	public final boolean checkLoS(Point p1, Point p2) {
		return checkLoS(p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
	public final boolean hitObstacle(HitInteractable object) {
		return !object.point().inStage() || structures.intersected(object);
	}
	public final boolean hitObstacle(HitInteractable object, Point newPoint) {
		return !inStage(newPoint) || structures.intersected(object, newPoint);
	}
	
	//information
	/**
	 * Judge if a coordinate were in permitted area.
	 * @return true - in stage / false - in outside of stage
	 */
	public boolean inStage(int x, int y) {
		return 0 < x && x < WIDTH && 0 < y && y < HEIGHT;
	}
	public final boolean inStage(Point point) {
		return inStage(point.intX(), point.intY());
	}
	public final boolean inStage(HasPoint hasPoint) {
		return inStage(hasPoint.point());
	}
	
	//debug-information
	public void unitDebugPaint(final Graphics2D g2) {
		GHQ.translateForGUI(false);
		for(Unit unit : units) {
			final int RECT_X = unit.dynam().intX(), RECT_Y = unit.dynam().intY();
			g2.setStroke(GHQ.stroke1);
			g2.drawRect(RECT_X - 50, RECT_Y - 50, 100, 100);
			g2.drawLine(RECT_X + 50, RECT_Y - 50, RECT_X + 60, RECT_Y - 60);
			g2.setStroke(GHQ.stroke5);
			g2.drawString(unit.getName(), RECT_X + 62, RECT_Y - 68);
		}
		GHQ.translateForGUI(true);
	}
	public String entityAmountInfo() {
		return "Unit:" + units.size() + " EF:" + effects.size() + " B:" + bullets.size();
	}

	@Override
	public Rectangle2D boundingBox() {
		return new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	}
}
