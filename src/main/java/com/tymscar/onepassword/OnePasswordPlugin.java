package com.tymscar.onepassword;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WidgetLoaded;
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
	private Client client;
	
	@Inject
	private CredentialsManager credentialsManager;

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGED_IN) {
			credentialsManager.clearCredentials();
		} else if (event.getGameState() == GameState.LOGIN_SCREEN || 
		           event.getGameState() == GameState.LOGIN_SCREEN_AUTHENTICATOR) {
			credentialsManager.injectCredentials(null);
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event) {
		if (event.getGroupId() == 549) {
			javax.swing.Timer timer = new javax.swing.Timer(100, e -> {
				String otpCode = credentialsManager.getOtpCode();
				if (otpCode != null && !otpCode.isEmpty()) {
					client.setOtp(otpCode);
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	@Override
	protected void startUp() throws Exception {
		credentialsManager.injectCredentials(null);
		
		GameState currentState = client.getGameState();
		if (currentState == GameState.LOGIN_SCREEN || currentState == GameState.LOGIN_SCREEN_AUTHENTICATOR) {
			javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
				credentialsManager.injectCredentials(null);
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	@Override
	protected void shutDown() throws Exception {
		credentialsManager.reset();
	}
}
