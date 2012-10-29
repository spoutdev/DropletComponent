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
package org.spout.droplet.component.command;

import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.NestedCommand;
import org.spout.api.exception.CommandException;

import org.spout.droplet.component.DropletComponent;

public class DropletCommand {
	private final DropletComponent plugin;

	public DropletCommand(DropletComponent plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"droplet"}, usage = "", desc = "Access droplet commands", min = 1, max = 1)
	@NestedCommand(DropletCommands.class)
	public void droplet(CommandContext args, CommandSource source) throws CommandException {
	}
}
