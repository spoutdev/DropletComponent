/*
 * This file is part of DropletEntity.
 *
 * Copyright (c) 2012, SpoutDev <http://www.spout.org/>
 * DropletEntity is licensed under the SpoutDev License Version 1.
 *
 * DropletEntity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * DropletEntity is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.droplet.entity.protocol;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.builtin.message.AddEntityMessage;
import org.spout.api.protocol.builtin.message.RemoveEntityMessage;

import org.spout.droplet.entity.DropletControllerTypes;
import org.spout.droplet.entity.controller.NPC;

public class NPCProtocol implements EntityProtocol {
	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof NPC)) {
			return new Message[0];
		}
		return new Message[]{new AddEntityMessage(entity.getId(), DropletControllerTypes.DROPLET_NPC, entity.getTransform())};
	}

	@Override
	public Message[] getDestroyMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof NPC)) {
			return new Message[0];
		}
		return new Message[]{new RemoveEntityMessage(entity.getId())};
	}

	@Override
	public Message[] getUpdateMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof NPC)) {
			return new Message[0];
		}
		return new Message[0];
	}
}
