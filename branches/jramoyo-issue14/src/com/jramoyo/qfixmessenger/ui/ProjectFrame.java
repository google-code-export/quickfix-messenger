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
 * ProjectFrame.java
 * Sep 23, 2012
 */
package com.jramoyo.qfixmessenger.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.jramoyo.fix.xml.ProjectType;
import com.jramoyo.qfixmessenger.ui.model.ProjectTreeNode;
import com.jramoyo.qfixmessenger.ui.model.ProjectTypeProjectTreeNode;

/**
 * ProjectFrame
 * 
 * @author jramoyo
 */
public class ProjectFrame extends JFrame
{
	private static final long serialVersionUID = -1653220967743151936L;

	private final QFixMessengerFrame frame;
	private final ProjectType xmlProjectType;

	private JPanel mainPanel;

	private JTree projectTree;

	public ProjectFrame(QFixMessengerFrame frame, ProjectType xmlProjectType)
	{
		this.frame = frame;
		this.xmlProjectType = xmlProjectType;
	}

	public void launch()
	{
		setIconImage(new ImageIcon(frame.getMessenger().getConfig()
				.getAppIconLocation()).getImage());
		setTitle("Project Tree");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		initComponents();
		setVisible(true);
	}

	private void initComponents()
	{
		setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JScrollPane mainScrollPane = new JScrollPane();
		mainScrollPane.setPreferredSize(new Dimension(300, 400));
		add(mainScrollPane, BorderLayout.CENTER);

		projectTree = new JTree(new ProjectTypeProjectTreeNode(xmlProjectType));
		projectTree.setCellRenderer(new ProjectTreeCellRenderer());
		mainScrollPane.getViewport().add(projectTree);

		pack();
	}

	private static class ProjectTreeCellRenderer extends
			DefaultTreeCellRenderer
	{
		private static final long serialVersionUID = -435212244413010769L;

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus)
		{
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree,
					value, sel, expanded, leaf, row, hasFocus);
			label.setText(((ProjectTreeNode<?>) value).getLabel());

			return label;
		}
	}
}
