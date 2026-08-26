package meldexun.betterconfig.asm;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.ClassNodeClassTransformer;
import meldexun.asmutil2.ClassNodeTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class LoadEarlyClassTransformer extends ClassNodeClassTransformer implements IClassTransformer {

	@Override
	protected List<ClassNodeTransformer> getClassNodeTransformers(String className) {
		return Collections.singletonList(new ClassNodeTransformer() {
			@Override
			public boolean transform(ClassNode classNode) {
				if (classNode.visibleAnnotations == null
						|| classNode.visibleAnnotations.stream().noneMatch(annotation -> annotation.desc.equals("Lmeldexun/betterconfig/api/BetterConfig;"))
						|| classNode.visibleAnnotations.stream().noneMatch(annotation -> annotation.desc.equals("Lmeldexun/betterconfig/api/LoadEarly;"))) {
					return false;
				}

				MethodNode clinit;
				AbstractInsnNode returnInsn;
				try {
					clinit = ASMUtil.find(classNode, "<clinit>");
					returnInsn = ASMUtil.last(clinit).opcode(Opcodes.RETURN).find();
				} catch (NoSuchElementException e) {
					clinit = new MethodNode(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
					clinit.instructions.insert(returnInsn = new InsnNode(Opcodes.RETURN));
					classNode.methods.add(clinit);
				}

				clinit.instructions.insertBefore(returnInsn, ASMUtil.listOf(
						new LdcInsnNode(Type.getType("L" + classNode.name + ";")),
						new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/betterconfig/ConfigManager", "registerAndLoad", "(Ljava/lang/Class;)V", false)));

				Optional<MethodNode> callback = ASMUtil.stream(classNode)
						.filter(m -> (m.access & Opcodes.ACC_STATIC) != 0)
						.filter(m -> m.desc.equals("()V"))
						.filter(m -> m.visibleAnnotations != null && m.visibleAnnotations.stream().anyMatch(annotation -> annotation.desc.equals("Lmeldexun/betterconfig/api/LoadEarly$Callback;")))
						.findFirst();
				if (callback.isPresent()) {
					clinit.instructions.insertBefore(returnInsn, new MethodInsnNode(Opcodes.INVOKESTATIC, classNode.name, callback.get().name, callback.get().desc, false));
				}

				return true;
			}

			@Override
			public int writeFlags() {
				return ClassWriter.COMPUTE_FRAMES;
			}

			@Override
			public int priority() {
				return 0;
			}
		});
	}

}
