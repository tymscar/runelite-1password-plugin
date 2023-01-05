package com.tymscar.onepassword;

import javax.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
		name = "1Password",
		description = "Lets you automatically login using credentials stored in 1Password",
		tags = {"pass", "password", "manager", "1pass", "onePass", "1Password", "onePassword"}
)
public class OnePasswordPlugin extends Plugin {
	@Inject
	private CredentialsManager credentialsManager;

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGED_IN) {
			credentialsManager.clearCredentials();
		}
	}

	@Override
	protected void startUp() throws Exception {
		credentialsManager.injectCredentials(null);
	}

	@Override
	protected void shutDown() throws Exception {
		credentialsManager.reset();
	}
}
