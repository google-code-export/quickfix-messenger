/*
 * Copyright (c) 2011, Jan Amoyo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *
 * - Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer 
 *   in the documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS 
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED 
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH 
 * DAMAGE.
 *
 * MessagePanel.java
 * Oct 2, 2012
 */
package com.jramoyo.qfixmessenger.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jramoyo.fix.model.Component;
import com.jramoyo.fix.model.Field;
import com.jramoyo.fix.model.FixDictionary;
import com.jramoyo.fix.model.Group;
import com.jramoyo.fix.model.Member;
import com.jramoyo.fix.model.Message;
import com.jramoyo.fix.xml.MessageType;
import com.jramoyo.qfixmessenger.ui.QFixMessengerFrame;

/**
 * @author jramoyo
 */
public class MessagePanel extends JPanel implements MemberPanel
{
	private static final long serialVersionUID = 7937359075224178112L;

	private final Message message;

	private final boolean isRequiredOnly;

	private final boolean isModifyHeader;

	private final boolean isModifyTrailer;

	private final boolean isFixTSession;

	private final List<MemberPanel> headerMembers;

	private final List<MemberPanel> bodyMembers;

	private final List<MemberPanel> trailerMembers;

	private final List<MemberPanel> prevHeaderMembers;

	private final List<MemberPanel> prevBodyMembers;

	private final List<MemberPanel> prevTrailerMembers;

	private final FixDictionary activeDictionary;

	private final FixDictionary fixTDictionary;

	private MessagePanel(MessagePanelBuilder builder)
	{
		this.message = builder.message;

		this.isRequiredOnly = builder.isRequiredOnly;
		this.isModifyHeader = builder.isModifyHeader;
		this.isModifyTrailer = builder.isModifyTrailer;
		this.isFixTSession = builder.isFixTSession;

		this.headerMembers = builder.headerMembers;
		this.bodyMembers = builder.bodyMembers;
		this.trailerMembers = builder.trailerMembers;

		this.prevHeaderMembers = builder.prevHeaderMembers;
		this.prevBodyMembers = builder.prevBodyMembers;
		this.prevTrailerMembers = builder.prevTrailerMembers;

		this.activeDictionary = builder.activeDictionary;
		this.fixTDictionary = builder.fixTDictionary;

		initComponents();
	}

	private void initComponents()
	{
		prevHeaderMembers.clear();
		prevHeaderMembers.addAll(bodyMembers);

		prevBodyMembers.clear();
		prevBodyMembers.addAll(bodyMembers);

		prevTrailerMembers.clear();
		prevTrailerMembers.addAll(bodyMembers);

		headerMembers.clear();
		bodyMembers.clear();
		trailerMembers.clear();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Load the header
		if (isModifyHeader)
		{
			JPanel headerPanel = new JPanel();
			headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

			TitledBorder headerBorder = new TitledBorder(new LineBorder(
					Color.BLACK), "Message Header");
			// TODO Workaround for Java Bug ID: 7022041
			Font headerTitleBorderFont = UIManager.getDefaults().getFont(
					"TitledBorder.font");
			if (headerTitleBorderFont != null)
			{
				headerBorder.setTitleFont(new Font(headerTitleBorderFont
						.getName(), Font.BOLD, 15));
			}
			headerPanel.setBorder(headerBorder);

			if (!isFixTSession)
			{
				for (Entry<Member, Boolean> entry : activeDictionary
						.getHeader().getMembers().entrySet())
				{
					loadMember(headerPanel, prevHeaderMembers, headerMembers,
							entry);
				}
			} else
			{
				for (Entry<Member, Boolean> entry : fixTDictionary.getHeader()
						.getMembers().entrySet())
				{
					loadMember(headerPanel, prevHeaderMembers, headerMembers,
							entry);
				}
			}

			add(headerPanel);
			add(Box.createRigidArea(new Dimension(0, 20)));
		}

		// Load the body
		JPanel bodyPanel = new JPanel();
		bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

		TitledBorder bodyBorder = new TitledBorder(new LineBorder(Color.BLACK),
				"Message Body");
		// TODO Workaround for Java Bug ID: 7022041
		Font bodyTitleBorderFont = UIManager.getDefaults().getFont(
				"TitledBorder.font");
		if (bodyTitleBorderFont != null)
		{
			bodyBorder.setTitleFont(new Font(bodyTitleBorderFont.getName(),
					Font.BOLD, 15));
		}
		bodyPanel.setBorder(bodyBorder);

		for (Entry<Member, Boolean> entry : message.getMembers().entrySet())
		{
			loadMember(bodyPanel, prevBodyMembers, bodyMembers, entry);
		}

		add(bodyPanel);

		// Load the trailer
		if (isModifyTrailer)
		{
			JPanel trailerPanel = new JPanel();
			trailerPanel
					.setLayout(new BoxLayout(trailerPanel, BoxLayout.Y_AXIS));

			TitledBorder trailerBorder = new TitledBorder(new LineBorder(
					Color.BLACK), "Message Trailer");
			// TODO Workaround for Java Bug ID: 7022041
			Font trailerTitleBorderFont = UIManager.getDefaults().getFont(
					"TitledBorder.font");
			if (trailerTitleBorderFont != null)
			{
				trailerBorder.setTitleFont(new Font(trailerTitleBorderFont
						.getName(), Font.BOLD, 15));
			}
			trailerPanel.setBorder(trailerBorder);

			if (!isFixTSession)
			{
				for (Entry<Member, Boolean> entry : activeDictionary
						.getTrailer().getMembers().entrySet())
				{
					loadMember(trailerPanel, prevTrailerMembers,
							trailerMembers, entry);
				}
			} else
			{
				for (Entry<Member, Boolean> entry : fixTDictionary.getTrailer()
						.getMembers().entrySet())
				{
					loadMember(trailerPanel, prevTrailerMembers,
							trailerMembers, entry);
				}
			}

			add(Box.createRigidArea(new Dimension(0, 20)));
			add(trailerPanel);
		}
	}

	private void loadMember(JPanel mainPanel,
			List<MemberPanel> previousMemberList, List<MemberPanel> memberList,
			Entry<Member, Boolean> entry)
	{
		if (isRequiredOnly && !entry.getValue())
		{
			return;
		}

		if (entry.getKey() instanceof Field)
		{
			Field field = (Field) entry.getKey();

			FieldPanel fieldPanel = MemberPanelFactory.createFieldPanel(
					previousMemberList, field, entry.getValue());
			fieldPanel.setMaximumSize(new Dimension(
					QFixMessengerFrame.MIDDLE_PANEL_WIDTH, fieldPanel
							.getPreferredSize().height));

			mainPanel.add(fieldPanel);
			memberList.add(fieldPanel);
		}

		if (entry.getKey() instanceof Group)
		{
			Group group = (Group) entry.getKey();

			GroupPanel groupPanel = MemberPanelFactory
					.createGroupPanel(previousMemberList, group,
							isRequiredOnly, entry.getValue());
			groupPanel.setMaximumSize(new Dimension(
					QFixMessengerFrame.MIDDLE_PANEL_WIDTH, groupPanel
							.getPreferredSize().height));

			mainPanel.add(groupPanel);
			memberList.add(groupPanel);
		}

		if (entry.getKey() instanceof Component)
		{
			Component component = (Component) entry.getKey();

			ComponentPanel componentPanel = MemberPanelFactory
					.createComponentPanel(previousMemberList, component,
							isRequiredOnly, entry.getValue());
			componentPanel.setMaximumSize(new Dimension(
					QFixMessengerFrame.MIDDLE_PANEL_WIDTH, componentPanel
							.getPreferredSize().height));

			mainPanel.add(componentPanel);
			memberList.add(componentPanel);
		}
	}

	@Override
	public String getFixString()
	{
		// TODO
		return null;
	}

	@Override
	public Member getMember()
	{
		return message;
	}

	public MessageType getXmlMessage()
	{
		return null;
	}

	public quickfix.Message getQuickFixMessage()
	{
		return null;
	}

	public void populate(MessageType xmlMessageType)
	{
	}

	public static class MessagePanelBuilder
	{
		private Message message;

		private boolean isRequiredOnly;

		private boolean isModifyHeader;

		private boolean isModifyTrailer;

		private boolean isFixTSession;

		private List<MemberPanel> headerMembers;

		private List<MemberPanel> bodyMembers;

		private List<MemberPanel> trailerMembers;

		private List<MemberPanel> prevHeaderMembers;

		private List<MemberPanel> prevBodyMembers;

		private List<MemberPanel> prevTrailerMembers;

		private FixDictionary activeDictionary;

		private FixDictionary fixTDictionary;

		public MessagePanel build()
		{
			return new MessagePanel(this);
		}

		public Message getMessage()
		{
			return message;
		}

		public void setMessage(Message message)
		{
			this.message = message;
		}

		public boolean isRequiredOnly()
		{
			return isRequiredOnly;
		}

		public void setRequiredOnly(boolean isRequiredOnly)
		{
			this.isRequiredOnly = isRequiredOnly;
		}

		public boolean isModifyHeader()
		{
			return isModifyHeader;
		}

		public void setModifyHeader(boolean isModifyHeader)
		{
			this.isModifyHeader = isModifyHeader;
		}

		public boolean isModifyTrailer()
		{
			return isModifyTrailer;
		}

		public void setModifyTrailer(boolean isModifyTrailer)
		{
			this.isModifyTrailer = isModifyTrailer;
		}

		public boolean isFixTSession()
		{
			return isFixTSession;
		}

		public void setFixTSession(boolean isFixTSession)
		{
			this.isFixTSession = isFixTSession;
		}

		public List<MemberPanel> getHeaderMembers()
		{
			return headerMembers;
		}

		public void setHeaderMembers(List<MemberPanel> headerMembers)
		{
			this.headerMembers = headerMembers;
		}

		public List<MemberPanel> getBodyMembers()
		{
			return bodyMembers;
		}

		public void setBodyMembers(List<MemberPanel> bodyMembers)
		{
			this.bodyMembers = bodyMembers;
		}

		public List<MemberPanel> getTrailerMembers()
		{
			return trailerMembers;
		}

		public void setTrailerMembers(List<MemberPanel> trailerMembers)
		{
			this.trailerMembers = trailerMembers;
		}

		public List<MemberPanel> getPrevHeaderMembers()
		{
			return prevHeaderMembers;
		}

		public void setPrevHeaderMembers(List<MemberPanel> prevHeaderMembers)
		{
			this.prevHeaderMembers = prevHeaderMembers;
		}

		public List<MemberPanel> getPrevBodyMembers()
		{
			return prevBodyMembers;
		}

		public void setPrevBodyMembers(List<MemberPanel> prevBodyMembers)
		{
			this.prevBodyMembers = prevBodyMembers;
		}

		public List<MemberPanel> getPrevTrailerMembers()
		{
			return prevTrailerMembers;
		}

		public void setPrevTrailerMembers(List<MemberPanel> prevTrailerMembers)
		{
			this.prevTrailerMembers = prevTrailerMembers;
		}

		public FixDictionary getActiveDictionary()
		{
			return activeDictionary;
		}

		public void setActiveDictionary(FixDictionary activeDictionary)
		{
			this.activeDictionary = activeDictionary;
		}

		public FixDictionary getFixTDictionary()
		{
			return fixTDictionary;
		}

		public void setFixTDictionary(FixDictionary fixTDictionary)
		{
			this.fixTDictionary = fixTDictionary;
		}

	}
}
