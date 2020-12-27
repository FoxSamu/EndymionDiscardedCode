package endymion.util.mixin;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.injection.invoke.RedirectInjector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;

import java.util.List;

public class CustomInjectInjector extends RedirectInjector {
    private final IInject inject;

    public CustomInjectInjector(InjectionInfo info, String cls) {
        super(info, "@CustomInject");
        try {
            inject = (IInject) Class.forName(cls).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate " + cls, e);
        }
    }

    @Override
    protected void inject(Target target, InjectionNodes.InjectionNode node) {
        if (!preInject(node)) {
            return;
        }

        if (node.isReplaced()) {
            throw new UnsupportedOperationException("Target failure for " + this.info);
        }

        AbstractInsnNode targetNode = node.getCurrentTarget();
        injectCustomModifier(target, targetNode);
    }

    private void injectCustomModifier(Target target, AbstractInsnNode node) {
        inject.modify(target, target.insns, node, this::invokeHandler);
    }

    @Override
    protected void sanityCheck(Target target, List<InjectionPoint> injectionPoints) {
        if (target.classNode != this.classNode) {
            throw new InvalidInjectionException(this.info, "Target class does not match injector class in " + this);
        }
    }
}
