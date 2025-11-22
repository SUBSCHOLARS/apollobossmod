package org.apollo.apollobossmod.apolloboss;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ApolloBeamGoal extends Goal {
    private final ApolloBoss apolloBoss;
    private LivingEntity target;

    // アニメーションと攻撃の進行管理用
    private int attackTime; // 現在の経過tick
    private final int warmUpTime; // 予備動作（構え）の時間
    private final int animationTime; // 攻撃全体の時間

    // クールダウン（連続使用防止）
    private final int cooldownTime;

    public ApolloBeamGoal(ApolloBoss apolloBoss, int warmUpTicks, int totalAnimationTicks, int cooldownTicks)
    {
        this.apolloBoss=apolloBoss;
        this.warmUpTime=warmUpTicks;
        this.animationTime=totalAnimationTicks;
        this.cooldownTime=cooldownTicks;

        // 移動（MOVE）と視線（LOOK）をこのゴールが制御するようフラグを設定
        this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));
    }
    // AIがこのゴールを開始できるかどうかの判定
    @Override
    public boolean canUse() {
        LivingEntity target=this.apolloBoss.getTarget();
        if(target==null || !target.isAlive())
        {
            return false;
        }
        // クールダウン中でなければ使用可能
        return this.apolloBoss.isBeamReady() && this.apolloBoss.distanceToSqr(target) < 400.0D;
    }
    // ゴールが実行可能だが、継続すべきかの判定
    @Override
    public boolean canContinueToUse() {
        // ターゲットが存在し、攻撃時間が終わっていなければ継続
        return this.target != null && this.target.isAlive() && this.attackTime < this.animationTime;
    }
    // ゴール開始時の処理（1回だけ呼ばれる）
    @Override
    public void start() {
        this.attackTime=0;
        this.target=this.apolloBoss.getTarget();
        // 移動を停止させる
        this.apolloBoss.getNavigation().stop();
        // アニメーションフラグON
        this.apolloBoss.setBeam(true);
    }
    // ゴール終了時の処理（中断や完了時）
    @Override
    public void stop() {
        this.target=null;
        this.apolloBoss.setBeam(false);
        this.apolloBoss.setBeamCooldown(this.cooldownTime);
        super.stop();
    }
    // 毎tick実行されるメインロジック

    @Override
    public void tick() {
        this.attackTime++;
        // 常にターゲットの方向を向く
        if(this.target!=null)
        {
            this.apolloBoss.lookAt(this.target, 30.0F, 30.0F);
        }
        // 設定した予備動作時間が来たらビーム発射
        if(this.attackTime==this.warmUpTime)
        {
            this.apolloBoss.performBeamAttack(this.target);
        }
        super.tick();
    }
}
