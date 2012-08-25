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
package org.spout.droplet.entity.controller;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.data.Data;
import org.spout.api.entity.BasicController;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

import org.spout.droplet.entity.DropletControllerTypes;

public class NPC extends BasicController {
	private final Transform transformLive = new Transform();
	//Movement
	private Vector3 maxSpeed = new Vector3(0.4, 0.4, 0.4);
	private Vector3 velocity = Vector3.ZERO;
	private int positionTicks = 0, velocityTicks = 0;
	//
	private String name;
	//
	private ItemStack heldItem;

	public NPC() {
		super(DropletControllerTypes.NPC);
	}

	@Override
	public void onAttached() {
		if (name.isEmpty()) {
			name = getDataMap().get(Data.TITLE);
		}
		if (heldItem == null) {
			heldItem = getDataMap().get(Data.HELD_ITEM);
		}
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);

		this.setVelocity(this.getVelocity().subtract(0, 0.04, 0));
		getParent().translate(MathHelper.min(getVelocity(), maxSpeed));
		this.setVelocity(this.getVelocity().multiply(0.98));

		//TODO Remove when collisions are in
		Block below = getParent().getWorld().getBlock(getParent().getPosition().subtract(0.0, 0.2, 0.0), getParent());
		if (below.getMaterial().isSolid()) {
			this.setVelocity(this.getVelocity().multiply(0.7, 0.0, 0.7).add(0.0, 0.06, 0.0));
		}
		positionTicks++;
		velocityTicks++;
	}

	@Override
	public void onInteract(Entity entity, Action action) {
		if (action == Action.RIGHT_CLICK) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				player.sendMessage(new ChatArguments("<", ChatStyle.BLUE, name, ChatStyle.WHITE, "> You touched me you dirty person!"));
			}
		} else if (action == Action.LEFT_CLICK) {
			//TODO Health
		}
	}

	@Override
	public boolean isSavable() {
		return true;
	}

	@Override
	public void onSave() {
		super.onSave();
		getDataMap().put(Data.TITLE, name);
		getDataMap().put(Data.HELD_ITEM, heldItem);
	}

	public Transform getTransformLive() {
		return transformLive;
	}

	public void setTransformLive(Transform live) {
		if (live == null) {
			throw new NullPointerException("Live transform cannot be null!");
		}
	}

	public Vector3 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	public boolean needsVelocityUpdate() {
		return velocityTicks % 5 == 0;
	}

	public boolean needsPositionUpdate() {
		return positionTicks % 30 == 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHeldItem(ItemStack heldItem) {
		this.heldItem = heldItem;
	}

	public ItemStack getHeldItem() {
		return heldItem;
	}
}
