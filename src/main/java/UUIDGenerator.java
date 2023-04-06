import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public final class UUIDGenerator {
    private static long get64LeastSignificantBits() {
        final long random63BitLong = new Random().nextLong() & 0x3FFFFFFFFFFFFFFFL;
        final long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }
    private static long get64MostSignificantBits() {
        final LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
        final Duration duration = Duration.between(start, LocalDateTime.now());
        final long seconds = duration.getSeconds();
        final long nanos = duration.getNano();
        final long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        final long least12SignificantBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        final long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificantBitOfTime;
    }
    public static UUID generateType1UUID() {
        final long most64SigBits = get64MostSignificantBits();
        final long least64SigBits = get64LeastSignificantBits();
        return new UUID(most64SigBits, least64SigBits);
    }



}
