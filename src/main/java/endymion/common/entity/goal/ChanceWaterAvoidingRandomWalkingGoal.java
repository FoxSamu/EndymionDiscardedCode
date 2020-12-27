package endymion.common.entity.goal;

import javax.annotation.Nullable;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.util.math.vector.Vector3d;

public class ChanceWaterAvoidingRandomWalkingGoal extends RandomWalkingGoal {
   protected final float probability;

   public ChanceWaterAvoidingRandomWalkingGoal(CreatureEntity entity, double speed, int executionChance) {
      this(entity, speed, executionChance, 0.001F);
   }

   public ChanceWaterAvoidingRandomWalkingGoal(CreatureEntity entity, double speed, int executionChance, float avoidWaterProbability) {
      super(entity, speed, executionChance);
      this.probability = avoidWaterProbability;
   }

   @Nullable
   protected Vector3d getPosition() {
      if (this.creature.isInWaterOrBubbleColumn()) {
         Vector3d vector3d = RandomPositionGenerator.getLandPos(this.creature, 15, 7);
         return vector3d == null ? super.getPosition() : vector3d;
      } else {
         return this.creature.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.creature, 10, 7) : super.getPosition();
      }
   }
}
