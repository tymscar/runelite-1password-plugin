package com.tymscar.onepassword;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OnePasswordPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(OnePasswordPlugin.class);
		RuneLite.main(args);
	}
}