package meldexun.betterconfig.asm;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
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
				try {
					clinit = ASMUtil.find(classNode, "<clinit>");
				} catch (NoSuchElementException e) {
					clinit = new MethodNode(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
					clinit.instructions.insert(new InsnNode(Opcodes.RETURN));
					classNode.methods.add(clinit);
				}
				clinit.instructions.insertBefore(ASMUtil.last(clinit).opcode(Opcodes.RETURN).find(), ASMUtil.listOf(
						new LdcInsnNode(Type.getType("L" + classNode.name + ";")),
						new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/betterconfig/ConfigManager", "registerAndLoad", "(Ljava/lang/Class;)V", false)));
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
