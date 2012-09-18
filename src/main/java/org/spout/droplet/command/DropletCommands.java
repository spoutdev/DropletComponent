/*
 * This file is part of DropletComponent.
 *
 * Copyright (c) 2012, SpoutDev <http://www.spout.org/>
 * DropletComponent is licensed under the SpoutDev License Version 1.
 *
 * DropletComponent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * DropletComponent is distributed in the hope that it will be useful,
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
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import org.spout.droplet.DropletComponentPlugin;
import org.spout.droplet.components.living.DropletHuman;

import org.spout.vanilla.component.substance.MovingBlock;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.material.VanillaMaterials;

public class DropletCommands {
	private final DropletComponentPlugin plugin;

	public DropletCommands(DropletComponentPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"spawn"}, usage = "<component>", desc = "Spawn a controller!", min = 1, max = 1)
	@CommandPermissions("droplet.command.spawn")
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Must be in-game to spawn an entity!");
		}
		Player spawner = (Player) source;
		String component = args.getString(0);
		Entity entity = spawner.getWorld().createEntity(spawner.getTransform().getPosition(), null);
		String name = "";
		if (component.equalsIgnoreCase("npc")) {
			DropletHuman human = entity.add(DropletHuman.class);
			name = human.getClass().getName();
			human.setGamemode(GameMode.SURVIVAL);
		} else if (component.equalsIgnoreCase("moving")) {
			MovingBlock block = entity.add(MovingBlock.class);
			name = block.getClass().getName();
			block.setMaterial(VanillaMaterials.OBSIDIAN);
		}
		spawner.getTransform().getPosition().getWorld().spawnEntity(entity);
		source.sendMessage(new ChatArguments("[", ChatStyle.BLUE, plugin.getName(), ChatStyle.WHITE, "] Spawned an entity with a " + name + " component."));
	}
}
