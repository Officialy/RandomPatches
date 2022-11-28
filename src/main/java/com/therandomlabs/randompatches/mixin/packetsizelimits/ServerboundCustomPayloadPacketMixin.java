package com.therandomlabs.randompatches.mixin.packetsizelimits;

import com.therandomlabs.randompatches.RandomPatches;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerboundCustomPayloadPacket.class)
public class ServerboundCustomPayloadPacketMixin {

    @ModifyConstant(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", constant = @Constant(intValue = 32767))
    private int injected(int value) {
        return RandomPatches.config().packetSizeLimits.maxCustomPayloadPacketSize;
    }
}
