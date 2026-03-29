/*
 * Copyright (C) 2024-2026 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Monster;

public class Relations {
    public static boolean isFriendly (LivingEntity entity1, LivingEntity entity2) {
        if (entity1 == entity2) return true;
        else return Relations.friendlyInternal(entity1, entity2) && Relations.friendlyInternal(entity2, entity1);
    }

    public static boolean isSameTeam (LivingEntity entity1, LivingEntity entity2) {
        if (entity1.getTeam() == null) return false;
        return entity1.getTeam() == entity2.getTeam();
    }

    private static boolean friendlyInternal (LivingEntity entity1, LivingEntity entity2) {
        if (entity1 instanceof OwnableEntity tame && tame.getOwner() == entity2) return true;
        if (entity1.getLastHurtByMob() == entity2) return false;
        if (entity1.getLastHurtMob() == entity2) return false;
        if (Relations.isSameTeam(entity1, entity2)) return true;
        if (entity1 instanceof Monster != entity2 instanceof Monster) return false;
        return true;
    }
}
