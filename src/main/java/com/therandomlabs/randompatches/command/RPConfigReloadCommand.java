/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020-2021 TheRandomLabs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.therandomlabs.randompatches.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.therandomlabs.randompatches.RandomPatches;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

/**
 * The command that reloads the RandomPatches configuration.
 */
public final class RPConfigReloadCommand {
	private RPConfigReloadCommand() {}

	/**
	 * Registers the command that reloads the RandomPatches configuration.
	 *
	 * @param dispatcher the {@link CommandDispatcher}.
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		final String name = RandomPatches.config().misc.configReloadCommand;

		if (!name.isEmpty()) {
			dispatcher.register(
					LiteralArgumentBuilder.<CommandSourceStack>literal(name).
							requires(source -> source.hasPermission(4)).
							executes(context -> execute(context.getSource()))
			);
		}
	}

	private static int execute(CommandSourceStack source) {
		RandomPatches.reloadConfig();
		source.sendSuccess(new TextComponent("RandomPatches configuration reloaded!"), true);
		return Command.SINGLE_SUCCESS;
	}
}
