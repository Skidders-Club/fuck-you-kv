package keystrokesmod.tweaker;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;

import keystrokesmod.tweaker.transformers.*;
import keystrokesmod.tweaker.transformers.Transformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ASMTransformerClass implements IClassTransformer {
   public static String eventHandlerClassName = ASMEventHandler.class.getName().replace(".", "/"); //added replace or it won't launch
   private final Multimap<String, Transformer> m = ArrayListMultimap.create();
   public static final boolean outputBytecode = Boolean.parseBoolean(System.getProperty("debugBytecode", "false"));

   public ASMTransformerClass() {/*
      this.addTransformer(new TransformerFontRenderer());
      this.addTransformer(new TransformerGuiUtilRenderComponents());
      this.addTransformer(new TransformerEntityPlayerSP());
      */
      this.addTransformer(new TransformerEntity());/*
      this.addTransformer(new TransformerEntityPlayer());
      this.addTransformer(new TransformerMinecraft());*/
   }

   private void addTransformer(Transformer transformer) {
      String[] var2 = transformer.getClassName();

      for (String c : var2) {
         this.m.put(c, transformer);
      }
   }

   @SuppressWarnings("ResultOfMethodCallIgnored")
   @Override
   public byte[] transform(String name, String transformedName, byte[] basicClass) {
      if (basicClass == null) {
         return null;
      } else {
         Collection<Transformer> tr = this.m.get(transformedName);
         if (tr.isEmpty()) {
            return basicClass;
         } else {
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
            //tr.forEach((transformer) -> {
             //  transformer.transform(classNode, transformedName);
            //});
            for (Transformer transformer : tr) {
               transformer.transform(classNode, transformedName);
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            try {
               classNode.accept(classWriter);
            } catch (Throwable var9) {
               //LOGGER.error("Exception when transforming " + transformedName + " : " + t.getClass().getSimpleName());
               var9.printStackTrace();
            }

            return classWriter.toByteArray();
         }
      }
   }
}
