package com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.ai;

import java.util.EnumSet;

import com.minecraftabnormals.upgrade_aquatic.common.entities.jellyfish.AbstractJellyfishEntity;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.teamabnormals.abnormals_core.core.utils.EntityUtils;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.RayTraceResult.Type;

public class JellyfishBoostGoal extends Goal {
	private final AbstractJellyfishEntity jellyfish;
	private final Endimation boostAnimation;
	
	public JellyfishBoostGoal(AbstractJellyfishEntity jellyfish, Endimation boostAnimation) {
		this.jellyfish = jellyfish;
		this.boostAnimation = boostAnimation;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		float[] jellyfishRotations = this.jellyfish.getRotationController().getRotations(1.0F);
		float chance = jellyfishRotations[1] < 70.0F && jellyfishRotations[1] > -70.0F ? 0.05F : 0.03F;
		if(this.jellyfish.isInWater() && this.jellyfish.getRNG().nextFloat() < chance && this.jellyfish.isNoEndimationPlaying() && EntityUtils.rayTraceUpWithCustomDirection(this.jellyfish, jellyfishRotations[1], jellyfishRotations[0], 2.0F, 1.0F).getType() != Type.BLOCK) {
			return !this.jellyfish.willBeBoostedOutOfWater(jellyfishRotations[0], jellyfishRotations[1]);
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.jellyfish.isInWater() && this.jellyfish.isNoEndimationPlaying();
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.jellyfish, this.boostAnimation);
	}
}