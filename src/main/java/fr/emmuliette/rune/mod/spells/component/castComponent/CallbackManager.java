package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CallbackManager {
	private static final Set<Callback> listeningCB = new HashSet<Callback>();
	private static int currentTick = 0;
	private static final Map<Integer, List<Callback>> callBackList = new HashMap<Integer, List<Callback>>();
	private static final CallbackManager INSTANCE = new CallbackManager();

	public static CallbackManager getInstance() {
		return INSTANCE;
	}

	public static void register(Callback cb) {
		if (cb.getTriggerTick() <= currentTick && cb.getTriggerTick() != -1) {
			// TODO throw badTickException
			RuneMain.LOGGER.error(
					"WARNING registering on bad tick ! trigger " + cb.getTriggerTick() + " vs current " + currentTick);
			return;
		}
		if (cb.isListening() && listeningCB.contains(cb)) {
			// TODO throw alreadyRegisteredException
			RuneMain.LOGGER.error("WARNING CallBack already registered !");
			return;
		}
		List<Callback> cbList;
		if ((cbList = callBackList.get(cb.getTriggerTick())) == null) {
			cbList = new ArrayList<Callback>();
			callBackList.put(cb.getTriggerTick(), cbList);
		}
		cbList.add(cb);
		if (cb.isListening())
			listeningCB.add(cb);
	}

	public static void unregister(Callback cb) {
		List<Callback> cbList;
		if ((cbList = callBackList.get(cb.getTriggerTick())) != null) {
			cbList.remove(cb);
		}
		if (listeningCB.contains(cb)) {
			listeningCB.remove(cb);
		}
	}

	@SubscribeEvent
	public static void worldTickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		currentTick = getCurrentTick() + 1;
		Iterator<Callback> cbIt = listeningCB.iterator();
		Set<Callback> finalize = new HashSet<Callback>();
		while(cbIt.hasNext()) {
			Callback cb = cbIt.next();
			if(cb.tick() == false) {
				cbIt.remove();
				finalize.add(cb);
			}
		}
		if(!finalize.isEmpty()) {
			for(Callback cb:finalize) {
				cb.finalize(false);
			}
		}
		if (!callBackList.containsKey(getCurrentTick())) {
			return;
		}
		for (Callback cb : callBackList.get(getCurrentTick())) {
			cb.callBack();
		}
		callBackList.remove(getCurrentTick());
	}

	public static int getCurrentTick() {
		return currentTick;
	}
}
