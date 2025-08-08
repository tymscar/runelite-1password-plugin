package com.tymscar.onepassword;

import javax.inject.Inject;
import javax.swing.*;

import net.runelite.api.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CredentialsManager extends JFrame implements ActionListener {
	private final Client client;
	private CommandRunner commandRunner = null;
	private CommandRunner otpCommandRunner = null;
	private String selectedAccountId = null;
	private ArrayList<String> AccountIds = new ArrayList<>();
	private JComboBox<String> accountsComboBox;
	private JButton confirmButton;
	private JFrame popupFrame;

	@Inject
	CredentialsManager(Client client) {
		this.client = client;
	}

	private void parseIssue(String result) {
		commandRunner = null;

		if (result.contains("authorization") || result.contains("app is locked")) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(null, "You are not logged into the 1Password CLI.",
					"Auth error", JOptionPane.INFORMATION_MESSAGE);
			});
		} else if (result.contains("More than one item matches")) {
			SwingUtilities.invokeLater(() -> {
				handleMultipleAccounts(result);
			});
		} else {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(null, "There is no account with the url set to https://runescape.com",
						"", JOptionPane.ERROR_MESSAGE);
			});
		}
	}

	private void handleMultipleAccounts(String commandOutput) {
		Pattern r = Pattern.compile("for the item \"([^\"]+)\" in vault [^:]+: ([a-zA-Z0-9]+)");
		Matcher matches = r.matcher(commandOutput);

		ArrayList<String> accounts = new ArrayList<>();
		this.AccountIds = new ArrayList<>();
		while (matches.find()) {
			accounts.add(matches.group(1));
			this.AccountIds.add(matches.group(2));
		}

		popupFrame = new JFrame("More than one account detected!");
		popupFrame.setLayout(new FlowLayout());

		accountsComboBox = new JComboBox<>(accounts.toArray(new String[0]));

		JLabel label = new JLabel("You have more than one account. Please select the entry name you want!");
		label.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		label.setForeground(Color.white);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(label);

		p.add(accountsComboBox);

		confirmButton = new JButton("Login with this account!");
		confirmButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		confirmButton.addActionListener(this);
		p.add(confirmButton);

		popupFrame.add(p);
		popupFrame.setSize(600, 200);
		popupFrame.setLocationRelativeTo(null);

		popupFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirmButton) {
			int index = accountsComboBox.getSelectedIndex();
			this.selectedAccountId = this.AccountIds.get(index);
			popupFrame.dispatchEvent(new WindowEvent(popupFrame, WindowEvent.WINDOW_CLOSING));
			this.injectCredentials(this.selectedAccountId);
		}
	}

	private void consumeResult(String result) {
		if (result.startsWith("[ERROR]")) {
			parseIssue(result);
			return;
		}

		String[] retrievedCredentials = result.split(",");

		String username = retrievedCredentials[0].trim();
		String password = retrievedCredentials[1].trim();

		setPassword(password);
		setUsername(username);

		fetchOtpCode();

		commandRunner = null;
	}

	private void consumeOtpResult(String result) {
		if (!result.startsWith("[ERROR]") && result.trim().length() == 6) {
			String otpCode = result.trim();
			setOtpCode(otpCode);
		}
		otpCommandRunner = null;
	}

	private void fetchOtpCode() {
		if (otpCommandRunner == null) {
			otpCommandRunner = new CommandRunner(this.selectedAccountId, this::consumeOtpResult, true);
			otpCommandRunner.start();
		}
	}

	private void setPassword(String password) {
		if (password != null) {
			client.setPassword(password);
		}
	}

	private void setUsername(String username) {
		if (username != null) {
			client.setUsername(username);
		}
	}

	private void setOtpCode(String otpCode) {
		if (otpCode != null) {
			client.setOtp(otpCode);
		}
	}



	void reset() {
		this.selectedAccountId = null;
		commandRunner = null;
		otpCommandRunner = null;
	}

	void injectCredentials(String accountId) {
		if (commandRunner == null) {
			commandRunner = new CommandRunner(accountId, this::consumeResult);
			commandRunner.start();
		}
	}

}
