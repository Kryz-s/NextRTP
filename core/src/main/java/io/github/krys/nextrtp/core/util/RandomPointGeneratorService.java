package io.github.krys.nextrtp.core.util;

import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.common.service.Service;

import java.util.Random;

public final class RandomPointGeneratorService implements Service {

    private static final Random RANDOM = new Random();

    public static double[] getRandomPoint(final BaseTeleportInfo teleportInfo) {
        return switch (teleportInfo.algorithm) {
            case SQUARE -> generateRandomPointInAnnulus(teleportInfo.minRadius, teleportInfo.maxRadius, teleportInfo.centerX, teleportInfo.centerZ);
            case CIRCLE -> generateRandomPointInCircle(teleportInfo.minRadius, teleportInfo.maxRadius, teleportInfo.centerX, teleportInfo.centerZ);
        };
    }


    private static double[] generateRandomPointInCircle(int minRadius, int maxRadius, double baseX, double baseZ) {
        double angle = RANDOM.nextDouble() * 2 * Math.PI;

        double radius = Math.sqrt(RANDOM.nextDouble()) * (maxRadius - minRadius) + minRadius;

        final int x = (int) (baseX + (int) (radius * Math.cos(angle)));
        final int z = (int) (baseZ + (int) (radius * Math.sin(angle)));

        return new double[]{x + 0.5D, z + 0.5D};
    }

    private static double[] generateRandomPointInAnnulus(
            int minRadius,
            int maxRadius,
            double baseX,
            double baseZ) {

        final double angle = RANDOM.nextDouble(0.0, 2 * Math.PI);

        final double radius = RANDOM.nextDouble(minRadius, maxRadius);

        final int x = (int) (baseX + (radius * Math.cos(angle)));
        final int z = (int) (baseZ + (radius * Math.sin(angle)));

        return new double[]{x + 0.5D, z + 0.5D};
    }

}
