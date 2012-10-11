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
 * MemberPanelFactory.java
 * 14 Jun 2011
 */
package com.jramoyo.qfixmessenger.ui.panels;

import com.jramoyo.fix.model.Component;
import com.jramoyo.fix.model.Field;
import com.jramoyo.fix.model.Group;
import com.jramoyo.qfixmessenger.ui.QFixMessengerFrame;

/**
 * Static factory for creating MemberPanels
 * 
 * @author jamoyo
 */
public class MemberPanelFactory
{
	public static ComponentPanel createComponentPanel(QFixMessengerFrame frame,
			Component component, boolean isRequiredOnly, boolean isRequired)
	{
		MemberPanel<?, ?, ?> prevMemberPanel = frame.getMemberPanelCache()
				.getMemberPanel(component);
		if (prevMemberPanel != null)
		{
			ComponentPanel prevComponentPanel = (ComponentPanel) prevMemberPanel;
			frame.getMemberPanelCache().encacheMembers(
					prevComponentPanel.getMembers());
		}

		ComponentPanel componentPanel = new ComponentPanel(frame, component,
				isRequiredOnly, isRequired);
		frame.getMemberPanelCache().encacheMember(componentPanel);

		return componentPanel;
	}

	public static FieldPanel createFieldPanel(QFixMessengerFrame frame,
			Field field, boolean isRequired)
	{
		MemberPanel<?, ?, ?> prevMemberPanel = frame.getMemberPanelCache()
				.getMemberPanel(field);
		String value = null;
		if (prevMemberPanel != null)
		{
			FieldPanel prevFieldPanel = (FieldPanel) prevMemberPanel;
			value = prevFieldPanel.getValue();
		}

		FieldPanel fieldPanel = new FieldPanel(frame, field, isRequired, value);
		return fieldPanel;
	}

	public static GroupPanel createGroupPanel(QFixMessengerFrame frame,
			Group group, boolean isRequiredOnly, boolean isRequired)
	{
		MemberPanel<?, ?, ?> prevMemberPanel = frame.getMemberPanelCache()
				.getMemberPanel(group);
		int noOfGroups = -1;
		if (prevMemberPanel != null)
		{
			GroupPanel prevGroupPanel = (GroupPanel) prevMemberPanel;
			noOfGroups = prevGroupPanel.getNoOfGroups();
			for (int i = 0; i < prevGroupPanel.getGroups().size(); i++)
			{
				// Encache as a group member
				frame.getMemberPanelCache().encacheGroupMembers(i,
						prevGroupPanel.getGroups().get(i));
			}
		}

		GroupPanel groupPanel = new GroupPanel(frame, group, isRequiredOnly,
				isRequired, noOfGroups);
		frame.getMemberPanelCache().encacheMember(groupPanel);

		return groupPanel;
	}
}
