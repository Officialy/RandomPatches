package com.therandomlabs.randompatches.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import com.therandomlabs.randomlib.TRLUtils;
import com.therandomlabs.randomlib.config.Config;
import com.therandomlabs.randompatches.RandomPatches;
import com.therandomlabs.randompatches.client.WindowIconHandler;
import com.therandomlabs.randompatches.patch.packetsize.NettyCompressionDecoderPatch;
import org.lwjgl.opengl.Display;

@Config(RandomPatches.MOD_ID)
public final class RPConfig {
	public static final class Boats {
		@Config.RequiresMCRestart
		@Config.Property("Whether to patch EntityBoat.")
		public static boolean patchEntityBoat = true;

		@Config.Property(
				"Prevents underwater boat passengers from being ejected after 60 ticks " +
						"(3 seconds)."
		)
		public static boolean preventUnderwaterBoatPassengerEjection =
				RandomPatches.IS_DEOBFUSCATED;

		@Config.Property({
				"The buoyancy of boats when they are under flowing water.",
				"The vanilla default is -0.0007."
		})
		public static double underwaterBoatBuoyancy = RandomPatches.IS_DEOBFUSCATED ? 5.0 : 0.023;
	}

	public static final class Client {
		@Config.Category("Options related to the Minecraft window.")
		public static final Window window = null;

		@Config.RequiresMCRestart
		@Config.Property("Adds a separate keybind for dismounting.")
		public static boolean dismountKeybind = TRLUtils.MC_VERSION_NUMBER > 8;

		@Config.RequiresMCRestart
		@Config.Property("Speeds up language switching.")
		public static boolean fastLanguageSwitch = true;

		@Config.Property(
				"Forces Minecraft to show the title screen after disconnecting rather than " +
						"the Multiplayer or Realms menu."
		)
		public static boolean forceTitleScreenOnDisconnect = RandomPatches.IS_DEOBFUSCATED;

		@Config.RequiresMCRestart
		@Config.RangeDouble(min = Double.MIN_VALUE, max = 260.0)
		@Config.Property({
				"The framerate limit slider step size.",
				"If this is set to 10.0, vanilla behavior is not changed."
		})
		public static float framerateLimitSliderStepSize = 1.0F;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresMCRestart
		@Config.Property(
				"Whether to fix the player model occasionally disappearing when flying with " +
						"elytra in a straight line in third-person mode."
		)
		public static boolean invisiblePlayerModelFix = true;

		@Config.MCVersion("[1.12,1.13)")
		@Config.RequiresMCRestart
		@Config.Property("Whether to add the Toggle Narrator keybind to the controls.")
		public static boolean narratorKeybind = true;

		@Config.RequiresMCRestart
		@Config.Property(
				"Set this to false to disable the Minecraft class patches " +
						"(the Toggle Narrator keybind and custom window title/icon)."
		)
		public static boolean patchMinecraftClass = true;

		@Config.RequiresMCRestart
		@Config.Property(
				"Set this to false to force disable the \"force title screen on disconnect\" " +
						"patch."
		)
		public static boolean patchTitleScreenOnDisconnect = true;

		@Config.RequiresMCRestart
		@Config.Property(
				"Whether to apply the potion glint patch so that the potion glowing effect can " +
						"be toggled."
		)
		public static boolean patchPotionGlint = true;

		@Config.Property("Whether to remove the glowing effect from potions.")
		public static boolean removePotionGlint = RandomPatches.IS_DEOBFUSCATED;

		@Config.RequiresMCRestart
		@Config.Property(
				"Backports the smooth eye level change animations from Minecraft 1.13 and newer."
		)
		public static boolean patchSmoothEyeLevelChanges = true;

		@Config.Property("Whether smooth eye level change animations should be enabled.")
		public static boolean smoothEyeLevelChanges = true;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresWorldReload
		@Config.Property("Enables the /rpreloadclient command.")
		public static boolean rpreloadclient = true;

		public static boolean isDismountKeybindEnabled() {
			return dismountKeybind && !RandomPatches.UNRIDE_KEYBIND_INSTALLED && TRLUtils.IS_CLIENT;
		}

		public static boolean isNarratorKeybindEnabled() {
			return narratorKeybind && TRLUtils.MC_VERSION_NUMBER > 11 &&
					!RandomPatches.REBIND_INSTALLED && !RandomPatches.REBIND_NARRATOR_INSTALLED &&
					TRLUtils.IS_CLIENT;
		}
	}

	public static final class Misc {
		@Config.MCVersion("[1.11,1.13)")
		@Config.RequiresMCRestart
		@Config.Property({
				"Whether to prevent the observer from emitting a signal when it is placed.",
				"This fixes MC-109832."
		})
		public static boolean disableObserverSignalOnPlace = true;

		@Config.MCVersion("[1.11,1.13)")
		@Config.RequiresMCRestart
		@Config.Property({
				"Whether to fix dismount positions being too high.",
				"This fixes MC-3328 and MC-111726."
		})
		public static boolean dismountPositionFix = true;

		@Config.MCVersion("[1.11,1.13)")
		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes the End portal and End gateway break particle textures and " +
						"improves End portal rendering."
		)
		public static boolean endPortalTweaks = true;

		@Config.MCVersion("[1.10,1.13)")
		@Config.RequiresMCRestart
		@Config.Property(
				"Whether to patch WorldServer to prevent a \"TickNextTick list out of synch\" " +
						"IllegalStateException."
		)
		public static boolean fixTickNextTickListOutOfSynch = true;

		@Config.MCVersion("[1.10,1.13)")
		@Config.RequiresMCRestart
		@Config.Property({
				"Fixes MC-2025.",
				"More information can be found here: " +
						"https://www.reddit.com/r/Mojira/comments/8pgd4q/final_and_proper_fix_to_" +
						"mc2025_simple_reliable/"
		})
		public static boolean mc2025Fix = true;

		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes MC-64836, which causes non-player entities to be allowed to control " +
						"minecarts using their AI."
		)
		public static boolean minecartAIFix = true;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes MC-5694, which causes fast mining to sometimes only destroy blocks " +
						"client-side only."
		)
		public static boolean miningGhostBlocksFix = true;

		@Config.MCVersion("[1.10,1.13)")
		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes MC-10369 (server-side particle spawning not creating particles for " +
						"clients) and MC-93826 (breeding hearts only showing once instead of all " +
						"of the time an animal can breed)."
		)
		public static boolean particleFixes = true;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresMCRestart
		@Config.Property({
				"Set this to false to disable the NetHandlerPlayServer patches " +
						"(the speed limits and disconnect timeouts)."
		})
		public static boolean patchNetHandlerPlayServer = true;

		@Config.RequiresMCRestart
		@Config.Property("Whether to patch the packet size limit.")
		public static boolean patchPacketSizeLimit = true;

		@Config.RangeInt(min = 257)
		@Config.Property({
				"The packet size limit.",
				"The vanilla limit is " + NettyCompressionDecoderPatch.VANILLA_LIMIT + "."
		})
		public static int packetSizeLimit = 0x1000000;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes MC-54026, which causes blocks attached to slime blocks in some " +
						"circumstances to create ghost blocks if a piston pushes the slime block."
		)
		public static boolean pistonGhostBlocksFix = true;

		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes MC-11944, which allows players to replace End portals, " +
						"End gateways and Nether portals using buckets."
		)
		public static boolean portalBucketReplacementFix = true;

		@Config.Property("Enables the portal bucket replacement fix for Nether portals.")
		public static boolean portalBucketReplacementFixForNetherPortals;

		@Config.RequiresMCRestart
		@Config.Property(
				"Fixes MC-129057, which prevents ingredients with NBT data from being " +
						"transferred to the crafting grid when a recipe is clicked in the " +
						"recipe book."
		)
		public static boolean recipeBookNBTFix = true;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresWorldReload
		@Config.Property("Enables the /rpreload command.")
		public static boolean rpreload = true;

		@Config.RequiresMCRestart
		@Config.Property("Fixes player skull stacking.")
		public static boolean skullStackingFix = true;

		@Config.Property(
				"Whether skull stacking requires the same textures or just the same player " +
						"profile."
		)
		public static boolean skullStackingRequiresSameTextures = true;

		public static long packetSizeLimitLong;

		public static boolean areEndPortalTweaksEnabled() {
			return endPortalTweaks && TRLUtils.MC_VERSION_NUMBER > 10 && TRLUtils.IS_CLIENT;
		}

		public static boolean isRecipeBookNBTFixEnabled() {
			return recipeBookNBTFix && TRLUtils.MC_VERSION_NUMBER > 11 &&
					!RandomPatches.VANILLAFIX_INSTALLED;
		}

		public static boolean isObserverSignalFixEnabled() {
			return disableObserverSignalOnPlace && TRLUtils.MC_VERSION_NUMBER > 10 &&
					!RandomPatches.EIGENCRAFT_INSTALLED;
		}

		public static void onReload() {
			packetSizeLimitLong = packetSizeLimit;
		}
	}

	public static final class SpeedLimits {
		@Config.RangeDouble(min = 1.0)
		@Config.Property({
				"The maximum player speed.",
				"The vanilla default is 100.0."
		})
		public static float maxPlayerSpeed = 1000000.0F;

		@Config.RangeDouble(min = 1.0)
		@Config.Property({
				"The maximum player elytra speed.",
				"The vanilla default is 300.0."
		})
		public static float maxPlayerElytraSpeed = 1000000.0F;

		@Config.RangeDouble(min = 1.0)
		@Config.Property({
				"The maximum player vehicle speed.",
				"The vanilla default is 100.0."
		})
		public static double maxPlayerVehicleSpeed = 1000000.0;
	}

	public static final class Timeouts {
		@Config.RangeInt(min = 1)
		@Config.Property("The interval at which the server sends the KeepAlive packet.")
		public static int keepAlivePacketInterval = 15;

		@Config.RangeInt(min = 1)
		@Config.Property("The login timeout.")
		public static int loginTimeout = 900;

		@Config.MCVersion("[1.9,1.13)")
		@Config.RequiresMCRestart
		@Config.Property("Whether to apply the login timeout.")
		public static boolean patchLoginTimeout = true;

		@Config.MCVersion("[1.12,1.13)")
		@Config.RangeInt(min = 1)
		@Config.Property({
				"The read timeout.",
				"This is the time it takes for a player to be disconnected after not " +
						"responding to a KeepAlive packet.",
				"This value is automatically rounded up to a product of keepAlivePacketInterval."
		})
		public static int readTimeout = 90;

		@Config.RequiresMCRestart
		@Config.Property("Whether to patch NetworkManager to apply the client-sided read timeout.")
		public static boolean patchNetworkManager = true;

		public static long keepAlivePacketIntervalMillis;
		public static long keepAlivePacketIntervalLong;
		public static long readTimeoutMillis;

		public static void onReload() {
			if (readTimeout < keepAlivePacketInterval) {
				readTimeout = keepAlivePacketInterval * 2;
			} else if (readTimeout % keepAlivePacketInterval != 0) {
				readTimeout =
						keepAlivePacketInterval * (readTimeout / keepAlivePacketInterval + 1);
			}

			keepAlivePacketIntervalMillis = keepAlivePacketInterval * 1000L;
			keepAlivePacketIntervalLong = keepAlivePacketInterval;
			readTimeoutMillis = readTimeout * 1000L;

			System.setProperty("fml.readTimeout", Integer.toString(readTimeout));
			System.setProperty("fml.loginTimeout", Integer.toString(loginTimeout));
		}
	}

	public static final class Window {
		public static final Path DEFAULT_ICON = Paths.get(
				RandomPatches.IS_DEOBFUSCATED ?
						"../src/main/resources/assets/randompatches/logo.png" : ""
		);

		@Config.Property({
				"The path to the 16x16 Minecraft window icon.",
				"Leave this and the 32x32 icon blank to use the default icon."
		})
		public static Path icon16 = DEFAULT_ICON;

		@Config.Property({
				"The path to the 32x32 Minecraft window icon.",
				"Leave this and the 16x16 icon blank to use the default icon."
		})
		public static Path icon32 = DEFAULT_ICON;

		@Config.Property({
				"The path to the 256x256 window icon which is used on Mac OS X.",
				"Leave this, the 16x16 icon and the 32x32 icon blank to use the default icon."
		})
		public static Path icon256 = DEFAULT_ICON;

		@Config.Property("The Minecraft window title.")
		public static String title = RandomPatches.IS_DEOBFUSCATED ?
				RandomPatches.NAME : RandomPatches.DEFAULT_WINDOW_TITLE;

		public static String icon16String;
		public static String icon32String;
		public static String icon256String;

		//For some reason the game freezes if the window title or icon is changed while in-game,
		//but not in the configuration GUI
		public static boolean setWindowSettings = true;

		public static void onReload() {
			icon16String = icon16.toString();
			icon32String = icon32.toString();
			icon256String = icon256.toString();

			if (icon16String.isEmpty()) {
				if (!icon256String.isEmpty()) {
					icon16 = icon256;
					icon16String = icon256String;
				} else if (!icon32String.isEmpty()) {
					icon16 = icon32;
					icon16String = icon32String;
				}
			}

			if (icon32String.isEmpty()) {
				if (!icon256String.isEmpty()) {
					icon32 = icon256;
					icon32String = icon256String;
				} else if (!icon16String.isEmpty()) {
					icon32 = icon16;
					icon32String = icon16String;
				}
			}

			if (icon256String.isEmpty()) {
				if (!icon32String.isEmpty()) {
					icon256 = icon32;
					icon256String = icon32String;
				} else if (!icon16String.isEmpty()) {
					icon256 = icon16;
					icon256String = icon16String;
				}
			}
		}

		public static void onReloadClient() {
			if (!Display.isCreated() || !setWindowSettings) {
				return;
			}

			WindowIconHandler.setWindowIcon();
			Display.setTitle(title);
		}
	}

	@Config.MCVersion("[1.9,1.13)")
	@Config.Category("Options related to boats.")
	public static final Boats boats = null;

	@Config.Category("Options related to client-sided features.")
	public static final Client client = null;

	@Config.Category("Options that don't fit into any other categories.")
	public static final Misc misc = null;

	@Config.Category("Options related to the movement speed limits.")
	public static final SpeedLimits speedLimits = null;

	@Config.Category("Options related to the disconnect timeouts.")
	public static final Timeouts timeouts = null;
}
