package de.tudarmstadt.informatik.fop.breakout.engine.event;

import de.tudarmstadt.informatik.fop.breakout.handlers.EntityHandler;
import eea.engine.action.Action;
import eea.engine.component.Component;
import eea.engine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by PC - Andreas on 09.04.2017.
 *
 * @author Andreas Dörr
 */
public class IteratedCollisionEvent extends Component {

	private ArrayList<Action> actions = null;
	private Entity collidedEntity;

	public IteratedCollisionEvent() {
		super("IteratedCollisionEvent");
		this.actions = new ArrayList();
	}

	public void addAction(Action action) {
		this.actions.add(action);
	}
	public void clearActions() {
		this.actions.clear();
	}
	public void removeAction(Action action) {
		this.actions.remove(action);
	}
	public void removeAction(int index) {
		this.actions.remove(index);
	}


	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		int startAt = this.collidesWithIndex(sb,0) + 1;
		while (startAt >= 0) {
			Iterator var5 = this.actions.iterator();
			while(var5.hasNext()) {
				Action action = (Action)var5.next();
				action.update(gc, sb, delta,this);
			}

			startAt = this.collidesWithIndex(sb, startAt) + 1;
		}

	}

	private int collidesWithIndex(StateBasedGame sb, int startAt) {
		Entity ownerEntity = this.owner;
		Entity[] candidates = EntityHandler.getCollidablesArray();
		if(candidates[0] != null && ownerEntity != null) {
			for (; startAt < candidates.length; startAt++) {
				if(ownerEntity.collides(candidates[startAt])) {
					collidedEntity = candidates[startAt];
					return startAt;
				}
			}
		}
		return -2;
	}

	public Entity getCollidedEntity() {
		return this.collidedEntity;
	}
}
