/*
 * This file is part of DropletComponent.
 *
 * Copyright (c) 2012, Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.spout.droplet.component.command;

import org.spout.api.chat.ChatArguments;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import org.spout.droplet.component.DropletComponent;

import org.spout.vanilla.api.data.GameMode;

import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.substance.object.FallingBlock;
import org.spout.vanilla.plugin.material.VanillaMaterials;

public class DropletCommands {
	private final DropletComponent plugin;

	public DropletCommands(DropletComponent plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"spawn"}, usage = "<component>", desc = "Spawn an Entity!", min = 1, max = 1)
	@CommandPermissions("droplet.command.spawn")
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Must be in-game to spawn an entity!");
		}
		final Player spawner = (Player) source;
		final String component = args.getString(0);
		Entity entity;
		String name;
		if (component.equalsIgnoreCase("npc")) {
			entity = spawner.getWorld().createEntity(spawner.getScene().getPosition(), Human.class);
			final Human human = entity.get(Human.class);
			name = human.getClass().getName();
			human.setGamemode(GameMode.SURVIVAL);
		} else if (component.equalsIgnoreCase("moving")) {
			entity = spawner.getWorld().createEntity(spawner.getScene().getPosition(), FallingBlock.class);
			final FallingBlock falling = spawner.get(FallingBlock.class);
			falling.setMaterial(VanillaMaterials.END_STONE);
			name = VanillaMaterials.END_STONE.getName();
		} else {
			source.sendMessage(new ChatArguments("[", ChatStyle.BLUE, plugin.getName(), ChatStyle.WHITE, "] Component " + component + " can't be spawned."));
			return;
		}
		spawner.getScene().getPosition().getWorld().spawnEntity(entity);
		source.sendMessage(new ChatArguments("[", ChatStyle.BLUE, plugin.getName(), ChatStyle.WHITE, "] Spawned an entity with a " + name + " component."));
	}
}
