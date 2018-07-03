package com.therandomlabs.randompatches.asm.transformer;

import com.therandomlabs.randompatches.asm.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class IngameMenuTransformer extends Transformer {
	@Override
	public boolean transform(ClassNode node) {
		final MethodNode method = findMethod(node, "actionPerformed", "a");

		AbstractInsnNode toPatch = null;

		for(int i = 0, frames = 0; i < method.instructions.size() && frames <= 2; i++) {
			final AbstractInsnNode instruction = method.instructions.get(i);

			if(instruction.getType() == AbstractInsnNode.FRAME &&
					((FrameNode) instruction).type == Opcodes.F_SAME) {
				frames++;
			}

			if(frames == 2 && instruction.getType() == AbstractInsnNode.VAR_INSN &&
					instruction.getOpcode() == Opcodes.ISTORE) {
				toPatch = instruction;
				break;
			}
		}

		if(toPatch == null) {
			return false;
		}

		final LabelNode label = new LabelNode();
		final FieldInsnNode getEnabled = new FieldInsnNode(
				Opcodes.GETSTATIC,
				"com/therandomlabs/randompatches/RPStaticConfig",
				"forceTitleScreenOnDisconnect",
				"Z"
		);
		final JumpInsnNode jumpIfNotEnabled = new JumpInsnNode(Opcodes.IFEQ, label);
		final InsnNode loadTrue = new InsnNode(Opcodes.ICONST_1);
		final VarInsnNode storeTrue = new VarInsnNode(Opcodes.ISTORE, 2);

		method.instructions.insert(toPatch, getEnabled);
		method.instructions.insert(getEnabled, jumpIfNotEnabled);
		method.instructions.insert(jumpIfNotEnabled, loadTrue);
		method.instructions.insert(loadTrue, storeTrue);
		method.instructions.insert(storeTrue, label);

		return true;
	}
}
