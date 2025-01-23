package com.tymscar.onepassword;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.runelite.client.util.OSType;
import javax.swing.*;

class CommandRunner extends Thread {

	CommandRunner(String accountId, Consumer<String> consumer) {
		super(() -> {
			try {
				ProcessBuilder pb = buildCommand(accountId);
				pb.redirectErrorStream(true);
				Process p = pb.start();

				byte[] bytes = ByteStreams.toByteArray(p.getInputStream());
				p.waitFor();

				consumer.accept(new String(bytes));
			} catch (IOException | InterruptedException e) {
				SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(),
						"Error", JOptionPane.INFORMATION_MESSAGE));
			}
		});
	}

	private static ProcessBuilder buildCommand(String accountId) {
		List<String> params = new ArrayList<>();

		String query = accountId != null ? accountId : "https://runescape.com";

		if (OSType.getOSType() == OSType.Windows) {
			params.add("cmd");
			params.add("/c");
		} else {
			params.add("bash");
			params.add("-c");
		}

		String redirect = OSType.getOSType() == OSType.Windows ? " < NUL" : " < /dev/null";

		params.add("op item get " + query + " --fields label=username,label=password --reveal" + redirect);

		return new ProcessBuilder(params);
	}
}
