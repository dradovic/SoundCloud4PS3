package net.pms.external;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.pms.PMS;
import net.pms.dlna.DLNAResource;
import soundcloud4ps3.Authorization;
import soundcloud4ps3.Cloud;
import soundcloud4ps3.CloudFolder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SoundCloud4PS3 implements AdditionalFolderAtRoot {

	private static final String PLUGIN_NAME = "SoundCloud4PS3";
	private static final String VERSION = "0.3";

	private final Authorization authorization = new Authorization();
	private Cloud cloud;

	private final ArrayList<Component> authorizationComponents = new ArrayList<Component>();
	private final ArrayList<Component> unauthorizationComponents = new ArrayList<Component>();
	private final JLabel authorizationStateLabel = new JLabel();
	private final JTextArea authorizationUrlArea = new JTextArea();
	private final JTextField verificationCodeField = new JTextField();

	private final CloudFolder topFolder;

	public SoundCloud4PS3() {
		log("v%s", VERSION);
		
//		ResourceNode root = new ResourceNode("me");
//		root.addChild(new ResourceNode("me/favorites"));
		topFolder = new CloudFolder("SoundCloud", "me");
		
		onAuthorizationStateChanged();
	}

	@Override
	public JComponent config() {

		FormLayout layout = new FormLayout(
				"70dlu, 10dlu, 300dlu", //$NON-NLS-1$
				"p, 5dlu, p, 5dlu, p, 10dlu, 10dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 0:grow"); //$NON-NLS-1$
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setBorder(Borders.EMPTY_BORDER);
		builder.setOpaque(false);

		CellConstraints cc = new CellConstraints();

		//
		// Authorization
		//
		int row = 1;
		JComponent cmp = builder.addSeparator("Authorization", cc.xyw(1, row, 3));
		cmp = (JComponent) cmp.getComponent(0);
		cmp.setFont(cmp.getFont().deriveFont(Font.BOLD));
		row += 2;

		// Authorization State
		builder.addLabel("Current State:", cc.xy(1, row));
		builder.add(authorizationStateLabel, cc.xy(3, row));
		row += 2;

		// Authorization Explanation
		authorizationComponents.add(builder.addLabel("You must authorize this plugin to retrieve your favorites. In order to do so, navigate to the authorization URL,", cc.xyw(1, row++, 3)));
		authorizationComponents.add(builder.addLabel("then enter the retrieved verification code and click 'Authorize'.", cc.xyw(1, row++, 3)));
		row += 1;

		// Authorization URL
		authorizationUrlArea.setEditable(false);
		authorizationComponents.add(builder.addLabel("Authorization URL:", cc.xy(1, row)));
		authorizationComponents.add(builder.add(authorizationUrlArea, cc.xy(3, row)));
		row += 2;

		// Verification Code
		authorizationComponents.add(builder.addLabel("Verification Code:", cc.xy(1, row)));
		authorizationComponents.add(builder.add(verificationCodeField, cc.xy(3,	row)));
		row += 2;

		// Authorize Button
		JButton authorizeButton = new JButton("Authorize");
		authorizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authorization.authorize(verificationCodeField.getText());
				assert authorization.isAuthorized();
				onAuthorizationStateChanged();
			}
		});
		authorizationComponents.add(builder.add(authorizeButton, cc.xy(1, row)));
		row += 2;

		// Unauthorize Button
		JButton unauthorizeButton = new JButton("Unauthorize");
		unauthorizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				authorization.unauthorize();
				assert !authorization.isAuthorized();
				onAuthorizationStateChanged();
			}
		});
		unauthorizationComponents.add(builder.add(unauthorizeButton, cc.xy(1, row)));
		row += 2;

		// enable/disable controls
		onAuthorizationStateChanged();

		return builder.getPanel();
	}

	@Override
	public String name() {
		return "SoundCloud for PS3 Media Server";
	}

	@Override
	public void shutdown() {
	}

	@Override
	public DLNAResource getChild() {
		return topFolder;
	}

	public static void log(String message, Object... args) {
		PMS.minimal(PLUGIN_NAME + ": " + String.format(message, args));
	}

	private void onAuthorizationStateChanged() {
		authorizationStateLabel.setText(authorization.getState().toString());
		cloud = authorization.getCloud();
		boolean isAuthorized = authorization.isAuthorized();
		for (Component c : authorizationComponents) {
			c.setEnabled(!isAuthorized);
		}
		for (Component c : unauthorizationComponents) {
			c.setEnabled(isAuthorized);
		}
		if (!isAuthorized) {
			authorizationUrlArea.setText(authorization.getAuthorizationUrl());
		}
		topFolder.setCloud(cloud);
	}
}
