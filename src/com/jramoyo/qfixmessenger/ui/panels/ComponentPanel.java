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
 * ComponentPanel.java
 * 13 Jun 2011
 */
package com.jramoyo.qfixmessenger.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import quickfix.StringField;

import com.jramoyo.fix.model.Component;
import com.jramoyo.fix.model.Field;
import com.jramoyo.fix.model.Group;
import com.jramoyo.fix.model.MemberOrder;
import com.jramoyo.fix.xml.ComponentType;
import com.jramoyo.fix.xml.FieldType;
import com.jramoyo.fix.xml.GroupsType;
import com.jramoyo.fix.xml.ObjectFactory;
import com.jramoyo.qfixmessenger.QFixMessengerConstants;
import com.jramoyo.qfixmessenger.quickfix.ComponentHelper;
import com.jramoyo.qfixmessenger.ui.QFixMessengerFrame;

/**
 * @author jamoyo
 */
public class ComponentPanel extends
		AbstractMemberPanel<Component, ComponentHelper, ComponentType>
{
	private static final long serialVersionUID = 1982089310942186498L;

	private final boolean isRequired;

	private final boolean isRequiredOnly;

	private final List<MemberPanel<?, ?, ?>> members;

	private JLabel componentLabel;

	private JPanel membersPanel;

	public ComponentPanel(QFixMessengerFrame frame, Component component,
			boolean isRequiredOnly, boolean isRequired)
	{
		super(frame, component);
		this.isRequiredOnly = isRequiredOnly;
		this.isRequired = isRequired;
		this.members = new ArrayList<MemberPanel<?, ?, ?>>();

		initComponents();
	}

	@Override
	public String getFixString()
	{
		StringBuilder sb = new StringBuilder();

		for (MemberPanel<?, ?, ?> memberPanel : members)
		{
			sb.append(memberPanel.getFixString());
			sb.append(QFixMessengerConstants.SOH);
		}

		return sb.toString();
	}

	@Override
	public ComponentHelper getQuickFixMember()
	{
		List<StringField> fields = new ArrayList<StringField>();
		for (MemberPanel<?, ?, ?> memberPanel : members)
		{
			if (memberPanel instanceof FieldPanel)
			{
				FieldPanel fieldPanel = (FieldPanel) memberPanel;
				if (fieldPanel.getQuickFixMember() != null)
				{
					fields.add(fieldPanel.getQuickFixMember());
				}
			}
		}

		List<quickfix.Group> groups = new ArrayList<quickfix.Group>();
		for (MemberPanel<?, ?, ?> memberPanel : members)
		{
			if (memberPanel instanceof GroupPanel)
			{
				GroupPanel groupPanel = (GroupPanel) memberPanel;
				groups.addAll(groupPanel.getQuickFixMember());
			}
		}

		return new ComponentHelper(fields, groups);
	}

	@Override
	public ComponentType getXmlMember()
	{
		ObjectFactory xmlObjectFactory = new ObjectFactory();
		ComponentType xmlComponentType = xmlObjectFactory.createComponentType();
		xmlComponentType.setName(getMember().getName());

		for (MemberPanel<?, ?, ?> memberPanel : members)
		{
			if (memberPanel instanceof FieldPanel)
			{
				FieldType xmlFieldType = ((FieldPanel) memberPanel)
						.getXmlMember();
				if (xmlFieldType != null)
				{
					xmlComponentType.getFieldOrGroupsOrComponent().add(
							xmlFieldType);
				}
			}

			if (memberPanel instanceof GroupPanel)
			{
				GroupsType xmlGroupsTypeMember = ((GroupPanel) memberPanel)
						.getXmlMember();
				if (xmlGroupsTypeMember != null)
				{
					xmlComponentType.getFieldOrGroupsOrComponent().add(
							xmlGroupsTypeMember);
				}
			}
		}

		return xmlComponentType;
	}

	@Override
	public boolean hasValidFormat()
	{
		boolean hasValidContent = true;

		for (MemberPanel<?, ?, ?> memberPanel : members)
		{
			hasValidContent = hasValidContent && memberPanel.hasValidFormat();
		}

		return hasValidContent;
	}

	public void populateXml(ComponentType xmlComponentType)
	{
		for (Object xmlMember : xmlComponentType.getFieldOrGroupsOrComponent())
		{
			if (xmlMember instanceof FieldType)
			{
				FieldType xmlFieldType = (FieldType) xmlMember;
				FieldPanel fieldPanel = (FieldPanel) MemberPanelUtil
						.findMemberPanelByName(xmlFieldType.getName(), members);
				fieldPanel.populateXml(xmlFieldType);
			}

			if (xmlMember instanceof GroupsType)
			{
				GroupsType xmlGroupsType = (GroupsType) xmlMember;
				GroupPanel groupPanel = (GroupPanel) MemberPanelUtil
						.findMemberPanelByName(xmlGroupsType.getName(), members);
				groupPanel.populateXml(xmlGroupsType);
			}

			if (xmlMember instanceof ComponentType)
			{
				ComponentType xmlComponentTypeMember = (ComponentType) xmlMember;
				ComponentPanel componentPanel = (ComponentPanel) MemberPanelUtil
						.findMemberPanelByName(
								xmlComponentTypeMember.getName(), members);
				componentPanel.populateXml(xmlComponentTypeMember);
			}
		}
	}

	List<MemberPanel<?, ?, ?>> getMembers()
	{
		return members;
	}

	private GridBagConstraints createGridBagConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 0.5;
		c.weighty = 0.0;

		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		return c;
	}

	private void initComponents()
	{
		setLayout(new BorderLayout());

		componentLabel = new JLabel(getMember().toString());
		componentLabel
				.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		componentLabel.addMouseListener(new LinkMouseAdapter(this));
		componentLabel.setToolTipText("Double-click to look-up in FIXwiki");
		if (isRequired)
		{
			componentLabel.setForeground(Color.BLUE);
		}

		componentLabel.setFont(new Font(componentLabel.getFont().getName(),
				Font.BOLD, componentLabel.getFont().getSize()));

		membersPanel = new JPanel();
		if (isRequired)
		{
			membersPanel.setBorder(new LineBorder(Color.BLUE));
		} else
		{
			membersPanel.setBorder(new LineBorder(Color.BLACK));
		}
		loadMembers();

		add(componentLabel, BorderLayout.NORTH);
		add(membersPanel, BorderLayout.SOUTH);
	}

	private void loadMembers()
	{
		membersPanel.setLayout(new GridBagLayout());

		Field firstTag = getMember().getFirstField();
		if (firstTag != null)
		{
			FieldPanel fieldPanel = MemberPanelFactory.createFieldPanel(
					getFrame(), firstTag, true);
			fieldPanel.setMaximumSize(new Dimension(getPreferredSize().width,
					fieldPanel.getPreferredSize().height));

			membersPanel.add(fieldPanel, createGridBagConstraints());
			members.add(fieldPanel);
		}

		for (Entry<MemberOrder, Boolean> entry : getMember().getMembers()
				.entrySet())
		{
			if (isRequiredOnly && !entry.getValue())
			{
				continue;
			}

			if (entry.getKey().getMember() instanceof Field)
			{
				Field field = (Field) entry.getKey().getMember();
				if (!field.equals(firstTag))
				{
					FieldPanel fieldPanel = MemberPanelFactory
							.createFieldPanel(getFrame(), field,
									entry.getValue());
					fieldPanel.setMaximumSize(new Dimension(
							getPreferredSize().width, fieldPanel
									.getPreferredSize().height));

					membersPanel.add(fieldPanel, createGridBagConstraints());
					members.add(fieldPanel);
				}
			}

			if (entry.getKey().getMember() instanceof Group)
			{
				Group group = (Group) entry.getKey().getMember();

				GroupPanel groupPanel = MemberPanelFactory.createGroupPanel(
						getFrame(), group, isRequiredOnly, entry.getValue());
				groupPanel.setMaximumSize(new Dimension(
						getPreferredSize().width,
						groupPanel.getPreferredSize().height));

				membersPanel.add(groupPanel, createGridBagConstraints());
				members.add(groupPanel);
			}

			if (entry.getKey().getMember() instanceof Component)
			{
				Component component = (Component) entry.getKey().getMember();

				ComponentPanel componentPanel = MemberPanelFactory
						.createComponentPanel(getFrame(), component,
								isRequiredOnly, entry.getValue());
				componentPanel.setMaximumSize(new Dimension(
						getPreferredSize().width, componentPanel
								.getPreferredSize().height));

				membersPanel.add(componentPanel, createGridBagConstraints());
				members.add(componentPanel);
			}
		}
	}
}
