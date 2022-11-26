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

package com.therandomlabs.randompatches.client;

import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;
import com.therandomlabs.randompatches.RPConfig;
import com.therandomlabs.randompatches.RandomPatches;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

/**
 * Contains key binding-related code for RandomPatches.
 */
public final class RPKeyBindingHandler {
	/**
	 * Contains key bindings added by RandomPatches.
	 * <p>
	 * A separate class in necessary to prevent certain classes from being loaded too early.
	 */
	public static final class KeyBindings {
		/**
		 * The secondary sprint key binding.
		 */
		public static final KeyMapping SECONDARY_SPRINT = new KeyMapping(
				"key.secondarySprint", GLFW.GLFW_KEY_W, "key.categories.movement"
		);

		/**
		 * The dismount key binding.
		 */
		public static final KeyMapping DISMOUNT = new KeyMapping(
				"key.dismount", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories.gameplay"
		);

		/**
		 * The narrator toggle key binding.
		 */
		public static final KeyMapping TOGGLE_NARRATOR = new KeyMapping(
				"key.narrator", ToggleNarratorKeyConflictContext.INSTANCE, KeyModifier.CONTROL,
				InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.misc"
		);

		/**
		 * The pause key binding.
		 */
		public static final KeyMapping PAUSE = new KeyMapping(
				"key.pause", GLFW.GLFW_KEY_ESCAPE, "key.categories.misc"
		);

		/**
		 * The GUI toggle key binding.
		 */
		public static final KeyMapping TOGGLE_GUI = new KeyMapping(
				"key.gui", GLFW.GLFW_KEY_F1, "key.categories.misc"
		);

		/**
		 * The debug key binding.
		 */
		public static final KeyMapping TOGGLE_DEBUG_INFO = new KeyMapping(
				"key.debugInfo", GLFW.GLFW_KEY_F3, "key.categories.misc"
		);

		private static final Minecraft mc = Minecraft.getInstance();

		private KeyBindings() {}

		/**
		 * Handles key events. This should only be called by
		 * {/todo @link com.therandomlabs.randompatches.mixin.client.keybindings.KeyboardListenerMixin}
		 * and {@link com.therandomlabs.randompatches.mixin.client.keybindings.MouseHelperMixin}.
		 *
		 * @param key the key.
		 * @param action the action.
		 * @param scanCode the scan code. If this is a mouse event, this should be
		 * {@link Integer#MIN_VALUE}.
		 */
		public static void onKeyEvent(int key, int action, int scanCode) {
			final RPConfig.KeyBindings config = RandomPatches.config().client.keyBindings;

			if (config.toggleNarrator && action != GLFW.GLFW_RELEASE &&
					TOGGLE_NARRATOR.isConflictContextAndModifierActive() &&
					matches(TOGGLE_NARRATOR, key, scanCode)) {
				Option.NARRATOR.createButton(mc.options, 1, 1, 1); //todo other two ints

				if (mc.screen instanceof SimpleOptionsSubScreen) {
					((SimpleOptionsSubScreen) mc.screen).updateNarratorButton();
				}
			}

			if (config.pause && action != GLFW.GLFW_RELEASE && matches(PAUSE, key, scanCode)) {
				if (mc.screen == null) {
					mc.pauseGame(InputConstants.isKeyDown(
							mc.getWindow().getWindow(), GLFW.GLFW_KEY_F3
					));
				} else if (mc.screen instanceof PauseScreen) {
					mc.screen.onClose();
				}
			}

			if (mc.screen != null && !mc.screen.passEvents) {
				return;
			}

			if (config.toggleGUI && action != GLFW.GLFW_RELEASE &&
					matches(TOGGLE_GUI, key, scanCode)) {
				mc.options.hideGui = !mc.options.hideGui;
			}

			/*if (config.toggleDebugInfo && action == GLFW.GLFW_RELEASE &&
					matches(TOGGLE_DEBUG_INFO, key, scanCode)) {
				if (TOGGLE_DEBUG_INFO.getKey().getType() == InputConstants.Type.KEYSYM &&
						TOGGLE_DEBUG_INFO.getKey().getValue() == GLFW.GLFW_KEY_F3 &&
						mc.keyboardlistener.actionKeyF3) {
					mc.keyboardlistener.actionKeyF3 = false;
				} else {
					mc.options.renderDebug = !mc.options.renderDebug;
					mc.options.renderDebugCharts =
							mc.options.renderDebug && Screen.hasShiftDown();
					mc.options.renderFpsChart =
							mc.options.renderDebug && Screen.hasAltDown();
				}
			}*/
		}

		private static boolean matches(KeyMapping keyBinding, int key, int scanCode) {
			return scanCode == Integer.MIN_VALUE ?
					keyBinding.matchesMouse(key) : keyBinding.matches(key, scanCode);
		}

		private static void register() {
			final RPConfig.KeyBindings config = RandomPatches.config().client.keyBindings;
			final List<String> mixinBlacklist = RandomPatches.config().misc.mixinBlacklist;

			register(SECONDARY_SPRINT, config.secondarySprint());
			register(DISMOUNT, config.dismount());

			if (!mixinBlacklist.contains("KeyboardListener")) {
				register(TOGGLE_NARRATOR, config.toggleNarrator);
				register(PAUSE, config.pause);
				register(TOGGLE_GUI, config.toggleGUI);
				register(TOGGLE_DEBUG_INFO, config.toggleDebugInfo);
			}
		}

		private static void register(KeyMapping keyBinding, boolean enabled) {
			if (enabled) {
				if (!ArrayUtils.contains(mc.options.keyMappings, keyBinding)) {
					mc.options.keyMappings =
							ArrayUtils.add(mc.options.keyMappings, keyBinding);
				}
			} else {
				final int index = ArrayUtils.indexOf(mc.options.keyMappings, keyBinding);

				if (index != ArrayUtils.INDEX_NOT_FOUND) {
					mc.options.keyMappings =
							ArrayUtils.remove(mc.options.keyMappings, index);
				}
			}
		}
	}

	private static final class ToggleNarratorKeyConflictContext implements IKeyConflictContext {
		private static final ToggleNarratorKeyConflictContext INSTANCE =
				new ToggleNarratorKeyConflictContext();

		@Override
		public boolean isActive() {
			final Screen screen = KeyBindings.mc.screen;
			return screen == null || !(screen.getFocused() instanceof EditBox) ||
					!((EditBox) screen.getFocused()).canConsumeInput();
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return true;
		}
	}

	private static boolean enabled;

	private RPKeyBindingHandler() {}

	/**
	 * Enables this class's functionality if it has not already been enabled.
	 */
	public static void enable() {
		if (FMLEnvironment.dist == Dist.CLIENT && !enabled) {
			enabled = true;
			onConfigReload();
		}
	}

	/**
	 * Called by {@link com.therandomlabs.randompatches.RPConfig.KeyBindings} when the RandomPatches
	 * configuration is reloaded.
	 */
	public static void onConfigReload() {
		if (FMLEnvironment.dist == Dist.CLIENT && enabled) {
			KeyBindings.register();
		}
	}
}
