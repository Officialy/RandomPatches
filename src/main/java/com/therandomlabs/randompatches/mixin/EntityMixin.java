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
 *//*


package com.therandomlabs.randompatches.mixin;

import com.therandomlabs.randompatches.RandomPatches;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract AABB getBoundingBox();

	@Shadow
	public abstract void setBoundingBox(AABB boundingBox);

	@Shadow
	public abstract boolean isInWater();

	@Shadow
	protected abstract boolean isInRain();

	@Shadow
	public Level level;

	@Shadow
	public abstract BlockPos getBlockPos();

	@Shadow
	public abstract Level getEntityWorld();

	@Inject(method = "isWet", at = @At("HEAD"), cancellable = true)
	private void isWet(CallbackInfoReturnable<Boolean> info) {
		if (RandomPatches.config().misc.bugFixes.fixEntitiesNotBeingConsideredWetInCauldrons) {
			info.setReturnValue(isInWater() || isInRain() || isInCauldronFilledWithWater());
		}
	}

	@Inject(method = "writeWithoutTypeId", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.getMotion()" +
					"Lnet/minecraft/util/math/vector/Vector3d;"))
	private void writeWithoutTypeId(CompoundTag compound, CallbackInfoReturnable<CompoundTag> info) {
		if (!RandomPatches.config().misc.bugFixes.fixMC2025) {
			return;
		}

		final AABB boundingBox = getBoundingBox();
		final ListTag boundingBoxList = new ListTag();

		//Because of floating point precision errors, the bounding box of an entity can be
		//calculated as smaller than the expected value. When the entity is saved then reloaded, the
		//bounding box may be recomputed such that it intersects a wall.
		//To counter this, we store the bounding box when an entity is saved, then use the same
		//bounding box when it is loaded.
		//See: https://redd.it/8pgd4q
		boundingBoxList.add(DoubleTag.valueOf(boundingBox.minX));
		boundingBoxList.add(DoubleTag.valueOf(boundingBox.minY));
		boundingBoxList.add(DoubleTag.valueOf(boundingBox.minZ));
		boundingBoxList.add(DoubleTag.valueOf(boundingBox.maxX));
		boundingBoxList.add(DoubleTag.valueOf(boundingBox.maxY));
		boundingBoxList.add(DoubleTag.valueOf(boundingBox.maxZ));

		compound.put("BoundingBox", boundingBoxList);
	}

	@Inject(method = "read", at = @At("TAIL"))
	private void read(CompoundTag compound, CallbackInfo info) {
		if (!RandomPatches.config().misc.bugFixes.fixMC2025 || !compound.contains("BoundingBox")) {
			return;
		}

		final ListTag boundingBoxList = compound.getList("BoundingBox", Constants.NBT.TAG_DOUBLE);

		setBoundingBox(new AABB(
				boundingBoxList.getDouble(0),
				boundingBoxList.getDouble(1),
				boundingBoxList.getDouble(2),
				boundingBoxList.getDouble(3),
				boundingBoxList.getDouble(4),
				boundingBoxList.getDouble(5)
		));
	}

	@Unique
	private boolean isInCauldronFilledWithWater() {
		final BlockState state = getEntityWorld().getBlockState(getBlockPos());
		//This will need to be changed in 1.17 to make sure that it's water.
		return state.isIn(Blocks.CAULDRON) && state.get(CauldronBlock.LEVEL) > 0;
	}
}
*/
