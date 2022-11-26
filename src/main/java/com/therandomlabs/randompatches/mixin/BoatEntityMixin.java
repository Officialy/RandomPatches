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

package com.therandomlabs.randompatches.mixin;

import com.therandomlabs.randompatches.RandomPatches;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public final class BoatEntityMixin {
    @Shadow
    private Boat.Status status;

    @Shadow
    private float outOfControlTicks;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo info) {
        if (status == Boat.Status.UNDER_FLOWING_WATER) {
            final Vec3 motion = ((Entity) (Object) this).getDeltaMovement();
            ((Entity) (Object) this).setDeltaMovement(
                    motion.x,
                    motion.y + 0.0007 + RandomPatches.config().misc.boatBuoyancyUnderFlowingWater,
                    motion.z
            );
        }
    }

    @ModifyConstant(method = {"tick", "interact"}, constant = @Constant(floatValue = 60.0F)) //was processInitialInteract
    private float getUnderwaterBoatPassengerEjectionDelay(float delay) {
        final int newDelay = RandomPatches.config().misc.underwaterBoatPassengerEjectionDelayTicks;
        return newDelay == -1 ? Float.MAX_VALUE : newDelay;
    }

   /* @Redirect(method = "updateFallState", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/item/BoatEntity;" +
            "status:Lnet/minecraft/entity/item/BoatEntity$Status;"))
    private Boat.Status getLocation(Boat boat) {
        return RandomPatches.config().misc.bugFixes.fixBoatFallDamage ?
                Boat.Status.ON_LAND : status;
    }*/
}
