package endymion.util.injector;

import endymion.util.mixin.IInject;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.injection.struct.Target;

import java.util.function.Consumer;

public class NoiseChunkGeneratorInjector implements IInject {
    @Override
    public void modify(Target target, InsnList methodInsnList, AbstractInsnNode targetNode, Consumer<InsnList> handlerInvoker) {
        System.out.println("INJECTING!!!!!!!!!!!!!");

        int index = methodInsnList.indexOf(targetNode);
        methodInsnList.remove(targetNode);

        // Clear out the rest of the branch and change to our own code
        while(methodInsnList.get(index).getOpcode() != Opcodes.GOTO) {
            methodInsnList.remove(methodInsnList.get(index));
        }
        AbstractInsnNode gotoinsn = methodInsnList.get(index);

        Target.Extension stackExt = target.extendStack();

        // Assuming our method signature is still on the operand stack we only have to push ourselves
        methodInsnList.insertBefore(gotoinsn, new VarInsnNode(Opcodes.ALOAD, 0));
        stackExt.add();

        // Invoke mixin method, must be done in a separate list
        InsnList list = new InsnList();
        handlerInvoker.accept(list);
        methodInsnList.insertBefore(gotoinsn, list);

        // We now have a double array on the stack, and want to push it's first two elements into LVT 5 and 7
        InsnList expandArray = new InsnList();
        expandArray.add(new InsnNode(Opcodes.DUP)); // We want 2 values, so it must be on the stack twice
        expandArray.add(new LdcInsnNode(0)); // Index 0
        expandArray.add(new InsnNode(Opcodes.DALOAD)); // Load double from array on stack
        expandArray.add(new VarInsnNode(Opcodes.DSTORE, 5)); // Save to LVT 5
        expandArray.add(new LdcInsnNode(1)); // Index 1
        expandArray.add(new InsnNode(Opcodes.DALOAD)); // Load double from array on stack
        expandArray.add(new VarInsnNode(Opcodes.DSTORE, 7)); // Save to LVT 7
        methodInsnList.insertBefore(gotoinsn, expandArray);

        // Configure max stack size
        stackExt.apply();
    }
}
