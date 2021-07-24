package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.Set;

import fr.emmuliette.rune.mod.spells.SpellContext;

public abstract class Callback {
	private Set<Callback> setCB;
	private AbstractCastModContainerComponent container;
	private AbstractCastModComponent parent;
	private SpellContext context;
	private int triggerTick;
	private boolean listening;
	private boolean triggered;

	public SpellContext getContext() {
		return context;
	}

	public Callback(AbstractCastModComponent parent, SpellContext context) {
		this(parent, context, -1, false);
	}

	public Callback(AbstractCastModComponent parent, SpellContext context, int delay) {
		this(parent, context, delay, false);
	}

	public Callback(AbstractCastModComponent parent, SpellContext context, int delay, boolean listening) {
		this.parent = parent;
		this.context = context;
		this.triggerTick = (delay==-1)?-1:(CallbackManager.getCurrentTick() + delay);
		this.listening = listening;
		this.triggered = false;
		this.setContainer(null);
	}

	public boolean isTriggered() {
		return triggered;
	}

	public AbstractCastModComponent getParent() {
		return parent;
	}

	public AbstractCastModContainerComponent getContainer() {
		return container;
	}

	public Set<Callback> getSetCB() {
		return setCB;
	}

	public final boolean callBack() {
		triggered = true;
		boolean result = false;
		if (_callBack()) {
			if (getContainer() == null) {
				result = castChildren();
			} else {
				getContainer().update(this, context, false);
			}
		}
		finalize(result);
		return result;
	}

	public boolean castChildren() {
		boolean result = false;
		for (AbstractCastComponent<?> child : parent.getChildrens()) {
			result |= child.internalCast(context);
		}
		return result;
	}
	
	public void finish(boolean triggerUpdate) {
		end(triggerUpdate, false);
	}

	public void cancel(boolean triggerUpdate) {
		end(triggerUpdate, true);
	}
	
	protected void end(boolean triggerUpdate, boolean failed) {
		if (triggerUpdate && getContainer() != null) {
			getContainer().update(this, context, failed);
		}
		CallbackManager.unregister(this);
		finalize(!failed);
	}

	// BEFORE REGISTERING
	public abstract boolean begin();

	// WHEN TICK IS DONE
	public abstract boolean _callBack();

	// EVERY TICK
	public abstract boolean tick();

	// AFTER TICK IS DONE
	public abstract boolean finalize(boolean result);

	public void setContainer(AbstractCastModContainerComponent container) {
		this.container = container;
	}

	public void setSetCB(Set<Callback> setCB) {
		this.setCB = setCB;
	}

	public int getTriggerTick() {
		return triggerTick;
	}

	public boolean isListening() {
		return listening;
	}

}
