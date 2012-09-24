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
 * BodyTypeProjectTreeNode.java
 * Sep 24, 2012
 */
package com.jramoyo.qfixmessenger.ui.model;

import com.jramoyo.fix.xml.BodyType;
import com.jramoyo.fix.xml.ComponentType;
import com.jramoyo.fix.xml.FieldType;
import com.jramoyo.fix.xml.GroupType;

/**
 * @author jramoyo
 */
public class BodyTypeProjectTreeNode extends AbstractProjectTreeNode<BodyType>
{
	private static final long serialVersionUID = 5263142011975975670L;

	public BodyTypeProjectTreeNode(BodyType xmlObject,
			AbstractProjectTreeNode<?> parent)
	{
		super(xmlObject, parent, true);
	}

	@Override
	public String getLabel()
	{
		return "Body";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void populateChildren(BodyType xmlObject)
	{
		for (Object child : xmlObject.getFieldOrGroupsOrComponent())
		{
			if (child instanceof FieldType)
			{
				children.add(new FieldTypeProjectTreeNode((FieldType) child,
						this));
			}

			else if (child instanceof GroupType)
			{
				children.add(new GroupTypeProjectTreeNode((GroupType) child,
						this));
			}

			else if (child instanceof ComponentType)
			{
				children.add(new ComponentProjectTreeNode(
						(ComponentType) child, this));
			}
		}
	}
}
