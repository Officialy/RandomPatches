///*
// * The MIT License (MIT)
// *
// * Copyright (c) 2020-2021 TheRandomLabs
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of
// * this software and associated documentation files (the "Software"), to deal in
// * the Software without restriction, including without limitation the rights to
// * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
// * the Software, and to permit persons to whom the Software is furnished to do so,
// * subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
//
//package com.therandomlabs.randompatches.world;
//
//import java.util.Random;
//import java.util.UUID;
//
//import com.therandomlabs.randompatches.RandomPatches;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.chunk.ChunkAccess;
//import net.minecraft.world.level.chunk.LevelChunk;
//import net.minecraftforge.event.world.ChunkEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
///**
// * Handles the duplicate entity UUID fix.
// */
//@Mod.EventBusSubscriber(modid = RandomPatches.MOD_ID)
//public final class DuplicateEntityUUIDFixHandler {
//	private static final Random random = new Random();
//
//	private DuplicateEntityUUIDFixHandler() {}
//
//	@SubscribeEvent
//	public static void onChunkLoad(ChunkEvent.Load event) {
//		if (!RandomPatches.config().misc.bugFixes.fixDuplicateEntityUUIDs ||
//				event.getWorld().isClientSide()) {
//			return;
//		}
//
//		final ServerLevel world = (ServerLevel) event.getWorld();
//		final LevelChunk chunk = (LevelChunk) event.getChunk();
//
//		//Fix found by CAS_ual_TY:
//		//https://www.curseforge.com/minecraft/mc-mods/deuf-duplicate-entity-uuid-fix
//		for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists()) {
//			for (Entity entity : entityList) {
//				if (entity instanceof Player) {
//					continue;
//				}
//
//				final UUID uniqueID = entity.getUUID();
//
//				if (world.getEntityByUuid(uniqueID) == entity) {
//					continue;
//				}
//
//				UUID newUniqueID;
//
//				do {
//					newUniqueID = MathHelper.getRandomUUID(random);
//				} while (world.getEntityByUuid(newUniqueID) != null);
//
//				entity.setUniqueId(uniqueID);
//
//				if (RandomPatches.config().misc.bugFixes.logFixedDuplicateEntityUUIDs) {
//					RandomPatches.logger.info(
//							"Changing UUID of duplicate entity {} from {} to {}",
//							entity.getType().getRegistryName(), uniqueID, newUniqueID
//					);
//				}
//			}
//		}
//	}
//}
