package meldexun.betterconfig.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import meldexun.asmutil2.AbstractClassTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class BetterConfigClassTransformer extends AbstractClassTransformer implements IClassTransformer {

	@Override
	public byte[] transformOrNull(String obfName, String name, byte[] basicClass) {
		if (!name.equals("net.minecraftforge.common.config.ConfigManager")) {
			return null;
		}
		ClassReader reader = new ClassReader(basicClass);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		reader.accept(new ClassVisitor(Opcodes.ASM5, writer) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
				MethodVisitor methodWriter = super.visitMethod(access, name, desc, signature, exceptions);
				if (name.equals("sync") && desc.equals("(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V")) {
					methodWriter.visitCode();
					methodWriter.visitVarInsn(Opcodes.ALOAD, 0);
					methodWriter.visitVarInsn(Opcodes.ALOAD, 1);
					methodWriter.visitMethodInsn(Opcodes.INVOKESTATIC, "meldexun/betterconfig/ConfigurationManager", "sync", "(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V", false);
					methodWriter.visitInsn(Opcodes.RETURN);
					methodWriter.visitEnd();
					return null;
				}
				return methodWriter;
			}
		}, 0);
		return writer.toByteArray();
	}

}
