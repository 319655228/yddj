package com.hckj.yddxst.data;

import com.faceunity.entity.FUEffect;
import com.faceunity.fuenum.FUEffectType;

import java.util.List;

/**
 * 描述: 形象相关 <br>
 * 日期: 2020-02-26 22:26 <br>
 * 作者: 林明健 <br>
 */
public class EffectFactory {

    private static Effect defaultEffect;
    private static String[] effecAnimPaths;

    public static void setEffect(Effect effect) {
        defaultEffect = effect;
    }

    public static Effect getEffect() {
        return defaultEffect;
    }

    public static double getFixedX() {
        return defaultEffect != null && defaultEffect.getCoordinate() != null ? defaultEffect.getCoordinate().getFixX() : 0;
    }

    public static double getFixedY() {
        return defaultEffect != null && defaultEffect.getCoordinate() != null ? defaultEffect.getCoordinate().getFixY() : 0;
    }

    public static double getFixedZ() {
        return defaultEffect != null && defaultEffect.getCoordinate() != null ? defaultEffect.getCoordinate().getFixZ() : 0;
    }

    public static String[] getAnimsPath() {
        if (effecAnimPaths != null)
            return effecAnimPaths;

        if (defaultEffect != null && defaultEffect.getAnimPath() != null) {
            List<Action> animPath = defaultEffect.getAnimPath();
            int animSize = animPath.size();
            effecAnimPaths = new String[animSize];
            for (int i = 0; i < animSize; i++) {
                effecAnimPaths[i] = animPath.get(i).getPath();
            }

            return effecAnimPaths;
        } else {
            return new String[0];
        }
    }

    /**
     * 自定义形象转为SDK形象
     */
    public static FUEffect convertEffect() {

        if (defaultEffect != null) {
            List<String> paths = defaultEffect.getBundlePath();
            String[] pathArr = new String[]{};
            if (paths != null && paths.size() > 0) {
                pathArr = new String[paths.size()];
                for (int i = 0; i < paths.size(); i++) {
                    pathArr[i] = paths.get(i);
                }
            }
            List<Action> action = defaultEffect.getDefaultAnimPath();
            String[] actionArr = new String[]{};
            if (action != null && action.size() > 0) {
                actionArr = new String[action.size()];
                for (int i = 0; i < action.size(); i++) {
                    actionArr[i] = action.get(i).getPath();
                }
            }

            Coordinate coordinate = defaultEffect.getCoordinate();
            return new FUEffect(defaultEffect.getEffectID(), defaultEffect.getAvatarPath(), pathArr, actionArr, defaultEffect.getBsConfigPath(), defaultEffect.getLightPath(), defaultEffect.getBackgroundPath(), new double[]{coordinate.getFixX(), coordinate.getFixY(), coordinate.getFixZ()}, defaultEffect.getEmotionCode() > 0 ? FUEffectType.CARTOON : FUEffectType.REAL);
        }
        return null;
    }
}
