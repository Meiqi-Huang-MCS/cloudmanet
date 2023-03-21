import java.util.Random;

public class LocationGenerator {
    private static final float MIN_LATITUDE = -90f;
    private static final float MAX_LATITUDE = 90f;
    private static final float MIN_LONGITUDE = -180f;
    private static final float MAX_LONGITUDE = 180f;

    private static final Random random = new Random();

    public static float generateLatitude() {
        return MIN_LATITUDE + random.nextFloat() * (MAX_LATITUDE - MIN_LATITUDE);
    }

    public static float generateLongitude() {
        return MIN_LONGITUDE + random.nextFloat() * (MAX_LONGITUDE - MIN_LONGITUDE);
    }
}
