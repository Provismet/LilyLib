/*
 * Copyright (C) 2024 Provismet
 * 
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.mob.HostileEntity;

public class Relations {
    public static boolean isFriendly (LivingEntity entity1, LivingEntity entity2) {
        if (entity1 == entity2) return true;
        else return Relations.friendlyInternal(entity1, entity2) && Relations.friendlyInternal(entity2, entity1);
    }

    public static boolean isSameTeam (LivingEntity entity1, LivingEntity entity2) {
        if (entity1.getScoreboardTeam() == null) return false;
        return entity1.getScoreboardTeam() == entity2.getScoreboardTeam();
    }

    private static boolean friendlyInternal (LivingEntity entity1, LivingEntity entity2) {
        if (entity1 instanceof Tameable tame && tame.getOwner() == entity2) return true;
        if (entity1.getAttacker() == entity2) return false;
        if (entity1.getAttacking() == entity2) return false;
        if (Relations.isSameTeam(entity1, entity2)) return true;
        if (entity1 instanceof HostileEntity != entity2 instanceof HostileEntity) return false;
        return true;
    }
}
