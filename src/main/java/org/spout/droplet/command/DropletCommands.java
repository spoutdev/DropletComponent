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
package org.spout.droplet.command;

import java.util.List;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.entity.controller.type.ControllerRegistry;
import org.spout.api.entity.controller.type.ControllerType;
import org.spout.api.exception.CommandException;
import org.spout.api.inventory.ItemStack;

import org.spout.droplet.DropletComponentPlugin;
import org.spout.droplet.entity.controller.NPC;

import org.spout.vanilla.material.VanillaMaterials;

public class DropletCommands {
	private final DropletComponentPlugin plugin;

	public DropletCommands(DropletComponentPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"spawn"}, usage = "<controllertype>", desc = "Spawn a controller!", min = 1, max = 1)
	@CommandPermissions("droplet.command.spawn")
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Must be in-game to spawn a controller!");
		}
		Player spawner = (Player) source;
		ControllerType spawning = ControllerRegistry.get(args.getString(0));
		if (spawning == null) {
			throw new CommandException("The type " + args.getString(0) + " was not found in the Controller Registry!");
		}
		if (!spawning.canCreateController()) {
			throw new CommandException("Cannot create the controller!");
		}
		Controller controller = spawning.createController();
		if (controller instanceof NPC) {
			((NPC) controller).setName("Spouty");
			((NPC) controller).setHeldItem(new ItemStack(VanillaMaterials.DIAMOND_SWORD, 1));
		}
		spawner.getWorld().createAndSpawnEntity(spawner.getPosition(), controller);
		source.sendMessage(new ChatArguments("[", ChatStyle.BLUE, plugin.getName(), ChatStyle.WHITE, "] Spawned a " + spawning.getName() + " controller."));
	}

	@Command(aliases = {"despawn"}, usage = "<controllertype>", desc = "Despawn a controller!", min = 1, max = 1)
	@CommandPermissions("droplet.command.despawn")
	public void despawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Must be in-game to despawn a controller!");
		}
		Player despawner = (Player) source;
		ControllerType despawning = ControllerRegistry.get(args.getString(0));
		List<Entity> all = despawner.getWorld().getAll();
		int count = 0;
		for (Entity e : all) {
			if (e.getController().getType().equals(despawning)) {
				e.kill();
				count++;
			}
		}
		source.sendMessage(new ChatArguments("[", ChatStyle.BLUE, plugin.getName(), ChatStyle.WHITE, "] Despawned " + count + " controller(s) " +
				"of type: " + despawning.getName() + "."));
	}
}
