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

import soundcloud4ps3.Authorization;
import soundcloud4ps3.Cloud;
import soundcloud4ps3.Track;
import soundcloud4ps3.User;

import net.pms.Messages;
import net.pms.PMS;
import net.pms.dlna.DLNAResource;
import net.pms.dlna.WebAudioStream;
import net.pms.dlna.virtual.VirtualFolder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SoundCloud4PS3 implements AdditionalFolderAtRoot {
	
	private static final String VERSION = "0.2";

	private Authorization authorization = new Authorization();
	private Cloud cloud;

	private ArrayList<Component> authorizationComponents = new ArrayList<Component>();
	private ArrayList<Component> unauthorizationComponents = new ArrayList<Component>();	
	private JLabel authorizationStateLabel = new JLabel();
	private JTextArea authorizationUrlArea = new JTextArea();
	private JTextField verificationCodeField = new JTextField();
	private JLabel userLabel = new JLabel();

	public SoundCloud4PS3() {
		log("v%s", VERSION);
		onAuthorizationStateChanged();
	}

	@Override
	public JComponent config() {

		FormLayout layout = new FormLayout("70dlu, 10dlu, 300dlu", //$NON-NLS-1$
				"p, 5dlu, p, 5dlu, p, 10dlu, 10dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 0:grow"); //$NON-NLS-1$
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
		authorizationComponents.add(builder.add(verificationCodeField, cc.xy(3, row)));
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

		//
		// Account
		//
		cmp = builder.addSeparator("Account", cc.xyw(1, row, 3));
		cmp = (JComponent) cmp.getComponent(0);
		cmp.setFont(cmp.getFont().deriveFont(Font.BOLD));
		row += 2;

		// User Name
		builder.addLabel("User:", cc.xy(1, row));
		builder.add(userLabel, cc.xy(3, row));
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
		VirtualFolder vfs;
		if (cloud != null) {
			User user = cloud.getUser();
			vfs = new VirtualFolder(Messages.getString(user.getUserName()
					+ "'s SoundCloud"), user.getAvatarUrl());
			for (Track track : cloud.getFavoriteTracks()) {
				vfs.addChild(new WebAudioStream(track.getTitle(), track
						.getStreamUrl(), track.getArtworkUrl()));
			}
		} else {
			vfs = new VirtualFolder(
					Messages
							.getString("Please configure and authorize SoundCloud on your Media Server"),
					null);
		}
		return vfs;
	}
	
	public static void log(String message, Object... args)
	{
		PMS.minimal("SoundCloud4PS3: " + String.format(message, args));
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
	    userLabel.setText(cloud != null ? cloud.getUser().getUserName() : "-");
	}
}
