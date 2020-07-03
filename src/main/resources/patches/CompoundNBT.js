var Opcodes = Java.type("org.objectweb.asm.Opcodes");

function log(message) {
	print("[RandomPatches CompoundNBT Transformer]: " + message);
}

function patch(method, name, patchFunction) {
	if (method.name != name) {
		return false;
	}

	log("Patching method: " + name + " (" + method.name + ")");
	patchFunction(method.instructions);
	return true;
}

function initializeCoreMod() {
	return {
		"RandomPatches CompoundNBT Transformer": {
			"target": {
				"type": "CLASS",
				"name": "net.minecraft.nbt.CompoundNBT"
			},
			"transformer": function(classNode) {
				log("Transforming class: " + classNode.name);

				var methods = classNode.methods;

				for (var i in methods) {
					if (patch(methods[i], "equals", patchEquals)) {
						break;
					}
				}

				return classNode;
			}
		}
	};
}

function patchEquals(instructions) {
	var equals;

	for (var i = 0; i < instructions.size(); i++) {
		var instruction = instructions.get(i);

		if (instruction.getOpcode() == Opcodes.INVOKESTATIC && instruction.name == "equals") {
			equals = instruction;
			break;
		}
	}

	//Call CompoundNBTHook#areTagMapsEqual
	equals.owner = "com/therandomlabs/randompatches/hook/CompoundNBTHook";
	equals.name = "areTagMapsEqual";
	equals.desc = "(Ljava/util/Map;Ljava/util/Map;)Z";
}
