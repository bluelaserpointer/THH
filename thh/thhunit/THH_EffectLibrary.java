package thhunit;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import core.GHQ;
import effect.Effect;
import paint.ImageFrame;
import paint.dot.DotPaint;
import physics.HasDynam;

public abstract class THH_EffectLibrary extends Effect{
	public THH_EffectLibrary(HasDynam source) {
		super(source);
	}
	/////////////////
	//Marisa - 1
	/////////////////
	public static class SparkHitEF extends THH_EffectLibrary{
		private static final DotPaint paint = ImageFrame.create("thhimage/NarrowSpark_HitEffect.png");
		public SparkHitEF(HasDynam source) {
			super(source);
			name = "SparkHitEF";
			paintScript = paint;
			limitFrame = 3;
			dynam.stop();
			dynam.setMoveAngle(GHQ.random2(0, 2*PI));
		}
		public SparkHitEF getOriginal(){
			return new SparkHitEF(SHOOTER);
		}
	}
	/////////////////
	//Marisa - 2
	/////////////////
	public static class LightningEF extends THH_EffectLibrary{
		static final DotPaint paint = ImageFrame.create("thhimage/ReuseBomb_Effect.png");
		
		public LightningEF(HasDynam source) {
			super(source);
			name = "LIGHTNING";
			paintScript = paint;
			limitFrame = 2;
			dynam.fastParaAdd_DASpd(10,2*PI*random(),20);
		}
		public LightningEF getOriginal(){
			return new LightningEF(SHOOTER);
		}
	}
	/////////////////
	//Marisa - 3
	/////////////////
	public static class MissileTraceA_EF extends THH_EffectLibrary{
		static final DotPaint paint = ImageFrame.create("thhimage/StarEffect2.png");

		public MissileTraceA_EF(HasDynam source, boolean appendClones) {
			super(source);
			name = "MissileTraceAEF";
			paintScript = paint;
			accel = -0.2;
			limitFrame = 7;
			dynam.fastParaAdd_DASpd(10,2*PI*random(),GHQ.random2(0,12));
			if(appendClones) {
				for(int i = 0;i < 3;i++) {
					GHQ.stage().addEffect(new MissileTraceA_EF(source, false));
				}
			}
		}
		public MissileTraceA_EF getOriginal(){
			return new MissileTraceA_EF(SHOOTER, false);
		}
		@Override
		public final void paint(boolean doAnimation) {
			fadingPaint();
		}
	}
	/////////////////
	//Marisa - 4
	/////////////////
	public static class MissileTraceB_EF extends THH_EffectLibrary{
		static final DotPaint paint = ImageFrame.create("thhimage/MagicMissile.png");
		
		public MissileTraceB_EF(HasDynam source) {
			super(source);
			name = "MISSILE_TRACE2_EF";
			paintScript = paint;
			limitFrame = 7;
			dynam.stop();
		}
		public MissileTraceB_EF getOriginal(){
			return new MissileTraceB_EF(SHOOTER);
		}
		@Override
		public final void paint(boolean doAnimation) {
			fadingPaint();
		}
	}
	/////////////////
	//Marisa - 5
	/////////////////
	public static class MissileHitEF extends THH_EffectLibrary{
		static final DotPaint paint = ImageFrame.create("thhimage/MissileHitEffect.png");
		
		public MissileHitEF(HasDynam source, boolean makeClones) {
			super(source);
			name = "MissileHitEF";
			paintScript = paint;
			limitFrame = GHQ.random2(10,40);
			dynam.fastParaAdd_DASpd(30,2*PI*random(),GHQ.random2(2,5));
			accel = -0.1;
			if(makeClones) {
				for(int i = 0;i < 30;i++)
					GHQ.stage().addEffect(new MissileHitEF(source, false));
			}
		}
		public MissileHitEF getOriginal(){
			return new MissileHitEF(SHOOTER, false);
		}
		@Override
		public final void paint(boolean doAnimation) {
			fadingPaint();
		}
	}
	/////////////////
	//Reimu - 1
	/////////////////
	public static class FudaHitEF extends THH_EffectLibrary{
		static final DotPaint paint = ImageFrame.create("thhimage/FudaHitEffect.png");
		
		public FudaHitEF(HasDynam source, boolean makeClones) {
			super(source);
			name = "FudaHitEF";
			accel = 0.5;
			limitFrame = 4;
			paintScript = paint;
			dynam.stop();
			dynam.fastParaAdd_DASpd(10,2*PI*random(),GHQ.random2(0,22));
			if(makeClones) {
				for(int i = 0;i < 15;i++)
					GHQ.stage().addEffect(new FudaHitEF(source, false));
			}
		}
		public FudaHitEF getOriginal(){
			return new FudaHitEF(SHOOTER, false);
		}
		@Override
		public final void paint(boolean doAnimation) {
			fadingPaint();
		}
	}
}
