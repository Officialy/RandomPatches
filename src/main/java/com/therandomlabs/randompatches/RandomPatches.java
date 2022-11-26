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

package com.therandomlabs.randompatches;

import com.therandomlabs.randompatches.client.CauldronWaterTranslucencyHandler;
import com.therandomlabs.randompatches.client.RPContributorCapeHandler;
import com.therandomlabs.randompatches.client.RPKeyBindingHandler;
import com.therandomlabs.randompatches.command.RPConfigReloadCommand;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The main class for RandomPatches.
 */
@Mod.EventBusSubscriber
@Mod(RandomPatches.MOD_ID)
public final class RandomPatches {
	/**
	 * The RandomPatches mod ID.
	 */
	public static final String MOD_ID = "randompatches";

	/**
	 * The RandomPatches logger. This should only be used by RandomPatches.
	 */
	public static final Logger logger = LogManager.getLogger(MOD_ID);

	@SuppressWarnings("PMD.NonThreadSafesingleton")
	@Nullable
	private static Toml4jConfigSerializer<RPConfig> serializer;

	/**
	 * Constructs a {@link RandomPatches} instance.
	 */
	public RandomPatches() throws ConfigSerializer.SerializationException {
		if (ModList.get().isLoaded("cloth-config")) {
//	todo		ModLoadingContext.get().registerExtensionPoint(
//					ConfigGuiHandler.getGuiFactoryFor((screen) -> AutoConfig.getConfigScreen(RPConfig.class, screen).get())
//			);
		}

		CauldronWaterTranslucencyHandler.enable();
		RPKeyBindingHandler.enable();

		if (RandomPatches.config().client.contributorCapes) {
			RPContributorCapeHandler.downloadContributorList();
		}
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		RPConfigReloadCommand.register(event.getDispatcher());
	}

	/**
	 * Returns the RandomPatches configuration.
	 *
	 * @return an {@link RPConfig} object.
	 */
	public static RPConfig config() {
		if (serializer == null) {
			reloadConfig();
		}

		return serializer.createDefault(); //could be deserialize
	}

	/**
	 * Reloads the RandomPatches configuration from disk.
	 */
	public static void reloadConfig()  {
		if (serializer == null) {
			AutoConfig.register(RPConfig.class, (definition, configClass) -> {
				serializer = new Toml4jConfigSerializer<>(definition, configClass);
				return serializer;
			});
		} else {
			serializer.createDefault(); //was reloadFromDisk
		}
	}
}
