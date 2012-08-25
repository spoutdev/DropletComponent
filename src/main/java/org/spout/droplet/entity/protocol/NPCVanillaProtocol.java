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

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.droplet.entity.controller.NPC;

import org.spout.vanilla.protocol.msg.DestroyEntitiesMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRelativePositionRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnPlayerMessage;
import org.spout.vanilla.protocol.msg.entity.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.entity.EntityVelocityMessage;

import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyPosition;
import static org.spout.vanilla.protocol.ChannelBufferUtils.protocolifyRotation;

/**
 * Provides compatibility with VanillaPlugin's protocol
 */
public class NPCVanillaProtocol implements EntityProtocol {
	@Override
	public Message[] getSpawnMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof NPC)) {
			return new Message[0];
		}
		NPC npc = (NPC) c;
		int id = entity.getId();
		int x = (int) (entity.getPosition().getX() * 32);
		int y = (int) (entity.getPosition().getY() * 32);
		int z = (int) (entity.getPosition().getZ() * 32);
		int r = (int) (-entity.getYaw() * 32);
		int p = (int) (entity.getPitch() * 32);

		int item = 0;
		ItemStack heldItem = npc.getHeldItem();
		if (heldItem != null) {
			item = heldItem.getMaterial().getId();
		}

		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 0, (byte) 0));
		return new Message[]{new EntitySpawnPlayerMessage(id, npc.getName(), x, y, z, r, p, item, parameters)};
	}

	@Override
	public Message[] getDestroyMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof NPC)) {
			return new Message[0];
		}
		return new Message[]{new DestroyEntitiesMessage(new int[]{entity.getId()})};
	}

	@Override
	public Message[] getUpdateMessage(Entity entity) {
		Controller c = entity.getController();
		if (c == null || !(c instanceof NPC)) {
			return new Message[0];
		}
		NPC npc = (NPC) c;
		Transform prevTransform = npc.getTransformLive();
		Transform newTransform = entity.getTransform();

		int lastX = protocolifyPosition(prevTransform.getPosition().getX());
		int lastY = protocolifyPosition(prevTransform.getPosition().getY());
		int lastZ = protocolifyPosition(prevTransform.getPosition().getZ());

		int newX = protocolifyPosition(newTransform.getPosition().getX());
		int newY = protocolifyPosition(newTransform.getPosition().getY());
		int newZ = protocolifyPosition(newTransform.getPosition().getZ());
		int newYaw = protocolifyRotation(newTransform.getRotation().getYaw());
		int newPitch = protocolifyRotation(newTransform.getRotation().getPitch());

		int deltaX = newX - lastX;
		int deltaY = newY - lastY;
		int deltaZ = newZ - lastZ;

		List<Message> messages = new ArrayList<Message>(3);

		if (npc.needsPositionUpdate() || deltaX > 128 || deltaX < -128 || deltaY > 128 || deltaY < -128 || deltaZ > 128 || deltaZ < -128) {
			messages.add(new EntityTeleportMessage(entity.getId(), newX, newY, newZ, newYaw, newPitch));
			npc.setTransformLive(newTransform);
		} else {
			boolean moved = !prevTransform.getPosition().equals(newTransform.getPosition());
			boolean looked = !prevTransform.getRotation().equals(newTransform.getRotation());
			if (moved) {
				if (looked) {
					messages.add(new EntityRelativePositionRotationMessage(entity.getId(), deltaX, deltaY, deltaZ, newYaw, newPitch));
					npc.setTransformLive(newTransform);
				} else {
					messages.add(new EntityRelativePositionMessage(entity.getId(), deltaX, deltaY, deltaZ));
					npc.setTransformLive(newTransform);
				}
			} else if (looked) {
				messages.add(new EntityRotationMessage(entity.getId(), newYaw, newPitch));
				npc.setTransformLive(newTransform);
			}
		}

		if (npc.needsVelocityUpdate()) {
			messages.add(new EntityVelocityMessage(entity.getId(), npc.getVelocity()));
		}
		return new Message[0];
	}
}
