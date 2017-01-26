package com.novoda.peepz;

import com.google.firebase.database.DataSnapshot;

import java.util.concurrent.TimeUnit;

class Converter {

    private static final int MINUTES_UNTIL_STALE = 15;

    public Peep convert(DataSnapshot dataSnapshot) throws ConverterException {
        ApiPeep value = dataSnapshot.getValue(ApiPeep.class);

        Image image = null;
        if (value.image != null) {
            String payload = (String) value.image.get(ApiPeep.KEY_IMAGE_PAYLOAD);
            long timestamp = (long) value.image.get(ApiPeep.KEY_IMAGE_TIMESTAMP);
            image = new Image(payload, timestamp);
        }

        if (value.uid == null || value.name == null) {
            throw new ConverterException();
        }

        return new Peep(
                value.uid,
                value.name,
                image,
                value.lastSeen,
                getOnlineStatusFor(value)
        );
    }

    private Peep.OnlineStatus getOnlineStatusFor(ApiPeep value) {
        long diffMillis = System.currentTimeMillis() - value.lastSeen;
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);

        if (diffMinutes < MINUTES_UNTIL_STALE) {
            return Peep.OnlineStatus.FRESH;
        } else {
            return Peep.OnlineStatus.STALE;
        }
    }

    public static class ConverterException extends Exception {
    }

}
