package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.Set;

import fr.emmuliette.rune.mod.spells.SpellContext;

public interface CallbackMod {
	/*
	protected default boolean internalCast(SpellContext context) {
		Callback cb = ((CallbackMod) this).castCallback(context);

		if (cb != null) {
			cb.begin();
			CallbackManager.register(cb);
			return true;
		}
		return false;
	}*/

	abstract Callback castCallback(SpellContext context);

	default Callback buildNRegisterCallback(SpellContext context, AbstractCastModContainerComponent container,
			Set<Callback> setCB) {
		Callback cb = castCallback(context);
		if (cb != null) {
			cb.setContainer(container);
			cb.setSetCB(setCB);
			setCB.add(cb);
			cb.begin();
			CallbackManager.register(cb);
		}
		return cb;
	}
}
