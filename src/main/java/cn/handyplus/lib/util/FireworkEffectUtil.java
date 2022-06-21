package cn.handyplus.lib.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

/**
 * 烟花工具类
 *
 * @author handy
 * @since 1.4.7
 */
public class FireworkEffectUtil {
    private final FireworkEffect.Builder fb;
    private final Random r;
    private FireworkEffect f;

    /**
     * 构建烟花帮助类
     *
     * @since 1.4.7
     */
    public FireworkEffectUtil() {
        fb = FireworkEffect.builder();
        r = new Random();
    }

    /**
     * 玩家位置放个随机烟花
     *
     * @param player 玩家
     * @since 1.4.7
     */
    public static void spawnFireworkToPlayer(Player player) {
        new FireworkEffectUtil().setType().setColor().isRandomWithFade(3).isRandomWithFlicker(3).isRandomWithTrail(3).spawnEntity(player);
    }

    /**
     * 位置放个随机烟花
     *
     * @param location 位置
     * @since 1.4.7
     */
    public static void spawnFireworkToLocation(Location location) {
        new FireworkEffectUtil().setType().setColor().isRandomWithFade(3).isRandomWithFlicker(3).isRandomWithTrail(3).spawnEntity(location);
    }

    /**
     * 设置颜色-随机
     *
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil setColor() {
        fb.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        return this;
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil setColor(Color color) {
        fb.withColor(color);
        return this;
    }

    /**
     * 设置类型-随机
     *
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil setType() {
        FireworkEffect.Type[] types = FireworkEffect.Type.values();
        fb.with(types[r.nextInt(types.length)]);
        return this;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil setType(FireworkEffect.Type type) {
        fb.with(type);
        return this;
    }

    /**
     * 向烟花添加一个尾迹效果
     *
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isWithTrail() {
        fb.withTrail();
        return this;
    }

    /**
     * 向烟花添加一个尾迹效果-随机
     *
     * @param random 随机概率
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isRandomWithTrail(int random) {
        if (r.nextInt(random) == 0) {
            fb.withTrail();
        }
        return this;
    }

    /**
     * 向烟花效果添加几种淡出颜色
     *
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isWithFade() {
        // 向烟花效果添加几种淡出颜色。
        fb.withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        return this;
    }

    /**
     * 向烟花效果添加几种淡出颜色-随机
     *
     * @param random 随机概率
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isRandomWithFade(int random) {
        if (r.nextInt(random) == 0) {
            // 向烟花效果添加几种淡出颜色。
            fb.withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
        return this;
    }

    /**
     * 向烟花效果添加几种淡出颜色
     *
     * @param random 随机
     * @param color  颜色
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isRandomWithFade(int random, Color color) {
        if (r.nextInt(random) == 0) {
            // 向烟花效果添加几种淡出颜色。
            fb.withFade(color);
        }
        return this;
    }

    /**
     * 向烟花添加一个闪烁效果
     *
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isWithFlicker() {
        fb.withFlicker();
        return this;
    }

    /**
     * 向烟花添加一个闪烁效果-随机
     *
     * @param random 随机概率
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil isRandomWithFlicker(int random) {
        if (r.nextInt(random) == 0) {
            fb.withFlicker();
        }
        return this;
    }

    /**
     * 构建烟花
     *
     * @return this
     * @since 1.4.7
     */
    public FireworkEffectUtil builder() {
        f = fb.build();
        return this;
    }

    /**
     * 发射烟花
     *
     * @param player 玩家
     * @since 1.4.7
     */
    public void spawnEntity(Player player) {
        this.spawnEntity(player.getLocation());
    }

    /**
     * 发射烟花
     *
     * @param location 位置
     * @since 1.4.7
     */
    public void spawnEntity(Location location) {
        if (location.getWorld() == null) {
            return;
        }
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        // 移除烟花火箭附带的所有效果.
        fwm.clearEffects();
        // 向烟花火箭添加一个烟花效果.
        fwm.addEffect(f);
        // 设置这个烟花火箭的飞行时间的近似值.
        fwm.setPower(0);
        // 为此烟花火箭应用提供的烟花火箭元数据.
        fw.setFireworkMeta(fwm);
        // 设置此实体的速度 (单位: 米每tick).
        fw.setVelocity(location.getDirection().multiply(0.5));
    }

}